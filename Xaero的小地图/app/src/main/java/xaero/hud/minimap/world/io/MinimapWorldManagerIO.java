package xaero.hud.minimap.world.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.file.SimpleBackup;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.io.WaypointIO;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.minimap.world.container.config.io.RootConfigIO;
import xaero.hud.path.XaeroPath;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/io/MinimapWorldManagerIO.class */
public class MinimapWorldManagerIO {
    private final HudMod modMain;
    private final RootConfigIO rootConfigIO;
    private final WaypointIO waypointIO;
    private final Pattern backupFilePattern = Pattern.compile("^backup-*$");

    public MinimapWorldManagerIO(HudMod modMain) {
        this.modMain = modMain;
        this.rootConfigIO = new RootConfigIO(modMain);
        this.waypointIO = new WaypointIO(modMain);
    }

    public void loadWorldsFromAllSources(MinimapSession session, ClientPacketListener connection) throws IOException {
        fixOldRootFolder(session);
        boolean shouldResave = this.waypointIO.getOldIO().load(session);
        loadAllWorlds(session);
        if (shouldResave) {
            saveAllWorlds(session);
        }
    }

    public void loadAllWorlds(MinimapSession session) throws InterruptedException, IOException {
        Path minimapFolderPath = this.modMain.getMinimapFolder();
        if (!Files.exists(minimapFolderPath, new LinkOption[0])) {
            Files.createDirectories(minimapFolderPath, new FileAttribute[0]);
        }
        Path minimapTempToAddFolder = minimapFolderPath.resolve("temp_to_add");
        if (Files.exists(minimapTempToAddFolder, new LinkOption[0])) {
            copyTempFilesBack(minimapTempToAddFolder);
        }
        convertWorldDimFilesToFolders();
        convertWorldDimFoldersToSingleFolder(session);
        Stream<Path> rootFiles = Files.list(minimapFolderPath);
        if (rootFiles == null) {
            return;
        }
        for (Path rootFilePath : rootFiles) {
            if (Files.isDirectory(rootFilePath, new LinkOption[0])) {
                String rootFolderName = rootFilePath.getFileName().toString();
                if (!this.backupFilePattern.matcher(rootFolderName).find()) {
                    loadWorldFolder(rootFilePath, rootFolderName, session);
                }
            }
        }
        rootFiles.close();
    }

    private void loadWorldFolder(Path folder, String rootFolderName, MinimapSession session) throws InterruptedException, IOException {
        XaeroPath rootPath = XaeroPath.root(rootFolderName);
        try {
            rootPath.applyToFilePath(this.modMain.getMinimapFolder());
            Path tempToAdd = folder.resolve("temp_to_add");
            if (Files.exists(tempToAdd, new LinkOption[0])) {
                copyTempFilesBack(tempToAdd);
            }
            Stream<Path> worldFiles = Files.list(folder);
            if (worldFiles == null) {
                return;
            }
            for (Path worldFile : worldFiles) {
                String worldFileName = worldFile.getFileName().toString();
                if (!this.backupFilePattern.matcher(worldFileName).find()) {
                    if (!Files.isDirectory(worldFile, new LinkOption[0])) {
                        if (worldFileName.contains("_")) {
                            MinimapWorldContainer container = session.getWorldManager().addWorldContainer(rootPath);
                            loadWorldFile(container, worldFileName, null);
                        }
                    } else {
                        loadDimensionFolder(worldFileName, worldFile, rootFolderName, session);
                    }
                }
            }
            if (session.getWorldManager().getWorldContainer(rootPath).isEmpty()) {
                session.getWorldManager().removeContainer(rootPath);
            }
            worldFiles.close();
        } catch (InvalidPathException e) {
            MinimapLogs.LOGGER.warn("Ignoring minimap world folder {} for somehow containing characters invalid for use with your file system.", rootFolderName);
        }
    }

    private void loadDimensionFolder(String dimensionName, Path folder, String rootFolderName, MinimapSession session) throws InterruptedException, IOException {
        Path tempToAdd2 = folder.resolve("temp_to_add");
        if (Files.exists(tempToAdd2, new LinkOption[0])) {
            copyTempFilesBack(tempToAdd2);
        }
        String fixedDimensionName = this.waypointIO.getOldIO().fixOldDimensionName(dimensionName);
        XaeroPath containerKey = XaeroPath.root(rootFolderName).resolve(fixedDimensionName);
        MinimapWorldContainer container = session.getWorldManager().addWorldContainer(containerKey);
        Stream<Path> dimensionFiles = Files.list(folder);
        if (dimensionFiles != null) {
            for (Path dimensionFile : dimensionFiles) {
                String fileName = dimensionFile.getFileName().toString();
                loadWorldFile(container, fileName, dimensionFile);
            }
            dimensionFiles.close();
        }
        if (container.isEmpty()) {
            session.getWorldManager().removeContainer(containerKey);
        }
        if (!fixedDimensionName.equals(dimensionName)) {
            SimpleBackup.moveToBackup(folder);
            saveWorlds(container);
        }
    }

    public boolean loadWorldFile(MinimapWorldContainer container, String fileName, Path filePath) throws IOException {
        if (!fileName.endsWith(".txt")) {
            return false;
        }
        String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
        String multiworldId = noExtension;
        if (!noExtension.equals("waypoints")) {
            String[] multiworld = noExtension.split("_");
            if (multiworld.length < 2) {
                return false;
            }
            multiworldId = multiworld[0];
            String multiworldName = multiworld[1].replace("%us%", "_");
            container.setName(multiworldId, multiworldName);
        }
        MinimapWorld world = container.addWorld(multiworldId);
        if (world != null) {
            loadWorld(world, filePath);
            return true;
        }
        return true;
    }

    public void loadWorld(MinimapWorld world, Path filePath) throws IOException {
        Long legacySlimeSeed;
        if (filePath == null) {
            filePath = getWorldFile(world);
        }
        if (!Files.exists(filePath, new LinkOption[0])) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toFile()), "UTF8"));
        while (true) {
            try {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                String[] args = s.split(":");
                try {
                    checkWorldFileLine(args, world);
                } catch (Throwable e) {
                    MinimapLogs.LOGGER.error("Skipping minimap world file line:" + Arrays.toString(args), e);
                }
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        reader.close();
        if (world.getSlimeChunkSeed() != null || (legacySlimeSeed = this.modMain.getSettings().getLegacySlimeChunksSeed(world.getFullPath())) == null) {
            return;
        }
        world.setSlimeChunkSeed(legacySlimeSeed);
        saveWorld(world);
        this.modMain.getSettings().removeLegacySlimeChunksSeed(world.getFullPath());
    }

    public boolean checkWorldFileLine(String[] args, MinimapWorld world) {
        if (this.waypointIO.checkLine(args, world)) {
            return true;
        }
        if (args[0].equals("slime_chunk_seed")) {
            try {
                world.setSlimeChunkSeed(Long.valueOf(Long.parseLong(args[1])));
                return false;
            } catch (NumberFormatException e) {
                world.setSlimeChunkSeed(Long.valueOf(args[1].hashCode()));
                return false;
            }
        }
        return false;
    }

    public void saveWorlds(MinimapWorldContainer container) throws IOException {
        for (MinimapWorld world : container.getAllWorldsIterable()) {
            saveWorld(world);
        }
    }

    public void saveAllWorlds(MinimapSession session) throws IOException {
        for (MinimapWorldRootContainer rootContainer : session.getWorldManager().getRootContainers()) {
            saveWorlds(rootContainer);
        }
    }

    public void saveWorld(MinimapWorld wpw) throws IOException {
        saveWorld(wpw, true);
    }

    public void saveWorld(MinimapWorld world, boolean overwrite) throws IOException {
        if (world == null) {
            return;
        }
        Path worldFilePath = getWorldFile(world);
        if (Files.exists(worldFilePath, new LinkOption[0]) && !overwrite) {
            return;
        }
        Path worldFileTempPath = worldFilePath.getParent().resolve(String.valueOf(worldFilePath.getFileName()) + ".temp");
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(worldFileTempPath.toFile()));
        try {
            OutputStreamWriter output = new OutputStreamWriter(bufferedOutput, StandardCharsets.UTF_8);
            try {
                this.waypointIO.saveWaypoints(world, output);
                if (world.getSlimeChunkSeed() != null) {
                    output.write("slime_chunk_seed:" + world.getSlimeChunkSeed());
                }
                output.close();
                bufferedOutput.close();
                IOUtils.safeMoveAndReplace(worldFileTempPath, worldFilePath, true);
                if (world.hasSomethingToRemoveOnSave()) {
                    world.cleanupOnSave(worldFilePath);
                }
            } finally {
            }
        } catch (Throwable th) {
            try {
                bufferedOutput.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void convertWorldDimFilesToFolders() throws IOException {
        Stream<Path> files = Files.list(this.modMain.getMinimapFolder());
        Path backupFolder = this.modMain.getMinimapFolder().resolve("backup");
        Files.createDirectories(backupFolder, new FileAttribute[0]);
        if (files == null) {
            return;
        }
        for (Path rootFilePath : files) {
            if (!Files.isDirectory(rootFilePath, new LinkOption[0])) {
                String fileName = rootFilePath.getFileName().toString();
                if (fileName.endsWith(".txt") && fileName.contains("_")) {
                    int lastUnderscore = fileName.lastIndexOf("_");
                    if (!fileName.startsWith("Multiplayer_") && !fileName.startsWith("Realms_")) {
                        fileName = fileName.substring(0, lastUnderscore).replace("_", "%us%") + fileName.substring(lastUnderscore);
                    }
                    String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
                    Path folderPath = rootFilePath.getParent().resolve(noExtension);
                    Path fixedFilePath = folderPath.resolve("waypoints.txt");
                    Path backupFilePath = backupFolder.resolve(fileName);
                    if (!Files.exists(folderPath, new LinkOption[0])) {
                        Files.createDirectories(folderPath, new FileAttribute[0]);
                    }
                    if (!Files.exists(backupFilePath, new LinkOption[0])) {
                        Files.copy(rootFilePath, backupFilePath, new CopyOption[0]);
                    }
                    try {
                        Files.move(rootFilePath, fixedFilePath, new CopyOption[0]);
                    } catch (FileAlreadyExistsException e) {
                        if (Files.exists(backupFilePath, new LinkOption[0])) {
                            Files.deleteIfExists(rootFilePath);
                        }
                    }
                }
            }
        }
        files.close();
    }

    public void convertWorldDimFoldersToSingleFolder(MinimapSession session) throws IOException {
        Stream<Path> files = Files.list(this.modMain.getMinimapFolder());
        if (files == null) {
            return;
        }
        for (Path rootFilePath : files) {
            if (Files.isDirectory(rootFilePath, new LinkOption[0])) {
                String folderName = rootFilePath.getFileName().toString();
                String[] folderArgs = folderName.split("_");
                if (folderArgs.length > 2 || (folderArgs.length == 2 && !folderArgs[0].equals("Multiplayer"))) {
                    String lastArg = folderArgs[folderArgs.length - 1];
                    if (lastArg.equals("null") || (lastArg.startsWith("DIM") && lastArg.length() != 3)) {
                        int dimensionId = lastArg.equals("null") ? 0 : Integer.parseInt(lastArg.substring(3));
                        String dimensionName = "dim%" + dimensionId;
                        ResourceKey<Level> dimRegistryKey = session.getDimensionHelper().getDimensionKeyForDirectoryName(dimensionName);
                        if (dimRegistryKey != null) {
                            dimensionName = session.getDimensionHelper().getDimensionDirectoryName(dimRegistryKey);
                        }
                        Path correctDimensionFolder = rootFilePath.getParent().resolve(folderName.substring(0, folderName.lastIndexOf("_"))).resolve(dimensionName);
                        if (!Files.exists(correctDimensionFolder, new LinkOption[0])) {
                            Files.createDirectories(correctDimensionFolder, new FileAttribute[0]);
                        }
                        Stream<Path> dimensionFiles = Files.list(rootFilePath);
                        if (dimensionFiles != null) {
                            for (Path dimensionFilePath : dimensionFiles) {
                                if (!Files.isDirectory(dimensionFilePath, new LinkOption[0])) {
                                    Path correctFilePath = correctDimensionFolder.resolve(dimensionFilePath.getFileName());
                                    Files.move(dimensionFilePath, correctFilePath, new CopyOption[0]);
                                }
                            }
                            dimensionFiles.close();
                        }
                        Stream<Path> deleteCheck = Files.list(rootFilePath);
                        if (deleteCheck != null) {
                            boolean oldFolderEmpty = deleteCheck.count() == 0;
                            deleteCheck.close();
                            if (oldFolderEmpty) {
                                Files.deleteIfExists(rootFilePath);
                            }
                        }
                    }
                }
            }
        }
        files.close();
    }

    public static void copyTempFilesBack(Path folder) throws InterruptedException, IOException {
        Stream<Path> tempFiles = Files.list(folder);
        if (tempFiles != null) {
            for (Path tempFile : tempFiles) {
                Path newLocation = folder.getParent().resolve(tempFile.getFileName());
                if (!Files.exists(newLocation, new LinkOption[0]) || (!Files.isDirectory(newLocation, new LinkOption[0]) && Files.size(newLocation) == 0)) {
                    IOUtils.safeMoveAndReplace(tempFile, newLocation, false);
                } else {
                    SimpleBackup.moveToBackup(folder.getParent(), tempFile);
                }
            }
            tempFiles.close();
        }
        Files.delete(folder);
    }

    private void fixOldRootFolder(MinimapSession session) throws IOException {
        XaeroPath autoRootContainerPath = session.getWorldState().getAutoRootContainerPath();
        for (int format = 3; format >= 0; format--) {
            fixOldRootFolder(autoRootContainerPath, session.getWorldState().getOutdatedAutoRootContainerPath(format));
        }
    }

    private void fixOldRootFolder(XaeroPath path, XaeroPath outdatedPath) throws IOException {
        if (!path.equals(outdatedPath)) {
            try {
                Path oldFormatRootFolder = outdatedPath.applyToFilePath(this.modMain.getMinimapFolder());
                if (Files.exists(oldFormatRootFolder, new LinkOption[0])) {
                    Path fixedFolder = path.applyToFilePath(this.modMain.getMinimapFolder());
                    if (!Files.exists(fixedFolder, new LinkOption[0])) {
                        Files.move(oldFormatRootFolder, fixedFolder, new CopyOption[0]);
                    }
                }
            } catch (InvalidPathException e) {
            }
        }
    }

    public void onRootContainerAdded(MinimapWorldRootContainer rootContainer) {
        if (!rootContainer.isConfigLoaded()) {
            this.rootConfigIO.load(rootContainer);
        }
    }

    public Path getWorldFile(MinimapWorld w) throws IOException {
        Path containerFolderPath = w.getContainer().getDirectoryPath();
        if (!Files.exists(containerFolderPath, new LinkOption[0])) {
            Files.createDirectories(containerFolderPath, new FileAttribute[0]);
        }
        String fileName = w.getNode();
        String worldName = w.getContainer().getName(w.getNode());
        if (worldName != null) {
            fileName = fileName + "_" + worldName.replace("_", "%us%").replace(":", "§§");
        }
        return containerFolderPath.resolve(fileName + ".txt");
    }

    public RootConfigIO getRootConfigIO() {
        return this.rootConfigIO;
    }
}
