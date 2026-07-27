package xaero.lib.common.config.profile.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import java.util.stream.Stream;
import xaero.lib.common.config.io.ConfigIO;
import xaero.lib.common.config.io.serialization.IConfigSerializer;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.ConfigProfileManager;
import xaero.lib.common.config.profile.io.serialization.cfg.ConfigProfileCfgSerializer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/io/ConfigProfileManagerIO.class */
public final class ConfigProfileManagerIO {
    private final ConfigProfileManager manager;
    private final Path path;
    private final Path defaultConfigsPath;
    private final String extension;
    private final ConfigIO<ConfigProfile> configIO;

    private ConfigProfileManagerIO(ConfigProfileManager manager, Path path, Path defaultConfigsPath, String extension, ConfigIO<ConfigProfile> configIO) {
        this.manager = manager;
        this.path = path;
        this.defaultConfigsPath = defaultConfigsPath;
        this.extension = extension;
        this.configIO = configIO;
    }

    public void load() throws IOException {
        this.manager.reset();
        Path pathToLoad = this.path;
        if (!Files.exists(this.path, new LinkOption[0])) {
            if (this.defaultConfigsPath != null && Files.exists(this.defaultConfigsPath, new LinkOption[0])) {
                pathToLoad = this.defaultConfigsPath;
            } else {
                return;
            }
        }
        load(pathToLoad, 10);
    }

    private void load(Path pathToLoad, int attempts) throws IOException {
        if (!Files.isDirectory(pathToLoad, new LinkOption[0])) {
            loadFile(pathToLoad);
            return;
        }
        try {
            Stream<Path> allFiles = Files.list(pathToLoad);
            try {
                allFiles.forEach(fileOrFolder -> {
                    if (Files.isDirectory(fileOrFolder, new LinkOption[0])) {
                        return;
                    }
                    loadFile(fileOrFolder);
                });
                if (allFiles != null) {
                    allFiles.close();
                }
            } finally {
            }
        } catch (IOException ioe) {
            int attempts2 = attempts - 1;
            if (attempts2 < 1) {
                throw new RuntimeException("Failed to load config profiles!", ioe);
            }
            load(pathToLoad, attempts2);
        }
    }

    private void loadFile(Path file) {
        loadFile(file, 10);
    }

    private void loadFile(Path file, int attempts) {
        ConfigProfile loadedProfile;
        String fileName = file.getFileName().toString();
        if (!fileName.endsWith(this.extension) || (loadedProfile = (ConfigProfile) this.configIO.load(file, attempts)) == null) {
            return;
        }
        this.manager.add(loadedProfile);
    }

    public void saveAll() throws InterruptedException, IOException {
        Iterator<ConfigProfile> it = this.manager.iterator();
        while (it.hasNext()) {
            ConfigProfile profile = it.next();
            save(profile);
        }
    }

    public void save(ConfigProfile profile) throws InterruptedException, IOException {
        save(profile, 10);
    }

    private void save(ConfigProfile profile, int attempts) throws InterruptedException, IOException {
        Path file = getProfilePath(profile);
        if (file == null) {
            return;
        }
        this.configIO.save(profile, file, attempts);
    }

    private Path getProfilePath(ConfigProfile profile) {
        Path file;
        boolean pathIsFolder = !this.path.getFileName().toString().endsWith(this.extension);
        if (!createDirectories(pathIsFolder, 10)) {
            return null;
        }
        if (pathIsFolder) {
            file = this.path.resolve(profile.getId() + this.extension);
        } else {
            file = this.path;
        }
        return file;
    }

    public void delete(ConfigProfile profile) throws InterruptedException {
        delete(profile, 10);
    }

    public void delete(ConfigProfile profile, int attempts) throws InterruptedException {
        Path file = getProfilePath(profile);
        if (file == null) {
            return;
        }
        this.configIO.delete(profile, file, attempts);
    }

    private boolean createDirectories(boolean pathIsFolder, int attempts) throws IOException {
        try {
            if (!Files.exists(this.path.getParent(), new LinkOption[0])) {
                Files.createDirectories(this.path.getParent(), new FileAttribute[0]);
            }
            if (pathIsFolder && !Files.exists(this.path, new LinkOption[0])) {
                Files.createDirectories(this.path, new FileAttribute[0]);
                return true;
            }
            return true;
        } catch (IOException e) {
            int attempts2 = attempts - 1;
            if (attempts2 < 1) {
                this.manager.logger.error("Failed to create config profile directory!", e);
                return false;
            }
            return createDirectories(pathIsFolder, attempts2);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/io/ConfigProfileManagerIO$Builder.class */
    public static final class Builder {
        private ConfigProfileManager manager;
        private Path path;
        private Path defaultConfigsPath;
        private boolean singleFile;
        private String extension;
        private IConfigSerializer<ConfigProfile> serializer;
        private ConfigOptionManager optionsDefault;
        private boolean allowNullValues;
        private boolean configsTrackDirtyOptionsDefault;

        private Builder() {
        }

        public Builder setDefault() {
            setManager(null);
            setPath(null);
            setDefaultConfigsPath(null);
            setSingleFile(false);
            setExtension(null);
            setSerializer(null);
            setOptionsDefault(null);
            setAllowNullValues(false);
            setConfigsTrackDirtyOptionsDefault(false);
            return this;
        }

        public Builder setManager(ConfigProfileManager manager) {
            this.manager = manager;
            return this;
        }

        public Builder setPath(Path path) {
            this.path = path;
            return this;
        }

        public Builder setDefaultConfigsPath(Path defaultConfigsPath) {
            this.defaultConfigsPath = defaultConfigsPath;
            return this;
        }

        public Builder setSingleFile(boolean singleFile) {
            this.singleFile = singleFile;
            return this;
        }

        public Builder setExtension(String extension) {
            this.extension = extension;
            return this;
        }

        public Builder setSerializer(IConfigSerializer<ConfigProfile> serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder setOptionsDefault(ConfigOptionManager optionsDefault) {
            this.optionsDefault = optionsDefault;
            return this;
        }

        public Builder setAllowNullValues(boolean allowNullValues) {
            this.allowNullValues = allowNullValues;
            return this;
        }

        public Builder setConfigsTrackDirtyOptionsDefault(boolean configsTrackDirtyOptionsDefault) {
            this.configsTrackDirtyOptionsDefault = configsTrackDirtyOptionsDefault;
            return this;
        }

        public ConfigProfileManagerIO build() {
            if (this.manager == null || this.path == null || this.extension == null || (this.serializer == null && this.optionsDefault == null)) {
                throw new IllegalStateException();
            }
            if (this.serializer == null) {
                this.serializer = ((ConfigProfileCfgSerializer.Builder) ((ConfigProfileCfgSerializer.Builder) ConfigProfileCfgSerializer.Builder.begin().setConfigType(this.manager.getConfigType()).setOptions(this.optionsDefault)).setConfigsTrackDirtyOptions(this.configsTrackDirtyOptionsDefault)).build();
            }
            if (this.singleFile && !this.path.getFileName().toString().endsWith(this.extension)) {
                this.path = this.path.resolveSibling(String.valueOf(this.path.getFileName()) + this.extension);
                if (this.defaultConfigsPath != null) {
                    this.defaultConfigsPath = this.defaultConfigsPath.resolveSibling(this.path.getFileName());
                }
            }
            ConfigIO<ConfigProfile> configIO = ConfigIO.Builder.begin().setLogger(this.manager.logger).setSerializer(this.serializer).setAllowNullValues(this.allowNullValues).build();
            return new ConfigProfileManagerIO(this.manager, this.path, this.defaultConfigsPath, this.extension, configIO);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
