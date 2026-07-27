package xaero.hud.minimap.info.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import xaero.common.gui.GuiInfoDisplayEdit;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/widget/InfoDisplayWidgetFactory.class */
public interface InfoDisplayWidgetFactory<T> {
    AbstractWidget create(int i, int i2, int i3, int i4, GuiInfoDisplayEdit.MoveableEntry<T> moveableEntry, Runnable runnable, boolean z);
}
