package xaero.hud.xminimap.preset;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.preset.HudPreset;
import xaero.hud.preset.HudPresetManager;
import xaero.hud.preset.ModulePreset;
import xaero.minimap.XaeroMinimap;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/xminimap/preset/BuiltInHudPresets.class */
public class BuiltInHudPresets {
    public static final HudPreset TOP_LEFT = HudPreset.Builder.begin().setId(ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, "top_left")).setName(Component.translatable("gui.xaero_preset_topleft")).addModulePreset(ModulePreset.Builder.begin(BuiltInHudModules.MINIMAP).setPlacement(0, 0, false, false, false, false, false).build()).build();
    public static final HudPreset TOP_RIGHT = HudPreset.Builder.begin().setId(ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, "top_right")).setName(Component.translatable("gui.xaero_preset_topright")).addModulePreset(ModulePreset.Builder.begin(BuiltInHudModules.MINIMAP).setPlacement(0, 0, false, true, false, false, false).build()).build();
    public static final HudPreset BOTTOM_LEFT = HudPreset.Builder.begin().setId(ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, "bottom_left")).setName(Component.translatable("gui.xaero_preset_bottom_left")).addModulePreset(ModulePreset.Builder.begin(BuiltInHudModules.MINIMAP).setPlacement(0, 0, false, false, true, false, false).build()).build();
    public static final HudPreset BOTTOM_RIGHT = HudPreset.Builder.begin().setId(ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, "bottom_right")).setName(Component.translatable("gui.xaero_preset_bottom_right")).addModulePreset(ModulePreset.Builder.begin(BuiltInHudModules.MINIMAP).setPlacement(0, 0, false, true, true, false, false).build()).build();

    public static void addAll(HudPresetManager manager) {
        manager.register(TOP_LEFT);
        manager.register(TOP_RIGHT);
        manager.register(BOTTOM_LEFT);
        manager.register(BOTTOM_RIGHT);
    }
}
