package xaero.lib.client.gui.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/MyTinyButton.class */
public class MyTinyButton extends Button {
    private Tooltip tooltip;

    public MyTinyButton(int par1, int par2, Component par4Str, Button.OnPress onPress) {
        this(null, par1, par2, par4Str, onPress);
    }

    public MyTinyButton(Tooltip tooltip, int par1, int par2, Component par5Str, Button.OnPress onPress) {
        super(par1, par2, 75, 20, par5Str, onPress, DEFAULT_NARRATION);
        this.tooltip = tooltip;
    }

    public Tooltip getMyTooltip() {
        return this.tooltip;
    }
}
