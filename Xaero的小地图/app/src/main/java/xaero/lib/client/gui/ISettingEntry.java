package xaero.lib.client.gui;

import net.minecraft.client.gui.components.AbstractWidget;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/ISettingEntry.class */
public interface ISettingEntry {
    String getStringForSearch();

    AbstractWidget createWidget(int i, int i2, int i3);
}
