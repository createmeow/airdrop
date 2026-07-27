package xaero.hud.minimap.common.config;

import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/MinimapConfigConstants.class */
public class MinimapConfigConstants {
    public static final float DEFAULT_WORLD_WAYPOINT_SCALE = 0.8f;
    public static final float WORLD_WAYPOINT_MINECRAFT_SCALE = 0.02666667f;
    public static final double WAYPOINT_ICON_WORLD_SCALE = 0.02133333496749401d;
    public static final int MAX_ZOOM = 5;
    public static final Component[] DOTS_STYLES = {Component.translatable("gui.xaero_dots_style_default"), Component.translatable("gui.xaero_dots_style_legacy")};
    public static final Component[] MAIN_ENTITY_TYPES = {Component.translatable("gui.xaero_crosshair"), Component.translatable("gui.xaero_dot"), Component.translatable("gui.xaero_arrow")};
    public static Component[] ARROW_COLOR_NAMES = {Component.translatable("gui.xaero_red"), Component.translatable("gui.xaero_green"), Component.translatable("gui.xaero_blue"), Component.translatable("gui.xaero_yellow"), Component.translatable("gui.xaero_purple"), Component.translatable("gui.xaero_white"), Component.translatable("gui.xaero_black"), Component.translatable("gui.xaero_legacy_color")};
    public static float[][] ARROW_COLORS = {new float[]{0.8f, 0.1f, 0.1f, 1.0f}, new float[]{0.09f, 0.57f, 0.0f, 1.0f}, new float[]{0.0f, 0.55f, 1.0f, 1.0f}, new float[]{1.0f, 0.93f, 0.0f, 1.0f}, new float[]{0.73f, 0.33f, 0.83f, 1.0f}, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, new float[]{0.4588f, 0.0f, 0.0f, 1.0f}};
    public static final Component AUTO_SCALE_COMPONENT = Component.translatable("gui.xaero_ui_scale_auto");
    public static final Component MINECRAFT_SCALE_COMPONENT = Component.translatable("gui.xaero_ui_scale_mc");
    public static final Component[] MULTIPLE_WAYPOINTS_INFO_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_while_sneaking"), Component.translatable("gui.xaero_multiple_waypoints_always")};
    public static final Component[] WAYPOINT_DISTANCE_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_looking_at"), Component.translatable("gui.xaero_all")};
    public static final Component UNLIMITED_COMPONENT = Component.translatable("gui.xaero_unlimited");
    public static final Component CONVERT_DISTANCE_NEVER_COMPONENT = Component.translatable("gui.xaero_auto_convert_wp_distance_km_never");
    public static final Component[] HIDE_WORLD_NAMES_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_partial"), Component.translatable("gui.xaero_full")};
    public static final Component[] COMPASS_LOCATION_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_minimap_compass_inside_frame"), Component.translatable("gui.xaero_minimap_compass_on_frame")};
    public static final Component[] COLOR_NAMES = {Component.translatable("gui.xaero_black"), Component.translatable("gui.xaero_dark_blue"), Component.translatable("gui.xaero_dark_green"), Component.translatable("gui.xaero_dark_aqua"), Component.translatable("gui.xaero_dark_red"), Component.translatable("gui.xaero_dark_purple"), Component.translatable("gui.xaero_gold"), Component.translatable("gui.xaero_gray"), Component.translatable("gui.xaero_dark_gray"), Component.translatable("gui.xaero_blue"), Component.translatable("gui.xaero_green"), Component.translatable("gui.xaero_aqua"), Component.translatable("gui.xaero_red"), Component.translatable("gui.xaero_purple"), Component.translatable("gui.xaero_yellow"), Component.translatable("gui.xaero_white")};
    public static final int[] COLORS = {-16777216, -16777046, -16733696, -16733526, -5636096, -5635926, -22016, -5592406, -11184811, -11184641, -11141291, -11141121, -65536, -43521, -171, -1};
    public static final String[] COLOR_CODES = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static final Component[] BLOCK_COLOR_NAMES = {Component.translatable("gui.xaero_accurate"), Component.translatable("gui.xaero_vanilla")};
    public static final Component[] TERRAIN_SLOPES_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_slopes_legacy"), Component.translatable("gui.xaero_slopes_default_3d"), Component.translatable("gui.xaero_slopes_default_2d")};
    public static final Component[] MINIMAP_SHAPE_NAMES = {Component.translatable("gui.xaero_minimap_shape_square"), Component.translatable("gui.xaero_minimap_shape_circle")};
    public static final Component[] FRAME_NAMES = {Component.translatable("gui.xaero_minimap_frame_default"), Component.translatable("gui.xaero_minimap_frame_colored_thick"), Component.translatable("gui.xaero_minimap_frame_colored_thin"), Component.translatable("gui.xaero_off")};
    public static final Component[] PUSH_BOX_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_pushbox_normal"), Component.translatable("gui.xaero_pushbox_screen_height")};
    public static final Component[] LIGHT_NAMES = {Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_light_block"), Component.translatable("gui.xaero_light_sky"), Component.translatable("gui.xaero_light_all"), Component.translatable("gui.xaero_light_both2")};
    public static final Component[] INFO_DISPLAY_ALIGNMENT_NAMES = {Component.translatable("gui.xaero_center"), Component.translatable("gui.xaero_left"), Component.translatable("gui.xaero_right")};
}
