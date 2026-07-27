package xaero.hud.minimap.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.misc.Misc;
import xaero.hud.render.module.IModuleRenderer;
import xaero.hud.render.module.ModuleRenderContext;
import xaero.lib.client.gui.IScreenBase;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/module/MinimapRenderer.class */
public class MinimapRenderer implements IModuleRenderer<MinimapSession> {
    @Override // xaero.hud.render.module.IModuleRenderer
    public void render(MinimapSession session, ModuleRenderContext c, GuiGraphics guiGraphics, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (Misc.hasEffect(mc.player, Effects.NO_MINIMAP) || Misc.hasEffect(mc.player, Effects.NO_MINIMAP_HARMFUL) || session.getProcessor().getNoMinimapMessageReceived()) {
            return;
        }
        if (!session.getHideMinimapUnderScreen() || mc.screen == null || (mc.screen instanceof IScreenBase) || (mc.screen instanceof ChatScreen) || (mc.screen instanceof DeathScreen)) {
            if (session.getHideMinimapUnderF3() && mc.getDebugOverlay().showDebugScreen()) {
                return;
            }
            MinimapRendererHelper.restoreDefaultShaderBlendState();
            session.getProcessor().onRender(guiGraphics, c.x, c.y, c.screenWidth, c.screenHeight, c.screenScale, session.getConfiguredWidth(), c.w, partialTicks, HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers());
            MinimapRendererHelper.restoreDefaultShaderBlendState();
        }
    }
}
