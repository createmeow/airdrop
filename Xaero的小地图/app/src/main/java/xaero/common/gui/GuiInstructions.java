package xaero.common.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.lib.client.gui.ScreenBase;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiInstructions.class */
public class GuiInstructions extends ScreenBase {
    public GuiInstructions(IXaeroMinimap modMain, Screen par1GuiScreen, Screen escape) {
        super(par1GuiScreen, escape, Component.translatable("gui.xaero_instructions"));
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.translatable("gui.xaero_OK", new Object[0]), b -> {
            goBack();
        }).bounds((this.width / 2) - 100, (this.height / 6) + 168, 200, 20).build());
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        super.renderBackground(guiGraphics, par1, par2, par3);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_select", new Object[0]), this.width / 2, (this.height / 7) + 10, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_drag", new Object[0]), this.width / 2, (this.height / 7) + 21, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_deselect", new Object[0]), this.width / 2, (this.height / 7) + 32, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_center", new Object[0]), this.width / 2, (this.height / 7) + 43, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_different_centered", new Object[0]), this.width / 2, (this.height / 7) + 54, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_flip", new Object[0]), this.width / 2, (this.height / 7) + 65, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_settings", new Object[0]), this.width / 2, (this.height / 7) + 76, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_preset", new Object[0]), this.width / 2, (this.height / 7) + 87, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_save", new Object[0]), this.width / 2, (this.height / 7) + 98, 16777215);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_howto_cancel", new Object[0]), this.width / 2, (this.height / 7) + 109, 16777215);
    }
}
