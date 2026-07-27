package xaero.common.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.widget.MySmallButton;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiNewSet.class */
public class GuiNewSet extends ScreenBase {
    private EditBox nameTextField;
    private MinimapSession session;
    private MinimapWorldManager manager;
    private MinimapWorld minimapWorld;
    private Button confirmButton;

    @Deprecated
    public GuiNewSet(IXaeroMinimap modMain, XaeroMinimapSession session, Screen par1GuiScreen, WaypointWorld waypointWorld) {
        this(modMain, session, par1GuiScreen, (Screen) null, waypointWorld);
    }

    @Deprecated
    public GuiNewSet(IXaeroMinimap modMain, XaeroMinimapSession session, Screen par1GuiScreen, Screen escapeScreen, WaypointWorld waypointWorld) {
        this(modMain, (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession(), par1GuiScreen, escapeScreen, waypointWorld);
    }

    public GuiNewSet(IXaeroMinimap modMain, MinimapSession session, Screen par1GuiScreen, Screen escapeScreen, MinimapWorld minimapWorld) {
        super(par1GuiScreen, escapeScreen, Component.translatable("gui.xaero_create_set"));
        this.session = session;
        this.manager = this.session.getWorldManager();
        this.minimapWorld = minimapWorld;
        this.canSkipWorldRender = true;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        this.nameTextField = new EditBox(this.font, (this.width / 2) - 100, 60, 200, 20, Component.translatable("gui.xaero_set_name"));
        setFocused(this.nameTextField);
        this.nameTextField.setFocused(true);
        addRenderableWidget(this.nameTextField);
        MySmallButton mySmallButton = new MySmallButton(200, (this.width / 2) - 155, (this.height / 6) + 168, Component.translatable("gui.xaero_confirm", new Object[0]), b -> {
            if (canConfirm()) {
                String setName = this.nameTextField.getValue().replace(":", "§§");
                this.minimapWorld.setCurrentWaypointSetId(setName);
                this.minimapWorld.addWaypointSet(setName);
                try {
                    this.session.getWorldManagerIO().saveWorld(this.minimapWorld);
                } catch (IOException e) {
                    MinimapLogs.LOGGER.error("suppressed exception", e);
                }
                goBack();
            }
        });
        this.confirmButton = mySmallButton;
        addRenderableWidget(mySmallButton);
        addRenderableWidget(new MySmallButton(201, (this.width / 2) + 5, (this.height / 6) + 168, Component.translatable("gui.xaero_cancel", new Object[0]), b2 -> {
            goBack();
        }));
        updateConfirmButton();
    }

    protected void setInitialFocus() {
    }

    private boolean canConfirm() {
        return this.nameTextField.getValue().length() > 0 && this.minimapWorld.getWaypointSet(this.nameTextField.getValue()) == null;
    }

    private void updateConfirmButton() {
        this.confirmButton.active = canConfirm();
    }

    public boolean keyPressed(int par1, int par2, int par3) {
        boolean result = super.keyPressed(par1, par2, par3);
        if (par1 == 257 && canConfirm()) {
            this.confirmButton.onClick(0.0d, 0.0d);
            return true;
        }
        return result;
    }

    public void tick() {
        updateConfirmButton();
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void renderBackground(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        super.renderBackground(guiGraphics, par1, par2, par3);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        this.nameTextField.render(guiGraphics, par1, par2, par3);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int par1, int par2, float par3) {
        renderEscapeScreen(guiGraphics, par1, par2, par3);
        super.render(guiGraphics, par1, par2, par3);
    }
}
