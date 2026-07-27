package xaero.hud.minimap.compass.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.misc.Misc;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/compass/render/CompassRenderer.class */
public class CompassRenderer {
    private final HudMod modMain;
    private final Minecraft mc;
    private double[] partialDest = new double[2];

    public CompassRenderer(HudMod modMain, Minecraft mc) {
        this.modMain = modMain;
        this.mc = mc;
    }

    public void drawCompass(PoseStack matrixStack, int specW, int specH, double ps, double pc, double zoom, boolean circle, float minimapScale, boolean background, MultiBufferSource.BufferSource textRenderTypeBuffer, VertexConsumer nameBgBuilder) {
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        int compassLocationConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.COMPASS_LOCATION)).intValue();
        if (compassLocationConfig == 0) {
            return;
        }
        int shadowColorConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.COMPASS_SHADOW_COLOR)).intValue();
        WaypointColor defaultColor = WaypointColor.fromIndex(shadowColorConfig);
        this.modMain.getMinimap().getMinimapFBORenderer().getHelper();
        int northShadowColorConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.COMPASS_NORTH_SHADOW_COLOR)).intValue();
        int effectiveNorthShadowColorConfig = northShadowColorConfig == -1 ? shadowColorConfig : northShadowColorConfig;
        int i = 0;
        while (i < 4) {
            double offX = (i & 1) * (i == 1 ? 10000 : -10000);
            double offY = ((i + 1) & 1) * (i == 2 ? 10000 : -10000);
            matrixStack.pushPose();
            MinimapElementOverMapRendererHandler.translatePosition(matrixStack, specW, specH, specW, specH, ps, pc, offX, offY, zoom, circle, this.partialDest);
            matrixStack.translate(-1.0f, -1.0f, 0.0f);
            matrixStack.scale(minimapScale, minimapScale, 1.0f);
            Component initials = CardinalDirection.values()[i].getInitials();
            int halfW = (this.mc.font.width(initials) / 2) - 1;
            WaypointColor effectiveColor = i == 0 ? WaypointColor.fromIndex(effectiveNorthShadowColorConfig) : defaultColor;
            if (background) {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
                int addedFrame = halfW > 3 ? halfW - 3 : 0;
                RenderBufferUtil.addColoredRect(matrixStack.last().pose(), nameBgBuilder, (-4) - addedFrame, (-4) - addedFrame, 9 + (2 * addedFrame), 9 + (2 * addedFrame), (-1879048192) | (effectiveColor.getHex() & 16777215));
                RenderSystem.defaultBlendFunc();
            }
            Misc.drawNormalText(matrixStack, initials, (-halfW) + 1, -2.0f, effectiveColor.getHex(), false, textRenderTypeBuffer);
            matrixStack.translate(0.0f, 0.0f, 1.0f);
            Misc.drawNormalText(matrixStack, initials, -halfW, -3.0f, -1, false, textRenderTypeBuffer);
            matrixStack.popPose();
            i++;
        }
        matrixStack.translate(0.0f, 0.0f, 2.0f);
    }
}
