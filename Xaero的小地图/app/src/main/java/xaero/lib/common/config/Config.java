package xaero.lib.common.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import xaero.lib.common.config.listener.IConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.type.LargeConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/Config.class */
public class Config {
    private final Map<ConfigOption<?>, Object> values;
    private final boolean allowNullValues;
    private IConfigChangeListener changeListener;
    private final Set<ConfigOption<?>> dirtyOptions;
    private final Set<ConfigOption<?>> removedLargeOptions;
    private final Map<ConfigOption<?>, String> failedSerializedValues;
    protected final ConfigOptionManager options;

    protected Config(Map<ConfigOption<?>, Object> values, boolean allowNullValues, Set<ConfigOption<?>> dirtyOptions, Set<ConfigOption<?>> removedLargeOptions, Map<ConfigOption<?>, String> failedSerializedValues, ConfigOptionManager options) {
        this.values = values;
        this.allowNullValues = allowNullValues;
        this.dirtyOptions = dirtyOptions;
        this.removedLargeOptions = removedLargeOptions;
        this.failedSerializedValues = failedSerializedValues;
        this.options = options;
    }

    public <T> T get(ConfigOption<T> configOption) {
        if (this.allowNullValues && getFailedSerializedValue(configOption) != null) {
            return null;
        }
        return (T) this.values.get(configOption);
    }

    public <T> T set(ConfigOption<T> configOption, T t) {
        Object objRemove;
        if (!this.allowNullValues && t == null) {
            throw new IllegalArgumentException("Null values not allowed in this config!");
        }
        if (t != null && !configOption.isValidValue(t)) {
            throw new IllegalArgumentException("The provided value is not valid for option " + configOption.getId());
        }
        this.failedSerializedValues.remove(configOption);
        if (t != null) {
            objRemove = this.values.put(configOption, t);
        } else {
            objRemove = this.values.remove(configOption);
        }
        if (Objects.equals(objRemove, t)) {
            return (T) objRemove;
        }
        if (t == null) {
            handleValueRemoval(configOption);
        }
        if (objRemove == null) {
            handleValueAddition(configOption);
        }
        if (this.dirtyOptions != null) {
            this.dirtyOptions.add(configOption);
        }
        if (this.changeListener != null) {
            this.changeListener.onChange(this, configOption);
        }
        return (T) objRemove;
    }

    public void reset() {
        clearDirtyOptions();
        this.failedSerializedValues.clear();
        if (!this.allowNullValues) {
            for (ConfigOption<?> option : this.values.keySet()) {
                setOptionDefault(option);
            }
            return;
        }
        Iterator it = new HashSet(this.values.keySet()).iterator();
        while (it.hasNext()) {
            ConfigOption<?> option2 = (ConfigOption) it.next();
            set(option2, null);
        }
    }

    public Iterable<ConfigOption<?>> usedOptions() {
        return this.values.keySet();
    }

    public Iterable<ConfigOption<?>> getDirtyOptions() {
        return this.dirtyOptions;
    }

    public int getChangeCount() {
        if (this.dirtyOptions == null) {
            return 0;
        }
        return this.dirtyOptions.size();
    }

    public void clearDirtyOptions() {
        if (this.dirtyOptions != null) {
            this.dirtyOptions.clear();
        }
    }

    public void setChangeListener(IConfigChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public IConfigChangeListener getChangeListener() {
        return this.changeListener;
    }

    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    private <T> void copyOptionValue(ConfigOption<T> option, Object value, Config config) {
        config.set(option, value);
    }

    public <T> void addFailedSerializedValue(ConfigOption<T> option, String serializedValue) {
        set(option, option.getDefaultValue());
        this.failedSerializedValues.put(option, serializedValue);
    }

    public String getFailedSerializedValue(ConfigOption<?> option) {
        return this.failedSerializedValues.get(option);
    }

    private <T> void handleValueRemoval(ConfigOption<T> option) {
        if (!(option.getValueType() instanceof LargeConfigValueType)) {
            return;
        }
        this.removedLargeOptions.add(option);
    }

    private <T> void handleValueAddition(ConfigOption<T> option) {
        if (!(option.getValueType() instanceof LargeConfigValueType)) {
            return;
        }
        this.removedLargeOptions.remove(option);
    }

    public Iterable<ConfigOption<?>> getRemovedLargeOptions() {
        return this.removedLargeOptions;
    }

    public void postSave() {
        this.removedLargeOptions.clear();
    }

    public void setDefaults() {
        if (this.options == null) {
            throw new UnsupportedOperationException();
        }
        Iterator<ConfigOption<?>> it = this.options.iterator();
        while (it.hasNext()) {
            ConfigOption<?> option = it.next();
            if (!option.skipConfigReset()) {
                setOptionDefault(option);
            }
        }
    }

    public <T> void setOptionDefault(ConfigOption<T> option) {
        set(option, this.allowNullValues ? null : option.getDefaultValue());
    }

    public void copyOptionsFrom(Config other) {
        if (this.options == null) {
            throw new UnsupportedOperationException();
        }
        Iterator<ConfigOption<?>> it = this.options.iterator();
        while (it.hasNext()) {
            ConfigOption<?> option = it.next();
            copyOptionFrom(option, other);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void copyOptionFrom(ConfigOption<T> option, Config other) {
        set(option, other.get(option));
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/Config$Builder.class */
    public static abstract class Builder<C extends Config, B extends Builder<C, B>> {
        protected final B self = this;
        private boolean allowNullValues;
        private boolean trackDirtyOptions;
        protected ConfigOptionManager options;

        protected abstract C buildInternally(Map<ConfigOption<?>, Object> map, boolean z, Set<ConfigOption<?>> set, Set<ConfigOption<?>> set2, Map<ConfigOption<?>, String> map2);

        protected Builder() {
        }

        public B setDefault() {
            setAllowNullValues(false);
            setOptions(null);
            setTrackDirtyOptions(false);
            return this.self;
        }

        public B setAllowNullValues(boolean allowNullValues) {
            this.allowNullValues = allowNullValues;
            return this.self;
        }

        public B setOptions(ConfigOptionManager options) {
            this.options = options;
            return this.self;
        }

        public B setTrackDirtyOptions(boolean trackDirtyOptions) {
            this.trackDirtyOptions = trackDirtyOptions;
            return this.self;
        }

        public C build() {
            if (!this.allowNullValues && this.options == null) {
                throw new IllegalStateException();
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if (!this.allowNullValues) {
                Iterator<ConfigOption<?>> it = this.options.iterator();
                while (it.hasNext()) {
                    ConfigOption<?> next = it.next();
                    linkedHashMap.put(next, next.getDefaultValue());
                }
            }
            return (C) buildInternally(linkedHashMap, this.allowNullValues, this.trackDirtyOptions ? new HashSet<>() : null, new HashSet(), new HashMap());
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/Config$FinalBuilder.class */
    public static final class FinalBuilder extends Builder<Config, FinalBuilder> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.Config.Builder
        protected Config buildInternally(Map<ConfigOption<?>, Object> values, boolean allowNullValues, Set<ConfigOption<?>> dirtyOptions, Set<ConfigOption<?>> removedLargeOptions, Map<ConfigOption<?>, String> failedSerializedValues) {
            return new Config(values, allowNullValues, dirtyOptions, removedLargeOptions, failedSerializedValues, this.options);
        }

        public static FinalBuilder begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
