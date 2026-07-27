package xaero.lib.client.config.option.ui.factory;

import java.util.function.BooleanSupplier;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/factory/ViewEnforcedCondition.class */
public class ViewEnforcedCondition {
    private final BooleanSupplier condition;
    private final Component tooltip;

    public ViewEnforcedCondition(BooleanSupplier condition, Component tooltip) {
        this.condition = condition;
        this.tooltip = tooltip;
    }

    public BooleanSupplier getCondition() {
        return this.condition;
    }

    public Component getTooltip() {
        return this.tooltip;
    }
}
