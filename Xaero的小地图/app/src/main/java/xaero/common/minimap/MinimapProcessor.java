package xaero.common.minimap;

import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.anim.MultiplyAnimationHelper;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.minimap.mcworld.MinimapClientWorldData;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.write.MinimapWriter;
import xaero.common.misc.Misc;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.player.tracker.synced.ClientSyncedTrackedPlayerManager;
import xaero.hud.minimap.radar.RadarSession;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/MinimapProcessor.class */
public class MinimapProcessor {
    public static final boolean DEBUG = false;
    public static final int FRAME = 9;
    private IXaeroMinimap modMain;
    private MinimapSession minimapSession;
    private MinimapWriter minimapWriter;
    private RadarSession radarSession;
    private MinimapInterface minimapInterface;
    private EntityRadarCategoryManager entityCategoryManager;
    private ClientSyncedTrackedPlayerManager syncedTrackedPlayerManager;
    private boolean enlargedMap;
    private boolean manualCaveMode;
    private boolean noMinimapMessageReceived;
    private boolean fairPlayOnlyMessageReceived;
    private boolean consideringNetherFairPlayMessage;
    private ResourceKey<Level> lastMapDimension;
    private Item minimapItem;
    private double lastMapDimensionScale = 1.0d;
    private double lastPlayerDimDiv = 1.0d;
    private double minimapZoom = 1.0d;
    private boolean toResetImage = true;

    public MinimapProcessor(IXaeroMinimap modMain, MinimapSession minimapSession, MinimapWriter minimapWriter, RadarSession radarSession, ClientSyncedTrackedPlayerManager syncedTrackedPlayerManager) {
        this.modMain = modMain;
        this.minimapSession = minimapSession;
        this.minimapWriter = minimapWriter;
        this.radarSession = radarSession;
        this.minimapInterface = modMain.getInterfaces().getMinimapInterface();
        this.syncedTrackedPlayerManager = syncedTrackedPlayerManager;
        updateMinimapItem();
    }

    public int getMinimapSize() {
        int minimapSizeConfig = MinimapConfigClientUtils.getEffectiveMinimapSize();
        return this.enlargedMap ? RadarIconCreator.FAR_PLANE : minimapSizeConfig * 2;
    }

    public int getMinimapBufferSize(int minimapSize) {
        int bufferSize = 128 * ((int) Math.pow(2.0d, Math.ceil(Math.log(minimapSize / 128.0d) / Math.log(2.0d))));
        if (bufferSize < 128) {
            return 128;
        }
        if (bufferSize > 512) {
            return 512;
        }
        return bufferSize;
    }

    public boolean isEnlargedMap() {
        return this.enlargedMap;
    }

    public void setEnlargedMap(boolean enlargedMap) {
        this.enlargedMap = enlargedMap;
    }

    public double getMinimapZoom() {
        return this.minimapZoom;
    }

    public boolean isCaveModeDisplayed() {
        return this.minimapWriter.getLoadedCaving() != Integer.MAX_VALUE;
    }

    public double getTargetZoom() {
        int enlargedZoomConfig;
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        float target = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.ZOOM)).intValue();
        if (this.enlargedMap && (enlargedZoomConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.ZOOM_ENLARGED)).intValue()) > 0) {
            target = enlargedZoomConfig;
        }
        if (isCaveModeDisplayed()) {
            target *= ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.CAVE_ZOOM)).intValue();
        }
        if (target > 5.0f) {
            target = 5.0f;
        }
        return target;
    }

    public void instantZoom() {
        this.minimapZoom = getTargetZoom();
    }

    public void updateZoom() {
        double off;
        double target = getTargetZoom();
        double off2 = target - this.minimapZoom;
        if (off2 > 0.01d || off2 < -0.01d) {
            off = (float) MultiplyAnimationHelper.animate(off2, 0.8d);
        } else {
            off = 0.0d;
        }
        this.minimapZoom = target - off;
    }

    public MinimapWriter getMinimapWriter() {
        return this.minimapWriter;
    }

    public boolean canUseFrameBuffer() {
        return true;
    }

    public int getFBOBufferSize() {
        return 512;
    }

    public void onClientTick() {
        Level world = null;
        Player player = Minecraft.getInstance().player;
        if (player != null && (player.level() instanceof ClientLevel)) {
            world = player.level();
        }
        Entity renderEntity = Minecraft.getInstance().getCameraEntity();
        this.radarSession.update((ClientLevel) world, renderEntity, player);
    }

    public void onPlayerTick() {
    }

    public void checkFBO() {
        if (this.minimapInterface.getMinimapFBORenderer().isLoadedFBO() && !canUseFrameBuffer()) {
            this.minimapInterface.getMinimapFBORenderer().setLoadedFBO(false);
            this.minimapInterface.getMinimapFBORenderer().deleteFramebuffers();
            this.toResetImage = true;
        }
        boolean mapSafeMode = ((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.SAFE_MODE)).booleanValue();
        if (this.minimapInterface.getMinimapFBORenderer().isLoadedFBO() || mapSafeMode || this.minimapInterface.getMinimapFBORenderer().isTriedFBO() || Minecraft.getInstance().getOverlay() != null) {
            return;
        }
        this.minimapInterface.getMinimapFBORenderer().loadFrameBuffer(this);
    }

    public void onRender(GuiGraphics guiGraphics, int x, int y, int width, int height, double scale, int size, int boxSize, float partial, CustomVertexConsumers cvc) {
        try {
            if (this.enlargedMap && ((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.CENTERED_ENLARGED)).booleanValue()) {
                x = (width - boxSize) / 2;
                y = (height - boxSize) / 2;
            }
            if (this.minimapInterface.usingFBO()) {
                this.minimapInterface.getMinimapFBORenderer().renderMinimap(this.minimapSession, guiGraphics, this, x, y, width, height, scale, size, partial, cvc);
            } else {
                this.minimapInterface.getMinimapSafeModeRenderer().renderMinimap(this.minimapSession, guiGraphics, this, x, y, width, height, scale, size, partial, cvc);
            }
        } catch (Throwable e) {
            this.minimapInterface.setCrashedWith(e);
        }
    }

    public static boolean hasMinimapItem(Player player) {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (session == null) {
            return true;
        }
        MinimapProcessor processor = session.getProcessor();
        return processor.minimapItem == null || Misc.hasItem(player, processor.minimapItem);
    }

    public boolean isToResetImage() {
        return this.toResetImage;
    }

    public void setToResetImage(boolean toResetImage) {
        this.toResetImage = toResetImage;
    }

    @Deprecated
    public MinimapRadar getEntityRadar() {
        return (MinimapRadar) this.radarSession;
    }

    public RadarSession getRadarSession() {
        return this.radarSession;
    }

    public void cleanup() {
        this.minimapWriter.cleanup();
    }

    public boolean isManualCaveMode() {
        return this.manualCaveMode || (this.modMain.getSupportMods().shouldUseWorldMapCaveChunks() && this.modMain.getSupportMods().worldmapSupport.getManualCaveStart() != Integer.MAX_VALUE);
    }

    public void toggleManualCaveMode() {
        this.manualCaveMode = !isManualCaveMode();
    }

    public MinimapInterface getMinimapInterface() {
        return this.minimapInterface;
    }

    public boolean getNoMinimapMessageReceived() {
        if (HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getServerSynced().isChannelPresentOnServer()) {
            return false;
        }
        return this.noMinimapMessageReceived;
    }

    public void setNoMinimapMessageReceived(boolean noMinimapMessageReceived) {
        this.noMinimapMessageReceived = noMinimapMessageReceived;
    }

    public boolean getForcedFairPlay() {
        return this.fairPlayOnlyMessageReceived;
    }

    public void setFairPlayOnlyMessageReceived(boolean fairPlayOnlyMessageReceived) {
        this.fairPlayOnlyMessageReceived = fairPlayOnlyMessageReceived;
    }

    @Deprecated
    public xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager getClientSyncedTrackedPlayerManager() {
        return (xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager) this.syncedTrackedPlayerManager;
    }

    public ClientSyncedTrackedPlayerManager getSyncedTrackedPlayerManager() {
        return this.syncedTrackedPlayerManager;
    }

    public boolean serverHasMod() {
        MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
        return (worldData == null || worldData.serverLevelId == null) ? false : true;
    }

    public void setServerModNetworkVersion(int networkVersion) {
        MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
        if (worldData == null) {
            return;
        }
        worldData.setServerModNetworkVersion(networkVersion);
    }

    public int getServerModNetworkVersion() {
        MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
        if (worldData == null) {
            return 0;
        }
        return worldData.getServerModNetworkVersion();
    }

    public double getLastMapDimensionScale() {
        return this.lastMapDimensionScale;
    }

    public void setLastMapDimensionScale(double lastMapDimensionScale) {
        this.lastMapDimensionScale = lastMapDimensionScale;
    }

    public ResourceKey<Level> getLastMapDimension() {
        return this.lastMapDimension;
    }

    public void setLastMapDimension(ResourceKey<Level> lastMapDimension) {
        this.lastMapDimension = lastMapDimension;
    }

    @Deprecated
    public double getLastPlayerDimDiv() {
        return this.lastPlayerDimDiv;
    }

    @Deprecated
    public void setLastPlayerDimDiv(double lastPlayerDimDiv) {
        this.lastPlayerDimDiv = lastPlayerDimDiv;
    }

    public MinimapSession getSession() {
        return this.minimapSession;
    }

    public void updateMinimapItem() {
        String minimapItemString = ((String) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.MINIMAP_ITEM)).trim();
        if (minimapItemString.isEmpty() || minimapItemString.equals("-")) {
            this.minimapItem = null;
            MinimapLogs.LOGGER.info("Minimap required item set to nothing.");
            return;
        }
        try {
            ResourceLocation minimapItemRL = ResourceLocation.parse(minimapItemString);
            this.minimapItem = (Item) BuiltInRegistries.ITEM.get(minimapItemRL);
            if (this.minimapItem == Items.AIR) {
                this.minimapItem = null;
                MinimapLogs.LOGGER.error("Tried setting the minimap required item to an invalid ID: {}", minimapItemString);
            } else {
                MinimapLogs.LOGGER.info("Minimap item set: " + this.minimapItem.getDescription().getString());
            }
        } catch (ResourceLocationException rle) {
            this.minimapItem = null;
            MinimapLogs.LOGGER.error("Tried setting the minimap required item to a misformatted ID: {}; Error: {}", minimapItemString, rle.getMessage());
        }
    }

    public Item getMinimapItem() {
        return this.minimapItem;
    }

    public void setConsideringNetherFairPlayMessage(boolean consideringNetherFairPlay) {
        this.consideringNetherFairPlayMessage = consideringNetherFairPlay;
    }

    public boolean isConsideringNetherFairPlayMessage() {
        return this.consideringNetherFairPlayMessage;
    }
}
