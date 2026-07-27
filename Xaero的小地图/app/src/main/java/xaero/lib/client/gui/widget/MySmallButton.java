package xaero.lib.client.gui.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/MySmallButton.class */
public class MySmallButton extends Button {
    protected int id;

    public MySmallButton(int id, int par1, int par2, Component par5Str, Button.OnPress onPress) {
        super(par1, par2, 150, 20, par5Str, onPress, DEFAULT_NARRATION);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
