package xaero.lib.common.config.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import org.apache.logging.log4j.Logger;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.io.serialization.IConfigSerializer;
import xaero.lib.common.config.io.util.ConfigIOUtils;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.type.LargeConfigValueType;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/ConfigIO.class */
public class ConfigIO<C extends Config> {
    public static final int IO_ATTEMPTS = 10;
    public final Logger logger;
    private final IConfigSerializer<C> serializer;
    private final boolean allowNullValues;

    protected ConfigIO(Logger logger, IConfigSerializer<C> serializer, boolean allowNullValues) {
        this.logger = logger;
        this.serializer = serializer;
        this.allowNullValues = allowNullValues;
    }

    public C load(Path path) {
        return (C) load(path, 10);
    }

    public C load(Path path, int i) throws InterruptedException, IOException {
        if (!Files.exists(path, new LinkOption[0])) {
            return null;
        }
        String string = path.getFileName().toString();
        int iLastIndexOf = string.lastIndexOf(46);
        while (true) {
            int i2 = iLastIndexOf;
            if (i2 > 0) {
                string = string.substring(0, i2);
                iLastIndexOf = string.lastIndexOf(46);
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(path.toFile());
                    try {
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        try {
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            try {
                                StringBuilder sb = new StringBuilder();
                                bufferedReader.lines().forEach(line -> {
                                    sb.append(line).append("\n");
                                });
                                C c = (C) this.serializer.deserialize(sb.toString(), this.allowNullValues, string, path);
                                bufferedReader.close();
                                inputStreamReader.close();
                                fileInputStream.close();
                                return c;
                            } catch (Throwable th) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            try {
                                inputStreamReader.close();
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                            }
                            throw th3;
                        }
                    } finally {
                    }
                } catch (IOException e) {
                    int i3 = i - 1;
                    if (i3 < 1) {
                        throw new RuntimeException("Failed to load config " + string + "!", e);
                    }
                    this.logger.warn("IO exception trying to load config {}. Retrying...", string);
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e2) {
                    }
                    return (C) load(path, i3);
                }
            }
        }
    }

    public void save(C config, Path file) throws InterruptedException, IOException {
        save(config, file, 10);
    }

    public void save(C config, Path file, int attempts) throws InterruptedException, IOException {
        Path fileTemp = file.resolveSibling(file.getFileName().toString() + ".temp");
        try {
            FileOutputStream outputStream = new FileOutputStream(fileTemp.toFile());
            try {
                OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
                try {
                    BufferedWriter writer = new BufferedWriter(outputWriter);
                    try {
                        String serializedProfile = this.serializer.serialize(config, file);
                        writer.write(serializedProfile);
                        writer.close();
                        IOUtils.safeMoveAndReplace(fileTemp, file, true);
                        writer.close();
                        outputWriter.close();
                        outputStream.close();
                        for (ConfigOption<?> removedLargeOption : config.getRemovedLargeOptions()) {
                            deleteLargeOptionFile(file, removedLargeOption);
                        }
                        config.postSave();
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
                        outputWriter.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                }
            } finally {
            }
        } catch (IOException e) {
            int attempts2 = attempts - 1;
            if (attempts2 < 1) {
                this.logger.error("Failed to save config {}!", file.getFileName().toString(), e);
                return;
            }
            this.logger.warn("IO exception trying to save config {}. Retrying...", file.getFileName().toString());
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e2) {
            }
            save(config, file, attempts2);
        }
    }

    private void deleteLargeOptionFile(Path originalConfigFile, ConfigOption<?> option) throws InterruptedException {
        LargeConfigValueType<?> type = (LargeConfigValueType) option.getValueType();
        String subFolder = option.getId();
        String extension = type.getIoCodec().getExtension();
        Path largeValuePath = ConfigIOUtils.getLargeValueFilePath(originalConfigFile, subFolder, extension);
        if (!Files.exists(largeValuePath, new LinkOption[0])) {
            return;
        }
        try {
            IOUtils.deleteFile(largeValuePath, 2);
        } catch (IOException e) {
            this.logger.error("Failed to delete large value file {}", largeValuePath, e);
        }
    }

    public void delete(C config, Path file, int attempts) throws InterruptedException {
        String debugId = file.getFileName().toString();
        for (ConfigOption<?> option : config.usedOptions()) {
            if (option.getValueType() instanceof LargeConfigValueType) {
                deleteLargeOptionFile(file, option);
            }
        }
        try {
            IOUtils.deleteFile(file, attempts);
        } catch (IOException e) {
            this.logger.error("Failed to delete config profile {}.", debugId);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/ConfigIO$Builder.class */
    public static final class Builder<C extends Config> {
        private IConfigSerializer<C> serializer;
        private boolean allowNullValues;
        public Logger logger;

        private Builder() {
        }

        public Builder<C> setDefault() {
            setLogger(null);
            setSerializer(null);
            setAllowNullValues(false);
            return this;
        }

        public Builder<C> setLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder<C> setSerializer(IConfigSerializer<C> serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder<C> setAllowNullValues(boolean allowNullValues) {
            this.allowNullValues = allowNullValues;
            return this;
        }

        public ConfigIO<C> build() {
            if (this.logger == null || this.serializer == null) {
                throw new IllegalStateException();
            }
            return new ConfigIO<>(this.logger, this.serializer, this.allowNullValues);
        }

        public static <C extends Config> Builder<C> begin() {
            return new Builder().setDefault();
        }
    }
}
