package xaero.common.minimap.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xaero.common.HudMod;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.graphics.MinimapTexture;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.region.MinimapChunk;
import xaero.common.minimap.region.MinimapTile;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.entity.EntityUtils;
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
import xaero.hud.minimap.radar.state.RadarList;
import xaero.hud.minimap.radar.util.RadarUtils;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderer;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/render/MinimapSafeModeRenderer.class */
public class MinimapSafeModeRenderer extends MinimapRenderer {
    private static final ResourceLocation mapTextures = ResourceLocation.parse("xaeromaptexture");
    private byte[] bytes;
    private byte drawYState;
    private final int[] tempColor;
    private MinimapTexture mapTexture;

    public MinimapSafeModeRenderer(HudMod modMain, Minecraft mc, WaypointMapRenderer waypointMapRenderer, Minimap minimap, CompassRenderer compassRenderer) throws IOException {
        super(modMain, mc, waypointMapRenderer, minimap, compassRenderer);
        this.tempColor = new int[3];
        this.mapTexture = new MinimapTexture(mapTextures);
    }

    public void updateMapFrameSafeMode(MinimapSession session, MinimapProcessor minimap, Player player, Entity renderEntity, int bufferSize, int mapW, float partial, int level, boolean lockedNorth, int shape, double ps, double pc, boolean cave, ModSettings settings) {
        if (level < 0) {
            return;
        }
        System.currentTimeMillis();
        if (minimap.isToResetImage()) {
            this.bytes = new byte[bufferSize * bufferSize * 3];
            minimap.setToResetImage(false);
        }
        int debugFPS = this.mc.getXaeroMinimap_fps();
        boolean motionBlur = debugFPS >= 35;
        int increaseY = motionBlur ? 2 : 1;
        int halfW = mapW / 2;
        int halfH = mapW / 2;
        double halfWZoomed = halfW / this.zoom;
        double halfHZoomed = halfH / this.zoom;
        byte currentState = this.drawYState;
        RadarSession radarSession = minimap.getRadarSession();
        double playerX = EntityUtils.getEntityX(renderEntity, partial);
        double playerZ = EntityUtils.getEntityZ(renderEntity, partial);
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean terrainMapVisible = !cave || MinimapConfigClientUtils.getEffectiveCaveModeAllowed();
        int chunkGridConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.CHUNK_GRID)).intValue();
        boolean slimeChunks = MinimapConfigClientUtils.getEffectiveSlimeChunks(session);
        for (int currentX = 0; currentX < mapW; currentX++) {
            double currentXZoomed = (currentX + 0.5d) / this.zoom;
            double offx = currentXZoomed - halfWZoomed;
            double psx = ps * offx;
            double pcx = pc * offx;
            int i = motionBlur ? currentState : 0;
            while (true) {
                int currentY = i;
                if (currentY >= mapW) {
                    break;
                }
                double offy = ((currentY + 0.5d) / this.zoom) - halfHZoomed;
                if (terrainMapVisible) {
                    getLoadedBlockColor(session, minimap, this.tempColor, OptimizedMath.myFloor(playerX + psx + (pc * offy)), OptimizedMath.myFloor((playerZ + (ps * offy)) - pcx), level, chunkGridConfig, slimeChunks);
                } else {
                    int[] iArr = this.tempColor;
                    int[] iArr2 = this.tempColor;
                    this.tempColor[2] = 1;
                    iArr2[1] = 1;
                    iArr[0] = 1;
                }
                getHelper().putColor(this.bytes, currentX, (bufferSize - 1) - currentY, this.tempColor[0], this.tempColor[1], this.tempColor[2], bufferSize);
                i = currentY + increaseY;
            }
            currentState = (byte) (currentState == 1 ? 0 : 1);
        }
        boolean displayRadar = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DISPLAY_RADAR)).booleanValue();
        double maxDistance = RadarUtils.getMaxDistance(minimap, shape == 1);
        if (displayRadar) {
            Iterable<RadarList> entityLists = radarSession.getState().getRadarLists();
            for (RadarList entityList : entityLists) {
                int heightLimit = ((Double) entityList.getEffective(EntityRadarCategorySettings.HEIGHT_LIMIT)).intValue();
                boolean heightBasedFade = ((Boolean) entityList.getEffective(EntityRadarCategorySettings.HEIGHT_FADE)).booleanValue();
                int colorIndex = ((Double) entityList.getEffective(EntityRadarCategorySettings.COLOR)).intValue();
                int startFadingAt = ((Double) entityList.getEffective(EntityRadarCategorySettings.START_FADING_AT)).intValue();
                RadarColor radarColor = RadarColor.fromIndex(colorIndex);
                RadarColor fallbackColor = radarSession.getColorHelper().getFallbackColor(entityList);
                renderEntityListSafeMode(minimap, renderEntity, entityList.getEntities().iterator(), pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial, heightLimit, heightBasedFade, startFadingAt, radarColor, fallbackColor, maxDistance);
            }
        }
        int mainEntityAs = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.RADAR_MAIN_ENTITY)).intValue();
        if (mainEntityAs == 1) {
            EntityRadarCategoryManager categoryManager = radarSession.getCategoryManager();
            EntityRadarCategory mainEntityCategory = (EntityRadarCategory) categoryManager.getRuleResolver().resolve(categoryManager.getRootCategory(), renderEntity, player);
            if (mainEntityCategory == null) {
                mainEntityCategory = categoryManager.getRootCategory();
            }
            int colorIndex2 = ((Double) mainEntityCategory.getSettingValue(EntityRadarCategorySettings.COLOR)).intValue();
            RadarColor radarColor2 = RadarColor.fromIndex(colorIndex2);
            RadarColor fallbackColor2 = radarSession.getColorHelper().getFallbackColor(mainEntityCategory, null);
            renderEntityDotSafeMode(minimap, renderEntity, renderEntity, pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial, 100, false, 100, radarColor2, fallbackColor2, maxDistance);
        }
        this.drawYState = (byte) (this.drawYState == 1 ? 0 : 1);
        ByteBuffer buffer = this.mapTexture.buffer;
        buffer.clear();
        buffer.put(this.bytes);
        buffer.flip();
    }

    public void renderEntityListSafeMode(MinimapProcessor minimap, Entity renderEntity, Iterator<Entity> iter, double pc, double ps, int mapW, int bufferSize, int halfW, int halfH, double playerX, double playerZ, float partial, int heightLimit, boolean heightBasedFade, int startFadingAt, RadarColor radarColor, RadarColor fallbackColor, double maxDistance) {
        while (iter.hasNext()) {
            Entity e = iter.next();
            if (renderEntity != e && !renderEntityDotSafeMode(minimap, renderEntity, e, pc, ps, mapW, bufferSize, halfW, halfH, playerX, playerZ, partial, heightLimit, heightBasedFade, startFadingAt, radarColor, fallbackColor, maxDistance)) {
            }
        }
    }

    public boolean renderEntityDotSafeMode(MinimapProcessor minimap, Entity renderEntity, Entity e, double pc, double ps, int mapW, int bufferSize, int halfW, int halfH, double playerX, double playerZ, float partial, int heightLimit, boolean heightBasedFade, int startFadingAt, RadarColor radarColor, RadarColor fallbackColor, double maxDistance) {
        double offx = EntityUtils.getEntityX(e, partial) - playerX;
        double offx2 = offx * offx;
        if (offx2 > maxDistance) {
            return false;
        }
        double offz = EntityUtils.getEntityZ(e, partial) - playerZ;
        double offz2 = offz * offz;
        if (offz2 > maxDistance) {
            return false;
        }
        if ((e instanceof Player) && this.modMain.getTrackedPlayerRenderer().getCollector().playerExists(e.getUUID())) {
            this.modMain.getTrackedPlayerRenderer().getCollector().confirmPlayerRadarRender((Player) e);
        }
        float offh = (float) (renderEntity.getY() - e.getY());
        double Z = (pc * offx) + (ps * offz);
        double X = (ps * offx) - (pc * offz);
        double drawXDouble = halfW + (X * this.zoom);
        double drawYDouble = halfH + (Z * this.zoom);
        float drawLeft = ((float) drawXDouble) - 2.5f;
        float drawTop = ((float) drawYDouble) - 2.5f;
        int drawX = (mapW - Math.round(mapW - drawLeft)) + 2;
        int drawY = Math.round(drawTop) + 2;
        int color = minimap.getRadarSession().getColorHelper().getEntityColor(e, offh, false, heightLimit, startFadingAt, heightBasedFade, radarColor, fallbackColor);
        for (int a = drawX - 2; a < drawX + 4; a++) {
            if (a >= 0 && a < mapW) {
                for (int b = drawY - 2; b < drawY + 4; b++) {
                    if (b >= 0 && b < mapW && (((a != drawX - 2 && a != drawX + 3) || (b != drawY - 2 && b != drawY + 3)) && ((a != drawX + 2 || b != drawY - 2) && ((a != drawX + 3 || b != drawY - 1) && ((a != drawX - 2 || b != drawY + 2) && (a != drawX - 1 || b != drawY + 3)))))) {
                        if (a == drawX + 3 || b == drawY + 3 || (a == drawX + 2 && b == drawY + 2)) {
                            getHelper().putColor(this.bytes, a, (bufferSize - 1) - b, 0, 0, 0, bufferSize);
                        } else {
                            getHelper().putColor(this.bytes, a, (bufferSize - 1) - b, (color >> 16) & 255, (color >> 8) & 255, color & 255, bufferSize);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override // xaero.common.minimap.render.MinimapRenderer
    protected void renderChunks(MinimapSession session, GuiGraphics guiGraphics, MinimapProcessor minimap, Vec3 renderPos, ResourceKey<Level> mapDimension, double mapDimensionScale, int mapSize, int bufferSize, float sizeFix, float partial, int lightLevel, boolean useWorldMap, boolean lockedNorth, int shape, double ps, double pc, boolean cave, boolean circle, ModSettings settings, CustomVertexConsumers cvc) {
        PoseStack matrixStack = guiGraphics.pose();
        updateMapFrameSafeMode(session, minimap, this.mc.player, this.mc.getCameraEntity(), bufferSize, mapSize, partial, lightLevel, lockedNorth, shape, ps, pc, cave, settings);
        matrixStack.scale(sizeFix, sizeFix, 1.0f);
        try {
            this.mapTexture.loadIfNeeded();
            getHelper().bindTextureBuffer(this.mapTexture.buffer, bufferSize, bufferSize, this.mapTexture.getId());
            ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
            int opacityConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.OPACITY)).intValue();
            float opacity = opacityConfig / 100.0f;
            RenderSystem.setShaderColor(opacity, opacity, opacity, opacity);
        } catch (Throwable e) {
            this.minimap.setCrashedWith(e);
        }
    }

    private void getLoadedBlockColor(MinimapSession session, MinimapProcessor minimap, int[] result, int par1, int par2, int level, int chunkGridConfig, boolean slimeChunks) {
        MinimapTile tile;
        int tileX = par1 >> 4;
        int tileZ = par2 >> 4;
        int chunkX = (tileX >> 2) - minimap.getMinimapWriter().getLoadedMapChunkX();
        int chunkZ = (tileZ >> 2) - minimap.getMinimapWriter().getLoadedMapChunkZ();
        if (minimap.getMinimapWriter().getLoadedBlocks() == null || chunkX < 0 || chunkX >= minimap.getMinimapWriter().getLoadedBlocks().length || chunkZ < 0 || chunkZ >= minimap.getMinimapWriter().getLoadedBlocks().length) {
            result[2] = 1;
            result[1] = 1;
            result[0] = 1;
            return;
        }
        try {
            MinimapChunk current = minimap.getMinimapWriter().getLoadedBlocks()[chunkX][chunkZ];
            if (current != null && (tile = current.getTile(tileX & 3, tileZ & 3)) != null) {
                int insideX = par1 & 15;
                int insideZ = par2 & 15;
                chunkOverlay(session, result, tile.getRed(level, insideX, insideZ), tile.getGreen(level, insideX, insideZ), tile.getBlue(level, insideX, insideZ), chunkGridConfig, slimeChunks, tile);
                return;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        result[2] = 1;
        result[1] = 1;
        result[0] = 1;
    }

    private void chunkOverlay(MinimapSession session, int[] result, int red, int green, int blue, int chunkGridConfig, boolean slimeChunks, MinimapTile c) {
        if (slimeChunks && c.isSlimeChunk()) {
            getHelper().slimeOverlay(result, red, green, blue);
            return;
        }
        if (chunkGridConfig > -1 && c.isChunkGrid()) {
            getHelper().gridOverlay(result, MinimapConfigConstants.COLORS[chunkGridConfig], red, green, blue);
            return;
        }
        result[0] = red;
        result[1] = green;
        result[2] = blue;
    }
}
