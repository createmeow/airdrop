package xaero.lib.client.compat.prometheus;

import earth.terrarium.prometheus.client.screens.roles.options.entries.NumberBoxListEntry;
import java.util.OptionalInt;
import java.util.OptionalLong;
import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/compat/prometheus/FixedNumberBoxListEntry.class */
public class FixedNumberBoxListEntry extends NumberBoxListEntry {
    public FixedNumberBoxListEntry(Number amount, Component component, Component tooltip) {
        super(0, amount instanceof Double, component, tooltip);
        setText(String.valueOf(amount != null ? amount : ""));
    }

    public FixedNumberBoxListEntry(Number amount, Component component) {
        super(0, amount instanceof Double, component);
        setText(String.valueOf(amount != null ? amount : ""));
    }

    private void setText(String text) {
        this.text = text;
    }

    public OptionalLong getLongValue() {
        try {
            return OptionalLong.of(Long.parseLong(getText()));
        } catch (NumberFormatException e) {
            return OptionalLong.empty();
        }
    }

    public OptionalInt getByteValue() {
        try {
            return OptionalInt.of(255 & Integer.parseInt(getText()));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}
