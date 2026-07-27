package xaero.hud.minimap.info.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.misc.Misc;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.common.config.MinimapConfigConstants;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/render/InfoDisplayRenderer.class */
public final class InfoDisplayRenderer {
    public static final int DEPTH_OFFSET = 2;
    private final InfoDisplayCompiler compiler;

    public InfoDisplayRenderer(InfoDisplayCompiler compiler) {
        this.compiler = compiler;
    }

    public void render(GuiGraphics guiGraphics, MinimapSession session, Minimap minimap, int height, int size, BlockPos playerPos, int scaledX, int scaledY, float mapScale, MultiBufferSource.BufferSource renderTypeBuffer) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        PoseStack matrixStack = guiGraphics.pose();
        int scaledHeight = (int) (height * mapScale);
        int align = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.INFO_DISPLAY_ALIGNMENT)).intValue();
        boolean under = scaledY + (size / 2) < scaledHeight / 2;
        int stringY = scaledY + (under ? size : -9);
        int bgOpacityConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.INFO_DISPLAY_BG_OPACITY)).intValue();
        int bgOpacityMask = ((bgOpacityConfig * 255) / 100) << 24;
        matrixStack.translate(0.0d, 0.0d, 0.5d);
        for (InfoDisplay<?> infoDisplay : minimap.getInfoDisplays().getManager().getOrderedStream()) {
            List<Component> compiledLines = this.compiler.compile(infoDisplay, session, size, playerPos);
            int textColorIndex = infoDisplay.getTextColor();
            int backgroundColorIndex = infoDisplay.getBackgroundColor();
            int textColor = MinimapConfigConstants.COLORS[textColorIndex < 0 ? 15 : textColorIndex % MinimapConfigConstants.COLORS.length];
            int backgroundColor = backgroundColorIndex < 0 ? 0 : bgOpacityMask | (MinimapConfigConstants.COLORS[backgroundColorIndex % MinimapConfigConstants.COLORS.length] & 16777215);
            VertexConsumer backgroundVertexBuffer = renderTypeBuffer.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
            int startIndex = 0;
            int endIndex = compiledLines.size();
            int step = 1;
            if (!under) {
                startIndex = endIndex - 1;
                endIndex = -1;
                step = -1;
            }
            int i = startIndex;
            while (true) {
                int i2 = i;
                if (i2 != endIndex) {
                    Component s = compiledLines.get(i2);
                    int stringWidth = Minecraft.getInstance().font.width(s);
                    int stringX = scaledX + (align == 0 ? (size / 2) - (stringWidth / 2) : align == 1 ? 6 : (size - 6) - stringWidth);
                    if (backgroundColor != 0) {
                        RenderBufferUtil.addColoredRect(matrixStack.last().pose(), backgroundVertexBuffer, stringX - 1, stringY - 1, stringWidth + 2, 10, backgroundColor);
                    }
                    Misc.drawNormalText(matrixStack, s, stringX, stringY, textColor, true, renderTypeBuffer);
                    stringY += 10 * step;
                    i = i2 + step;
                }
            }
            compiledLines.clear();
        }
        matrixStack.translate(0.0d, 0.0d, -0.5d);
        renderTypeBuffer.endBatch();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/render/InfoDisplayRenderer$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        private Builder setDefault() {
            return this;
        }

        public InfoDisplayRenderer build() {
            return new InfoDisplayRenderer(InfoDisplayCompiler.Builder.begin().build());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
