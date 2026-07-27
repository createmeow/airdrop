package xaero.lib.client.gui;

import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/TextSettingEntry.class */
public class TextSettingEntry implements ISettingEntry {
    private final Supplier<Component> text;

    public TextSettingEntry(Supplier<Component> text) {
        this.text = text;
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public String getStringForSearch() {
        return this.text.get().getString();
    }

    @Override // xaero.lib.client.gui.ISettingEntry
    public AbstractWidget createWidget(int x, int y, int w) {
        Button result = Button.builder(this.text.get(), b -> {
        }).bounds(x, y, w, 20).build();
        result.active = false;
        return result;
    }
}
