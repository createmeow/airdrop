package xaero.hud.minimap.radar.category;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.category.serialization.EntityRadarCategorySerializationHandler;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/EntityRadarCategoryFileIO.class */
public final class EntityRadarCategoryFileIO {
    private final Path saveLocationPath;
    private final EntityRadarCategorySerializationHandler serializationHandler;

    private EntityRadarCategoryFileIO(@Nonnull Path saveLocationPath, @Nonnull EntityRadarCategorySerializationHandler serializationHandler) {
        this.saveLocationPath = saveLocationPath;
        this.serializationHandler = serializationHandler;
    }

    public void saveRootCategory(EntityRadarCategory category) {
        Path saveLocationTempPath = this.saveLocationPath.resolveSibling(this.saveLocationPath.getFileName().toString() + ".temp");
        String serializedData = this.serializationHandler.serialize(category);
        saveRootCategory(saveLocationTempPath, serializedData, 10);
    }

    public void saveRootCategory(Path saveLocationTempPath, String serializedData, int attempts) throws InterruptedException, IOException {
        try {
            FileOutputStream fileOutput = new FileOutputStream(saveLocationTempPath.toFile());
            try {
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(bufferedOutput, StandardCharsets.UTF_8);
                    try {
                        writer.write(serializedData);
                        writer.close();
                        IOUtils.safeMoveAndReplace(saveLocationTempPath, this.saveLocationPath, true);
                        writer.close();
                        bufferedOutput.close();
                        fileOutput.close();
                    } catch (Throwable th) {
                        try {
                            writer.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    try {
                        bufferedOutput.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                }
            } finally {
            }
        } catch (IOException e) {
            if (attempts <= 1) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
                return;
            }
            MinimapLogs.LOGGER.info("Failed to save entity radar categories. Retrying... " + attempts);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e2) {
            }
            saveRootCategory(saveLocationTempPath, serializedData, attempts - 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public EntityRadarCategory loadRootCategory() throws IOException {
        FileInputStream fileInput = new FileInputStream(this.saveLocationPath.toFile());
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput, "UTF8"));
            try {
                StringBuilder stringBuilder = new StringBuilder();
                reader.lines().forEach(line -> {
                    stringBuilder.append(line).append('\n');
                });
                String serializedData = stringBuilder.toString();
                reader.close();
                fileInput.close();
                try {
                    return (EntityRadarCategory) this.serializationHandler.deserialize(serializedData);
                } catch (Throwable t) {
                    MinimapLogs.LOGGER.error("A minimap entity radar config file is not usable (is likely corrupt)! Resolving...");
                    Path backupPath = IOUtils.quickFileBackupMove(this.saveLocationPath);
                    MinimapLogs.LOGGER.error(String.format("The broken file was backed up to %s and ignored.", backupPath), t);
                    return null;
                }
            } finally {
            }
        } catch (Throwable th) {
            try {
                fileInput.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/EntityRadarCategoryFileIO$Builder.class */
    public static final class Builder {
        private Path saveLocationPath;
        private final EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder;

        private Builder(EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder) {
            this.serializationHandlerBuilder = serializationHandlerBuilder;
        }

        private Builder setDefault() {
            this.saveLocationPath = null;
            return this;
        }

        public Builder setSaveLocationPath(Path saveLocationPath) {
            this.saveLocationPath = saveLocationPath;
            return this;
        }

        public EntityRadarCategoryFileIO build() {
            if (this.saveLocationPath == null || this.serializationHandlerBuilder == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return new EntityRadarCategoryFileIO(this.saveLocationPath, this.serializationHandlerBuilder.build());
        }

        public static Builder begin(EntityRadarCategorySerializationHandler.Builder serializationHandlerBuilder) {
            return new Builder(serializationHandlerBuilder).setDefault();
        }
    }
}
