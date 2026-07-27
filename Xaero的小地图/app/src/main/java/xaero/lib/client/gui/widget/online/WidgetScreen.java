package xaero.lib.client.gui.widget.online;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/WidgetScreen.class */
public interface WidgetScreen {
    <S extends Screen & WidgetScreen> S getScreen();

    void addButtonVisible(AbstractWidget abstractWidget);
}
