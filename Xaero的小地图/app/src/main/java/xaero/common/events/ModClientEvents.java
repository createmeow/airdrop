package xaero.common.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.joml.Vector4f;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.anim.MultiplyAnimationHelper;
import xaero.common.interfaces.InterfaceManager;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/ModClientEvents.class */
public abstract class ModClientEvents {
    protected IXaeroMinimap modMain;
    private final Vector4f REUSABLE_ZERO_VECTOR4 = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    public ModClientEvents(IXaeroMinimap modMain) {
        this.modMain = modMain;
    }

    public void handleTextureStitchEventPost(TextureAtlas texture) {
        if (texture.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            if (minimapSession != null) {
                minimapSession.getMinimapProcessor().getMinimapWriter().setClearBlockColours(true);
                minimapSession.getMinimapProcessor().getMinimapWriter().resetShortBlocks();
            }
            InterfaceManager interfaceManager = this.modMain.getInterfaces();
            if (interfaceManager != null) {
                interfaceManager.getMinimapInterface().getMinimapFBORenderer().resetEntityIcons();
                handleTextureStitchEventPost_onReset();
            }
        }
    }

    public void handleRenderModOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        MultiplyAnimationHelper.tick();
        if (Minecraft.getInstance().options.hideGui) {
            return;
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession != null) {
            float currentDepth = this.REUSABLE_ZERO_VECTOR4.mul(guiGraphics.pose().last().pose()).z;
            this.REUSABLE_ZERO_VECTOR4.set(0.0f, 0.0f, 0.0f, 1.0f);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().setIdentity();
            guiGraphics.pose().translate(0.0f, 0.0f, currentDepth - 1.0f);
            this.modMain.getHudRenderer().render(this.modMain.getHud(), guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(true));
            this.modMain.getMinimap().getWaypointGuiRenderer().drawSetChange(minimapSession, guiGraphics, Minecraft.getInstance().getWindow());
            guiGraphics.pose().popPose();
        }
    }

    protected void handleTextureStitchEventPost_onReset() {
    }
}
