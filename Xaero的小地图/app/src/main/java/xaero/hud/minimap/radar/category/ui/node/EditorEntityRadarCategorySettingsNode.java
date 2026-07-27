package xaero.hud.minimap.radar.category.ui.node;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xaero.common.misc.ListFactory;
import xaero.hud.category.rule.ObjectCategoryRule;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorFilterSettingsNode;
import xaero.hud.category.ui.node.EditorSettingsNode;
import xaero.hud.category.ui.node.options.EditorExpandingOptionsNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.options.EditorSimpleButtonNode;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode;
import xaero.hud.category.ui.node.options.text.EditorTextFieldOptionsNode;
import xaero.hud.category.ui.node.rule.EditorExcludeListNode;
import xaero.hud.category.ui.node.rule.EditorIncludeListNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.hud.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.hud.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.hud.minimap.radar.category.ui.EntityRadarCategoryUIConstants;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.common.gui.widget.TooltipInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/ui/node/EditorEntityRadarCategorySettingsNode.class */
public final class EditorEntityRadarCategorySettingsNode<SETTING_DATA extends EditorOptionsNode<?> & IEditorSettingNode<?>> extends EditorFilterSettingsNode<Entity, Player, SETTING_DATA> {
    private EditorEntityRadarCategorySettingsNode(Map<ObjectCategorySetting<?>, SETTING_DATA> settings, List<SETTING_DATA> settingList, @Nonnull EditorSimpleButtonNode deleteButton, @Nonnull EditorSimpleButtonNode protectionButton, @Nonnull EditorTextFieldOptionsNode nameOption, ListFactory listFactory, boolean rootSettings, EditorExpandingOptionsNode<ObjectCategoryRule<Entity, Player>> baseRule, EditorIncludeListNode includeList, EditorExcludeListNode excludeList, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, boolean protection) {
        super(settings, settingList, deleteButton, protectionButton, nameOption, listFactory, rootSettings, baseRule, includeList, excludeList, movable, listEntryFactory, tooltipSupplier, protection);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/ui/node/EditorEntityRadarCategorySettingsNode$Builder.class */
    public static final class Builder extends EditorFilterSettingsNode.Builder<Entity, Player, EditorEntityRadarCategorySettingsNode<?>, Builder> {
        @Override // xaero.hud.category.ui.node.EditorSettingsNode.Builder
        protected /* bridge */ /* synthetic */ EditorSettingsNode buildInternally(List list, Map map) {
            return buildInternally((List<IEditorSettingNode<?>>) list, (Map<ObjectCategorySetting<?>, IEditorSettingNode<?>>) map);
        }

        private Builder() {
            super(EntityRadarCategoryConstants.MAP_FACTORY, EntityRadarCategoryConstants.LIST_FACTORY, EntityRadarCategorySettings.SETTINGS_LIST, EntityRadarCategoryHardRules.HARD_RULES_LIST, EntityRadarCategoryUIConstants.SETTING_NODE_BUILDER_FACTORIES);
        }

        @Override // xaero.hud.category.ui.node.EditorFilterSettingsNode.Builder, xaero.hud.category.ui.node.EditorSettingsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder setDefault() {
            super.setDefault();
            getIncludeListBuilder().setTooltipInfoSupplier((parent, bd) -> {
                return new TooltipInfo("gui.xaero_box_category_include_list2");
            });
            getExcludeListBuilder().setTooltipInfoSupplier((parent2, bd2) -> {
                return new TooltipInfo("gui.xaero_box_category_exclude_list2");
            });
            this.baseRuleBuilder.setTooltipInfoSupplier((parent3, bd3) -> {
                return new TooltipInfo("gui.xaero_box_category_hard_include2");
            });
            this.baseRuleBuilder.setCurrentValue(EntityRadarCategoryHardRules.IS_NOTHING);
            getIncludeListBuilder().getIncludeInSuperToggleDataBuilder().setTooltipInfoSupplier((parent4, data) -> {
                return new TooltipInfo("gui.xaero_box_category_include_list_include_in_super2");
            });
            Tooltip listHelp = new Tooltip("gui.xaero_box_category_list_add");
            IEditorDataTooltipSupplier helpTooltipSupplier = (parent5, data2) -> {
                if (data2.isExpanded()) {
                    return listHelp;
                }
                return null;
            };
            getIncludeListBuilder().setHelpTooltipSupplier(helpTooltipSupplier);
            getExcludeListBuilder().setHelpTooltipSupplier(helpTooltipSupplier);
            EditorTextFieldOptionsNode.Builder includeListAdderBuilder = getIncludeListBuilder().getAdderBuilder();
            EditorTextFieldOptionsNode.Builder excludeListAdderBuilder = getExcludeListBuilder().getAdderBuilder();
            includeListAdderBuilder.setAllowCustomInput(true);
            excludeListAdderBuilder.setAllowCustomInput(true);
            getIncludeListBuilder().setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
            getIncludeListBuilder().setListRuleTypes(EntityRadarListRuleTypes.TYPE_LIST);
            getExcludeListBuilder().setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
            getExcludeListBuilder().setListRuleTypes(EntityRadarListRuleTypes.TYPE_LIST);
            return this;
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }

        @Override // xaero.hud.category.ui.node.EditorSettingsNode.Builder
        protected EditorEntityRadarCategorySettingsNode<?> buildInternally(List<IEditorSettingNode<?>> builtSettingData, Map<ObjectCategorySetting<?>, IEditorSettingNode<?>> builtSettingsDataMap) {
            EditorEntityRadarCategorySettingsNode<?> result = new EditorEntityRadarCategorySettingsNode<>(builtSettingsDataMap, builtSettingData, this.deleteButtonBuilder.build(), this.protectionButtonBuilder.build(), this.nameOptionBuilder.build(), this.listFactory, this.rootSettings, this.baseRuleBuilder.build(), buildIncludeList(), buildExcludeList(), this.movable, this.listEntryFactory, this.tooltipSupplier, this.protection);
            return result;
        }
    }
}
