package xaero.common.gui;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.RootCategorySettingEntry;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiEntityRadarSettings.class */
public class GuiEntityRadarSettings extends GuiMinimapSettings {
    public GuiEntityRadarSettings(IXaeroMinimap modMain, Screen backScreen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_entity_radar_settings"), backScreen, escScreen, context);
        BooleanSupplier allowNullSupplier = () -> {
            return HudMod.INSTANCE.getEntityRadarCategoryManager().getEditedCategoryConfig().isAllowNullValues();
        };
        Supplier<ObjectCategory<?, ?>> categorySupplier = () -> {
            return HudMod.INSTANCE.getEntityRadarCategoryManager().getEditedCategory();
        };
        BiConsumer<Object, Object> defaultValueChangeListener = (o, n) -> {
            HudMod.INSTANCE.getEntityRadarCategoryManager().setEditedCategoryNeedsSaving(true);
        };
        ObjectCategorySetting<Double> objectCategorySetting = EntityRadarCategorySettings.ENTITY_NUMBER;
        MutableComponent mutableComponentTranslatable = Component.translatable("gui.xaero_entity_amount");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> entityNumberEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting, mutableComponentTranslatable, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Boolean> objectCategorySetting2 = EntityRadarCategorySettings.HEIGHT_FADE;
        MutableComponent mutableComponentTranslatable2 = Component.translatable("gui.xaero_entity_depth");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> entityFadeEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting2, mutableComponentTranslatable2, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Boolean> objectCategorySetting3 = EntityRadarCategorySettings.ALWAYS_NAMETAGS;
        MutableComponent mutableComponentTranslatable3 = Component.translatable("gui.xaero_always_entity_nametags");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> alwaysNametagsEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting3, mutableComponentTranslatable3, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Double> objectCategorySetting4 = EntityRadarCategorySettings.DISPLAY_Y;
        MutableComponent mutableComponentTranslatable4 = Component.translatable("gui.xaero_entity_display_height_full");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> displayYEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting4, mutableComponentTranslatable4, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Boolean> objectCategorySetting5 = EntityRadarCategorySettings.ICON_NAME_FALLBACK;
        MutableComponent mutableComponentTranslatable5 = Component.translatable("gui.xaero_entity_icon_name_fallback");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> iconNameFallbackEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting5, mutableComponentTranslatable5, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Double> objectCategorySetting6 = EntityRadarCategorySettings.ICONS;
        MutableComponent mutableComponentTranslatable6 = Component.translatable("gui.xaero_radar_setting_icons");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> iconsEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting6, mutableComponentTranslatable6, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Double> objectCategorySetting7 = EntityRadarCategorySettings.NAMES;
        MutableComponent mutableComponentTranslatable7 = Component.translatable("gui.xaero_radar_setting_names");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> namesEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting7, mutableComponentTranslatable7, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Double> objectCategorySetting8 = EntityRadarCategorySettings.RENDER_OVER_MINIMAP;
        MutableComponent mutableComponentTranslatable8 = Component.translatable("gui.xaero_radar_render_radar_over_frame");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> renderOverMinimapEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting8, mutableComponentTranslatable8, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Double> objectCategorySetting9 = EntityRadarCategorySettings.DOT_SIZE;
        MutableComponent mutableComponentTranslatable9 = Component.translatable("gui.xaero_dots_size");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> dotSizeEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting9, mutableComponentTranslatable9, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        RootCategorySettingEntry<?> iconScaleEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, EntityRadarCategorySettings.ICON_SCALE, Component.translatable("gui.xaero_entity_heads_scale"), (currentScale, newScale) -> {
            defaultValueChangeListener.accept(currentScale, newScale);
            if (context.isClientSide()) {
                if (newScale.doubleValue() >= 1.0d) {
                    if ((newScale.doubleValue() < 1.0d) == (currentScale.doubleValue() < 1.0d)) {
                        return;
                    }
                }
                modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().resetEntityIcons();
            }
        });
        ObjectCategorySetting<Double> objectCategorySetting10 = EntityRadarCategorySettings.HEIGHT_LIMIT;
        MutableComponent mutableComponentTranslatable10 = Component.translatable("gui.xaero_height_limit");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> heightLimitEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting10, mutableComponentTranslatable10, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        ObjectCategorySetting<Double> objectCategorySetting11 = EntityRadarCategorySettings.START_FADING_AT;
        MutableComponent mutableComponentTranslatable11 = Component.translatable("gui.xaero_start_fading_at");
        Objects.requireNonNull(defaultValueChangeListener);
        RootCategorySettingEntry<?> startFadingEntry = new RootCategorySettingEntry<>(categorySupplier, allowNullSupplier, objectCategorySetting11, mutableComponentTranslatable11, (v1, v2) -> {
            r6.accept(v1, v2);
        });
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.RADAR_CATEGORIES), optionEntry(MinimapProfiledConfigOptions.DISPLAY_RADAR), dotSizeEntry, optionEntry(MinimapProfiledConfigOptions.RADAR_DOTS_STYLE), optionEntry(MinimapProfiledConfigOptions.RADAR_SMOOTH_DOTS), iconsEntry, namesEntry, iconScaleEntry, optionEntry(MinimapProfiledConfigOptions.RADAR_NAME_SCALE), alwaysNametagsEntry, iconNameFallbackEntry, entityNumberEntry, heightLimitEntry, entityFadeEntry, startFadingEntry, displayYEntry, optionEntry(MinimapProfiledConfigOptions.RADAR_MAIN_ENTITY), optionEntry(MinimapProfiledConfigOptions.RADAR_MAIN_DOT_SIZE), optionEntry(MinimapProfiledConfigOptions.ARROW_SCALE), optionEntry(MinimapProfiledConfigOptions.ARROW_COLOR), optionEntry(MinimapProfiledConfigOptions.ARROW_OPACITY), renderOverMinimapEntry, optionEntry(MinimapProfiledConfigOptions.RADAR_HIDE_INVISIBLE), optionEntry(MinimapProfiledConfigOptions.TRACKED_PLAYERS_ON_MINIMAP), optionEntry(MinimapProfiledConfigOptions.TRACKED_PLAYERS_IN_WORLD), optionEntry(MinimapProfiledConfigOptions.TRACKED_PLAYER_MINIMAP_ICON_SCALE), optionEntry(MinimapProfiledConfigOptions.TRACKED_PLAYER_WORLD_ICON_SCALE), optionEntry(MinimapProfiledConfigOptions.TRACKED_PLAYER_WORLD_NAME_SCALE)};
    }
}
