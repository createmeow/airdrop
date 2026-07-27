package xaero.hud.minimap.info;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.info.widget.InfoDisplayCommonWidgetFactories;
import xaero.hud.minimap.info.widget.InfoDisplayCycleWidgetFactory;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.lib.common.config.option.value.io.serialization.BuiltInConfigValueIOCodecs;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/BuiltInInfoDisplays.class */
public class BuiltInInfoDisplays {
    private static List<InfoDisplay<?>> ALL = new ArrayList();
    public static final InfoDisplay<Boolean> COORDINATES;
    public static final InfoDisplay<Boolean> OVERWORLD_COORDINATES;
    public static final InfoDisplay<Boolean> CHUNK_COORDINATES;
    public static final InfoDisplay<Boolean> ANGLES;
    public static final InfoDisplay<Boolean> DIMENSION;
    public static final InfoDisplay<Boolean> BIOME;
    public static final InfoDisplay<Boolean> WEATHER;
    public static final InfoDisplay<Integer> LIGHT_LEVEL;
    public static final InfoDisplay<Integer> TIME;
    private static final Calendar CALENDAR;
    public static final InfoDisplay<Integer> REAL_TIME;
    public static final InfoDisplay<Boolean> HIGHLIGHTS;
    public static final InfoDisplay<Boolean> LIGHT_OVERLAY_INDICATOR;
    public static final InfoDisplay<Boolean> MANUAL_CAVE_MODE_INDICATOR;
    public static final InfoDisplay<Boolean> CUSTOM_SUB_WORLD;

    static {
        InfoDisplay.Builder legacyGetter = InfoDisplay.Builder.begin().setId("coords").setName(Component.translatable("gui.xaero_infodisplay_coords")).setDefaultState(true).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo, compiler, session, availableWidth, playerPos) -> {
            if (!((Boolean) displayInfo.getEffectiveState()).booleanValue()) {
                return;
            }
            String coords = playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ();
            if (Minecraft.getInstance().font.width(coords) >= availableWidth) {
                String stringLevel = playerPos.getY();
                compiler.addLine(playerPos.getX() + ", " + playerPos.getZ());
                compiler.addLine(stringLevel);
                return;
            }
            compiler.addLine(coords);
        }).setLegacyGetter(legacySettings -> {
            Boolean result = legacySettings.showCoordsLegacy;
            legacySettings.showCoordsLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list = ALL;
        Objects.requireNonNull(list);
        COORDINATES = legacyGetter.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder compiler2 = InfoDisplay.Builder.begin().setId("overworld_coords").setName(Component.translatable("gui.xaero_infodisplay_overworld_coords")).setDefaultState(false).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo2, compiler3, session2, availableWidth2, playerPos2) -> {
            if (!((Boolean) displayInfo2.getEffectiveState()).booleanValue()) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            double coordinateScale = mc.level.dimensionType().coordinateScale();
            if (coordinateScale == 1.0d) {
                return;
            }
            int overworldPlayerX = Mth.floor(playerPos2.getX() * coordinateScale);
            int overworldPlayerZ = Mth.floor(playerPos2.getZ() * coordinateScale);
            String coords = "Xo: " + overworldPlayerX + ", Zo: " + overworldPlayerZ;
            compiler3.addWords(coords);
        });
        List<InfoDisplay<?>> list2 = ALL;
        Objects.requireNonNull(list2);
        OVERWORLD_COORDINATES = compiler2.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder compiler4 = InfoDisplay.Builder.begin().setId("chunk_coords").setName(Component.translatable("gui.xaero_infodisplay_chunk_coords")).setDefaultState(false).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo3, compiler5, session3, availableWidth3, playerPos3) -> {
            if (!((Boolean) displayInfo3.getEffectiveState()).booleanValue()) {
                return;
            }
            int chunkX = playerPos3.getX() >> 4;
            int chunkZ = playerPos3.getZ() >> 4;
            int insideX = playerPos3.getX() & 15;
            int insideZ = playerPos3.getZ() & 15;
            String coords = "C " + chunkX + ", " + chunkZ + " (" + insideX + "; " + insideZ + ")";
            compiler5.addWords(coords);
        });
        List<InfoDisplay<?>> list3 = ALL;
        Objects.requireNonNull(list3);
        CHUNK_COORDINATES = compiler4.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder legacyGetter2 = InfoDisplay.Builder.begin().setId("angles").setName(Component.translatable("gui.xaero_infodisplay_angles")).setDefaultState(false).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo4, compiler6, session4, availableWidth4, playerPos4) -> {
            if (!((Boolean) displayInfo4.getEffectiveState()).booleanValue()) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            compiler6.addLine((Component) Component.literal(String.format("%.1f / %.1f", Float.valueOf(Mth.wrapDegrees(mc.getCameraEntity().getYRot())), Float.valueOf(Mth.wrapDegrees(mc.getCameraEntity().getXRot())))));
        }).setLegacyGetter(legacySettings2 -> {
            Boolean result = legacySettings2.showAnglesLegacy;
            legacySettings2.showAnglesLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list4 = ALL;
        Objects.requireNonNull(list4);
        ANGLES = legacyGetter2.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder legacyGetter3 = InfoDisplay.Builder.begin().setId("dimension").setName(Component.translatable("gui.xaero_infodisplay_dimension")).setDefaultState(false).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo5, compiler7, session5, availableWidth5, playerPos5) -> {
            ResourceLocation dimensionId;
            if (((Boolean) displayInfo5.getEffectiveState()).booleanValue() && (dimensionId = Minecraft.getInstance().level.dimension().location()) != null) {
                String dimensionName = dimensionId.getNamespace().equals("minecraft") ? dimensionId.getPath() : dimensionId.toString();
                compiler7.addLine(dimensionName);
            }
        }).setLegacyGetter(legacySettings3 -> {
            Boolean result = legacySettings3.showDimensionNameLegacy;
            legacySettings3.showDimensionNameLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list5 = ALL;
        Objects.requireNonNull(list5);
        DIMENSION = legacyGetter3.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder legacyGetter4 = InfoDisplay.Builder.begin().setId("biome").setName(Component.translatable("gui.xaero_infodisplay_biome")).setDefaultState(false).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo6, compiler8, session6, availableWidth6, playerPos6) -> {
            if (!((Boolean) displayInfo6.getEffectiveState()).booleanValue()) {
                return;
            }
            Holder<Biome> biomeHolder = Minecraft.getInstance().level.getBiome(playerPos6);
            Biome biome = biomeHolder == null ? null : (Biome) biomeHolder.value();
            ResourceLocation biomeRL = biome == null ? null : Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
            String biomeText = biomeRL == null ? I18n.get("gui.xaero_unknown_biome", new Object[0]) : I18n.get("biome." + biomeRL.getNamespace() + "." + biomeRL.getPath(), new Object[0]);
            compiler8.addWords(biomeText);
        }).setLegacyGetter(legacySettings4 -> {
            Boolean result = legacySettings4.showBiomeLegacy;
            legacySettings4.showBiomeLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list6 = ALL;
        Objects.requireNonNull(list6);
        BIOME = legacyGetter4.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder legacyGetter5 = InfoDisplay.Builder.begin().setId("weather").setName(Component.translatable("gui.xaero_infodisplay_weather")).setDefaultState(false).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.OFF_ON).setCompiler((displayInfo7, compiler9, session7, availableWidth7, playerPos7) -> {
            if (!((Boolean) displayInfo7.getEffectiveState()).booleanValue()) {
                return;
            }
            if (Minecraft.getInstance().level.isThundering()) {
                compiler9.addLine((Component) Component.translatable("gui.xaero_weather_thundering"));
            } else if (Minecraft.getInstance().level.isRaining()) {
                compiler9.addLine((Component) Component.translatable("gui.xaero_weather_raining"));
            }
        }).setLegacyGetter(legacySettings5 -> {
            Boolean result = legacySettings5.displayWeatherInfoLegacy;
            legacySettings5.displayWeatherInfoLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list7 = ALL;
        Objects.requireNonNull(list7);
        WEATHER = legacyGetter5.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder legacyGetter6 = InfoDisplay.Builder.begin().setId("light_level").setName(Component.translatable("gui.xaero_infodisplay_light_level")).setDefaultState(0).setCodec(BuiltInConfigValueIOCodecs.INTEGER).setWidgetFactory(new InfoDisplayCycleWidgetFactory(Lists.newArrayList(new Integer[]{0, 1, 2, 3, 4}), Lists.newArrayList(new Component[]{Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_light_block"), Component.translatable("gui.xaero_light_sky"), Component.translatable("gui.xaero_light_all"), Component.translatable("gui.xaero_light_both2")}))).setCompiler((displayInfo8, compiler10, session8, availableWidth8, playerPos8) -> {
            int showLightLevel = ((Integer) displayInfo8.getEffectiveState()).intValue();
            if (showLightLevel == 0) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            int blockLight = mc.level.getBrightness(LightLayer.BLOCK, playerPos8);
            int skyLight = mc.level.getBrightness(LightLayer.SKY, playerPos8);
            if (showLightLevel == 1) {
                compiler10.addLine((Component) Component.translatable("gui.xaero_block_light_value", new Object[]{Integer.valueOf(blockLight)}));
                return;
            }
            if (showLightLevel == 2) {
                compiler10.addLine((Component) Component.translatable("gui.xaero_sky_light_value", new Object[]{Integer.valueOf(skyLight)}));
            } else if (showLightLevel == 3) {
                compiler10.addLine((Component) Component.translatable("gui.xaero_all_light_value", new Object[]{Integer.valueOf(Math.max(blockLight, skyLight))}));
            } else {
                compiler10.addLine((Component) Component.translatable("gui.xaero_both_light_value", new Object[]{Integer.valueOf(blockLight), Integer.valueOf(skyLight)}));
            }
        }).setLegacyGetter(legacySettings6 -> {
            Integer result = legacySettings6.showLightLevelLegacy;
            legacySettings6.showLightLevelLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list8 = ALL;
        Objects.requireNonNull(list8);
        LIGHT_LEVEL = legacyGetter6.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder legacyGetter7 = InfoDisplay.Builder.begin().setId("time").setName(Component.translatable("gui.xaero_infodisplay_time")).setDefaultState(0).setCodec(BuiltInConfigValueIOCodecs.INTEGER).setWidgetFactory(new InfoDisplayCycleWidgetFactory(Lists.newArrayList(new Integer[]{0, 1, 2, 3, 4}), Lists.newArrayList(new Component[]{Component.translatable("gui.xaero_off"), Component.translatable("%s+%s", new Object[]{Component.translatable("gui.xaero_day"), Component.translatable("gui.xaero_24h")}), Component.translatable("%s+%s", new Object[]{Component.translatable("gui.xaero_day"), Component.translatable("gui.xaero_12h")}), Component.translatable("gui.xaero_24h"), Component.translatable("gui.xaero_12h")}))).setCompiler((displayInfo9, compiler11, session9, availableWidth9, playerPos9) -> {
            int showTime = ((Integer) displayInfo9.getEffectiveState()).intValue();
            if (showTime <= 0) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            long totalTime = 6000 + mc.level.getDayTime();
            int dayNumber = ((int) (totalTime / 24000)) + 1;
            int dayTimeSinceMidnight = (int) (totalTime % 24000);
            int timeHours = dayTimeSinceMidnight / 1000;
            int minutes = (int) (((dayTimeSinceMidnight % 1000) / 1000.0d) * 60.0d);
            if ((showTime - 1) % 2 == 0) {
                if (showTime < 3) {
                    compiler11.addWords(I18n.get("gui.xaero_day", new Object[0]) + String.format(" %d, %02d:%02d", Integer.valueOf(dayNumber), Integer.valueOf(timeHours), Integer.valueOf(minutes)));
                    return;
                } else {
                    compiler11.addWords(String.format("%02d:%02d", Integer.valueOf(timeHours), Integer.valueOf(minutes)));
                    return;
                }
            }
            String half = "AM";
            if (timeHours >= 12) {
                timeHours -= 12;
                half = "PM";
            }
            if (timeHours == 0) {
                timeHours = 12;
            }
            if (showTime < 3) {
                compiler11.addWords(I18n.get("gui.xaero_day", new Object[0]) + String.format(" %d, %02d:%02d %s", Integer.valueOf(dayNumber), Integer.valueOf(timeHours), Integer.valueOf(minutes), half));
            } else {
                compiler11.addWords(String.format("%02d:%02d %s", Integer.valueOf(timeHours), Integer.valueOf(minutes), half));
            }
        }).setLegacyGetter(legacySettings7 -> {
            Integer result = legacySettings7.showTimeLegacy;
            legacySettings7.showTimeLegacy = null;
            return result;
        });
        List<InfoDisplay<?>> list9 = ALL;
        Objects.requireNonNull(list9);
        TIME = legacyGetter7.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        CALENDAR = Calendar.getInstance();
        InfoDisplay.Builder compiler12 = InfoDisplay.Builder.begin().setId("real_time").setName(Component.translatable("gui.xaero_infodisplay_real_time")).setDefaultState(0).setCodec(BuiltInConfigValueIOCodecs.INTEGER).setWidgetFactory(new InfoDisplayCycleWidgetFactory(Lists.newArrayList(new Integer[]{0, 1, 2}), Lists.newArrayList(new Component[]{Component.translatable("gui.xaero_off"), Component.translatable("gui.xaero_24h"), Component.translatable("gui.xaero_12h")}))).setCompiler((displayInfo10, compiler13, session10, availableWidth10, playerPos10) -> {
            int showTime = ((Integer) displayInfo10.getEffectiveState()).intValue();
            if (showTime <= 0) {
                return;
            }
            CALENDAR.setTimeInMillis(System.currentTimeMillis());
            int timeHours = CALENDAR.get(11);
            int minutes = CALENDAR.get(12);
            if (showTime == 1) {
                compiler13.addWords(String.format("%02d:%02d IRL", Integer.valueOf(timeHours), Integer.valueOf(minutes)));
                return;
            }
            String half = "AM";
            if (timeHours >= 12) {
                timeHours -= 12;
                half = "PM";
            }
            if (timeHours == 0) {
                timeHours = 12;
            }
            compiler13.addWords(String.format("%02d:%02d %s IRL", Integer.valueOf(timeHours), Integer.valueOf(minutes), half));
        });
        List<InfoDisplay<?>> list10 = ALL;
        Objects.requireNonNull(list10);
        REAL_TIME = compiler12.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder compiler14 = InfoDisplay.Builder.begin().setId("highlights").setName(Component.translatable("gui.xaero_infodisplay_highlights")).setDefaultState(true).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.ALWAYS_ON).setCompiler((displayInfo11, compiler15, session11, availableWidth11, playerPos11) -> {
            if (((Boolean) displayInfo11.getEffectiveState()).booleanValue() && session11.getProcessor().getMinimapWriter().getDimensionHighlightHandler() != null) {
                session11.getProcessor().getMinimapWriter().getDimensionHighlightHandler().addBlockHighlightTooltips(compiler15, playerPos11.getX(), playerPos11.getZ(), availableWidth11, true);
            }
        });
        List<InfoDisplay<?>> list11 = ALL;
        Objects.requireNonNull(list11);
        HIGHLIGHTS = compiler14.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder compiler16 = InfoDisplay.Builder.begin().setId("light_overlay_indicator").setName(Component.translatable("gui.xaero_infodisplay_light_overlay_indicator")).setDefaultState(true).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.ALWAYS_ON).setCompiler((displayInfo12, compiler17, session12, availableWidth12, playerPos12) -> {
            if (((Boolean) displayInfo12.getEffectiveState()).booleanValue() && ((Integer) session12.getModMain().getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.LIGHT_OVERLAY_TYPE)).intValue() > 0) {
                compiler17.addWords(I18n.get("gui.xaero_light_overlay_status", new Object[0]));
            }
        });
        List<InfoDisplay<?>> list12 = ALL;
        Objects.requireNonNull(list12);
        LIGHT_OVERLAY_INDICATOR = compiler16.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder compiler18 = InfoDisplay.Builder.begin().setId("manual_cave_mode_indicator").setName(Component.translatable("gui.xaero_infodisplay_manual_cave_mode_indicator")).setDefaultState(true).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.ALWAYS_ON).setCompiler((displayInfo13, compiler19, session13, availableWidth13, playerPos13) -> {
            if (((Boolean) displayInfo13.getEffectiveState()).booleanValue() && session13.getProcessor().isManualCaveMode()) {
                compiler19.addWords(I18n.get("gui.xaero_manual_cave_mode", new Object[0]));
            }
        });
        List<InfoDisplay<?>> list13 = ALL;
        Objects.requireNonNull(list13);
        MANUAL_CAVE_MODE_INDICATOR = compiler18.setDestination((v1) -> {
            r1.add(v1);
        }).build();
        InfoDisplay.Builder compiler20 = InfoDisplay.Builder.begin().setId("custom_sub_world").setName(Component.translatable("gui.xaero_infodisplay_custom_sub_world")).setDefaultState(true).setCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setWidgetFactory(InfoDisplayCommonWidgetFactories.ALWAYS_ON).setCompiler((displayInfo14, compiler21, hudSession, availableWidth14, playerPos14) -> {
            if (!((Boolean) displayInfo14.getEffectiveState()).booleanValue()) {
                return;
            }
            MinimapSession session14 = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            MinimapWorldManager minimapWorldManager = session14.getWorldManager();
            MinimapWorld currentWorld = minimapWorldManager.getCurrentWorld();
            if (currentWorld != null && minimapWorldManager.getAutoWorld() != currentWorld) {
                compiler21.addWords(I18n.get("gui.xaero_using_custom_subworld", new Object[]{currentWorld.getContainer().getSubName()}));
            }
        });
        List<InfoDisplay<?>> list14 = ALL;
        Objects.requireNonNull(list14);
        CUSTOM_SUB_WORLD = compiler20.setDestination((v1) -> {
            r1.add(v1);
        }).build();
    }

    public static void forEach(Consumer<InfoDisplay<?>> action) {
        ALL.forEach(action);
    }
}
