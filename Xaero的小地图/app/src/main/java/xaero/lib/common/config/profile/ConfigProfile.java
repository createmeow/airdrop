package xaero.lib.common.config.profile;

import java.util.Map;
import java.util.Set;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/ConfigProfile.class */
public class ConfigProfile extends Config implements IConfigProfileInfo {
    private final String id;

    private ConfigProfile(String id, Map<ConfigOption<?>, Object> values, boolean allowNullValues, Set<ConfigOption<?>> dirtyOptions, Set<ConfigOption<?>> removedLargeOptions, Map<ConfigOption<?>, String> failedEncodedValues, ConfigOptionManager options) {
        super(values, allowNullValues, dirtyOptions, removedLargeOptions, failedEncodedValues, options);
        this.id = id;
    }

    @Override // xaero.lib.common.config.Config
    public <T> T set(ConfigOption<T> configOption, T t) {
        if (configOption == BuiltInProfiledConfigOptions.PROFILE_NAME && t == null) {
            t = configOption.getDefaultValue();
        }
        return (T) super.set(configOption, t);
    }

    @Override // xaero.lib.common.config.profile.IConfigProfileInfo
    public String getId() {
        return this.id;
    }

    @Override // xaero.lib.common.config.profile.IConfigProfileInfo
    public String getName() {
        return (String) get(BuiltInProfiledConfigOptions.PROFILE_NAME);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/ConfigProfile$Builder.class */
    public static final class Builder extends Config.Builder<ConfigProfile, Builder> {
        private String id;

        @Override // xaero.lib.common.config.Config.Builder
        protected /* bridge */ /* synthetic */ Config buildInternally(Map map, boolean z, Set set, Set set2, Map map2) {
            return buildInternally((Map<ConfigOption<?>, Object>) map, z, (Set<ConfigOption<?>>) set, (Set<ConfigOption<?>>) set2, (Map<ConfigOption<?>, String>) map2);
        }

        private Builder() {
        }

        @Override // xaero.lib.common.config.Config.Builder
        public Builder setDefault() {
            setId(null);
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        @Override // xaero.lib.common.config.Config.Builder
        public ConfigProfile build() {
            if (this.id == null) {
                throw new IllegalStateException();
            }
            return (ConfigProfile) super.build();
        }

        @Override // xaero.lib.common.config.Config.Builder
        protected ConfigProfile buildInternally(Map<ConfigOption<?>, Object> values, boolean allowNullValues, Set<ConfigOption<?>> dirtyOptions, Set<ConfigOption<?>> removedLargeOptions, Map<ConfigOption<?>, String> failedSerializedValues) {
            if (!values.containsKey(BuiltInProfiledConfigOptions.PROFILE_NAME)) {
                values.put(BuiltInProfiledConfigOptions.PROFILE_NAME, BuiltInProfiledConfigOptions.PROFILE_NAME.getDefaultValue());
            }
            return new ConfigProfile(this.id, values, allowNullValues, dirtyOptions, removedLargeOptions, failedSerializedValues, this.options);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
