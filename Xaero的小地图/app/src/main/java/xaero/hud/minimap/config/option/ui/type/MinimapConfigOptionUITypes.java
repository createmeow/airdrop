package xaero.hud.minimap.config.option.ui.type;

import xaero.common.HudMod;
import xaero.common.gui.GuiEntityRadarCategoryEditor;
import xaero.common.gui.GuiInfoDisplayEdit;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.lib.client.config.option.ui.factory.StandardConfigWidgetFactories;
import xaero.lib.client.config.option.ui.factory.StandardViewEnforcedConditions;
import xaero.lib.client.config.option.ui.type.ConfigOptionUIType;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/config/option/ui/type/MinimapConfigOptionUITypes.class */
public class MinimapConfigOptionUITypes {
    public static final ConfigOptionUIType<ConfigOption<EntityRadarCategoryData>> RADAR_CATEGORIES_EDITOR = ConfigOptionUIType.Builder.begin().setWidgetFactory(StandardConfigWidgetFactories.getOpenScreenFactory((parent, escape, config, enforced, option, onChange, readOnly, includeNullValue) -> {
        if (HudMod.INSTANCE.getEntityRadarCategoryManager().getEditedCategory() == null) {
            return null;
        }
        return new GuiEntityRadarCategoryEditor(HudMod.INSTANCE, parent, parent, onChange, readOnly);
    }, StandardViewEnforcedConditions.SHIFT_PRESSED)).build();
    public static final ConfigOptionUIType<ConfigOption<InfoDisplayManagerConfigData>> INFO_DISPLAY_CONFIG_EDITOR = ConfigOptionUIType.Builder.begin().setWidgetFactory(StandardConfigWidgetFactories.getOpenScreenFactory((parent, escape, config, enforced, option, onChange, readOnly, includeNullValue) -> {
        return new GuiInfoDisplayEdit(parent, parent, config, onChange, readOnly);
    }, StandardViewEnforcedConditions.SHIFT_PRESSED)).build();
}
