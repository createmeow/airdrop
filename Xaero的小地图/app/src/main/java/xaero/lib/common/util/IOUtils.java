package xaero.lib.common.util;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;
import xaero.lib.XaeroLib;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/IOUtils.class */
public class IOUtils {
    public static Path quickFileBackupMove(Path file) throws IOException {
        int backupNumber = 0;
        while (true) {
            Path backupPath = file.resolveSibling(file.getFileName().toString() + ".backup" + backupNumber);
            if (Files.exists(backupPath, new LinkOption[0])) {
                backupNumber++;
            } else {
                Files.move(file, backupPath, new CopyOption[0]);
                return backupPath;
            }
        }
    }

    public static Path tryQuickFileBackupMove(Path file, int attempts) throws IOException {
        try {
            XaeroLib.LOGGER.info("Attempting to back up file {}", file);
            return quickFileBackupMove(file);
        } catch (IOException e) {
            int attempts2 = attempts - 1;
            if (attempts2 > 0) {
                XaeroLib.LOGGER.info("Failed due to an IO exception. A computer restart might help. Retrying... {}", Integer.valueOf(attempts2));
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                }
                return tryQuickFileBackupMove(file, attempts2);
            }
            throw e;
        }
    }

    public static void safeMoveAndReplace(Path from, Path to, boolean backupFrom) throws InterruptedException, IOException {
        Path fromBackupPath;
        Path backupPath = null;
        if (backupFrom) {
            while (true) {
                try {
                    fromBackupPath = quickFileBackupMove(from);
                    break;
                } catch (IOException e) {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e2) {
                    }
                }
            }
        } else {
            fromBackupPath = from;
        }
        if (Files.exists(to, new LinkOption[0])) {
            backupPath = quickFileBackupMove(to);
        }
        Files.move(fromBackupPath, to, new CopyOption[0]);
        if (backupPath != null) {
            Files.delete(backupPath);
        }
    }

    public static void deleteFile(Path file, int attempts) throws InterruptedException, IOException {
        deleteFileIf(file, path -> {
            return true;
        }, attempts);
    }

    public static void deleteFileIf(Path file, final Predicate<Path> condition, int attempts) throws InterruptedException, IOException {
        int attempts2 = attempts - 1;
        try {
            Files.walkFileTree(file, new SimpleFileVisitor<Path>() { // from class: xaero.lib.common.util.IOUtils.1
                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                    if (condition.test(path)) {
                        Files.delete(path);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public FileVisitResult postVisitDirectory(Path path, IOException iOException) throws IOException {
                    if (iOException != null) {
                        throw iOException;
                    }
                    if (condition.test(path)) {
                        Files.delete(path);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            if (attempts2 > 0) {
                XaeroLib.LOGGER.info("Failed to delete file/folder! Retrying... " + attempts2);
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                }
                deleteFileIf(file, condition, attempts2);
                return;
            }
            throw e;
        }
    }

    public static String replaceTrailingDots(String string, char replacement) {
        StringBuilder path = new StringBuilder(string);
        int dotCount = 0;
        while (!path.isEmpty() && path.charAt(path.length() - 1) == '.') {
            path.deleteCharAt(path.length() - 1);
            dotCount++;
        }
        for (int i = 0; i < dotCount; i++) {
            path.append(replacement);
        }
        return path.toString();
    }
}
