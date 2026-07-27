package xaero.common.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiSlimeSeed.class */
public class GuiSlimeSeed extends GuiMinimapSettings {
    public EditBox seedTextField;
    private final MinimapWorld minimapWorld;
    private final IXaeroMinimap modMain;
    private final MinimapSession session;

    public GuiSlimeSeed(IXaeroMinimap modMain, MinimapSession session, Screen parent, Screen escape, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_slime_chunks"), parent, escape, context);
        this.modMain = modMain;
        this.session = session;
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.SLIME_CHUNKS), optionEntry(MinimapProfiledConfigOptions.OPEN_SLIME_CHUNKS_SCREEN)};
        this.minimapWorld = session.getWorldManager().getAutoWorld();
    }

    @Override // xaero.lib.client.gui.config.EditConfigScreen, xaero.lib.client.gui.GuiSettings, xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        this.seedTextField = new EditBox(this.font, (this.width / 2) - 100, (this.height / 7) + 68, 200, 20, Component.translatable("gui.xaero_used_seed"));
        this.seedTextField.setValue(String.valueOf(this.minimapWorld.getSlimeChunkSeed() == null ? "" : this.minimapWorld.getSlimeChunkSeed()));
        addRenderableWidget(this.seedTextField);
    }

    @Override // xaero.common.gui.GuiMinimapSettings, xaero.lib.client.gui.config.EditConfigScreen, xaero.lib.client.gui.GuiSettings, xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.render(guiGraphics, mouseX, mouseY, partial);
        this.seedTextField.render(guiGraphics, mouseX, mouseY, partial);
        guiGraphics.drawCenteredString(this.font, I18n.get("gui.xaero_used_seed", new Object[0]), this.width / 2, (this.height / 7) + 55, 16777215);
    }

    @Override // xaero.lib.client.gui.GuiSettings
    public void tick() {
    }

    @Override // xaero.common.gui.GuiMinimapSettings, xaero.lib.client.gui.GuiSettings
    public boolean keyPressed(int par1, int par2, int par3) throws NumberFormatException {
        boolean result = super.keyPressed(par1, par2, par3);
        if (par1 == 257) {
            goBack();
        }
        updateSlimeSeed();
        return result;
    }

    @Override // xaero.lib.client.gui.GuiSettings
    public boolean charTyped(char par1, int par2) throws NumberFormatException {
        boolean result = super.charTyped(par1, par2);
        updateSlimeSeed();
        return result;
    }

    private void updateSlimeSeed() throws NumberFormatException {
        String s = this.seedTextField.getValue();
        if (!StringUtils.isEmpty(s)) {
            try {
                long j = Long.parseLong(s);
                this.minimapWorld.setSlimeChunkSeed(Long.valueOf(j));
            } catch (NumberFormatException e) {
                this.minimapWorld.setSlimeChunkSeed(Long.valueOf(s.hashCode()));
            }
        }
        try {
            this.session.getWorldManagerIO().saveWorld(this.minimapWorld);
        } catch (IOException e2) {
            MinimapLogs.LOGGER.error("suppressed exception", e2);
        }
    }
}
