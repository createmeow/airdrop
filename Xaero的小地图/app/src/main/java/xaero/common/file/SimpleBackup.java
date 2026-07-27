package xaero.common.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.EnumSet;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/file/SimpleBackup.class */
public class SimpleBackup {
    public static Path moveToBackup(Path directory) {
        return moveToBackup(directory.getParent(), directory);
    }

    public static Path moveToBackup(Path backupFolderParent, Path directory) throws IOException {
        Path pathResolve = backupFolderParent.resolve("backup");
        while (true) {
            Path backupFolder = pathResolve;
            if (Files.exists(backupFolder, new LinkOption[0])) {
                pathResolve = backupFolder.getParent().resolve(backupFolder.getFileName().toString() + "-");
            } else {
                Path backupPath = backupFolder.resolve(directory.getFileName());
                try {
                    Files.createDirectories(backupFolder, new FileAttribute[0]);
                    Files.move(directory, backupPath, new CopyOption[0]);
                    return backupPath;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to backup a directory! Can't continue.", e);
                }
            }
        }
    }

    public static void copyDirectoryWithContents(final Path from, final Path to, int maxDepth, final CopyOption... copyOptions) throws IOException {
        Files.walkFileTree(from, EnumSet.of(FileVisitOption.FOLLOW_LINKS), maxDepth, new SimpleFileVisitor<Path>() { // from class: xaero.common.file.SimpleBackup.1
            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetPath = to.resolve(from.relativize(dir));
                if (!Files.exists(targetPath, new LinkOption[0])) {
                    Files.createDirectory(targetPath, new FileAttribute[0]);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, to.resolve(from.relativize(file)), copyOptions);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
