package xaero.common.minimap.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.graphics.ImprovedFramebuffer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.region.MinimapChunk;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.compat.mods.ImmediatelyFastHelper;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.MinimapConfigConstants;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.compass.render.CompassRenderer;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.map.MinimapElementMapRendererHandler;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.icon.RadarIconManager;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.hud.minimap.radar.render.element.RadarRenderer;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderer;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.graphics.XaeroRenderType;
import xaero.lib.client.graphics.shader.LibShaders;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/render/MinimapFBORenderer.class */
public class MinimapFBORenderer extends MinimapRenderer {
    private ImprovedFramebuffer scalingFramebuffer;
    private ImprovedFramebuffer rotationFramebuffer;
    private MinimapElementMapRendererHandler minimapElementMapRendererHandler;
    private RadarRenderer entityRadarRenderer;
    private RadarIconManager radarIconManager;
    private boolean triedFBO;
    private boolean loadedFBO;

    public MinimapFBORenderer(HudMod modMain, Minecraft mc, WaypointMapRenderer waypointMapRenderer, Minimap minimap, CompassRenderer compassRenderer) {
        super(modMain, mc, waypointMapRenderer, minimap, compassRenderer);
    }

    public void loadFrameBuffer(MinimapProcessor minimapProcessor) {
        if (!minimapProcessor.canUseFrameBuffer()) {
            MinimapLogs.LOGGER.info("FBO mode not supported! Using minimap safe mode.");
        } else {
            this.scalingFramebuffer = new ImprovedFramebuffer(512, 512, false);
            this.rotationFramebuffer = new ImprovedFramebuffer(512, 512, true);
            this.rotationFramebuffer.setFilterMode(9729);
            this.radarIconManager = new RadarIconManager(new RadarIconCreator());
            this.loadedFBO = (this.scalingFramebuffer.frameBufferId == -1 || this.rotationFramebuffer.frameBufferId == -1) ? false : true;
            this.minimapElementMapRendererHandler = MinimapElementMapRendererHandler.Builder.begin().build();
            this.entityRadarRenderer = RadarRenderer.Builder.begin().setRadarIconManager(this.radarIconManager).setMinimap(this.minimap).build();
            this.minimapElementMapRendererHandler.add((MinimapElementRenderer<?, ?>) this.entityRadarRenderer);
            this.minimap.getOverMapRendererHandler().add((MinimapElementRenderer<?, ?>) this.entityRadarRenderer);
            if (this.modMain.getSupportMods().worldmap()) {
                this.modMain.getSupportMods().worldmapSupport.createRadarRenderWrapper(this.entityRadarRenderer);
            }
        }
        this.triedFBO = true;
    }

    @Override // xaero.common.minimap.render.MinimapRenderer
    protected void renderChunks(MinimapSession minimapSession, GuiGraphics guiGraphics, MinimapProcessor minimap, Vec3 renderPos, ResourceKey<Level> mapDimension, double mapDimensionScale, int mapSize, int bufferSize, float sizeFix, float partial, int lightLevel, boolean useWorldMap, boolean lockedNorth, int shape, double ps, double pc, boolean cave, boolean circle, ModSettings settings, CustomVertexConsumers cvc) throws IllegalAccessException, IllegalArgumentException {
        synchronized (minimap.getMinimapWriter()) {
            renderChunksToFBO(minimapSession, guiGraphics, minimap, renderPos, mapDimension, mapDimensionScale, mapSize, partial, lightLevel, useWorldMap, lockedNorth, shape, ps, pc, cave, cvc);
        }
        this.scalingFramebuffer.bindDefaultFramebuffer(Minecraft.getInstance());
        GlStateManager._viewport(0, 0, Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
        this.rotationFramebuffer.bindRead();
    }

    public void renderChunksToFBO(MinimapSession minimapSession, GuiGraphics guiGraphics, MinimapProcessor minimap, Vec3 renderPos, ResourceKey<Level> mapDimension, double mapDimensionScale, int viewW, float partial, int level, boolean useWorldMap, boolean lockedNorth, int shape, double ps, double pc, boolean cave, CustomVertexConsumers cvc) throws IllegalAccessException, IllegalArgumentException {
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        Matrix4f projectionMatrixBackup = RenderSystem.getProjectionMatrix();
        VertexSorting vertexSortingBackup = RenderSystem.getVertexSorting();
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.setIdentity();
        MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = minimapSession.getMultiTextureRenderTypeRenderers();
        double maxVisibleLength = (lockedNorth || shape == 1) ? viewW : viewW * Math.sqrt(2.0d);
        double halfMaxVisibleLength = maxVisibleLength / 2.0d;
        double radiusBlocks = (maxVisibleLength / 2.0d) / this.zoom;
        int xFloored = OptimizedMath.myFloor(renderPos.x);
        int zFloored = OptimizedMath.myFloor(renderPos.z);
        int playerChunkX = xFloored >> 6;
        int playerChunkZ = zFloored >> 6;
        int offsetX = xFloored & 63;
        int offsetZ = zFloored & 63;
        boolean zooming = ((double) ((int) this.zoom)) != this.zoom;
        guiGraphics.flush();
        ImmediatelyFastHelper.triggerBatchingBuffersFlush(guiGraphics);
        this.scalingFramebuffer.bindAsMainTarget(true);
        GL11.glClear(16640);
        Lighting.setupForFlatItems();
        System.currentTimeMillis();
        GlStateManager._clear(256, Minecraft.ON_OSX);
        this.helper.defaultOrtho(this.scalingFramebuffer);
        Matrix4fStack shaderMatrixStack = RenderSystem.getModelViewStack();
        shaderMatrixStack.pushMatrix();
        shaderMatrixStack.identity();
        System.currentTimeMillis();
        double xInsidePixel = renderPos.x - xFloored;
        if (xInsidePixel < 0.0d) {
            xInsidePixel += 1.0d;
        }
        double zInsidePixel = renderPos.z - zFloored;
        if (zInsidePixel < 0.0d) {
            zInsidePixel += 1.0d;
        }
        float halfWView = viewW / 2.0f;
        float angle = (float) (90.0d - getRenderAngle(lockedNorth));
        RenderSystem.enableBlend();
        shaderMatrixStack.translate(256.0f, 256.0f, -2000.0f);
        shaderMatrixStack.scale((float) this.zoom, (float) this.zoom, 1.0f);
        RenderSystem.applyModelViewMatrix();
        guiGraphics.fill(-256, -256, 256, 256, -16777216);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        MultiBufferSource.BufferSource renderTypeBuffers = cvc.getBetterPVPRenderTypeBuffers();
        VertexConsumer overlayBufferBuilder = renderTypeBuffers.getBuffer(CustomRenderTypes.MAP_CHUNK_OVERLAY);
        float chunkGridAlphaMultiplier = 1.0f;
        int minX = playerChunkX + ((int) Math.floor((offsetX - radiusBlocks) / 64.0d));
        int minZ = playerChunkZ + ((int) Math.floor((offsetZ - radiusBlocks) / 64.0d));
        int maxX = playerChunkX + ((int) Math.floor(((offsetX + 1) + radiusBlocks) / 64.0d));
        int maxZ = playerChunkZ + ((int) Math.floor(((offsetZ + 1) + radiusBlocks) / 64.0d));
        if (!cave || MinimapConfigClientUtils.getEffectiveCaveModeAllowed()) {
            if (useWorldMap) {
                chunkGridAlphaMultiplier = this.modMain.getSupportMods().worldmapSupport.getMinimapBrightness();
                this.modMain.getSupportMods().worldmapSupport.drawMinimap(minimapSession, matrixStack, getHelper(), xFloored, zFloored, minX, minZ, maxX, maxZ, zooming, this.zoom, mapDimensionScale, overlayBufferBuilder, multiTextureRenderTypeRenderers);
            } else if (minimap.getMinimapWriter().getLoadedBlocks() != null && level >= 0) {
                int loadedLevels = minimap.getMinimapWriter().getLoadedLevels();
                chunkGridAlphaMultiplier = loadedLevels <= 1 ? 1.0f : 0.375f + (0.625f * (1.0f - (level / (loadedLevels - 1))));
                int loadedMapChunkX = minimap.getMinimapWriter().getLoadedMapChunkX();
                int loadedMapChunkZ = minimap.getMinimapWriter().getLoadedMapChunkZ();
                int loadedWidth = minimap.getMinimapWriter().getLoadedBlocks().length;
                boolean slimeChunks = MinimapConfigClientUtils.getEffectiveSlimeChunks(minimapSession);
                minX = Math.max(minX, loadedMapChunkX);
                minZ = Math.max(minZ, loadedMapChunkZ);
                maxX = Math.min(maxX, (loadedMapChunkX + loadedWidth) - 1);
                maxZ = Math.min(maxZ, (loadedMapChunkZ + loadedWidth) - 1);
                MultiTextureRenderTypeRenderer multiTextureRenderTypeRenderer = multiTextureRenderTypeRenderers.getRenderer(t -> {
                    RenderSystem.setShaderTexture(0, t);
                }, MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_BILINEAR);
                MinimapRendererHelper helper = getHelper();
                for (int X = minX; X <= maxX; X++) {
                    int canvasX = X - minimap.getMinimapWriter().getLoadedMapChunkX();
                    for (int Z = minZ; Z <= maxZ; Z++) {
                        int canvasZ = Z - minimap.getMinimapWriter().getLoadedMapChunkZ();
                        MinimapChunk mchunk = minimap.getMinimapWriter().getLoadedBlocks()[canvasX][canvasZ];
                        if (mchunk != null) {
                            int texture = mchunk.bindTexture(level);
                            if (mchunk.isHasSomething() && level < mchunk.getLevelsBuffered() && texture != 0) {
                                if (!zooming) {
                                    GL11.glTexParameteri(3553, 10240, 9728);
                                } else {
                                    GL11.glTexParameteri(3553, 10240, 9729);
                                }
                                int drawX = ((X - playerChunkX) * 64) - offsetX;
                                int drawZ = ((Z - playerChunkZ) * 64) - offsetZ;
                                helper.prepareMyTexturedColoredModalRect(matrixStack.last().pose(), drawX, drawZ, 0, 64, 64.0f, 64.0f, -64.0f, 64.0f, texture, 1.0f, 1.0f, 1.0f, 1.0f, multiTextureRenderTypeRenderer);
                                if (slimeChunks) {
                                    for (int t2 = 0; t2 < 16; t2++) {
                                        if (mchunk.getTile(t2 % 4, t2 / 4) != null && mchunk.getTile(t2 % 4, t2 / 4).isSlimeChunk()) {
                                            int slimeDrawX = drawX + (16 * (t2 % 4));
                                            int slimeDrawZ = drawZ + (16 * (t2 / 4));
                                            RenderBufferUtil.addColoredRect(matrixStack.last().pose(), overlayBufferBuilder, slimeDrawX, slimeDrawZ, 16, 16, -2142047936);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                multiTextureRenderTypeRenderers.draw(multiTextureRenderTypeRenderer);
            }
        }
        int chunkGridConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.CHUNK_GRID)).intValue();
        if (chunkGridConfig > -1) {
            VertexConsumer lineBufferBuilder = renderTypeBuffers.getBuffer(CustomRenderTypes.MAP_LINES);
            int grid = MinimapConfigConstants.COLORS[chunkGridConfig];
            int r = (grid >> 16) & 255;
            int g = (grid >> 8) & 255;
            int b = grid & 255;
            LibShaders.FRAMEBUFFER_LINES.setFrameSize(this.scalingFramebuffer.viewWidth, this.scalingFramebuffer.viewHeight);
            float red = r / 255.0f;
            float green = g / 255.0f;
            float blue = b / 255.0f;
            float colorMultiplier = chunkGridAlphaMultiplier;
            float red2 = red * colorMultiplier;
            float green2 = green * colorMultiplier;
            float blue2 = blue * colorMultiplier;
            int chunkGridLineWidthConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.CHUNK_GRID_LINE_WIDTH)).intValue();
            RenderSystem.lineWidth(chunkGridLineWidthConfig);
            PoseStack.Pose matrices = matrixStack.last();
            for (int X2 = minX; X2 <= maxX; X2++) {
                int drawX2 = (((X2 - playerChunkX) + 1) * 64) - offsetX;
                for (int i = 0; i < 4; i++) {
                    float lineX = drawX2 + ((-16) * i);
                    this.helper.addColoredLineToExistingBuffer(matrices, lineBufferBuilder, lineX, -((float) halfMaxVisibleLength), lineX, ((float) halfMaxVisibleLength) + 1, red2, green2, blue2, 0.8f);
                }
            }
            for (int Z2 = minZ; Z2 <= maxZ; Z2++) {
                int drawZ2 = (((Z2 - playerChunkZ) + 1) * 64) - offsetZ;
                for (int i2 = 0; i2 < 4; i2++) {
                    float lineZ = drawZ2 + ((float) (((-16) * i2) - (1.0d / this.zoom)));
                    this.helper.addColoredLineToExistingBuffer(matrices, lineBufferBuilder, -((float) halfMaxVisibleLength), lineZ, ((float) halfMaxVisibleLength) + 1, lineZ, red2, green2, blue2, 0.8f);
                }
            }
        }
        renderTypeBuffers.endBatch();
        this.scalingFramebuffer.unbindWrite();
        this.rotationFramebuffer.bindAsMainTarget(false);
        GL11.glClear(16640);
        this.scalingFramebuffer.bindRead();
        shaderMatrixStack.identity();
        boolean antiAliasing = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.ANTI_ALIASING)).booleanValue();
        if (antiAliasing) {
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        } else {
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10241, 9728);
        }
        shaderMatrixStack.translate(halfWView, halfWView, -2980.0f);
        shaderMatrixStack.pushMatrix();
        if (!lockedNorth) {
            OptimizedMath.rotateMatrix(shaderMatrixStack, -angle, OptimizedMath.ZP);
        }
        shaderMatrixStack.translate((float) ((-xInsidePixel) * this.zoom), (float) ((-zInsidePixel) * this.zoom), 0.0f);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int opacityConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.OPACITY)).intValue();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacityConfig / 100.0f);
        this.helper.drawMyTexturedModalRect(matrixStack, -256.0f, -256.0f, 0, 0, 512.0f, 512.0f, 512.0f, 512.0f);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        shaderMatrixStack.popMatrix();
        RenderSystem.applyModelViewMatrix();
        System.currentTimeMillis();
        XaeroRenderType.resetTransparency();
        XaeroRenderType.resetDepthTest();
        RenderSystem.enableDepthTest();
        XaeroRenderType.resetWriteMask();
        GL11.glBindTexture(3553, 0);
        GlStateManager._bindTexture(0);
        this.minimapElementMapRendererHandler.prepareRender(ps, pc, this.zoom, halfWView);
        this.minimapElementMapRendererHandler.render(guiGraphics, renderPos, partial, this.rotationFramebuffer, mapDimensionScale, mapDimension);
        guiGraphics.flush();
        renderTypeBuffers.endBatch();
        ImmediatelyFastHelper.triggerBatchingBuffersFlush(guiGraphics);
        this.rotationFramebuffer.unbindWrite();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setProjectionMatrix(projectionMatrixBackup, vertexSortingBackup);
        shaderMatrixStack.popMatrix();
        RenderSystem.applyModelViewMatrix();
        matrixStack.popPose();
    }

    public void deleteFramebuffers() {
        this.scalingFramebuffer.destroyBuffers();
        this.rotationFramebuffer.destroyBuffers();
        if (this.radarIconManager != null) {
            this.radarIconManager.reset();
        }
    }

    public boolean isLoadedFBO() {
        return this.loadedFBO;
    }

    public void setLoadedFBO(boolean loadedFBO) {
        this.loadedFBO = loadedFBO;
    }

    public boolean isTriedFBO() {
        return this.triedFBO;
    }

    public boolean assumeUsingFBO() {
        boolean mapSafeMode = ((Boolean) this.modMain.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.SAFE_MODE)).booleanValue();
        return !(isTriedFBO() || mapSafeMode) || this.minimap.usingFBO();
    }

    public void resetEntityIcons() {
        if (this.radarIconManager != null) {
            this.radarIconManager.reset();
        }
    }

    public void resetEntityIconsResources() {
        if (this.radarIconManager != null) {
            this.radarIconManager.resetResources();
        }
    }

    public void onRadarIconModelRenderTrace(EntityModel<?> model, VertexConsumer vertexConsumer, int color) {
        this.radarIconManager.onModelRenderTrace(model, vertexConsumer, color);
    }

    public void onEntityIconModelPartRenderTrace(ModelPart modelRenderer, int color) {
        this.radarIconManager.onModelPartRenderTrace(modelRenderer, color);
    }

    public void renderMainEntityDot(GuiGraphics guiGraphics, Entity renderEntity, boolean cave, MultiBufferSource.BufferSource renderTypeBuffers) {
        guiGraphics.pose().pushPose();
        this.entityRadarRenderer.renderSingleEntity(renderEntity, cave, false, 2.0f, false, false, MinimapElementRenderLocation.OVER_MINIMAP, null, guiGraphics);
        renderTypeBuffers.endBatch();
        guiGraphics.pose().popPose();
    }

    @Deprecated
    public xaero.common.minimap.render.radar.element.RadarRenderer getRadarRenderer() {
        return (xaero.common.minimap.render.radar.element.RadarRenderer) this.entityRadarRenderer;
    }

    public RadarRenderer getEntityRadarRenderer() {
        return this.entityRadarRenderer;
    }
}
