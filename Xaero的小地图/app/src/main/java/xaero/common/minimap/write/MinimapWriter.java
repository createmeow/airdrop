package xaero.common.minimap.write;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.exception.SilentException;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.highlight.DimensionHighlighterHandler;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.region.MinimapChunk;
import xaero.common.minimap.region.MinimapTile;
import xaero.common.minimap.write.biome.BiomeBlendCalculator;
import xaero.common.misc.CachedFunction;
import xaero.common.misc.OptimizedMath;
import xaero.common.mods.SupportMods;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.MinimapConfigConstants;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.module.MinimapSession;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.util.ImageIOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/write/MinimapWriter.class */
public abstract class MinimapWriter {
    private static final int VOID_COLOR = -16121833;
    private static final float DEFAULT_AMBIENT_LIGHT = 0.7f;
    private static final float DEFAULT_AMBIENT_LIGHT_COLORED = 0.2f;
    private static final float DEFAULT_AMBIENT_LIGHT_WHITE = 0.5f;
    private static final float DEFAULT_MAX_DIRECT_LIGHT = 0.6666667f;
    private static final float GLOWING_MAX_DIRECT_LIGHT = 0.22222224f;
    private static final String[] dimensionsToIgnore = {"FZHammer"};
    private static final int UPDATE_EVERY_RUNS = 5;
    private static final int MAXIMUM_OVERLAYS = 5;
    public static final int SUN_MINIMUM = 9;
    public static final int NO_Y_VALUE = Integer.MAX_VALUE;
    private static final int MAX_TRANSPARENCY_BLEND_DEPTH = 5;
    private IXaeroMinimap modMain;
    private MinimapSession minimapSession;
    private MinimapInterface minimapInterface;
    private MinimapChunk[][] loadingBlocks;
    private int loadingMapChunkX;
    private int loadingMapChunkZ;
    private int loadingStartX;
    private int loadingStartZ;
    private int loadingEndX;
    private int loadingEndZ;
    private int loadingLevels;
    private boolean loadingLighting;
    private float loadingSingleLevelBrightness;
    private int loadingTerrainSlopes;
    private boolean loadingTerrainDepth;
    private boolean loadingRedstone;
    private int loadingColours;
    private boolean loadingTransparency;
    private boolean loadingBiomesVanillaMode;
    private ResourceKey<Level> loadingDimension;
    private boolean loadingIgnoreHeightmaps;
    private int loadingCaveMapsDepth;
    public int loadingLightOverlayType;
    public int loadingLightOverlayMaxLight;
    public int loadingLightOverlayMinLight;
    public int loadingLightOverlayColor;
    private boolean loadingFlowers;
    private boolean loadingAdjustHeightForCarpetLikeBlocks;
    private boolean loadingStainedGlass;
    private boolean loadingLegibleCaveMode;
    private boolean loadingBiomeBlending;
    private boolean loadingNonWorldMap;
    private Long loadingSlimeSeed;
    private int loadingHighlightVersion;
    private int loadedSideInChunks;
    private MinimapChunk[][] loadedBlocks;
    private int loadedMapChunkX;
    private int loadedMapChunkZ;
    private int loadedCaving;
    private int loadedLevels;
    private boolean loadedLighting;
    private int loadedTerrainSlopes;
    private boolean loadedTerrainDepth;
    private boolean loadedRedstone;
    private int loadedColours;
    private boolean loadedTransparency;
    private boolean loadedBiomesVanillaMode;
    private ResourceKey<Level> loadedDimension;
    private boolean loadedIgnoreHeightmaps;
    private int loadedCaveMapsDepth;
    public int loadedLightOverlayType;
    public int loadedLightOverlayMaxLight;
    public int loadedLightOverlayMinLight;
    public int loadedLightOverlayColor;
    private boolean loadedFlowers;
    private boolean loadedAdjustHeightForCarpetLikeBlocks;
    private boolean loadedStainedGlass;
    private boolean loadedLegibleCaveMode;
    private boolean loadedBiomeBlending;
    private boolean loadedNonWorldMap;
    private Long loadedSlimeSeed;
    private int loadedHighlightVersion;
    private long loadedTime;
    private boolean settingsChanged;
    private int workingFrameCount;
    private int writeFreeSizeTiles;
    private int writeFreeFullUpdateTargetTime;
    private int updateChunkX;
    private int updateChunkZ;
    private int tileInsideX;
    private int tileInsideZ;
    private int runNumber;
    private boolean previousShouldLoad;
    private boolean clearBlockColours;
    private boolean forcedRefresh;
    private MinimapChunk oldChunk;
    private int updates;
    private int loads;
    private long before;
    private int processingTime;
    public long totalTime;
    public long totalRuns;
    public long minTimeDebug;
    public long maxTimeDebug;
    public long averageTimeDebug;
    private long currentComparisonCode;
    private int firstBlockY;
    boolean isglowing;
    private int sun;
    private float currentTransparencyMultiplier;
    private int blockY;
    private int blockColor;
    private boolean underair;
    private BlockState previousTransparentState;
    private int firstTransparentStateY;
    private final BlockStateShortShapeCache blockStateShortShapeCache;
    private final HighlighterRegistry highlighterRegistry;
    private Level prevWorld;
    private DimensionHighlighterHandler dimensionHighlightHandler;
    protected final RandomSource usedRandom = RandomSource.create(0);
    private long framesFreedTime = -1;
    public long writeFreeSinceLastWrite = -1;
    private long lastWrite = -1;
    private long lastWriteTry = -1;
    private BlockState lastBlockStateForTextureColor = null;
    private int lastBlockStateForTextureColorResult = -1;
    public boolean debugTotalTime = false;
    public long minTime = -1;
    public long maxTime = -1;
    public long lastDebugTime = -1;
    private int loadingSideInChunks = 9;
    private int updateRadius = 16;
    private int loadingCaving = NO_Y_VALUE;
    private int prevLoadedCaving = NO_Y_VALUE;
    private int lastCaving = NO_Y_VALUE;
    private final HashMap<String, Integer> textureColours = new HashMap<>();
    private final HashMap<Integer, Integer> blockColours = new HashMap<>();
    private final int[] red = new int[5];
    private final int[] green = new int[5];
    private final int[] blue = new int[5];
    private final int[] underRed = new int[5];
    private final int[] underGreen = new int[5];
    private final int[] underBlue = new int[5];
    private final float[] brightness = new float[5];
    private final float[] postBrightness = new float[5];
    private final int[] tempColor = new int[3];
    private MinimapWriterHelper helper = new MinimapWriterHelper();
    private final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
    private final BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
    private final BlockPos.MutableBlockPos mutableBlockPos3 = new BlockPos.MutableBlockPos();
    private final int[][] intUpdateArrayBuffers = new int[5][MinimapChunk.INT_BUFFER_SIZE];
    private final List<BlockState> pixelBlockStates = new ArrayList();
    private final List<Integer> pixelTransparentSizes = new ArrayList();
    private final List<Integer> pixelBlockLights = new ArrayList();
    private final CachedFunction<StateHolder<?, ?>, Boolean> transparentCache = new CachedFunction<>(state -> {
        if (state instanceof BlockState) {
            BlockState blockState = (BlockState) state;
            if ((blockState.getBlock() instanceof AirBlock) || (blockState.getBlock() instanceof TransparentBlock)) {
                return true;
            }
            return Boolean.valueOf(blockStateHasTranslucentRenderType(blockState));
        }
        FluidState fluidState = (FluidState) state;
        return Boolean.valueOf(ItemBlockRenderTypes.getRenderLayer(fluidState) == RenderType.translucent());
    });
    private final Map<BlockState, Boolean> glowingCache = new HashMap();
    private ArrayList<BlockState> buggedStates = new ArrayList<>();
    private ArrayList<Long> detectedChunkChanges = new ArrayList<>();
    private final Minecraft mc = Minecraft.getInstance();
    private final BiomeBlendCalculator biomeBlendCalculator = new BiomeBlendCalculator();
    private CachedFunction<FluidState, BlockState> fluidToBlock = new CachedFunction<>((v0) -> {
        return v0.createLegacyBlock();
    });
    private final Object2IntMap<BlockState> blockTintIndices = new Object2IntOpenHashMap();

    protected abstract boolean blockStateHasTranslucentRenderType(BlockState blockState);

    protected abstract int getBlockStateLightEmission(BlockState blockState, Level level, BlockPos blockPos);

    protected abstract List<BakedQuad> getQuads(BakedModel bakedModel, BlockState blockState, Direction direction);

    protected abstract TextureAtlasSprite getParticleIcon(BlockModelShaper blockModelShaper, BakedModel bakedModel, BlockState blockState);

    public MinimapWriter(IXaeroMinimap modMain, MinimapSession minimapSession, BlockStateShortShapeCache blockStateShortShapeCache, HighlighterRegistry highlighterRegistry) {
        this.modMain = modMain;
        this.minimapSession = minimapSession;
        this.loadedCaving = NO_Y_VALUE;
        this.loadedCaving = NO_Y_VALUE;
        this.minimapInterface = modMain.getInterfaces().getMinimapInterface();
        this.blockStateShortShapeCache = blockStateShortShapeCache;
        this.highlighterRegistry = highlighterRegistry;
    }

    public void setupDimensionHighlightHandler(ResourceKey<Level> dimension) {
        this.dimensionHighlightHandler = new DimensionHighlighterHandler(dimension, this.highlighterRegistry, this);
    }

    private void updateTimeDebug(long before) {
        if (this.debugTotalTime) {
            long debugPassed = System.nanoTime() - before;
            this.totalTime += debugPassed;
            this.totalRuns++;
            if (debugPassed > this.maxTime) {
                this.maxTime = debugPassed;
            }
            if (this.minTime == -1 || debugPassed < this.minTime) {
                this.minTime = debugPassed;
            }
            long time = System.currentTimeMillis();
            if (this.lastDebugTime == -1) {
                this.lastDebugTime = time;
                return;
            }
            if (time - this.lastDebugTime > 1000) {
                this.maxTimeDebug = this.maxTime;
                this.minTimeDebug = this.minTime;
                this.averageTimeDebug = this.totalTime / this.totalRuns;
                this.maxTime = -1L;
                this.minTime = -1L;
                this.totalTime = 0L;
                this.totalRuns = 0L;
                this.lastDebugTime = time;
            }
        }
    }

    public void onRender() {
        Entity player;
        if (!ModSettings.canEditIngameSettings()) {
            return;
        }
        long before = System.nanoTime();
        MinimapProcessor minimapProcessor = this.minimapSession.getProcessor();
        try {
            player = Minecraft.getInstance().getCameraEntity();
        } catch (Throwable e) {
            this.minimapInterface.setCrashedWith(e);
        }
        if (player == null) {
            return;
        }
        Level world = player.level();
        if (world != this.prevWorld) {
            if (world != null) {
                setupDimensionHighlightHandler(world.dimension());
            } else {
                this.dimensionHighlightHandler = null;
            }
            this.loadedDimension = null;
            this.updateChunkZ = 0;
            this.updateChunkX = 0;
            this.tileInsideZ = 0;
            this.tileInsideX = 0;
            this.prevWorld = world;
            if (this.modMain.getSupportMods().framedBlocks()) {
                this.modMain.getSupportMods().supportFramedBlocks.onWorldChange();
            }
        }
        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean displayMinimap = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DISPLAY_MINIMAP)).booleanValue();
        if (this.modMain.getSettings() == null || !displayMinimap || world == null) {
            updateTimeDebug(before);
            return;
        }
        int lightOverlayTypeConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.LIGHT_OVERLAY_TYPE)).intValue();
        int cavingDestination = getCaving(playerX, playerY, playerZ, world);
        boolean attemptUsingWorldMapChunks = this.modMain.getSupportMods().shouldUseWorldMapChunks() && (cavingDestination == Integer.MAX_VALUE || this.modMain.getSupportMods().shouldUseWorldMapCaveChunks()) && lightOverlayTypeConfig <= 0;
        boolean shouldLoad = (ignoreWorld(world) || (attemptUsingWorldMapChunks && !this.loadedNonWorldMap && !this.loadingNonWorldMap && this.loadedCaving == cavingDestination && this.loadedCaving == this.loadingCaving)) ? false : true;
        if (shouldLoad != this.previousShouldLoad) {
            this.updateChunkZ = 0;
            this.updateChunkX = 0;
            this.tileInsideZ = 0;
            this.tileInsideX = 0;
            this.previousShouldLoad = shouldLoad;
        }
        if (!shouldLoad) {
            updateTimeDebug(before);
            return;
        }
        XaeroMinimapCore.ensureField();
        int lengthX = Math.min(this.loadingSideInChunks, (this.loadingEndX - this.loadingStartX) + 1);
        int lengthZ = Math.min(this.loadingSideInChunks, (this.loadingEndZ - this.loadingStartZ) + 1);
        if (this.lastWriteTry == -1) {
            lengthX = 3;
            lengthZ = 3;
        } else {
            if (lengthX > this.loadingSideInChunks) {
                lengthX = this.loadingSideInChunks;
            }
            if (lengthZ > this.loadingSideInChunks) {
                lengthZ = this.loadingSideInChunks;
            }
        }
        int sizeTileChunks = lengthX * lengthZ;
        int sizeTiles = sizeTileChunks * 4 * 4;
        int sizeBasedTargetTime = (sizeTiles * 1000) / 1500;
        int fullUpdateTargetTime = Math.max(100, sizeBasedTargetTime);
        long time = System.currentTimeMillis();
        long passed = this.lastWrite == -1 ? 0L : time - this.lastWrite;
        if (this.lastWriteTry == -1 || this.writeFreeSizeTiles != sizeTiles || this.writeFreeFullUpdateTargetTime != fullUpdateTargetTime || this.workingFrameCount > 30) {
            this.framesFreedTime = time;
            this.writeFreeSizeTiles = sizeTiles;
            this.writeFreeFullUpdateTargetTime = fullUpdateTargetTime;
            this.workingFrameCount = 0;
        }
        long sinceLastWrite = Math.min(passed, this.writeFreeSinceLastWrite);
        if (this.framesFreedTime != -1) {
            sinceLastWrite = time - this.framesFreedTime;
        }
        long tilesToUpdate = Math.min((sinceLastWrite * sizeTiles) / fullUpdateTargetTime, 100L);
        if (this.lastWrite == -1 || tilesToUpdate != 0) {
            this.lastWrite = time;
        }
        int flickeringTimer = ((Double) configManager.getEffective(MinimapProfiledConfigOptions.CAVE_MODE_TOGGLE_TIMER)).intValue();
        if (tilesToUpdate != 0) {
            if (this.framesFreedTime == -1) {
                int timeLimit = (int) (Math.min(sinceLastWrite, 50L) * 86960);
                long writeStartNano = System.nanoTime();
                if ((cavingDestination == Integer.MAX_VALUE) != (this.loadingCaving == Integer.MAX_VALUE) || attemptUsingWorldMapChunks == this.loadingNonWorldMap) {
                    this.updateChunkZ = 0;
                    this.updateChunkX = 0;
                    this.tileInsideZ = 0;
                    this.tileInsideX = 0;
                    this.loadedTime = time;
                }
                int i = 0;
                while (i < tilesToUpdate && !beforeWriting(attemptUsingWorldMapChunks, cavingDestination, flickeringTimer, time)) {
                    if (writeChunk(minimapProcessor, playerX, playerY, playerZ, world, cavingDestination, attemptUsingWorldMapChunks)) {
                        i--;
                    }
                    if (System.nanoTime() - writeStartNano >= timeLimit) {
                        break;
                    } else {
                        i++;
                    }
                }
                this.workingFrameCount++;
            } else {
                this.writeFreeSinceLastWrite = sinceLastWrite;
                this.framesFreedTime = -1L;
            }
        }
        this.lastWriteTry = time;
        updateTimeDebug(before);
    }

    private boolean beforeWriting(boolean attemptUsingWorldMapChunks, int cavingDestination, int flickeringTimer, long time) {
        if (this.tileInsideX == 0 && this.tileInsideZ == 0 && this.updateChunkX == 0 && this.updateChunkZ == 0 && attemptUsingWorldMapChunks) {
            this.loadingCaving = cavingDestination;
            this.loadingNonWorldMap = false;
            if ((this.loadedCaving == Integer.MAX_VALUE) == (this.loadingCaving == Integer.MAX_VALUE) || this.loadedTime == 0 || time - this.loadedTime >= flickeringTimer) {
                this.loadedCaving = this.loadingCaving;
                this.loadedNonWorldMap = false;
            }
            if (!this.loadedNonWorldMap) {
                this.detectedChunkChanges.clear();
                return true;
            }
            return true;
        }
        if (this.tileInsideX == 3 && this.tileInsideZ == 3 && this.updateChunkX == this.loadingSideInChunks - 1 && this.updateChunkZ == this.loadingSideInChunks - 1) {
            if ((this.loadingCaving == Integer.MAX_VALUE) != (this.loadedCaving == Integer.MAX_VALUE) && this.loadedTime != 0 && time - this.loadedTime < flickeringTimer) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:152:0x055c  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x057a  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0595  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x05b0  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x05cb  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x05e6  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0601  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x061c  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0637  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean writeChunk(xaero.common.minimap.MinimapProcessor r22, double r23, double r25, double r27, net.minecraft.world.level.Level r29, int r30, boolean r31) throws xaero.common.exception.SilentException, java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 2938
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.common.minimap.write.MinimapWriter.writeChunk(xaero.common.minimap.MinimapProcessor, double, double, double, net.minecraft.world.level.Level, int, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean writeTile(xaero.common.minimap.MinimapProcessor r67, double r68, double r70, double r72, net.minecraft.world.level.Level r74, xaero.common.minimap.region.MinimapChunk r75, xaero.common.minimap.region.MinimapChunk r76, xaero.common.minimap.region.MinimapChunk r77, xaero.common.minimap.region.MinimapChunk r78, xaero.common.minimap.region.MinimapChunk r79, int r80, int r81, int r82, int r83, boolean r84, boolean r85) throws xaero.common.exception.SilentException, java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 1503
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.common.minimap.write.MinimapWriter.writeTile(xaero.common.minimap.MinimapProcessor, double, double, double, net.minecraft.world.level.Level, xaero.common.minimap.region.MinimapChunk, xaero.common.minimap.region.MinimapChunk, xaero.common.minimap.region.MinimapChunk, xaero.common.minimap.region.MinimapChunk, xaero.common.minimap.region.MinimapChunk, int, int, int, int, boolean, boolean):boolean");
    }

    public MinimapTile loadBlockColor(int playerYi, Level world, int insideX, int insideZ, LevelChunk bchunk, int tileX, int tileZ, int tileInsideX, int tileInsideZ, int sectionBasedHeight, Heightmap.Types typeWorldSurface, MinimapTile oldTile, MinimapChunk mchunk, MinimapChunk topChunk, MinimapChunk topLeftChunk, MinimapChunk leftChunk, float shadowR, float shadowG, float shadowB, boolean sameCaveLevel, boolean sameHighlights, int canvasX, int canvasZ, int highlight, boolean settingsChanged, int loadingCaving, int loadingLevels, boolean loadingLighting, float loadingSingleLevelBrightness, int loadingTerrainSlopes, boolean loadingTerrainDepth, List<Integer> pixelTransparentSizes, List<BlockState> pixelBlockStates, List<Integer> pixelBlockLights, int[] underRed, int[] underGreen, int[] underBlue, float[] postBrightness, float[] brightness, int[] red, int[] green, int[] blue, int[] tempColor, boolean loadingIgnoreHeightmaps, int loadingCaveMapsDepth, BlockPos.MutableBlockPos mutableBlockPos, BlockPos.MutableBlockPos mutableBlockPos2, Long loadingSlimeSeed, int loadedLevels, IXaeroMinimap modMain, MinimapWriterHelper helper, int loadingColours, boolean loadingRedstone, boolean loadingTransparency, int loadingLightOverlayType, int loadingLightOverlayMaxLight, int loadingLightOverlayMinLight, int loadingLightOverlayColor, boolean loadingFlowers, boolean adjustHeightForCarpetLikeBlocks, boolean loadingStainedGlass, boolean loadingLegibleCaveMode, BlockPos.MutableBlockPos mutableBlockPos3, boolean framedBlocksExist) throws SilentException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int highY;
        float b;
        int worldBottomY = world.getMinBuildHeight();
        if (loadingCaving != Integer.MAX_VALUE) {
            highY = loadingCaving;
        } else {
            int height = bchunk.getHeight(typeWorldSurface, insideX, insideZ);
            if (loadingIgnoreHeightmaps || height < worldBottomY) {
                highY = sectionBasedHeight;
            } else {
                highY = height;
            }
        }
        if (highY >= world.getMaxBuildHeight()) {
            highY = world.getMaxBuildHeight() - 1;
        }
        int bottom = loadingCaving != Integer.MAX_VALUE ? (highY + 1) - loadingCaveMapsDepth : worldBottomY;
        int lowY = bottom;
        if (lowY < worldBottomY) {
            lowY = worldBottomY;
        }
        pixelTransparentSizes.clear();
        pixelBlockStates.clear();
        pixelBlockLights.clear();
        this.currentComparisonCode = 0L;
        byte currentComparisonCodeAdd = 0;
        this.blockY = 0;
        for (int i = 0; i < loadingLevels; i++) {
            underRed[i] = 0;
            underGreen[i] = 0;
            underBlue[i] = 0;
        }
        this.currentTransparencyMultiplier = 1.0f;
        this.sun = 15;
        this.blockColor = 0;
        this.isglowing = false;
        double secondaryBR = 1.0d;
        double secondaryBG = 1.0d;
        double secondaryBB = 1.0d;
        Block block = findBlock(world, bchunk, insideX, insideZ, highY, lowY, loadingCaving, loadingRedstone, mutableBlockPos, mutableBlockPos2, loadingColours, loadingTransparency, pixelBlockLights, pixelBlockStates, loadingLevels, loadingLighting, pixelTransparentSizes, loadingFlowers, loadingStainedGlass, mutableBlockPos3, framedBlocksExist);
        BlockState state = pixelBlockStates.isEmpty() ? null : pixelBlockStates.get(pixelBlockStates.size() - 1);
        if (adjustHeightForCarpetLikeBlocks && state != null && this.blockStateShortShapeCache.isShort(state)) {
            this.blockY--;
        }
        boolean isglowing = this.isglowing;
        int blockY = this.blockY;
        long currentComparisonCode = this.currentComparisonCode;
        boolean success = true;
        int prevHeight = Integer.MAX_VALUE;
        int prevHeightDiagonal = Integer.MAX_VALUE;
        int prevInsideX = insideX - 1;
        int prevInsideZ = insideZ - 1;
        boolean xEdge = prevInsideX < 0;
        boolean zEdge = prevInsideZ < 0;
        MinimapTile tile = mchunk.getTile(tileInsideX, tileInsideZ);
        MinimapTile prevHeightSrc = tile;
        MinimapTile prevHeightDiagonalSrc = tile;
        if (zEdge) {
            prevInsideZ = 15;
            if (tileInsideZ > 0) {
                prevHeightSrc = mchunk.getTile(tileInsideX, tileInsideZ - 1);
            } else if (topChunk != null) {
                prevHeightSrc = topChunk.getTile(tileInsideX, 3);
            }
        }
        if (xEdge) {
            prevInsideX = 15;
            if (zEdge) {
                if (tileInsideZ > 0 && tileInsideX > 0) {
                    prevHeightDiagonalSrc = mchunk.getTile(tileInsideX - 1, tileInsideZ - 1);
                } else if (tileInsideX == 0 && tileInsideZ == 0) {
                    if (topLeftChunk != null) {
                        prevHeightDiagonalSrc = topLeftChunk.getTile(3, 3);
                    }
                } else if (tileInsideX == 0) {
                    if (leftChunk != null) {
                        prevHeightDiagonalSrc = leftChunk.getTile(3, tileInsideZ - 1);
                    }
                } else if (topChunk != null) {
                    prevHeightDiagonalSrc = topChunk.getTile(tileInsideX - 1, 3);
                }
            } else if (tileInsideX > 0) {
                prevHeightDiagonalSrc = mchunk.getTile(tileInsideX - 1, tileInsideZ);
            } else if (leftChunk != null) {
                prevHeightDiagonalSrc = leftChunk.getTile(3, tileInsideZ);
            }
        } else {
            prevHeightDiagonalSrc = prevHeightSrc;
        }
        if (prevHeightSrc != null && (prevHeightSrc == tile || prevHeightSrc.hasTerrain())) {
            prevHeight = prevHeightSrc.getHeight(insideX, prevInsideZ);
            if (prevHeightSrc != tile && prevHeightSrc.caveLevel != loadingCaving) {
                success = false;
            }
        } else if (zEdge) {
            prevHeight = blockY;
            if (pixelTransparentSizes.isEmpty()) {
                try {
                    LevelChunk chunk = world.getChunk(tileX, tileZ - 1);
                    if (chunk != null) {
                        prevHeight = chunk.getHeight(typeWorldSurface, insideX, prevInsideZ);
                    }
                } catch (IllegalStateException e) {
                }
            }
            success = false;
        }
        if (prevHeightDiagonalSrc != null && (prevHeightDiagonalSrc == tile || prevHeightDiagonalSrc.hasTerrain())) {
            prevHeightDiagonal = prevHeightDiagonalSrc.getHeight(prevInsideX, prevInsideZ);
            if (prevHeightDiagonalSrc != tile && prevHeightDiagonalSrc.caveLevel != loadingCaving) {
                success = false;
            }
        } else if (xEdge || zEdge) {
            prevHeightDiagonal = blockY;
            if (pixelTransparentSizes.isEmpty()) {
                try {
                    LevelChunk chunk2 = (xEdge && zEdge) ? world.getChunk(tileX - 1, tileZ - 1) : zEdge ? world.getChunk(tileX, tileZ - 1) : world.getChunk(tileX - 1, tileZ);
                    if (chunk2 != null) {
                        prevHeightDiagonal = chunk2.getHeight(typeWorldSurface, prevInsideX, prevInsideZ);
                    }
                } catch (IllegalStateException e2) {
                }
            }
            success = false;
        }
        int verticalSlope = 0;
        int diagonalSlope = 0;
        if (loadingTerrainSlopes > 0) {
            if (prevHeight != Integer.MAX_VALUE) {
                verticalSlope = Math.max(-128, Math.min(127, blockY - prevHeight));
            }
            if (prevHeightDiagonal != Integer.MAX_VALUE) {
                diagonalSlope = Math.max(-128, Math.min(127, blockY - prevHeightDiagonal));
            }
        }
        for (int i2 = 0; i2 < pixelBlockLights.size(); i2++) {
            int l = pixelBlockLights.get(i2).intValue();
            if (i2 <= 1) {
                currentComparisonCodeAdd = (byte) (currentComparisonCodeAdd | (l << ((4 * i2) + 1)));
            }
            if (i2 >= 1) {
                currentComparisonCode |= (l << (4 * (i2 - 1))) >> 3;
            }
        }
        int add2Calculation = 17;
        for (int i3 = 0; i3 < pixelTransparentSizes.size(); i3++) {
            add2Calculation = (add2Calculation * 37) + pixelTransparentSizes.get(i3).intValue();
        }
        byte currentComparisonCodeAdd2 = (byte) (currentComparisonCodeAdd | ((add2Calculation >> 8) & 1));
        byte currentComparisonCodeAdd22 = (byte) add2Calculation;
        boolean reuseColour = !settingsChanged && sameCaveLevel && sameHighlights && !oldTile.pixelChanged(insideX, insideZ, currentComparisonCode, currentComparisonCodeAdd2, currentComparisonCodeAdd22, (byte) verticalSlope, (byte) diagonalSlope);
        if (!reuseColour) {
            if (highlight != 0 && block == null) {
                this.sun = 0;
            }
            int firstSun = this.sun;
            boolean hasTransparentLayer = (highlight == 0 && pixelTransparentSizes.isEmpty()) ? false : true;
            if (hasTransparentLayer && firstSun != 15) {
                this.sun = 15;
            }
            if (highlight != 0) {
                int hlRed = (highlight >> 8) & 255;
                int hlGreen = (highlight >> 16) & 255;
                int hlBlue = (highlight >> 24) & 255;
                int hlAlpha = highlight & 255;
                float hlAlphaFloat = hlAlpha / 255.0f;
                applyTransparentLayer(hlRed, hlGreen, hlBlue, hlAlphaFloat, true);
            }
            boolean legibleCaveMode = loadingLegibleCaveMode && loadingCaving != Integer.MAX_VALUE;
            calculateBlockColors(world, bchunk, insideX, insideZ, mutableBlockPos2, pixelTransparentSizes, pixelBlockStates, pixelBlockLights, loadingColours, loadingLightOverlayColor, loadingCaving, loadingLevels, loadingLighting, loadingSingleLevelBrightness, legibleCaveMode);
            int blockColor = this.blockColor;
            float currentTransparencyMultiplier = this.currentTransparencyMultiplier;
            int sun = this.sun;
            if (block == null) {
                sun = 15;
            }
            boolean isglowing2 = block != null && isglowing;
            int topLight = pixelBlockLights.isEmpty() ? 0 : pixelBlockLights.get(0).intValue();
            int cr = (blockColor >> 16) & 255;
            int cg = (blockColor >> 8) & 255;
            int cb = blockColor & 255;
            if (isglowing2) {
                helper.getGlowingColour(cr, cg, cb, tempColor);
                cr = tempColor[0];
                cg = tempColor[1];
                cb = tempColor[2];
                if (hasTransparentLayer && pixelTransparentSizes.isEmpty()) {
                    topLight = 15;
                }
            }
            if (!isglowing2 || hasTransparentLayer) {
                int blockLight = pixelBlockLights.isEmpty() ? 0 : pixelBlockLights.get(pixelBlockLights.size() - 1).intValue();
                int firstBlockY = this.firstBlockY;
                for (int i4 = 0; i4 < loadingLevels; i4++) {
                    postBrightness[i4] = 1.0f;
                    if (legibleCaveMode) {
                        if (!isglowing2) {
                            brightness[i4] = block == null ? 1.0f : ((1.0f + blockY) - bottom) / ((1 + highY) - bottom);
                        }
                        if (hasTransparentLayer) {
                            float transparentLayerCaveBrightness = (block == null && pixelTransparentSizes.isEmpty()) ? getFixedSkyLightBlockBrightness(9.0f, 0.0f, 0) : ((1.0f + firstBlockY) - bottom) / ((1 + highY) - bottom);
                            underRed[i4] = (int) (underRed[r1] * transparentLayerCaveBrightness);
                            underGreen[i4] = (int) (underGreen[r1] * transparentLayerCaveBrightness);
                            underBlue[i4] = (int) (underBlue[r1] * transparentLayerCaveBrightness);
                        }
                    } else {
                        if (!isglowing2) {
                            if (!hasTransparentLayer) {
                                if (block == null) {
                                    brightness[i4] = 1.0f;
                                } else {
                                    brightness[i4] = loadingLevels != 1 ? getBlockBrightness(9.0f, sun, i4, blockLight) : getFixedSkyLightBlockBrightness(9.0f, loadingSingleLevelBrightness, blockLight);
                                }
                            } else {
                                brightness[i4] = getBlockBrightness(9.0f, sun, 0, blockLight);
                            }
                        }
                        if (hasTransparentLayer) {
                            postBrightness[i4] = loadingLevels != 1 ? getBlockBrightness(9.0f, firstSun, i4, topLight) : getFixedSkyLightBlockBrightness(9.0f, loadingSingleLevelBrightness, topLight);
                        }
                    }
                }
            }
            float depthBrightness = 1.0f;
            if (block != null && !isglowing2 && loadingTerrainDepth && !legibleCaveMode) {
                if (loadingCaving != Integer.MAX_VALUE) {
                    depthBrightness = DEFAULT_AMBIENT_LIGHT + ((0.3f * (blockY - bottom)) / (highY - bottom));
                } else {
                    depthBrightness = blockY / 63.0f;
                }
                float max = loadingTerrainSlopes >= 2 ? 1.0f : 1.15f;
                float min = loadingTerrainSlopes >= 2 ? 0.9f : DEFAULT_AMBIENT_LIGHT;
                if (depthBrightness > max) {
                    depthBrightness = max;
                } else if (depthBrightness < min) {
                    depthBrightness = min;
                }
            }
            if (block != null && loadingTerrainSlopes > 0) {
                if (loadingTerrainSlopes == 1) {
                    if (!isglowing2) {
                        if (verticalSlope > 0) {
                            depthBrightness = (float) (depthBrightness * 1.15d);
                        } else if (verticalSlope < 0) {
                            depthBrightness = (float) (depthBrightness * 0.85d);
                        }
                    }
                } else {
                    float ambientLightColored = 0.2f;
                    float ambientLightWhite = 0.5f;
                    float maxDirectLight = 0.6666667f;
                    if (isglowing2) {
                        ambientLightColored = 0.0f;
                        ambientLightWhite = 1.0f;
                        maxDirectLight = 0.22222224f;
                    }
                    float cos = 0.0f;
                    if (loadingTerrainSlopes == 2) {
                        float crossZ = -verticalSlope;
                        if (crossZ < 1.0f) {
                            if (verticalSlope == 1 && diagonalSlope == 1) {
                                cos = 1.0f;
                            } else {
                                float crossX = verticalSlope - diagonalSlope;
                                float cast = 1.0f - crossZ;
                                float crossMagnitude = (float) Math.sqrt((crossX * crossX) + 1.0f + (crossZ * crossZ));
                                cos = (float) ((cast / crossMagnitude) / Math.sqrt(2.0d));
                            }
                        }
                    } else if (verticalSlope >= 0) {
                        if (verticalSlope == 1) {
                            cos = 1.0f;
                        } else {
                            float surfaceDirectionMagnitude = (float) Math.sqrt((verticalSlope * verticalSlope) + 1);
                            float castToMostLit = verticalSlope + 1;
                            cos = (float) ((castToMostLit / surfaceDirectionMagnitude) / Math.sqrt(2.0d));
                        }
                    }
                    float directLightClamped = 0.0f;
                    if (cos == 1.0f) {
                        directLightClamped = maxDirectLight;
                    } else if (cos > 0.0f) {
                        directLightClamped = (((float) Math.ceil(cos * 10.0f)) / 10.0f) * maxDirectLight * 0.88388f;
                    }
                    float whiteLight = ambientLightWhite + directLightClamped;
                    secondaryBR = 1.0d * ((shadowR * ambientLightColored) + whiteLight);
                    secondaryBG = 1.0d * ((shadowG * ambientLightColored) + whiteLight);
                    secondaryBB = 1.0d * ((shadowB * ambientLightColored) + whiteLight);
                }
            }
            double secondaryBR2 = secondaryBR * depthBrightness;
            double secondaryBG2 = secondaryBG * depthBrightness;
            double secondaryBB2 = secondaryBB * depthBrightness;
            if (loadingLightOverlayType > 0) {
                int blockLight2 = pixelBlockLights.isEmpty() ? 0 : pixelBlockLights.get(0).intValue();
                int testLight = loadingLightOverlayType == 1 ? blockLight2 : loadingLightOverlayType == 2 ? firstSun : Math.max(blockLight2, firstSun);
                if (testLight >= loadingLightOverlayMinLight && testLight <= loadingLightOverlayMaxLight) {
                    int overlayColor = MinimapConfigConstants.COLORS[loadingLightOverlayColor];
                    int overlayRed = (((overlayColor >> 16) & 255) * 2) / 3;
                    int overlayGreen = (((overlayColor >> 8) & 255) * 2) / 3;
                    int overlayBlue = ((overlayColor & 255) * 2) / 3;
                    for (int i5 = 0; i5 < loadingLevels; i5++) {
                        float destColorScale = (isglowing2 ? 1.0f : postBrightness[i5]) / 3.0f;
                        underRed[i5] = (int) (underRed[r1] * destColorScale);
                        underGreen[i5] = (int) (underGreen[r1] * destColorScale);
                        underBlue[i5] = (int) (underBlue[r1] * destColorScale);
                        int i6 = i5;
                        brightness[i6] = brightness[i6] * destColorScale;
                        postBrightness[i5] = 1.0f;
                        int i7 = i5;
                        underRed[i7] = underRed[i7] + overlayRed;
                        int i8 = i5;
                        underGreen[i8] = underGreen[i8] + overlayGreen;
                        int i9 = i5;
                        underBlue[i9] = underBlue[i9] + overlayBlue;
                    }
                    if (isglowing2) {
                        secondaryBR2 /= 3.0d;
                        secondaryBG2 /= 3.0d;
                        secondaryBB2 /= 3.0d;
                    }
                }
            }
            for (int i10 = 0; i10 < loadingLevels; i10++) {
                if (isglowing2) {
                    b = 1.0f;
                    if (!hasTransparentLayer) {
                        postBrightness[i10] = 1.0f;
                    }
                } else {
                    b = brightness[i10];
                }
                red[i10] = (int) (((cr * b * secondaryBR2 * currentTransparencyMultiplier) + underRed[i10]) * postBrightness[i10]);
                if (red[i10] > 255) {
                    red[i10] = 255;
                }
                green[i10] = (int) (((cg * b * secondaryBG2 * currentTransparencyMultiplier) + underGreen[i10]) * postBrightness[i10]);
                if (green[i10] > 255) {
                    green[i10] = 255;
                }
                blue[i10] = (int) (((cb * b * secondaryBB2 * currentTransparencyMultiplier) + underBlue[i10]) * postBrightness[i10]);
                if (blue[i10] > 255) {
                    blue[i10] = 255;
                }
            }
        } else {
            for (int i11 = 0; i11 < loadingLevels; i11++) {
                red[i11] = oldTile.getRed(i11, insideX, insideZ);
                green[i11] = oldTile.getGreen(i11, insideX, insideZ);
                blue[i11] = oldTile.getBlue(i11, insideX, insideZ);
            }
        }
        if (tile == null) {
            tile = MinimapTile.getANewTile(modMain.getSettings(), tileX, tileZ, loadingSlimeSeed);
            mchunk.setTile(tileInsideX, tileInsideZ, tile);
        }
        if (notEmptyColor(red, green, blue)) {
            tile.setHasSomething(true);
            mchunk.setHasSomething(true);
        }
        tile.setHeight(insideX, insideZ, blockY);
        tile.setCode(insideX, insideZ, currentComparisonCode, currentComparisonCodeAdd2, currentComparisonCodeAdd22, (byte) verticalSlope, (byte) diagonalSlope);
        if (tile.isSuccess()) {
            tile.setSuccess(success);
        }
        if (oldTile != null) {
            int oldTileDarkestLevel = loadedLevels - 1;
            int tileDarkestLevel = loadingLevels - 1;
            if (oldTile.getRed(oldTileDarkestLevel, insideX, insideZ) != red[tileDarkestLevel] || oldTile.getGreen(oldTileDarkestLevel, insideX, insideZ) != green[tileDarkestLevel] || oldTile.getBlue(oldTileDarkestLevel, insideX, insideZ) != blue[tileDarkestLevel]) {
                mchunk.setChanged(true);
            }
        } else {
            mchunk.setChanged(true);
        }
        for (int i12 = 0; i12 < loadingLevels; i12++) {
            tile.setRGB(i12, insideX, insideZ, red[i12], green[i12], blue[i12]);
        }
        return tile;
    }

    private BlockState unpackFramedBlocks(BlockState original, Level world, BlockPos globalPos) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        BlockEntity tileEntity;
        if (original.getBlock() instanceof AirBlock) {
            return original;
        }
        BlockState result = original;
        SupportMods supportMods = this.modMain.getSupportMods();
        if (supportMods.supportFramedBlocks.isFrameBlock(world, null, original) && (tileEntity = world.getBlockEntity(globalPos)) != null) {
            result = supportMods.supportFramedBlocks.unpackFramedBlock(world, null, original, tileEntity);
            if (result == null || (result.getBlock() instanceof AirBlock)) {
                result = original;
            }
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x01a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public net.minecraft.world.level.block.Block findBlock(net.minecraft.world.level.Level r25, net.minecraft.world.level.chunk.LevelChunk r26, int r27, int r28, int r29, int r30, int r31, boolean r32, net.minecraft.core.BlockPos.MutableBlockPos r33, net.minecraft.core.BlockPos.MutableBlockPos r34, int r35, boolean r36, java.util.List<java.lang.Integer> r37, java.util.List<net.minecraft.world.level.block.state.BlockState> r38, int r39, boolean r40, java.util.List<java.lang.Integer> r41, boolean r42, boolean r43, net.minecraft.core.BlockPos.MutableBlockPos r44, boolean r45) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 516
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.common.minimap.write.MinimapWriter.findBlock(net.minecraft.world.level.Level, net.minecraft.world.level.chunk.LevelChunk, int, int, int, int, int, boolean, net.minecraft.core.BlockPos$MutableBlockPos, net.minecraft.core.BlockPos$MutableBlockPos, int, boolean, java.util.List, java.util.List, int, boolean, java.util.List, boolean, boolean, net.minecraft.core.BlockPos$MutableBlockPos, boolean):net.minecraft.world.level.block.Block");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:87:0x015c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean findBlockHelp(net.minecraft.world.level.Level r9, net.minecraft.world.level.chunk.ChunkAccess r10, int r11, int r12, int r13, net.minecraft.world.level.block.state.BlockState r14, net.minecraft.world.level.material.FluidState r15, int r16, boolean r17, int r18, boolean r19, net.minecraft.core.BlockPos.MutableBlockPos r20, net.minecraft.core.BlockPos.MutableBlockPos r21, int r22, boolean r23, java.util.List<java.lang.Integer> r24, java.util.List<net.minecraft.world.level.block.state.BlockState> r25, int r26, boolean r27, java.util.List<java.lang.Integer> r28, boolean r29, boolean r30) {
        /*
            Method dump skipped, instructions count: 774
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.common.minimap.write.MinimapWriter.findBlockHelp(net.minecraft.world.level.Level, net.minecraft.world.level.chunk.ChunkAccess, int, int, int, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.material.FluidState, int, boolean, int, boolean, net.minecraft.core.BlockPos$MutableBlockPos, net.minecraft.core.BlockPos$MutableBlockPos, int, boolean, java.util.List, java.util.List, int, boolean, java.util.List, boolean, boolean):boolean");
    }

    private void calculateBlockColors(Level world, LevelChunk bchunk, int insideX, int insideZ, BlockPos.MutableBlockPos mutableBlockPos2, List<Integer> pixelTransparentSizes, List<BlockState> pixelBlockStates, List<Integer> pixelBlockLights, int loadingColours, int loadingLightOverlayColor, int loadingCaving, int loadingLevels, boolean loadingLighting, float loadingSingleLevelBrightness, boolean legibleCaveMaps) throws SilentException {
        int color;
        int firstBlockY = this.firstBlockY;
        BlockPos.MutableBlockPos globalPos = mutableBlockPos2.set((bchunk.getPos().x * 16) + insideX, firstBlockY, (bchunk.getPos().z * 16) + insideZ);
        if (!pixelTransparentSizes.isEmpty()) {
            for (int i = 0; i < pixelTransparentSizes.size(); i++) {
                BlockState state = pixelBlockStates.get(i);
                Block b = state.getBlock();
                int size = pixelTransparentSizes.get(i).intValue();
                int opacity = state.getLightBlock(bchunk.getLevel(), globalPos);
                applyTransparentLayer(world, bchunk, b, state, opacity * size, globalPos, pixelBlockLights.get(i).intValue(), loadingLighting, loadingSingleLevelBrightness, legibleCaveMaps);
                int nextY = globalPos.getY() - size;
                globalPos.setY(nextY);
            }
        }
        if (pixelBlockStates.size() > pixelTransparentSizes.size()) {
            BlockState state2 = pixelBlockStates.get(pixelBlockStates.size() - 1);
            Block b2 = state2.getBlock();
            if (loadingColours == 1) {
                MapColor minimapColor = state2.getMapColor(world, globalPos);
                color = minimapColor.col;
            } else {
                color = loadBlockColourFromTexture(world, state2, b2, globalPos, true);
            }
            this.blockColor = addBlockColorMultipliers(color, state2, world, globalPos);
            return;
        }
        this.blockColor = loadingCaving != Integer.MAX_VALUE ? 0 : VOID_COLOR;
    }

    private boolean isTransparent(StateHolder<?, ?> state) {
        return this.transparentCache.apply(state).booleanValue();
    }

    private boolean isGlowing(BlockState state, Level world, BlockPos pos) {
        Boolean cachedValue = this.glowingCache.get(state);
        if (cachedValue != null) {
            return cachedValue.booleanValue();
        }
        boolean isGlowing = false;
        try {
            isGlowing = getBlockStateLightEmission(state, world, pos) > 0;
        } catch (Exception e) {
        }
        this.glowingCache.put(state, Boolean.valueOf(isGlowing));
        return isGlowing;
    }

    private void applyTransparentLayer(Level world, LevelChunk bchunk, Block b, BlockState state, int opacity, BlockPos globalPos, int blockLight, boolean lighting, float loadingSingleLevelBrightness, boolean legibleCaveMaps) throws SilentException {
        int color;
        float vanillaTransparency = b instanceof LiquidBlock ? 0.75f : b instanceof IceBlock ? 0.85f : DEFAULT_AMBIENT_LIGHT_WHITE;
        if (this.loadingColours == 0) {
            color = loadBlockColourFromTexture(world, state, b, globalPos, true);
        } else {
            int color2 = state.getMapColor(world, globalPos).col;
            color = (((int) (255.0f * vanillaTransparency)) << 24) | (color2 & 16777215);
        }
        int color3 = addBlockColorMultipliers(color, state, world, globalPos);
        int red = (color3 >> 16) & 255;
        int green = (color3 >> 8) & 255;
        int blue = color3 & 255;
        float transparency = ((color3 >> 24) & 255) / 255.0f;
        if (transparency == 0.0f) {
            transparency = vanillaTransparency;
        }
        if (isGlowing(state, bchunk.getLevel(), globalPos)) {
            this.helper.getGlowingColour(red, green, blue, this.tempColor);
            red = this.tempColor[0];
            green = this.tempColor[1];
            blue = this.tempColor[2];
        }
        float brightness = legibleCaveMaps ? 1.0f : lighting ? getBlockBrightness(9.0f, this.sun, 0, blockLight) : getFixedSkyLightBlockBrightness(9.0f, loadingSingleLevelBrightness, blockLight);
        applyTransparentLayer(red, green, blue, transparency * brightness, false);
        this.sun -= opacity;
        if (this.sun < 0) {
            this.sun = 0;
        }
    }

    private void applyTransparentLayer(int red, int green, int blue, float transparency, boolean premultiplied) {
        float overlayIntensity = this.currentTransparencyMultiplier * (premultiplied ? 1.0f : transparency);
        for (int i = 0; i < this.loadingLevels; i++) {
            this.underRed[i] = (int) (r0[r1] + (red * overlayIntensity));
            this.underGreen[i] = (int) (r0[r1] + (green * overlayIntensity));
            this.underBlue[i] = (int) (r0[r1] + (blue * overlayIntensity));
        }
        this.currentTransparencyMultiplier *= 1.0f - transparency;
    }

    private int loadBlockColourFromTexture(Level world, BlockState state, Block b, BlockPos pos, boolean convert) throws SilentException {
        TextureAtlasSprite texture;
        int rgb;
        if (state == this.lastBlockStateForTextureColor) {
            return this.lastBlockStateForTextureColorResult;
        }
        int stateHash = Block.getId(state);
        Integer c = this.blockColours.get(Integer.valueOf(stateHash));
        int alpha = 0;
        if (c == null) {
            int tintIndex = -1;
            try {
                List<BakedQuad> upQuads = null;
                BlockModelShaper bms = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
                BakedModel model = bms.getBlockModel(state);
                if (convert) {
                    upQuads = getQuads(model, state, Direction.UP);
                }
                TextureAtlasSprite missingTexture = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(MissingTextureAtlasSprite.getLocation());
                if (upQuads == null || upQuads.isEmpty() || upQuads.get(0).getSprite() == missingTexture) {
                    texture = getParticleIcon(bms, model, state);
                    tintIndex = 0;
                    if (texture == missingTexture) {
                        for (int i = Direction.values().length - 1; i >= 0; i--) {
                            if (i != 1) {
                                List<BakedQuad> quads = getQuads(model, state, Direction.values()[i]);
                                if (!quads.isEmpty()) {
                                    texture = quads.get(0).getSprite();
                                    tintIndex = quads.get(0).getTintIndex();
                                    if (texture != missingTexture) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    texture = upQuads.get(0).getSprite();
                    tintIndex = upQuads.get(0).getTintIndex();
                }
            } catch (FileNotFoundException e) {
                if (convert) {
                    return loadBlockColourFromTexture(world, state, b, pos, false);
                }
                MinimapLogs.LOGGER.info("Block file not found: " + String.valueOf(((Registry) world.registryAccess().registry(Registries.BLOCK).get()).getKey(b)));
                c = 0;
                if (state != null && state.getMapColor(world, pos) != null) {
                    c = Integer.valueOf(state.getMapColor(world, pos).col);
                }
                if (0 != 0) {
                    this.textureColours.put(null, c);
                }
            } catch (Exception e2) {
                MinimapLogs.LOGGER.info("Exception when loading " + String.valueOf(((Registry) world.registryAccess().registry(Registries.BLOCK).get()).getKey(b)) + " texture, using material colour.");
                c = 0;
                if (state.getMapColor(world, pos) != null) {
                    c = Integer.valueOf(state.getMapColor(world, pos).col);
                }
                if (0 != 0) {
                    this.textureColours.put(null, c);
                }
                if (e2 instanceof SilentException) {
                    MinimapLogs.LOGGER.info(e2.getMessage());
                } else {
                    MinimapLogs.LOGGER.error("suppressed exception", e2);
                }
            }
            if (texture == null) {
                throw new SilentException("No texture for " + String.valueOf(state));
            }
            String name = String.valueOf(texture.contents().name()) + ".png";
            String[] args = name.split(":");
            if (args.length < 2) {
                args = new String[]{"minecraft", args[0]};
            }
            Integer cachedColour = this.textureColours.get(name);
            if (cachedColour == null) {
                ResourceLocation location = ResourceLocation.fromNamespaceAndPath(args[0], "textures/" + args[1]);
                Resource resource = (Resource) Minecraft.getInstance().getResourceManager().getResource(location).orElse(null);
                if (resource == null) {
                    throw new SilentException("No texture " + String.valueOf(location));
                }
                ImageInputStream input = ImageIO.createImageInputStream(resource.open());
                BufferedImage img = ImageIOUtils.getImageThroughZipError(input, location.toString());
                input.close();
                if (img == null) {
                    throw new SilentException("No image loaded " + String.valueOf(location));
                }
                int red = 0;
                int green = 0;
                int blue = 0;
                int total = 0;
                int ts = Math.min(img.getWidth(), img.getHeight());
                if (ts > 0) {
                    int diff = Math.max(1, Math.min(4, ts / 8));
                    int parts = ts / diff;
                    Raster raster = img.getData();
                    int[] colorHolder = null;
                    for (int i2 = 0; i2 < parts; i2++) {
                        for (int j = 0; j < parts; j++) {
                            if (img.getColorModel().getNumComponents() < 3) {
                                colorHolder = raster.getPixel(i2 * diff, j * diff, colorHolder);
                                int sample = colorHolder[0] & 255;
                                int a = 255;
                                if (colorHolder.length > 1) {
                                    a = colorHolder[1];
                                }
                                rgb = (a << 24) | (sample << 16) | (sample << 8) | sample;
                            } else {
                                rgb = img.getRGB(i2 * diff, j * diff);
                            }
                            int a2 = (rgb >> 24) & 255;
                            if (rgb != 0 && a2 != 0) {
                                red += (rgb >> 16) & 255;
                                green += (rgb >> 8) & 255;
                                blue += rgb & 255;
                                alpha += a2;
                                total++;
                            }
                        }
                    }
                }
                if (total == 0) {
                    total = 1;
                }
                int red2 = red / total;
                int green2 = green / total;
                int blue2 = blue / total;
                int alpha2 = alpha / total;
                if (convert && red2 == 0 && green2 == 0 && blue2 == 0) {
                    throw new SilentException("Black texture " + ts);
                }
                c = Integer.valueOf((alpha2 << 24) | (red2 << 16) | (green2 << 8) | blue2);
                this.textureColours.put(name, c);
            } else {
                c = cachedColour;
            }
            if (c != null) {
                this.blockColours.put(Integer.valueOf(stateHash), c);
                this.blockTintIndices.put(state, tintIndex);
            }
        }
        this.lastBlockStateForTextureColor = state;
        this.lastBlockStateForTextureColorResult = c.intValue();
        return c.intValue();
    }

    private int addBlockColorMultipliers(int c, BlockState state, Level world, BlockPos pos) {
        if (this.loadingColours == 1 && !this.loadingBiomesVanillaMode) {
            return c;
        }
        int grassColor = 16777215;
        try {
            grassColor = Minecraft.getInstance().getBlockColors().getColor(state, this.biomeBlendCalculator, pos, this.blockTintIndices.getInt(state));
        } catch (Throwable t) {
            MinimapLogs.LOGGER.error("suppressed exception", t);
        }
        if (grassColor != -1 && grassColor != 16777215) {
            float rMultiplier = ((c >> 16) & 255) / 255.0f;
            float gMultiplier = ((c >> 8) & 255) / 255.0f;
            float bMultiplier = (c & 255) / 255.0f;
            int red = (int) (((grassColor >> 16) & 255) * rMultiplier);
            int green = (int) (((grassColor >> 8) & 255) * gMultiplier);
            int blue = (int) ((grassColor & 255) * bMultiplier);
            c = (c & (-16777216)) | (red << 16) | (green << 8) | blue;
        }
        return c;
    }

    private boolean ignoreWorld(Level world) {
        for (int i = 0; i < dimensionsToIgnore.length; i++) {
            if (dimensionsToIgnore[i].equals(world.dimension().location().getPath())) {
                return true;
            }
        }
        return false;
    }

    private int getCaving(double playerX, double playerY, double playerZ, Level world) {
        boolean caveModeConfig = MinimapConfigClientUtils.getEffectiveCaveModeAllowed();
        if (!caveModeConfig) {
            return NO_Y_VALUE;
        }
        boolean manualCaveMode = this.minimapSession.getProcessor().isManualCaveMode();
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        int autoCaveModeConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.AUTO_CAVE_MODE)).intValue();
        if (autoCaveModeConfig == 0 && !manualCaveMode) {
            return NO_Y_VALUE;
        }
        if (ignoreWorld(world)) {
            return this.lastCaving;
        }
        if (manualCaveMode) {
            int customCaveStart = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START)).intValue();
            if (customCaveStart != Integer.MAX_VALUE) {
                return customCaveStart;
            }
        } else if (this.modMain.getSupportMods().worldmap() && this.modMain.getSupportMods().worldmapSupport.shouldPreventAutoCaveMode(world)) {
            return NO_Y_VALUE;
        }
        int worldBottomY = world.getMinBuildHeight();
        int worldTopY = world.getMaxBuildHeight() - 1;
        int y = ((int) playerY) + 1;
        int defaultCaveStart = y + 3;
        int defaultResult = manualCaveMode ? defaultCaveStart : NO_Y_VALUE;
        if (y > worldTopY || y < worldBottomY) {
            return defaultResult;
        }
        int x = OptimizedMath.myFloor(playerX);
        int z = OptimizedMath.myFloor(playerZ);
        int roofRadius = autoCaveModeConfig - 1;
        int roofDiameter = 1 + (roofRadius * 2);
        int startX = x - roofRadius;
        int startZ = z - roofRadius;
        boolean ignoringHeightmaps = this.modMain.getSettings().isIgnoreHeightmaps();
        int bottom = y;
        int top = Integer.MAX_VALUE;
        LevelChunk levelChunk = null;
        int potentialResult = defaultCaveStart;
        for (int o = 0; o < roofDiameter; o++) {
            for (int p = 0; p < roofDiameter; p++) {
                int currentX = startX + o;
                int currentZ = startZ + p;
                this.mutableBlockPos.set(currentX, y, currentZ);
                LevelChunk chunk = world.getChunk(currentX >> 4, currentZ >> 4);
                if (chunk != null) {
                    int skyLight = world.getBrightness(LightLayer.SKY, this.mutableBlockPos);
                    if (!ignoringHeightmaps) {
                        if (skyLight < 15) {
                            int insideX = currentX & 15;
                            int insideZ = currentZ & 15;
                            top = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, insideX, insideZ);
                        } else {
                            return defaultResult;
                        }
                    } else if (chunk != levelChunk) {
                        LevelChunkSection[] sections = chunk.getSections();
                        if (sections.length == 0) {
                            return defaultResult;
                        }
                        int playerSection = (y - worldBottomY) >> 4;
                        boolean foundSomething = false;
                        for (int i = playerSection; i < sections.length; i++) {
                            LevelChunkSection searchedSection = sections[i];
                            if (!searchedSection.hasOnlyAir()) {
                                if (!foundSomething) {
                                    bottom = Math.max(bottom, worldBottomY + (i << 4));
                                    foundSomething = true;
                                }
                                top = worldBottomY + (i << 4) + 15;
                            }
                        }
                        if (!foundSomething) {
                            return defaultResult;
                        }
                        levelChunk = chunk;
                    }
                    if (top < worldBottomY) {
                        return defaultResult;
                    }
                    if (top > worldTopY) {
                        top = worldTopY;
                    }
                    for (int i2 = bottom; i2 <= top; i2++) {
                        this.mutableBlockPos.setY(i2);
                        BlockState state = world.getBlockState(this.mutableBlockPos);
                        if (!state.isAir() && state.getPistonPushReaction() != PushReaction.DESTROY && !(state.getBlock() instanceof LiquidBlock) && !state.is(BlockTags.LEAVES) && !isTransparent(state) && state.getBlock() != Blocks.BARRIER) {
                            if (o == p && o == roofRadius) {
                                potentialResult = Math.min(i2, defaultCaveStart);
                            }
                        }
                    }
                    return defaultResult;
                }
                return defaultResult;
            }
        }
        int i3 = potentialResult;
        this.lastCaving = i3;
        return i3;
    }

    public int getSectionBasedHeight(LevelChunk bchunk, int startY) {
        LevelChunkSection[] sections = bchunk.getSections();
        if (sections.length == 0) {
            return 0;
        }
        int chunkBottomY = bchunk.getMinBuildHeight();
        int playerSection = Math.min((startY - chunkBottomY) >> 4, sections.length - 1);
        if (playerSection < 0) {
            playerSection = 0;
        }
        int result = 0;
        for (int i = playerSection; i < sections.length; i++) {
            LevelChunkSection searchedSection = sections[i];
            if (!searchedSection.hasOnlyAir()) {
                result = chunkBottomY + (i << 4) + 15;
            }
        }
        if (playerSection > 0 && result == 0) {
            int i2 = playerSection - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                LevelChunkSection searchedSection2 = sections[i2];
                if (!searchedSection2.hasOnlyAir()) {
                    result = chunkBottomY + (i2 << 4) + 15;
                    break;
                }
                i2--;
            }
        }
        return result;
    }

    public int getLoadSide() {
        return 9;
    }

    public int getUpdateRadiusInChunks() {
        return (int) Math.ceil((this.loadingSideInChunks / 2.0d) / this.minimapSession.getProcessor().getMinimapZoom());
    }

    public int getMapCoord(int side, int coord) {
        return (coord >> 6) - (side / 2);
    }

    public int getLoadedCaving() {
        return this.loadedCaving;
    }

    private boolean notEmptyColor(int[] red, int[] green, int[] blue) {
        return (red[0] == 0 && green[0] == 0 && blue[0] == 0) ? false : true;
    }

    public float getFixedSkyLightBlockBrightness(float min, float fixedSun, int blockLight) {
        return (min + Math.max(fixedSun * 15.0f, blockLight)) / (15.0f + min);
    }

    public float getBlockBrightness(float min, int sun, int lightLevel, int blockLight) {
        return (min + Math.max(((lightLevel == -1 || lightLevel == 0) ? 1.0f : ((this.loadingLevels - 1.0f) - lightLevel) / (this.loadingLevels - 1.0f)) * sun, blockLight)) / (15.0f + min);
    }

    public int getLoadingMapChunkX() {
        return this.loadingMapChunkX;
    }

    public int getLoadingMapChunkZ() {
        return this.loadingMapChunkZ;
    }

    public int getLoadingSideInChunks() {
        return this.loadingSideInChunks;
    }

    public MinimapChunk[][] getLoadedBlocks() {
        return this.loadedBlocks;
    }

    public int getLoadedMapChunkZ() {
        return this.loadedMapChunkZ;
    }

    public int getLoadedMapChunkX() {
        return this.loadedMapChunkX;
    }

    public int getLoadedLevels() {
        return this.loadedLevels;
    }

    public void setClearBlockColours(boolean clearBlockColours) {
        this.clearBlockColours = clearBlockColours;
    }

    public void cleanup() {
        if (this.loadedBlocks != null) {
            for (int i = 0; i < this.loadedBlocks.length; i++) {
                for (int j = 0; j < this.loadedBlocks.length; j++) {
                    MinimapChunk m = this.loadedBlocks[i][j];
                    if (m != null) {
                        m.cleanup(this.minimapInterface);
                    }
                }
            }
        }
    }

    public void resetShortBlocks() {
        this.blockStateShortShapeCache.reset();
    }

    public DimensionHighlighterHandler getDimensionHighlightHandler() {
        return this.dimensionHighlightHandler;
    }

    public int getLoadedSideInChunks() {
        return this.loadedSideInChunks;
    }

    public boolean isLoadedNonWorldMap() {
        return this.loadedNonWorldMap;
    }
}
