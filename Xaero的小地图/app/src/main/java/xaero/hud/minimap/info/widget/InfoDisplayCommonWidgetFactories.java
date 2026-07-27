package xaero.hud.minimap.info.widget;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/widget/InfoDisplayCommonWidgetFactories.class */
public class InfoDisplayCommonWidgetFactories {
    public static final InfoDisplayCycleWidgetFactory<Boolean> OFF_ON = new InfoDisplayCycleWidgetFactory<>(Lists.newArrayList(new Boolean[]{false, true}), Lists.newArrayList(new Component[]{Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_on")}));
    public static final InfoDisplayWidgetFactory<Boolean> ALWAYS_ON = (x, y, w, h, entry, onChange, includeNull) -> {
        return null;
    };
}
