package xaero.common.minimap.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.entity.EntityUtils;
import xaero.hud.gui.util.GuiUtils;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.common.config.MinimapConfigConstants;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.compass.render.CompassRenderer;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.RadarSession;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.hud.minimap.radar.color.RadarColor;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderer;
import xaero.hud.render.TextureLocations;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.gui.util.graphics.GuiGraphicsUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/render/MinimapRenderer.class */
public abstract class MinimapRenderer {
    public static final int black = -16777216;
    public static final int slime = -2142047936;
    protected HudMod modMain;
    protected Minecraft mc;
    protected Minimap minimap;
    protected WaypointMapRenderer waypointMapRenderer;
    private int lastMinimapSize;
    protected final CompassRenderer compassRenderer;
    protected double zoom = 1.0d;
    protected MinimapRendererHelper helper = new MinimapRendererHelper();
    private BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

    protected abstract void renderChunks(MinimapSession minimapSession, GuiGraphics guiGraphics, MinimapProcessor minimapProcessor, Vec3 vec3, ResourceKey<Level> resourceKey, double d, int i, int i2, float f, float f2, int i3, boolean z, boolean z2, int i4, double d2, double d3, boolean z3, boolean z4, ModSettings modSettings, CustomVertexConsumers customVertexConsumers);

    public MinimapRenderer(HudMod modMain, Minecraft mc, WaypointMapRenderer waypointMapRenderer, Minimap minimap, CompassRenderer compassRenderer) {
        this.modMain = modMain;
        this.mc = mc;
        this.waypointMapRenderer = waypointMapRenderer;
        this.minimap = minimap;
        this.compassRenderer = compassRenderer;
    }

    public double getRenderAngle(boolean lockedNorth) {
        if (lockedNorth) {
            return 90.0d;
        }
        return getActualAngle();
    }

    private double getActualAngle() {
        double rotation = this.mc.gameRenderer.getMainCamera().getYRot();
        return (-90.0d) - rotation;
    }

    public void renderMinimap(MinimapSession minimapSession, GuiGraphics guiGraphics, MinimapProcessor minimap, int x, int y, int width, int height, double scale, int size, float partial, CustomVertexConsumers cvc) {
        float r;
        float g;
        float b;
        float a;
        PoseStack matrixStack = guiGraphics.pose();
        ModSettings settings = this.modMain.getSettings();
        int minimapSizeConfig = MinimapConfigClientUtils.getEffectiveMinimapSize();
        if (minimapSizeConfig != this.lastMinimapSize) {
            this.lastMinimapSize = minimapSizeConfig;
            minimap.setToResetImage(true);
        }
        minimap.getRadarSession().getStateUpdater().setLastRenderViewEntity(this.mc.getCameraEntity());
        int mapSize = minimapSession.getProcessor().getMinimapSize();
        int bufferSize = minimapSession.getProcessor().getMinimapBufferSize(mapSize);
        if (this.minimap.usingFBO()) {
            bufferSize = minimap.getFBOBufferSize();
        }
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        float minimapScale = GuiUtils.getMinimapScale(configManager);
        float mapScale = (float) (scale / minimapScale);
        minimap.updateZoom();
        this.zoom = minimap.getMinimapZoom();
        Lighting.setupForFlatItems();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.pixelStore(3317, 4);
        RenderSystem.pixelStore(3316, 0);
        RenderSystem.pixelStore(3315, 0);
        RenderSystem.pixelStore(3314, 0);
        float sizeFix = bufferSize / 512.0f;
        int shape = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.SHAPE)).intValue();
        boolean lockedNorth = MinimapConfigClientUtils.getEffectiveNorthLocked(mapSize / 2, shape);
        double angle = Math.toRadians(getRenderAngle(lockedNorth));
        double ps = Math.sin(3.141592653589793d - angle);
        double pc = Math.cos(3.141592653589793d - angle);
        boolean useWorldMap = this.modMain.getSupportMods().shouldUseWorldMapChunks() && !minimap.getMinimapWriter().isLoadedNonWorldMap();
        boolean lightingConfig = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.LIGHTING)).booleanValue();
        int lightLevel = (int) ((1.0f - Math.min(1.0f, getSunBrightness(minimap, lightingConfig))) * (minimap.getMinimapWriter().getLoadedLevels() - 1));
        boolean cave = minimap.isCaveModeDisplayed();
        boolean circleShape = shape == 1;
        double playerX = EntityUtils.getEntityX(this.mc.getCameraEntity(), partial);
        double playerY = EntityUtils.getEntityY(this.mc.getCameraEntity(), partial);
        double playerZ = EntityUtils.getEntityZ(this.mc.getCameraEntity(), partial);
        double renderX = playerX;
        double renderZ = playerZ;
        double mapDimensionScale = this.mc.level.dimensionType().coordinateScale();
        ResourceKey<Level> mapDimension = this.mc.level.dimension();
        double playerDimDiv = 1.0d;
        if (useWorldMap) {
            mapDimensionScale = this.modMain.getSupportMods().worldmapSupport.getMapDimensionScale();
            mapDimension = this.modMain.getSupportMods().worldmapSupport.getMapDimension();
            if (mapDimensionScale == 0.0d) {
                mapDimensionScale = minimap.getLastMapDimensionScale();
                mapDimension = minimap.getLastMapDimension();
            }
            playerDimDiv = mapDimensionScale / mapDimensionScale;
            renderX /= playerDimDiv;
            renderZ /= playerDimDiv;
        }
        minimap.setLastMapDimensionScale(mapDimensionScale);
        minimap.setLastMapDimension(mapDimension);
        minimap.setLastPlayerDimDiv(playerDimDiv);
        Vec3 renderPos = new Vec3(renderX, playerY, renderZ);
        matrixStack.pushPose();
        renderChunks(minimapSession, guiGraphics, minimap, renderPos, mapDimension, mapDimensionScale, mapSize, bufferSize, sizeFix, partial, lightLevel, useWorldMap, lockedNorth, shape, ps, pc, cave, circleShape, settings, cvc);
        if (this.minimap.usingFBO()) {
            sizeFix = 1.0f;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        matrixStack.scale(1.0f / mapScale, 1.0f / mapScale, 1.0f);
        int scaledX = (int) (x * mapScale);
        int scaledY = (int) (y * mapScale);
        int minimapFrameSize = (int) ((mapSize / 2) / sizeFix);
        int circleSides = Math.max(32, ((int) Math.ceil(((3.141592653589793d * (minimapFrameSize + 8)) / 8.0d) / 4.0d)) * 4);
        double circleStartAngle = 0.0d;
        if (circleShape) {
            float outerRadius = (mapSize / 4) + 4;
            circleStartAngle = (-0.7853981633974483d) - ((32 / 2) / outerRadius);
            getHelper().drawTexturedElipseInsideRectangle(matrixStack, circleStartAngle, circleSides, (int) ((scaledX + 9) / sizeFix), (int) ((scaledY + 9) / sizeFix), 0, 256 - minimapFrameSize, minimapFrameSize, 256.0f);
        } else {
            getHelper().drawMyTexturedModalRect(matrixStack, (int) ((scaledX + 9) / sizeFix), (int) ((scaledY + 9) / sizeFix), 0, 256 - minimapFrameSize, minimapFrameSize, minimapFrameSize, minimapFrameSize, 256.0f);
        }
        RenderSystem.defaultBlendFunc();
        if (!this.minimap.usingFBO()) {
            matrixStack.scale(1.0f / sizeFix, 1.0f / sizeFix, 1.0f);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        int frameType = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.FRAME)).intValue();
        boolean renderFrame = frameType < MinimapConfigConstants.FRAME_NAMES.length - 1;
        if (frameType > 0) {
            int frameColorConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.FRAME_COLOR)).intValue();
            int frameColor = MinimapConfigConstants.COLORS[frameColorConfig];
            RenderSystem.setShaderColor(((frameColor >> 16) & 255) / 255.0f, ((frameColor >> 8) & 255) / 255.0f, (frameColor & 255) / 255.0f, 1.0f);
        }
        MinimapRendererHelper helper = getHelper();
        if (renderFrame) {
            RenderSystem.setShaderTexture(0, TextureLocations.MINIMAP_FRAME_TEXTURES);
        }
        if (renderFrame && !circleShape) {
            int rightCornerStartX = (((scaledX + 9) + (mapSize / 2)) + 4) - 16;
            int bottomCornerStartY = (((scaledY + 9) + (mapSize / 2)) + 4) - 16;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder vertexBuffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix = matrixStack.last().pose();
            int cornerTextureX = frameType == 0 ? 192 : frameType == 1 ? 208 : 224;
            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (scaledX + 9) - 4, (scaledY + 9) - 4, cornerTextureX, 97, 16, 16);
            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, rightCornerStartX, (scaledY + 9) - 4, cornerTextureX, 113, 16, 16);
            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (scaledX + 9) - 4, bottomCornerStartY, cornerTextureX, 129, 16, 16);
            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, rightCornerStartX, bottomCornerStartY, cornerTextureX, 145, 16, 16);
            int horLineStartX = ((scaledX + 9) - 4) + 16;
            int horLineWidth = rightCornerStartX - horLineStartX;
            int horPieceTextureY = frameType == 0 ? 0 : frameType == 1 ? 32 : 64;
            int horLineLength = (int) Math.ceil(horLineWidth / 226);
            for (int i = 0; i < horLineLength; i++) {
                int pieceX = ((scaledX + 9) - 4) + 16 + (i * 226);
                int pieceW = 226;
                if (i == horLineLength - 1 && pieceX + 226 > rightCornerStartX) {
                    pieceW = rightCornerStartX - pieceX;
                }
                helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, pieceX, (scaledY + 9) - 4, 0, horPieceTextureY, pieceW, 16);
                helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, pieceX, ((scaledY + 9) + (mapSize / 2)) - 12, 0, horPieceTextureY + 16, pieceW, 16);
            }
            int verLineStartY = ((scaledY + 9) - 4) + 16;
            int verLineHeight = bottomCornerStartY - verLineStartY;
            int verPieceTextureX = frameType == 0 ? 0 : frameType == 1 ? 64 : 128;
            int vertLineLength = (int) Math.ceil(verLineHeight / 113);
            for (int i2 = 0; i2 < vertLineLength; i2++) {
                int pieceY = ((scaledY + 9) - 4) + 16 + (i2 * 113);
                int pieceU = verPieceTextureX + (32 * (i2 & 1));
                int pieceH = 113;
                if (i2 == vertLineLength - 1 && pieceY + 113 > bottomCornerStartY) {
                    pieceH = bottomCornerStartY - pieceY;
                }
                helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (scaledX + 9) - 4, pieceY, pieceU, 97, 16, pieceH);
                helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, ((scaledX + 9) + (mapSize / 2)) - 12, pieceY, pieceU + 16, 97, 16, pieceH);
            }
            BufferUploader.drawWithShader(vertexBuffer.build());
        } else if (renderFrame) {
            int frameTextureY = frameType == 0 ? 210 : frameType == 1 ? 214 : 218;
            double shadeStartAngle = 0.7853981633974483d - circleStartAngle;
            int shadeStartIndex = (int) (((shadeStartAngle / 2.0d) / 3.141592653589793d) * circleSides);
            int circleLeftX = scaledX + 9;
            int circleTopY = scaledY + 9;
            int innerCircleDiameter = mapSize / 2;
            helper.drawTexturedElipseInsideRectangleFrame(matrixStack, false, false, circleStartAngle, 0, shadeStartIndex, circleSides, 4, circleLeftX, circleTopY, 0, frameTextureY, innerCircleDiameter, 73.0f, 4, 32, 256.0f);
            helper.drawTexturedElipseInsideRectangleFrame(matrixStack, true, false, circleStartAngle, shadeStartIndex, shadeStartIndex + (circleSides / 4), circleSides, 4, circleLeftX, circleTopY, 138, frameTextureY, innerCircleDiameter, 68.0f, 4, 20, 256.0f);
            helper.drawTexturedElipseInsideRectangleFrame(matrixStack, true, true, circleStartAngle, shadeStartIndex + (circleSides / 4), shadeStartIndex + (circleSides / 2), circleSides, 4, circleLeftX, circleTopY, 138, frameTextureY, innerCircleDiameter, 68.0f, 4, 20, 256.0f);
            helper.drawTexturedElipseInsideRectangleFrame(matrixStack, false, false, circleStartAngle, shadeStartIndex + (circleSides / 2), circleSides, circleSides, 4, circleLeftX, circleTopY, 0, frameTextureY, innerCircleDiameter, 73.0f, 4, 32, 256.0f);
        }
        RenderSystem.setShaderTexture(0, TextureLocations.GUI_TEXTURES);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pushPose();
        matrixStack.translate(scaledX + 9, scaledY + 9, 0.0f);
        matrixStack.scale(1.0f / minimapScale, 1.0f / minimapScale, 1.0f);
        int halfFrame = (int) (((mapSize * minimapScale) / 2.0f) / 2.0f);
        matrixStack.translate(halfFrame, halfFrame, 0.5d);
        int specW = halfFrame + ((int) (3.0f * minimapScale));
        boolean safeMode = this instanceof MinimapSafeModeRenderer;
        MultiBufferSource.BufferSource renderTypeBuffers = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
        minimapSession.getMultiTextureRenderTypeRenderers();
        double scaledZoom = (this.zoom * minimapScale) / 2.0d;
        boolean compassOverEverythingConfig = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.COMPASS_OVER_EVERYTHING)).booleanValue();
        if (!compassOverEverythingConfig) {
            renderCompass(matrixStack, settings, configManager, renderTypeBuffers, specW, specW, halfFrame, ps, pc, circleShape, minimapScale);
        }
        this.minimap.getOverMapRendererHandler().prepareRender(ps, pc, scaledZoom, specW, specW, halfFrame, halfFrame, circleShape, minimapScale);
        this.minimap.getOverMapRendererHandler().render(guiGraphics, renderPos, partial, null, mapDimensionScale, mapDimension);
        if (compassOverEverythingConfig) {
            renderCompass(matrixStack, settings, configManager, renderTypeBuffers, specW, specW, halfFrame, ps, pc, circleShape, minimapScale);
        }
        renderTypeBuffers.endBatch();
        matrixStack.popPose();
        int depthClearerX = scaledX - 25;
        int depthClearerY = scaledY - 25;
        int depthClearerW = 18 + (mapSize / 2) + 50;
        guiGraphics.fill(CustomRenderTypes.DEPTH_CLEAR, depthClearerX, depthClearerY, depthClearerX + depthClearerW, depthClearerY + depthClearerW, -16777216);
        RenderSystem.enableBlend();
        int mainEntityAs = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.RADAR_MAIN_ENTITY)).intValue();
        boolean crosshairDisplayed = mainEntityAs == 0 && !lockedNorth;
        if (crosshairDisplayed) {
            matrixStack.pushPose();
            matrixStack.translate(scaledX + 9, scaledY + 9, 0.0f);
            matrixStack.scale(0.5f, 0.5f, 1.0f);
            matrixStack.translate(mapSize / 2, mapSize / 2, 0.0f);
            RenderSystem.blendFuncSeparate(775, 0, 1, 0);
            getHelper().drawMyColoredRect(matrixStack, -5.0f, -1.0f, 5.0f, 1.0f);
            getHelper().drawMyColoredRect(matrixStack, -1.0f, 3.0f, 1.0f, 5.0f);
            getHelper().drawMyColoredRect(matrixStack, -1.0f, -5.0f, 1.0f, -3.0f);
            RenderSystem.blendFunc(770, 771);
            RadarSession radarSession = minimap.getRadarSession();
            EntityRadarCategoryManager categoryManager = radarSession.getCategoryManager();
            EntityRadarCategory mainEntityCategory = (EntityRadarCategory) categoryManager.getRuleResolver().resolve(categoryManager.getRootCategory(), this.mc.getCameraEntity(), this.mc.player);
            if (mainEntityCategory == null) {
                mainEntityCategory = categoryManager.getRootCategory();
            }
            RadarColor crosshairRadarColor = RadarColor.fromIndex(((Double) mainEntityCategory.getSettingValue(EntityRadarCategorySettings.COLOR)).intValue());
            RadarColor crosshairFallbackColor = radarSession.getColorHelper().getFallbackColor(mainEntityCategory, null);
            int crosshairColor = radarSession.getColorHelper().getEntityColor(this.mc.getCameraEntity(), 0.0f, false, 100, 100, false, crosshairRadarColor, crosshairFallbackColor);
            RenderSystem.setShaderColor(((crosshairColor >> 16) & 255) / 255.0f, ((crosshairColor >> 8) & 255) / 255.0f, (crosshairColor & 255) / 255.0f, 1.0f);
            getHelper().drawMyColoredRect(matrixStack, 1.0f, -1.0f, 3.0f, 1.0f);
            getHelper().drawMyColoredRect(matrixStack, -3.0f, -1.0f, -1.0f, 1.0f);
            getHelper().drawMyColoredRect(matrixStack, -1.0f, 1.0f, 1.0f, 3.0f);
            getHelper().drawMyColoredRect(matrixStack, -1.0f, -3.0f, 1.0f, -1.0f);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            matrixStack.popPose();
        }
        double centerX = (2 * scaledX) + 18 + (mapSize / 2);
        double centerY = (2 * scaledY) + 18 + (mapSize / 2);
        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 1.0f);
        matrixStack.translate(centerX, centerY, 0.0d);
        this.mc.getTextureManager().bindForSetup(TextureLocations.GUI_TEXTURES);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        Entity mainEntity = this.mc.getCameraEntity();
        if (!safeMode && !crosshairDisplayed) {
            this.minimap.getMinimapFBORenderer().renderMainEntityDot(guiGraphics, mainEntity, cave, cvc.getBetterPVPRenderTypeBuffers());
        }
        RenderSystem.setShaderTexture(0, TextureLocations.GUI_TEXTURES);
        RenderSystem.enableBlend();
        if (lockedNorth || mainEntityAs == 2) {
            float arrowAngle = lockedNorth ? mainEntity.getViewYRot(partial) : 180.0f;
            int arrowOpacityInt = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.ARROW_OPACITY)).intValue();
            float arrowOpacity = arrowOpacityInt / 100.0f;
            if (arrowOpacity == 1.0f) {
                drawArrow(matrixStack, arrowAngle, 0.0d, 1.0d, 0.0f, 0.0f, 0.0f, 0.5f, configManager);
            }
            int arrowColour = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.ARROW_COLOR)).intValue();
            if (arrowColour != -1) {
                float[] c = MinimapConfigConstants.ARROW_COLORS[arrowColour];
                r = c[0];
                g = c[1];
                b = c[2];
                a = c[3];
            } else {
                int rgb = minimap.getRadarSession().getColorHelper().getTeamColor(this.mc.player == null ? mainEntity : this.mc.player);
                if (rgb != -1) {
                    r = ((rgb >> 16) & 255) / 255.0f;
                    g = ((rgb >> 8) & 255) / 255.0f;
                    b = (rgb & 255) / 255.0f;
                    a = 1.0f;
                } else {
                    float[] c2 = MinimapConfigConstants.ARROW_COLORS[0];
                    r = c2[0];
                    g = c2[1];
                    b = c2[2];
                    a = c2[3];
                }
            }
            drawArrow(matrixStack, arrowAngle, 0.0d, 0.0d, r, g, b, a * arrowOpacity, configManager);
        }
        matrixStack.popPose();
        this.mc.getTextureManager().bindForSetup(TextureLocations.GUI_TEXTURES);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9728);
        int playerBlockX = OptimizedMath.myFloor(mainEntity.getX());
        int playerBlockY = OptimizedMath.myFloor(mainEntity.getY());
        int playerBlockZ = OptimizedMath.myFloor(mainEntity.getZ());
        BlockPos pos = this.mutableBlockPos.set(playerBlockX, playerBlockY, playerBlockZ);
        this.minimap.getInfoDisplays().getRenderer().render(guiGraphics, minimapSession, this.minimap, height, size, pos, scaledX, scaledY, mapScale, renderTypeBuffers);
        matrixStack.popPose();
        Lighting.setupFor3DItems();
    }

    private void renderCompass(PoseStack matrixStack, ModSettings settings, ClientConfigManager configManager, MultiBufferSource.BufferSource renderTypeBuffers, int specW, int specH, int halfFrame, double ps, double pc, boolean circleShape, float minimapScale) {
        int compassScale;
        VertexConsumer nameBgBuilder = renderTypeBuffers.getBuffer(CustomRenderTypes.RADAR_NAME_BGS);
        int compassScale2 = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.COMPASS_SCALE)).intValue();
        int compassLocation = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.COMPASS_LOCATION)).intValue();
        if (compassScale2 <= 0) {
            compassScale = compassLocation == 1 ? (int) Math.ceil(minimapScale / 2.0f) : (int) minimapScale;
        } else {
            compassScale = (int) MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.COMPASS_SCALE);
        }
        if (compassLocation == 1) {
            if (Minecraft.getInstance().isEnforceUnicode()) {
                compassScale *= 2;
            }
            int halfFrame2 = (int) (halfFrame - ((7.0f * minimapScale) / 2.0f));
            this.compassRenderer.drawCompass(matrixStack, halfFrame2 - (3 * compassScale), halfFrame2 - (3 * compassScale), ps, pc, 1.0d, circleShape, compassScale, true, renderTypeBuffers, nameBgBuilder);
            return;
        }
        if (compassLocation == 2) {
            this.compassRenderer.drawCompass(matrixStack, specW, specH, ps, pc, this.zoom, circleShape, compassScale, false, renderTypeBuffers, null);
        }
    }

    private void drawArrow(PoseStack matrixStack, float angle, double arrowX, double arrowY, float r, float g, float b, float a, ClientConfigManager configManager) {
        matrixStack.pushPose();
        matrixStack.translate(arrowX, arrowY, 0.0d);
        OptimizedMath.rotatePose(matrixStack, angle, OptimizedMath.ZP);
        double arrowScale = ((Double) configManager.getEffective(MinimapProfiledConfigOptions.ARROW_SCALE)).doubleValue();
        matrixStack.scale((float) (0.5d * arrowScale), (float) (0.5d * arrowScale), 1.0f);
        matrixStack.translate(-13.0f, -6, 0.0f);
        RenderSystem.setShaderColor(r, g, b, a);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GuiGraphicsUtils.blit(matrixStack, 0, 0, 49.0f, 0, 26, 28);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
    }

    public double getZoom() {
        return this.zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public float getSunBrightness(MinimapProcessor minimap, boolean lighting) {
        ClientLevel world = this.mc.level;
        float sunBrightness = (world.getSkyDarken(1.0f) - 0.2f) / 0.8f;
        float ambient = (world.dimensionType().ambientLight() * 24.0f) / 15.0f;
        if (ambient > 1.0f) {
            ambient = 1.0f;
        }
        return ambient + ((1.0f - ambient) * Mth.clamp(sunBrightness, 0.0f, 1.0f));
    }

    public MinimapRendererHelper getHelper() {
        return this.helper;
    }

    @Deprecated
    public double getLastPlayerDimDiv() {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (session == null) {
            return 1.0d;
        }
        return session.getProcessor().getLastPlayerDimDiv();
    }
}
