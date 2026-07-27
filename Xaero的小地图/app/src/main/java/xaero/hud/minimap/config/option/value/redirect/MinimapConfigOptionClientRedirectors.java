package xaero.hud.minimap.config.option.value.redirect;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.minimap.MinimapProcessor;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;
import xaero.lib.common.config.channel.ConfigChannel;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/config/option/value/redirect/MinimapConfigOptionClientRedirectors.class */
public class MinimapConfigOptionClientRedirectors {
    public static final Component LEGACY_PLUGIN_TOOLTIP = Component.translatable("gui.xaero_minimap_redirect_legacy");
    public static final Component FAIRPLAY_TOOLTIP = Component.translatable("gui.xaero_config_redirect_fairplay");
    public static final Component SAFE_MODE_TOOLTIP = Component.translatable("gui.xaero_config_redirect_safe_mode");
    public static final Component EFFECT_TOOLTIP = Component.translatable("gui.xaero_config_redirect_effect");
    public static final Component FROM_WORLDMAP_TOOLTIP = Component.translatable("gui.xaero_uses_worldmap_value").withStyle(ChatFormatting.YELLOW);
    public static final Component HARD_WORLDMAP_TOOLTIP = Component.translatable("gui.xaero_uses_worldmap_hard_value").withStyle(ChatFormatting.YELLOW);
    public static final Component FROM_WORLDMAP_SCREEN_TOOLTIP = Component.translatable("gui.xaero_uses_worldmap_screen_value").withStyle(ChatFormatting.YELLOW);
    public static final Component WORLD_MAP_COMPONENT = Component.translatable("gui.xaero_world_map").withStyle(ChatFormatting.YELLOW);

    public static void registerAll(ClientOptionValueRedirectorManager manager) {
        manager.register(MinimapProfiledConfigOptions.DISPLAY_MINIMAP, () -> {
            return false;
        }, channel -> {
            return HudMod.INSTANCE.getSettings().minimapDisabled() || !(Minecraft.getInstance().player == null || MinimapProcessor.hasMinimapItem(Minecraft.getInstance().player));
        }, null, null, () -> {
            if (HudMod.INSTANCE.getSettings().minimapDisabled()) {
                return LEGACY_PLUGIN_TOOLTIP;
            }
            if (Minecraft.getInstance().player != null && !MinimapProcessor.hasMinimapItem(Minecraft.getInstance().player)) {
                String minimapItemId = (String) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.MINIMAP_ITEM);
                return Component.translatable("gui.xaero_minimap_redirect_item", new Object[]{minimapItemId});
            }
            return null;
        });
        manager.register(MinimapProfiledConfigOptions.DISPLAY_RADAR, () -> {
            return false;
        }, channel2 -> {
            return HudMod.INSTANCE.isFairPlay();
        }, null, null, () -> {
            return FAIRPLAY_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.RADAR_DOTS_STYLE, () -> {
            return 1;
        }, channel3 -> {
            return !HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().assumeUsingFBO();
        }, null, null, () -> {
            return SAFE_MODE_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.RADAR_SMOOTH_DOTS, () -> {
            return false;
        }, channel4 -> {
            return !HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().assumeUsingFBO();
        }, null, null, () -> {
            return SAFE_MODE_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.RADAR_MAIN_DOT_SIZE, () -> {
            return 2;
        }, channel5 -> {
            return !HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().assumeUsingFBO();
        }, null, null, () -> {
            return SAFE_MODE_TOOLTIP;
        });
        Predicate<ConfigChannel> waypointDisableCondition = channel6 -> {
            return HudMod.INSTANCE.getSettings().showWaypointsDisabled() || !(Minecraft.getInstance().player == null || MinimapProcessor.hasMinimapItem(Minecraft.getInstance().player));
        };
        Supplier<Component> waypointDisableTooltipSupplier = () -> {
            if (HudMod.INSTANCE.getSettings().showWaypointsDisabled()) {
                return LEGACY_PLUGIN_TOOLTIP;
            }
            if (Minecraft.getInstance().player != null && !MinimapProcessor.hasMinimapItem(Minecraft.getInstance().player)) {
                String minimapItemId = (String) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.MINIMAP_ITEM);
                return Component.translatable("gui.xaero_minimap_redirect_item", new Object[]{minimapItemId});
            }
            return null;
        };
        manager.register(MinimapProfiledConfigOptions.WAYPOINTS_ON_MINIMAP, () -> {
            return false;
        }, waypointDisableCondition, null, null, waypointDisableTooltipSupplier);
        manager.register(MinimapProfiledConfigOptions.WAYPOINTS_IN_WORLD, () -> {
            return false;
        }, waypointDisableCondition, null, null, waypointDisableTooltipSupplier);
        manager.register(MinimapProfiledConfigOptions.DEATHPOINTS, () -> {
            return false;
        }, channel7 -> {
            return HudMod.INSTANCE.getSettings().deathpointsDisabled();
        }, null, null, () -> {
            return LEGACY_PLUGIN_TOOLTIP;
        });
        Function<Screen, Screen> worldMapSettingsScreenFactory = current -> {
            return HudMod.INSTANCE.getSupportMods().worldmapSupport.getSettingsScreen(current);
        };
        manager.register(MinimapProfiledConfigOptions.WAYPOINT_PARTIAL_Y_TELEPORT, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getPartialYTeleport());
        }, channel8 -> {
            return HudMod.INSTANCE.getSupportMods().worldmap();
        }, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        Predicate<ConfigChannel> shouldUseWorldMapChunksPredicate = channel9 -> {
            return HudMod.INSTANCE.getSupportMods().shouldUseWorldMapChunks();
        };
        manager.register(MinimapProfiledConfigOptions.BLOCK_COLORS, () -> {
            return Integer.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getWorldMapColours());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.BIOMES_IN_VANILLA_COLORS, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getWorldMapBiomeColorsVanillaMode());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED, () -> {
            return false;
        }, channel10 -> {
            return MinimapConfigClientUtils.isFairPlayForCaveMode() || HudMod.INSTANCE.getSettings().caveMapsDisabled() || MinimapConfigClientUtils.hasNoCaveModeEffect();
        }, null, null, () -> {
            return MinimapConfigClientUtils.isFairPlayForCaveMode() ? FAIRPLAY_TOOLTIP : HudMod.INSTANCE.getSettings().caveMapsDisabled() ? LEGACY_PLUGIN_TOOLTIP : EFFECT_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START, () -> {
            return Integer.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getManualCaveStart());
        }, channel11 -> {
            return HudMod.INSTANCE.getSupportMods().shouldUseWorldMapCaveChunks();
        }, currentScreen -> {
            return HudMod.INSTANCE.getSupportMods().worldmapSupport.getWorldMapScreenForOption(MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START, currentScreen);
        }, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_SCREEN_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.BIOME_BLENDING, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getBiomeBlending());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.CAVE_MODE_DEPTH, () -> {
            return Integer.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getCaveModeDepth());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.LEGIBLE_CAVE_MAPS, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.isLegibleCaveMaps());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.DISPLAY_WORLD_MAP_CHUNKS, () -> {
            return false;
        }, channel12 -> {
            return !HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().assumeUsingFBO();
        }, null, null, () -> {
            return SAFE_MODE_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.TERRAIN_DEPTH, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getWorldMapTerrainDepth());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.TERRAIN_SLOPES, () -> {
            return Integer.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getWorldMapTerrainSlopes());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.DISPLAY_REDSTONE, () -> {
            return false;
        }, shouldUseWorldMapChunksPredicate, null, WORLD_MAP_COMPONENT, () -> {
            return HARD_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.DISPLAY_FLOWERS, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getWorldMapFlowers());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.DISPLAY_STAINED_GLASS, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.isStainedGlassDisplayed());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getAdjustHeightForCarpetLikeBlocks());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.ANTI_ALIASING, () -> {
            return false;
        }, channel13 -> {
            return !HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().assumeUsingFBO();
        }, null, null, () -> {
            return SAFE_MODE_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.OPAC_CLAIMS, () -> {
            return Boolean.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getDisplayClaims());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.OPAC_CLAIMS_FILL_OPACITY, () -> {
            return Integer.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getClaimsFillOpacity());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
        manager.register(MinimapProfiledConfigOptions.OPAC_CLAIMS_BORDER_OPACITY, () -> {
            return Integer.valueOf(HudMod.INSTANCE.getSupportMods().worldmapSupport.getClaimsBorderOpacity());
        }, shouldUseWorldMapChunksPredicate, worldMapSettingsScreenFactory, WORLD_MAP_COMPONENT, () -> {
            return FROM_WORLDMAP_TOOLTIP;
        });
    }
}
