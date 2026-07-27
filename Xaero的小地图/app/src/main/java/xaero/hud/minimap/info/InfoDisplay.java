package xaero.hud.minimap.info;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import xaero.common.gui.GuiInfoDisplayEdit;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.info.config.InfoDisplayConfigData;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.info.codec.InfoDisplayStateCodec;
import xaero.hud.minimap.info.render.compile.InfoDisplayOnCompile;
import xaero.hud.minimap.info.widget.InfoDisplayWidgetFactory;
import xaero.lib.common.config.option.value.io.serialization.ConfigValueIOCodec;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/InfoDisplay.class */
public final class InfoDisplay<T> {
    private final String id;
    private final Component name;
    private final T defaultState;
    private final ConfigValueIOCodec<T> codec;
    private final InfoDisplayWidgetFactory<T> widgetFactory;
    private final InfoDisplayOnCompile<T> compiler;
    private final Function<ModSettings, T> legacyGetter;
    private InfoDisplay<T>.ConfigCache configCache;
    private InfoDisplayManager manager;

    private InfoDisplay(String id, Component name, T defaultState, ConfigValueIOCodec<T> codec, InfoDisplayWidgetFactory<T> widgetFactory, InfoDisplayOnCompile<T> compiler, Function<ModSettings, T> legacyGetter) {
        this.id = id;
        this.name = name;
        this.defaultState = defaultState;
        this.codec = codec;
        this.widgetFactory = widgetFactory;
        this.compiler = compiler;
        this.legacyGetter = legacyGetter;
    }

    public void setManager(InfoDisplayManager manager) {
        if (this.manager != null) {
            throw new IllegalStateException();
        }
        this.manager = manager;
    }

    @Deprecated
    public T getState() {
        return getEffectiveState();
    }

    public T getEffectiveState() {
        if (this.manager == null) {
            throw new IllegalStateException("The info display must be added to a manager first!");
        }
        updateConfigCache();
        return ((ConfigCache) this.configCache).state;
    }

    public int getTextColor() {
        if (this.manager == null) {
            throw new IllegalStateException("The info display must be added to a manager first!");
        }
        updateConfigCache();
        return ((ConfigCache) this.configCache).textColor;
    }

    public int getBackgroundColor() {
        if (this.manager == null) {
            throw new IllegalStateException("The info display must be added to a manager first!");
        }
        updateConfigCache();
        return ((ConfigCache) this.configCache).backgroundColor;
    }

    private void updateConfigCache() {
        if (this.configCache == null) {
            this.configCache = new ConfigCache();
        }
    }

    public String getId() {
        return this.id;
    }

    public Component getName() {
        return this.name;
    }

    public T getDefaultState() {
        return this.defaultState;
    }

    public ConfigValueIOCodec<T> getCodec() {
        return this.codec;
    }

    public AbstractWidget createWidget(int x, int y, int w, int h, GuiInfoDisplayEdit.MoveableEntry<T> entry, Runnable onChange, boolean includeNull) {
        return this.widgetFactory.create(x, y, w, h, entry, onChange, includeNull);
    }

    public InfoDisplayOnCompile<T> getCompiler() {
        return this.compiler;
    }

    public void clearStateCache() {
        this.configCache = null;
    }

    public T getLegacyValue(ModSettings legacySettings) {
        if (this.legacyGetter == null || legacySettings == null) {
            return null;
        }
        return this.legacyGetter.apply(legacySettings);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/InfoDisplay$ConfigCache.class */
    private class ConfigCache {
        private static final Function<InfoDisplayConfigData, Integer> BG_COLOR_GETTER = (v0) -> {
            return v0.getBackgroundColor();
        };
        private static final Function<InfoDisplayConfigData, Integer> TEXT_COLOR_GETTER = (v0) -> {
            return v0.getTextColor();
        };
        private T state = (T) fetchStateFromConfig();
        private int backgroundColor = fetchBackgroundColorFromConfig();
        private int textColor = fetchTextColorFromConfig();

        public ConfigCache() {
        }

        private T fetchStateFromConfig() {
            InfoDisplayManagerConfigData enforcedConfig = InfoDisplay.this.manager.getEnforcedConfig();
            InfoDisplayConfigData infoDisplayConfigData = enforcedConfig == null ? null : enforcedConfig.get(InfoDisplay.this.id);
            String state = infoDisplayConfigData == null ? null : infoDisplayConfigData.getState();
            if (state != null) {
                try {
                    return InfoDisplay.this.codec.decode(state, null, null);
                } catch (Throwable th) {
                    MinimapLogs.LOGGER.error("Error trying to decode info display state from enforced config: {}", state, th);
                }
            }
            InfoDisplayConfigData infoDisplayConfigData2 = InfoDisplay.this.manager.getLocalConfig().get(InfoDisplay.this.id);
            String state2 = infoDisplayConfigData2 == null ? null : infoDisplayConfigData2.getState();
            if (state2 == null) {
                return (T) InfoDisplay.this.getDefaultState();
            }
            try {
                return InfoDisplay.this.codec.decode(state2, null, null);
            } catch (Throwable th2) {
                MinimapLogs.LOGGER.error("Error trying to decode info display state from current config profile: {}", state2, th2);
                return (T) InfoDisplay.this.getDefaultState();
            }
        }

        private int fetchBackgroundColorFromConfig() {
            return fetchColorFromConfig(BG_COLOR_GETTER, -1);
        }

        private int fetchTextColorFromConfig() {
            return fetchColorFromConfig(TEXT_COLOR_GETTER, 15);
        }

        private int fetchColorFromConfig(Function<InfoDisplayConfigData, Integer> colorGetter, int defaultColor) {
            InfoDisplayManagerConfigData enforcedManagerConfig = InfoDisplay.this.manager.getEnforcedConfig();
            InfoDisplayConfigData enforcedDisplayConfig = enforcedManagerConfig == null ? null : enforcedManagerConfig.get(InfoDisplay.this.id);
            Integer enforcedValue = enforcedDisplayConfig == null ? null : colorGetter.apply(enforcedDisplayConfig);
            if (enforcedValue != null) {
                return enforcedValue.intValue();
            }
            InfoDisplayManagerConfigData localManagerConfig = InfoDisplay.this.manager.getLocalConfig();
            InfoDisplayConfigData localDisplayConfig = localManagerConfig.get(InfoDisplay.this.id);
            Integer localValue = localDisplayConfig == null ? null : colorGetter.apply(localDisplayConfig);
            if (localValue == null) {
                return defaultColor;
            }
            return localValue.intValue();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/InfoDisplay$Builder.class */
    public static final class Builder<T> {
        private String id;
        private Component name;
        private T defaultState;
        private ConfigValueIOCodec<T> codec;
        private InfoDisplayWidgetFactory<T> widgetFactory;
        private InfoDisplayOnCompile<T> compiler;
        private Consumer<InfoDisplay<?>> destination;
        private Function<ModSettings, T> legacyGetter;

        private Builder() {
        }

        public Builder<T> setDefault() {
            setId(null);
            setName(null);
            setDefaultState(null);
            setCodec((InfoDisplayStateCodec) null);
            setWidgetFactory(null);
            setCompiler(null);
            setDestination(null);
            setLegacyGetter(null);
            return this;
        }

        public Builder<T> setId(String id) {
            this.id = id;
            return this;
        }

        public Builder<T> setName(Component name) {
            this.name = name;
            return this;
        }

        public Builder<T> setDefaultState(T defaultState) {
            this.defaultState = defaultState;
            return this;
        }

        @Deprecated
        public Builder<T> setCodec(InfoDisplayStateCodec<T> codec) {
            return setCodec((ConfigValueIOCodec) codec);
        }

        public Builder<T> setCodec(ConfigValueIOCodec<T> codec) {
            this.codec = codec;
            return this;
        }

        public Builder<T> setWidgetFactory(InfoDisplayWidgetFactory<T> widgetFactory) {
            this.widgetFactory = widgetFactory;
            return this;
        }

        public Builder<T> setCompiler(InfoDisplayOnCompile<T> compiler) {
            this.compiler = compiler;
            return this;
        }

        public Builder<T> setDestination(Consumer<InfoDisplay<?>> destination) {
            this.destination = destination;
            return this;
        }

        public Builder<T> setLegacyGetter(Function<ModSettings, T> legacyGetter) {
            this.legacyGetter = legacyGetter;
            return this;
        }

        public InfoDisplay<T> build() {
            if (this.id == null || this.name == null || this.defaultState == null || this.codec == null || this.widgetFactory == null || this.compiler == null) {
                throw new IllegalStateException();
            }
            InfoDisplay<T> result = new InfoDisplay<>(this.id, this.name, this.defaultState, this.codec, this.widgetFactory, this.compiler, this.legacyGetter);
            if (this.destination != null) {
                this.destination.accept(result);
            }
            return result;
        }

        public static <T> Builder<T> begin() {
            return new Builder().setDefault();
        }
    }
}
