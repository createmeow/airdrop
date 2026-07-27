package xaero.common.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.category.ui.EditorCategoryNodeConverter;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.node.EditorCategoryNode;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.minimap.radar.category.ui.EditorEntityRadarCategoryNodeConverter;
import xaero.hud.minimap.radar.category.ui.node.EditorEntityRadarCategoryNode;
import xaero.hud.minimap.radar.category.ui.node.EditorEntityRadarCategorySettingsNode;
import xaero.lib.client.gui.config.EditConfigScreen;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiEntityRadarCategoryEditor.class */
public class GuiEntityRadarCategoryEditor extends GuiCategoryEditor<EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder> {
    public static final Component SERVER_ENFORCED_COMPONENT = Component.translatable("gui.xaero_entity_category_editor_server_enforced").withStyle(ChatFormatting.YELLOW);
    private final EntityRadarCategoryManager entityRadarCategoryManager;
    private final boolean clientSide;
    private final Runnable onChange;
    private final boolean viewingEnforced;

    @Override // xaero.hud.category.ui.GuiCategoryEditor
    protected /* bridge */ /* synthetic */ EditorCategoryNode constructDefaultData(EditorCategoryNodeConverter editorCategoryNodeConverter) {
        return constructDefaultData((EditorCategoryNodeConverter<EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder>) editorCategoryNodeConverter);
    }

    @Override // xaero.hud.category.ui.GuiCategoryEditor
    protected /* bridge */ /* synthetic */ EditorCategoryNode constructEditorData(EditorCategoryNodeConverter editorCategoryNodeConverter) {
        return constructEditorData((EditorCategoryNodeConverter<EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder>) editorCategoryNodeConverter);
    }

    public GuiEntityRadarCategoryEditor(IXaeroMinimap modMain, EditConfigScreen parent, Screen escape, Runnable onChange, boolean viewingEnforced) {
        super(modMain, parent, escape, Component.translatable("gui.xaero_entity_radar_categories"), EditorEntityRadarCategoryNodeConverter.Builder.begin().build(), viewingEnforced);
        this.entityRadarCategoryManager = modMain.getEntityRadarCategoryManager();
        this.clientSide = parent.getContext().isClientSide();
        this.onChange = onChange;
        this.viewingEnforced = viewingEnforced;
    }

    @Override // xaero.hud.category.ui.GuiCategoryEditor
    protected EditorEntityRadarCategoryNode constructEditorData(EditorCategoryNodeConverter<EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder> dataConverter) {
        EntityRadarCategory editedCategory;
        if (this.readOnly) {
            editedCategory = this.modMain.getEntityRadarCategoryManager().getSyncedRootCategory();
        } else {
            editedCategory = this.modMain.getEntityRadarCategoryManager().getEditedCategory();
        }
        EntityRadarCategory editedCategory2 = editedCategory;
        return (EditorEntityRadarCategoryNode) dataConverter.convert(editedCategory2, !this.readOnly && ((EditConfigScreen) this.parent).getContext().isClientSide());
    }

    @Override // xaero.hud.category.ui.GuiCategoryEditor
    protected EditorEntityRadarCategoryNode constructDefaultData(EditorCategoryNodeConverter<EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder> dataConverter) {
        EntityRadarCategory entityRadarCategoryFetchDefaultServerCategory;
        this.modMain.getSettings().resetEntityRadarBackwardsCompatibilityConfig();
        if (this.clientSide) {
            entityRadarCategoryFetchDefaultServerCategory = this.entityRadarCategoryManager.fetchDefaultClientCategory();
        } else {
            entityRadarCategoryFetchDefaultServerCategory = this.entityRadarCategoryManager.fetchDefaultServerCategory();
        }
        EntityRadarCategory rootCategory = entityRadarCategoryFetchDefaultServerCategory;
        return (EditorEntityRadarCategoryNode) dataConverter.convert(rootCategory, this.clientSide);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.hud.category.ui.GuiCategoryEditor
    public void onConfigConfirmed(EntityRadarCategory confirmedRootCategory) {
        this.entityRadarCategoryManager.storeEditedCategory(confirmedRootCategory, this.clientSide);
        this.onChange.run();
    }

    @Override // xaero.hud.category.ui.GuiCategoryEditor, xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        if (this.viewingEnforced) {
            guiGraphics.drawCenteredString(this.font, SERVER_ENFORCED_COMPONENT, this.width / 2, 15, -1);
        }
    }
}
