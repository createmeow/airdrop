package xaero.common.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.minimap.MinimapInterface;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.lib.client.controls.util.KeyMappingUtils;
import xaero.lib.client.gui.config.EditConfigScreen;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapSettings.class */
public abstract class GuiMinimapSettings extends EditConfigScreen {
    public GuiMinimapSettings(Component title, Screen par1Screen, Screen escScreen, IEditConfigScreenContext context) {
        super(title, par1Screen, escScreen, context, HudMod.INSTANCE.getHudConfigs());
        if (!(par1Screen instanceof GuiMinimapSettings) && !(par1Screen instanceof GuiEntityRadarCategoryEditor)) {
            HudMod.INSTANCE.getEntityRadarCategoryManager().forgetEditedCategory();
        }
        this.canSkipWorldRender = false;
        this.shouldRenderEscapeScreen = false;
    }

    @Override // xaero.lib.client.gui.config.EditConfigScreen
    protected void onEditedProfileSwitch() {
        super.onEditedProfileSwitch();
        EntityRadarCategoryManager categoryManager = HudMod.INSTANCE.getEntityRadarCategoryManager();
        if (!this.context.hasPermission(this.channel) || !this.context.getSyncStatus(this.channel)) {
            if (categoryManager.getEditedCategory() != null) {
                categoryManager.forgetEditedCategory();
            }
        } else {
            ConfigProfile newConfig = getProfileOnUpdate();
            if (categoryManager.getEditedCategoryConfig() == newConfig) {
                return;
            }
            categoryManager.loadEditedCategory(newConfig, this.context.isClientSide());
        }
    }

    @Override // xaero.lib.client.gui.config.EditConfigScreen, xaero.lib.client.gui.GuiSettings, xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        super.render(guiGraphics, par1, par2, par3);
        MinimapInterface minimapInterface = HudMod.INSTANCE.getInterfaces().getMinimapInterface();
        boolean mapSafeMode = ((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.SAFE_MODE)).booleanValue();
        if (!mapSafeMode && minimapInterface.getMinimapFBORenderer().isTriedFBO() && !minimapInterface.getMinimapFBORenderer().isLoadedFBO()) {
            guiGraphics.drawCenteredString(this.font, "§4You've been forced into safe mode! :(", this.width / 2, 11, 16777215);
        }
    }

    @Override // xaero.lib.client.gui.config.EditConfigScreen
    protected void handleChanges() {
        saveEditedCategoryIfNeeded();
        super.handleChanges();
    }

    private void saveEditedCategoryIfNeeded() {
        EntityRadarCategoryManager categoryManager = HudMod.INSTANCE.getEntityRadarCategoryManager();
        if (!categoryManager.editedCategoryNeedsSaving()) {
            return;
        }
        categoryManager.storeEditedCategory(this.context.isClientSide());
        handleChangesOnExit();
    }

    @Override // xaero.lib.client.gui.GuiSettings
    public boolean keyPressed(int key, int scancode, int mods) {
        if (super.keyPressed(key, scancode, mods)) {
            return true;
        }
        if ((this.context.isClientSide() && KeyMappingUtils.inputMatches(InputConstants.Type.KEYSYM, key, (KeyMapping) HudMod.INSTANCE.getSettingsKey(), 0)) || (!this.context.isClientSide() && KeyMappingUtils.inputMatches(InputConstants.Type.KEYSYM, key, (KeyMapping) HudMod.INSTANCE.getServerSettingsKey(), 0))) {
            onExit(this.escape);
            return true;
        }
        return false;
    }
}
