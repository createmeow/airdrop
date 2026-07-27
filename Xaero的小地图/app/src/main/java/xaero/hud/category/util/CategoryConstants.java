package xaero.hud.category.util;

import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/util/CategoryConstants.class */
public class CategoryConstants {
    public static Component ON = Component.translatable("gui.xaero_on");
    public static Component OFF = Component.translatable("gui.xaero_off");
    public static Component INHERIT = Component.translatable("gui.xaero_category_setting_inherit");

    public static Component getBooleanComponent(boolean value) {
        return value ? ON : OFF;
    }
}
