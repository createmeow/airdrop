package xaero.lib.common.config.option.value.io.serialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.function.Function;
import javax.annotation.Nullable;
import xaero.lib.XaeroLib;
import xaero.lib.common.config.io.util.ConfigIOUtils;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/io/serialization/LargeConfigValueIOCodec.class */
public class LargeConfigValueIOCodec<T> extends ConfigValueIOCodec<T> {
    private static final int IO_ATTEMPTS = 10;
    private final String extension;

    public LargeConfigValueIOCodec(Function<T, String> encoder, Function<String, T> decoder, int maxStringLength, String extension) {
        super(encoder, decoder, maxStringLength);
        this.extension = extension;
    }

    @Override // xaero.lib.common.config.option.value.io.serialization.ConfigValueIOCodec
    public String encode(T value, @Nullable Path file, @Nullable ConfigOption<T> option) throws InterruptedException, IOException {
        String encodedValue = super.encode(value, file, option);
        if (file == null) {
            return encodedValue;
        }
        String subFolder = option.getId();
        Path largeValueFilePath = ConfigIOUtils.getLargeValueFilePath(file, subFolder, this.extension);
        saveValueToFile(largeValueFilePath, encodedValue, 10);
        return "This option's value is saved to sub-folder \"" + subFolder + "\".";
    }

    @Override // xaero.lib.common.config.option.value.io.serialization.ConfigValueIOCodec
    public T decode(String str, @Nullable Path path, @Nullable ConfigOption<T> configOption) throws InterruptedException, IOException {
        if (path == null) {
            return (T) super.decode(str, null, configOption);
        }
        Path largeValueFilePath = ConfigIOUtils.getLargeValueFilePath(path, configOption.getId(), this.extension);
        String strLoadValueFromFile = loadValueFromFile(largeValueFilePath, 10);
        if (strLoadValueFromFile == null) {
            return null;
        }
        try {
            return (T) super.decode(strLoadValueFromFile, path, configOption);
        } catch (Throwable th) {
            XaeroLib.LOGGER.error("Error trying to decode data loaded from file {}! It's likely corrupt. Resolving...", largeValueFilePath);
            try {
                XaeroLib.LOGGER.error(String.format("The broken file was backed up to %s and ignored.", IOUtils.tryQuickFileBackupMove(largeValueFilePath, 10)), th);
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveValueToFile(Path path, String encodedValue, int attempts) throws InterruptedException, IOException {
        Path pathParent = path.getParent();
        try {
            if (!Files.exists(pathParent, new LinkOption[0])) {
                Files.createDirectories(pathParent, new FileAttribute[0]);
            }
            File file = path.toFile();
            FileOutputStream fileOutput = new FileOutputStream(file);
            try {
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutput);
                try {
                    BufferedWriter writer = new BufferedWriter(outputWriter);
                    try {
                        writer.write(encodedValue);
                        writer.close();
                        outputWriter.close();
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
            if (attempts2 > 0) {
                XaeroLib.LOGGER.warn("IO exception trying to save config value to {}. Retrying...", path, e);
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                }
                saveValueToFile(path, encodedValue, attempts2);
                return;
            }
            XaeroLib.LOGGER.error("Failed to save config value to {}", path, e);
        }
    }

    private String loadValueFromFile(Path path, int attempts) throws InterruptedException, IOException {
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                return null;
            }
            File file = path.toFile();
            FileInputStream inputStream = new FileInputStream(file);
            try {
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                try {
                    BufferedReader reader = new BufferedReader(inputReader);
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        reader.lines().forEach(line -> {
                            if (!stringBuilder.isEmpty()) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append(line);
                        });
                        String string = stringBuilder.toString();
                        reader.close();
                        inputReader.close();
                        inputStream.close();
                        return string;
                    } catch (Throwable th) {
                        try {
                            reader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    try {
                        inputReader.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                }
            } finally {
            }
        } catch (IOException e) {
            int attempts2 = attempts - 1;
            if (attempts2 > 0) {
                XaeroLib.LOGGER.warn("IO exception trying to load config value from {}. Retrying...", path, e);
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                }
                return loadValueFromFile(path, attempts2);
            }
            throw new RuntimeException("Failed to load config value from file " + String.valueOf(path) + ". Try restarting your computer.", e);
        }
    }

    public String getExtension() {
        return this.extension;
    }
}
