package xaero.hud.minimap.common.config.util;

import java.util.function.BiFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xaero.common.platform.Services;
import xaero.hud.minimap.common.config.MinimapConfigConstants;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/util/MinimapConfigUtils.class */
public class MinimapConfigUtils {
    public static BiFunction<ConfigOption<Integer>, Integer, Component> getUIScaleDisplayGetter(int auto, int max, boolean includeAutoValue) {
        return getUIScaleDisplayGetter(auto, max, 1.0d, includeAutoValue);
    }

    public static BiFunction<ConfigOption<Integer>, Integer, Component> getUIScaleDisplayGetter(int auto, int max, double autoScale, boolean includeAutoValue) {
        return (o, v) -> {
            if (v.intValue() == auto) {
                MutableComponent result = MinimapConfigConstants.AUTO_SCALE_COMPONENT.copy();
                if (includeAutoValue && !Services.PLATFORM.isDedicatedServer()) {
                    MinimapConfigClientUtils.addAutoUIScaleValueToComponent(result, autoScale);
                }
                return result;
            }
            if (v.intValue() == max) {
                MutableComponent result2 = MinimapConfigConstants.MINECRAFT_SCALE_COMPONENT.copy();
                if (!Services.PLATFORM.isDedicatedServer()) {
                    MinimapConfigClientUtils.addAutoMCScaleValueToComponent(result2);
                }
                return result2;
            }
            return Component.literal(v.toString());
        };
    }

    public static BiFunction<ConfigOption<Integer>, Integer, Component> getUIScaleDisplayGetter(int auto, int max) {
        return getUIScaleDisplayGetter(auto, max, 1.0d);
    }

    public static BiFunction<ConfigOption<Integer>, Integer, Component> getUIScaleDisplayGetter(int auto, int max, double autoScale) {
        return getUIScaleDisplayGetter(auto, max, autoScale, true);
    }

    public static Component getAutoMinimapSizeName() {
        MutableComponent component = Component.translatable("gui.xaero_auto_map_size");
        if (!Services.PLATFORM.isDedicatedServer()) {
            component.getSiblings().add(Component.literal(" (" + MinimapConfigClientUtils.getAutoMinimapSize() + ")"));
        }
        return component;
    }
}
