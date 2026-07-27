package xaero.common.mods;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.region.MinimapTile;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.write.MinimapWriter;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.render.element.RadarRenderer;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.graphics.shader.LibShaders;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.context.BuiltInEditConfigScreenContexts;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.single.SingleConfigManager;
import xaero.map.MapProcessor;
import xaero.map.WorldMap;
import xaero.map.WorldMapSession;
import xaero.map.common.config.option.WorldMapProfiledConfigOptions;
import xaero.map.config.primary.option.WorldMapPrimaryClientConfigOptions;
import xaero.map.config.util.WorldMapClientConfigUtils;
import xaero.map.graphics.CustomRenderTypes;
import xaero.map.gui.GuiMap;
import xaero.map.gui.GuiWorldMapSettings;
import xaero.map.region.MapRegion;
import xaero.map.region.MapTileChunk;
import xaero.map.region.texture.LeafRegionTexture;
import xaero.map.world.MapDimension;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/SupportXaeroWorldmap.class */
public class SupportXaeroWorldmap {
    public static final String MINIMAP_MW = "minimap";
    public int compatibilityVersion;
    public static final int black = -16777216;
    public static final int slime = -2142047936;
    private IXaeroMinimap modMain;
    private long lastDestinationCavingSwitch;
    public static int WORLDMAP_COMPATIBILITY_VERSION = 20;
    private static final HashMap<MapTileChunk, Long> seedsUsed = new HashMap<>();
    private int destinationCaving = MinimapWriter.NO_Y_VALUE;
    private int previousRenderedCaveLayer = MinimapWriter.NO_Y_VALUE;
    private int lastRenderedCaveLayer = MinimapWriter.NO_Y_VALUE;
    private ArrayList<MapRegion> regionBuffer = new ArrayList<>();

    public SupportXaeroWorldmap(IXaeroMinimap modMain) {
        this.modMain = modMain;
        try {
            this.compatibilityVersion = WorldMap.MINIMAP_COMPATIBILITY_VERSION;
        } catch (NoSuchFieldError e) {
        }
        if (this.compatibilityVersion < 3) {
            throw new RuntimeException("Xaero's World Map 1.11.0 or newer required!");
        }
    }

    public void drawMinimap(MinimapSession minimapSession, PoseStack matrixStack, MinimapRendererHelper helper, int xFloored, int zFloored, int minViewX, int minViewZ, int maxViewX, int maxViewZ, boolean zooming, double zoom, double mapDimensionScale, VertexConsumer overlayBufferBuilder, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        IntConsumer binder;
        IntConsumer shaderBinder;
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return;
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.renderThreadPauseSync) {
            if (!mapProcessor.isRenderingPaused()) {
                if (mapProcessor.getCurrentDimension() == null) {
                    return;
                }
                int i = this.compatibilityVersion;
                String worldString = mapProcessor.getCurrentWorldId();
                if (worldString == null) {
                    return;
                }
                LibShaders.ensureShaders();
                int mapX = xFloored >> 4;
                int mapZ = zFloored >> 4;
                int chunkX = mapX >> 2;
                int chunkZ = mapZ >> 2;
                int tileX = mapX & 3;
                int tileZ = mapZ & 3;
                int insideX = xFloored & 15;
                int insideZ = zFloored & 15;
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.enableBlend();
                int minX = (mapX >> 2) - 4;
                int maxX = (mapX >> 2) + 4;
                int minZ = (mapZ >> 2) - 4;
                int maxZ = (mapZ >> 2) + 4;
                boolean slimeChunks = MinimapConfigClientUtils.getEffectiveSlimeChunks(minimapSession);
                mapProcessor.initMinimapRender(xFloored, zFloored);
                int renderedCaveLayer = mapProcessor.getCurrentCaveLayer();
                float brightness = getMinimapBrightness();
                if (renderedCaveLayer != this.lastRenderedCaveLayer) {
                    this.previousRenderedCaveLayer = this.lastRenderedCaveLayer;
                }
                LocalPlayer localPlayer = Minecraft.getInstance().player;
                boolean noCaveMaps = !MinimapConfigClientUtils.getEffectiveCaveModeAllowed();
                Runnable finalizer = null;
                if (zooming) {
                    binder = t -> {
                        MultiTextureRenderTypeRendererProvider.defaultTextureBind(t);
                        GL11.glTexParameteri(3553, 10240, 9729);
                    };
                    shaderBinder = t2 -> {
                        RenderSystem.setShaderTexture(0, t2);
                        MultiTextureRenderTypeRendererProvider.defaultTextureBind(t2);
                        GL11.glTexParameteri(3553, 10240, 9729);
                    };
                    finalizer = () -> {
                        GL11.glTexParameteri(3553, 10240, 9728);
                    };
                } else {
                    binder = MultiTextureRenderTypeRendererProvider::defaultTextureBind;
                    shaderBinder = t3 -> {
                        RenderSystem.setShaderTexture(0, t3);
                    };
                }
                MultiTextureRenderTypeRenderer mapWithLightRenderer = multiTextureRenderTypeRenderers.getRenderer(shaderBinder, binder, finalizer, CustomRenderTypes.MAP);
                MultiTextureRenderTypeRenderer mapNoLightRenderer = multiTextureRenderTypeRenderers.getRenderer(shaderBinder, binder, finalizer, CustomRenderTypes.MAP);
                MinimapWorld world = minimapSession.getWorldManager().getAutoWorld();
                Long seed = (!slimeChunks || world == null) ? null : MinimapConfigClientUtils.getEffectiveSlimeChunksSeed(world);
                renderChunks(matrixStack, minX, maxX, minZ, maxZ, minViewX, maxViewX, minViewZ, maxViewZ, mapProcessor, noCaveMaps, slimeChunks, chunkX, chunkZ, tileX, tileZ, insideX, insideZ, seed, mapWithLightRenderer, mapNoLightRenderer, helper, overlayBufferBuilder);
                LibShaders.WORLD_MAP.setBrightness(brightness);
                LibShaders.WORLD_MAP.setWithLight(true);
                multiTextureRenderTypeRenderers.draw(mapWithLightRenderer);
                LibShaders.WORLD_MAP.setWithLight(false);
                multiTextureRenderTypeRenderers.draw(mapNoLightRenderer);
                GL14.glBlendFuncSeparate(770, 771, 1, 0);
                RenderSystem.disableBlend();
                this.lastRenderedCaveLayer = renderedCaveLayer;
                mapProcessor.finalizeMinimapRender();
            }
        }
    }

    private void renderChunks(PoseStack matrixStack, int minX, int maxX, int minZ, int maxZ, int minViewX, int maxViewX, int minViewZ, int maxViewZ, MapProcessor mapProcessor, boolean noCaveMaps, boolean slimeChunks, int chunkX, int chunkZ, int tileX, int tileZ, int insideX, int insideZ, Long seed, MultiTextureRenderTypeRenderer mapWithLightRenderer, MultiTextureRenderTypeRenderer mapNoLightRenderer, MinimapRendererHelper helper, VertexConsumer overlayBufferBuilder) {
        MapRegion previousLayerRegion;
        MapTileChunk previousLayerChunk;
        Matrix4f matrix = matrixStack.last().pose();
        for (int i = minX; i <= maxX; i++) {
            for (int j = minZ; j <= maxZ; j++) {
                MapRegion region = mapProcessor.getMinimapMapRegion(i >> 3, j >> 3);
                mapProcessor.beforeMinimapRegionRender(region);
                if (i >= minViewX && i <= maxViewX && j >= minViewZ && j <= maxViewZ) {
                    MapTileChunk chunk = region == null ? null : region.getChunk(i & 7, j & 7);
                    boolean chunkIsVisible = (chunk == null || chunk.getLeafTexture().getGlColorTexture() == -1) ? false : true;
                    if (!chunkIsVisible && ((!noCaveMaps || this.previousRenderedCaveLayer == Integer.MAX_VALUE) && (previousLayerRegion = mapProcessor.getLeafMapRegion(this.previousRenderedCaveLayer, i >> 3, j >> 3, false)) != null && (previousLayerChunk = previousLayerRegion.getChunk(i & 7, j & 7)) != null && previousLayerChunk.getLeafTexture().getGlColorTexture() != -1)) {
                        region = previousLayerRegion;
                        chunk = previousLayerChunk;
                        chunkIsVisible = true;
                    }
                    if (chunkIsVisible) {
                        bumpLoadedRegion(mapProcessor, region);
                        GL11.glTexParameterf(3553, 33082, 0.0f);
                        int drawX = ((64 * (chunk.getX() - chunkX)) - (16 * tileX)) - insideX;
                        int drawZ = ((64 * (chunk.getZ() - chunkZ)) - (16 * tileZ)) - insideZ;
                        prepareMapTexturedRect(matrix, drawX, drawZ, 0, 0, 64.0f, 64.0f, chunk, mapNoLightRenderer, mapWithLightRenderer, helper);
                        if (slimeChunks) {
                            renderSlimeChunks(chunk, seed, drawX, drawZ, matrixStack, helper, overlayBufferBuilder);
                        }
                    }
                }
            }
        }
    }

    public void bumpLoadedRegion(MapProcessor mapProcessor, MapRegion region) {
        if (!mapProcessor.isUploadingPaused() && region.isLoaded()) {
            mapProcessor.getMapWorld().getCurrentDimension().getLayeredMapRegions().bumpLoadedRegion(region);
        }
    }

    public void renderSlimeChunks(MapTileChunk chunk, Long seed, int drawX, int drawZ, PoseStack matrixStack, MinimapRendererHelper helper, VertexConsumer overlayBufferBuilder) {
        Long savedSeed = seedsUsed.get(chunk);
        boolean newSeed = (seed == null && savedSeed != null) || !(seed == null || seed.equals(savedSeed));
        if (newSeed) {
            seedsUsed.put(chunk, seed);
        }
        for (int t = 0; t < 16; t++) {
            if (newSeed || (chunk.getTileGridsCache()[t % 4][t / 4] & 1) == 0) {
                chunk.getTileGridsCache()[t % 4][t / 4] = (byte) (1 | (MinimapTile.isSlimeChunk(this.modMain.getSettings(), (chunk.getX() * 4) + (t % 4), (chunk.getZ() * 4) + (t / 4), seed) ? 2 : 0));
            }
            if ((chunk.getTileGridsCache()[t % 4][t / 4] & 2) != 0) {
                int slimeDrawX = drawX + (16 * (t % 4));
                int slimeDrawZ = drawZ + (16 * (t / 4));
                RenderBufferUtil.addColoredRect(matrixStack.last().pose(), overlayBufferBuilder, slimeDrawX, slimeDrawZ, 16, 16, -2142047936);
            }
        }
    }

    public boolean getWorldMapWaypoints() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.WAYPOINTS)).booleanValue();
    }

    public int getWorldMapColours() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Integer) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.BLOCK_COLORS)).intValue();
    }

    public boolean getWorldMapFlowers() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.FLOWERS)).booleanValue();
    }

    public boolean getWorldMapTerrainDepth() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.TERRAIN_DEPTH)).booleanValue();
    }

    public int getWorldMapTerrainSlopes() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Integer) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.TERRAIN_SLOPES)).intValue();
    }

    public boolean getWorldMapBiomeColorsVanillaMode() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.BIOME_COLORS_IN_VANILLA)).booleanValue();
    }

    public boolean getWorldMapIgnoreHeightmaps() {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return false;
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        return mapProcessor.getMapWorld().isIgnoreHeightmaps();
    }

    public String tryToGetMultiworldId(ResourceKey<Level> dimId) {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.uiPauseSync) {
            if (mapProcessor.isUIPaused()) {
                return null;
            }
            return getMultiworldIdUnsynced(mapProcessor, dimId);
        }
    }

    private String getMultiworldIdUnsynced(MapProcessor mapProcessor, ResourceKey<Level> dimId) {
        MapDimension mapDim = (!mapProcessor.isMapWorldUsable() || mapProcessor.isWaitingForWorldUpdate()) ? null : mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
        if (mapDim == null) {
            return null;
        }
        return !mapDim.currentMultiworldWritable ? MINIMAP_MW : mapDim.getCurrentMultiworld();
    }

    public List<String> getPotentialMultiworldIds(ResourceKey<Level> dimId) {
        List<String> multiworldIdsCopy;
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.uiSync) {
            MapDimension mapDim = mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
            multiworldIdsCopy = (mapDim == null || (!mapProcessor.isWaitingForWorldUpdate() && mapDim.currentMultiworldWritable)) ? null : mapDim.getMultiworldIdsCopy();
        }
        return multiworldIdsCopy;
    }

    public List<String> getMultiworldIds(ResourceKey<Level> dimId) {
        List<String> multiworldIdsCopy;
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.uiSync) {
            MapDimension mapDim = mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
            multiworldIdsCopy = mapDim == null ? null : mapDim.getMultiworldIdsCopy();
        }
        return multiworldIdsCopy;
    }

    public String tryToGetMultiworldName(ResourceKey<Level> dimId, String multiworldId) {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.uiPauseSync) {
            if (mapProcessor.isUIPaused()) {
                return null;
            }
            return getMultiworldNameUnsynced(mapProcessor, dimId, multiworldId);
        }
    }

    private String getMultiworldNameUnsynced(MapProcessor mapProcessor, ResourceKey<Level> dimId, String multiworldId) {
        MapDimension mapDim = !mapProcessor.isMapWorldUsable() ? null : mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
        if (mapDim == null) {
            return null;
        }
        return mapDim.getMultiworldName(multiworldId);
    }

    public void openSettings() {
        Screen current = Minecraft.getInstance().screen;
        Minecraft.getInstance().setScreen(getSettingsScreen(current));
    }

    public Screen getSettingsScreen(Screen current) {
        Screen currentEscScreen = current instanceof ScreenBase ? ((ScreenBase) current).escape : null;
        return getSettingsScreen(current, currentEscScreen);
    }

    public Screen getSettingsScreen(Screen current, Screen currentEscScreen) {
        return new GuiWorldMapSettings(current, currentEscScreen, BuiltInEditConfigScreenContexts.CLIENT);
    }

    public float getMinimapBrightness() {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return 1.0f;
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        boolean lightingConfig = ((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.LIGHTING)).booleanValue();
        return mapProcessor.getBrightness(lightingConfig);
    }

    public void prepareMapTexturedRect(Matrix4f matrix, float x, float y, int textureX, int textureY, float width, float height, MapTileChunk chunk, MultiTextureRenderTypeRenderer noLightRenderer, MultiTextureRenderTypeRenderer withLightrenderer, MinimapRendererHelper helper) {
        LeafRegionTexture texture = chunk.getLeafTexture();
        int textureId = texture.getGlColorTexture();
        if (textureId == -1) {
            return;
        }
        helper.prepareMyTexturedModalRect(matrix, x, y, textureX, (int) height, width, height, -height, 64.0f, textureId, texture.getTextureHasLight() ? withLightrenderer : noLightRenderer);
    }

    public boolean getAdjustHeightForCarpetLikeBlocks() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS)).booleanValue();
    }

    public void registerHighlighters(HighlighterRegistry highlighterRegistry) {
        xaero.map.mods.SupportMods.xaeroMinimap.registerMinimapHighlighters(highlighterRegistry);
    }

    public void createRadarRenderWrapper(RadarRenderer radarRenderer) {
        xaero.map.mods.SupportMods.xaeroMinimap.createRadarRendererWrapper(radarRenderer);
    }

    public boolean worldMapIsRenderingRadar() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.MINIMAP_RADAR)).booleanValue();
    }

    public boolean getPartialYTeleport() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.PARTIAL_Y_TELEPORT)).booleanValue();
    }

    public boolean isStainedGlassDisplayed() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.STAINED_GLASS)).booleanValue();
    }

    public boolean isMultiplayerMap() {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return false;
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        return mapProcessor.getMapWorld().isMultiplayer();
    }

    public int getManualCaveStart() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        SingleConfigManager<Config> wmPrimaryConfigManager = wmConfigManager.getPrimaryConfigManager();
        int caveModeStart = ((Integer) wmPrimaryConfigManager.getEffective(WorldMapPrimaryClientConfigOptions.CAVE_MODE_START)).intValue();
        return caveModeStart == Integer.MAX_VALUE ? MinimapWriter.NO_Y_VALUE : caveModeStart;
    }

    public boolean hasEnabledCaveLayers() {
        return getCaveModeType() == 1;
    }

    public int getCaveModeType() {
        if (!WorldMapClientConfigUtils.getEffectiveCaveModeAllowed()) {
            return 0;
        }
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        int defaultCaveModeType = ((Integer) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.DEFAULT_CAVE_MODE_TYPE)).intValue();
        if (worldmapSession == null) {
            return defaultCaveModeType;
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.uiPauseSync) {
            if (mapProcessor.isUIPaused()) {
                return defaultCaveModeType;
            }
            MapDimension mapDim = mapProcessor.getMapWorld().getCurrentDimension();
            if (mapDim != null) {
                return mapDim.getCaveModeType();
            }
            return defaultCaveModeType;
        }
    }

    public Screen getWorldMapScreenForOption(ConfigOption<?> option, Screen current) {
        if (Minecraft.getInstance().level == null) {
            return null;
        }
        Screen currentEscScreen = current instanceof ScreenBase ? ((ScreenBase) current).escape : null;
        if (currentEscScreen instanceof GuiMap) {
            currentEscScreen = null;
        }
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        GuiMap guiMap = new GuiMap(current, currentEscScreen, mapProcessor, Minecraft.getInstance().getCameraEntity());
        if (option == MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START) {
            guiMap.enableCaveModeOptions();
        }
        return guiMap;
    }

    public int getCaveModeDepth() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Integer) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.CAVE_MODE_DEPTH)).intValue();
    }

    public boolean isLegibleCaveMaps() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.LEGIBLE_CAVE_MAPS)).booleanValue();
    }

    public boolean getBiomeBlending() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.BIOME_BLENDING)).booleanValue();
    }

    public void confirmPlayerRadarRender(Player e) {
        if (WorldMap.trackedPlayerRenderer.getCollector().playerExists(e.getUUID())) {
            WorldMap.trackedPlayerRenderer.getCollector().confirmPlayerRadarRender(e);
        }
    }

    public boolean getDisplayClaims() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Boolean) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.OPAC_CLAIMS)).booleanValue();
    }

    public int getClaimsBorderOpacity() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Integer) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.OPAC_CLAIMS_BORDER_OPACITY)).intValue();
    }

    public int getClaimsFillOpacity() {
        ClientConfigManager wmConfigManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        return ((Integer) wmConfigManager.getEffective(WorldMapProfiledConfigOptions.OPAC_CLAIMS_FILL_OPACITY)).intValue();
    }

    public void toggleChunkClaims() {
        WorldMapClientConfigUtils.tryTogglingCurrentProfileOption(WorldMapProfiledConfigOptions.OPAC_CLAIMS);
    }

    public boolean caveLayersAreUsable() {
        boolean result = hasEnabledCaveLayers();
        if (result) {
            WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
            if (worldmapSession == null) {
                return result;
            }
            Entity player = Minecraft.getInstance().getCameraEntity();
            if (player == null) {
                return result;
            }
            MapProcessor mapProcessor = worldmapSession.getMapProcessor();
            MapDimension mapDimension = mapProcessor.getMapWorld().getCurrentDimension();
            if (mapDimension == null) {
                return result;
            }
            if (mapDimension.getDimId() != player.level().dimension()) {
                return false;
            }
        }
        return result;
    }

    public boolean shouldPreventAutoCaveMode(Level world) {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return false;
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        MapDimension mapDimension = mapProcessor.getMapWorld().getCurrentDimension();
        return (mapDimension == null || mapDimension.getDimId() == world.dimension()) ? false : true;
    }

    public double getMapDimensionScale() {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return Minecraft.getInstance().level.dimensionType().coordinateScale();
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        synchronized (mapProcessor.renderThreadPauseSync) {
            if (mapProcessor.isRenderingPaused()) {
                return 0.0d;
            }
            Registry<DimensionType> dimTypes = mapProcessor.getWorldDimensionTypeRegistry();
            if (dimTypes == null) {
                return 0.0d;
            }
            return mapProcessor.getMapWorld().getCurrentDimension().calculateDimScale(dimTypes);
        }
    }

    public ResourceKey<Level> getMapDimension() {
        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
        if (worldmapSession == null) {
            return Minecraft.getInstance().level.dimension();
        }
        MapProcessor mapProcessor = worldmapSession.getMapProcessor();
        MapDimension mapDimension = mapProcessor.getMapWorld().getCurrentDimension();
        return mapDimension == null ? Minecraft.getInstance().level.dimension() : mapDimension.getDimId();
    }
}
