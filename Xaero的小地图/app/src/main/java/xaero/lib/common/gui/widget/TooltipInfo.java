package xaero.lib.common.gui.widget;

import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/gui/widget/TooltipInfo.class */
public class TooltipInfo {
    public final Component text;
    public final boolean flippedByDefault;
    public final boolean autoLineBreak;

    public TooltipInfo(Component text, boolean flippedByDefault, boolean autoLineBreak) {
        this.text = text;
        this.flippedByDefault = flippedByDefault;
        this.autoLineBreak = autoLineBreak;
    }

    public TooltipInfo(String translationCode, boolean autoLineBreak, boolean flippedByDefault) {
        this((Component) Component.translatable(translationCode), flippedByDefault, autoLineBreak);
    }

    public TooltipInfo(String translationCode) {
        this(translationCode, true, false);
    }
}
