package xaero.lib.mixin;

import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import xaero.lib.client.gui.widget.IClickableWidget;
import xaero.lib.client.gui.widget.Tooltip;

@Mixin({AbstractButton.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/mixin/MixinAbstractButton.class */
public class MixinAbstractButton implements IClickableWidget {
    private Supplier<Tooltip> xaero_tooltip;

    @Override // xaero.lib.client.gui.widget.ITooltipHaver
    public Supplier<Tooltip> getXaero_tooltip() {
        return this.xaero_tooltip;
    }

    @Override // xaero.lib.client.gui.widget.IClickableWidget
    public void setXaero_tooltip(Supplier<Tooltip> tooltip) {
        this.xaero_tooltip = tooltip;
    }
}
