package xaero.lib.client.gui.util;

import net.minecraft.client.gui.components.EditBox;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/util/GuiUtils.class */
public class GuiUtils {
    public static void setFieldText(EditBox field, String text) {
        setFieldText(field, text, -1);
    }

    public static void setFieldText(EditBox field, String text, int color) {
        field.setTextColor(color);
        if (field.getValue().equals(text)) {
            return;
        }
        field.setValue(text);
    }
}
