package xaero.hud.minimap.config.util;

import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.misc.Misc;
import xaero.hud.gui.util.GuiUtils;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.IndexedConfigOption;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/config/util/MinimapConfigClientUtils.class */
public class MinimapConfigClientUtils {
    public static float getUIScale(ClientConfigManager configManager, IndexedConfigOption<Integer> option) {
        List<Integer> validValues = option.getValidValues();
        return getUIScale(configManager, option, validValues.get(0).intValue(), validValues.get(validValues.size() - 1).intValue(), 1.0d);
    }

    public static float getUIScale(ClientConfigManager configManager, IndexedConfigOption<Integer> option, double autoScale) {
        List<Integer> validValues = option.getValidValues();
        return getUIScale(configManager, option, validValues.get(0).intValue(), validValues.get(validValues.size() - 1).intValue(), autoScale);
    }

    public static float getUIScale(ClientConfigManager configManager, ConfigOption<Integer> option, int auto, int max, double autoScale) {
        int configValue = ((Integer) configManager.getEffective(option)).intValue();
        float configBasedScale = GuiUtils.getUIScale(configValue, auto, max);
        if (configValue == auto && autoScale != 1.0d) {
            return (float) Math.ceil(configBasedScale * autoScale);
        }
        return configBasedScale;
    }

    public static double getWaypointsClampDepth(ClientConfigManager configManager, double fov, int height) {
        int baseIconScale = (int) getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_IN_WORLD);
        double ingameCloseScale = ((Double) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_CLOSE_SCALE_IN_WORLD)).doubleValue();
        double frameSizeAtClampDepth = ((ingameCloseScale * 0.02133333496749401d) * height) / baseIconScale;
        double fovMultiplier = 2.0d * Math.tan(Math.toRadians(fov / 2.0d));
        return frameSizeAtClampDepth / fovMultiplier;
    }

    public static void addAutoUIScaleValueToComponent(MutableComponent component, double autoScale) {
        component.getSiblings().add(Component.literal(" (" + ((int) Math.ceil(GuiUtils.getAutoUIScale() * autoScale)) + ")"));
    }

    public static void addAutoMCScaleValueToComponent(MutableComponent component) {
        component.getSiblings().add(Component.literal(" (" + ((int) Minecraft.getInstance().getWindow().getGuiScale()) + ")"));
    }

    public static int getAutoMinimapSize() {
        int height = Minecraft.getInstance().getWindow().getHeight();
        int width = Minecraft.getInstance().getWindow().getWidth();
        int size = (int) ((height <= width ? height : width) / GuiUtils.getMinimapScale(HudMod.INSTANCE.getHudConfigs().getClientConfigManager()));
        return Math.min(Math.max(55, ((2 * size) * 130) / 1080), 250);
    }

    public static int getEffectiveMinimapSize() {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        int minimapSizeConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.SIZE)).intValue();
        if (minimapSizeConfig > 0) {
            return minimapSizeConfig;
        }
        return getAutoMinimapSize();
    }

    public static boolean getEffectiveNorthLocked(int mapSize, int shape) {
        if (mapSize > 180 && shape == 0) {
            return true;
        }
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean northLockedConfig = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.NORTH_LOCKED)).booleanValue();
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession == null) {
            return northLockedConfig;
        }
        return northLockedConfig || (!((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.KEEP_ENLARGED_UNLOCKED)).booleanValue() && minimapSession.getProcessor().isEnlargedMap());
    }

    private static void changeZoomUnchecked(ClientConfigManager configManager, int direction) {
        ConfigProfile currentProfile = configManager.getCurrentProfile();
        currentProfile.set(MinimapProfiledConfigOptions.ZOOM, Integer.valueOf(1 + Mth.positiveModulo((((Integer) currentProfile.get(MinimapProfiledConfigOptions.ZOOM)).intValue() - 1) + direction, 5)));
    }

    public static void changeZoom(int direction) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession == null) {
            changeZoomUnchecked(configManager, direction);
            return;
        }
        double targetBefore = minimapSession.getProcessor().getTargetZoom();
        int attempts = 0;
        do {
            changeZoomUnchecked(configManager, direction);
            attempts++;
            if (attempts >= 5) {
                break;
            }
        } while (targetBefore == minimapSession.getProcessor().getTargetZoom());
        if (attempts == 5) {
            changeZoomUnchecked(configManager, direction);
        }
    }

    public static boolean getEffectiveSlimeChunks(MinimapSession session) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean slimeChunksConfig = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.SLIME_CHUNKS)).booleanValue();
        if (session == null) {
            return slimeChunksConfig;
        }
        if (!slimeChunksConfig) {
            return false;
        }
        if (Minecraft.getInstance().getSingleplayerServer() != null) {
            return true;
        }
        MinimapWorld autoWorld = session.getWorldManager().getAutoWorld();
        return (autoWorld == null || autoWorld.getSlimeChunkSeed() == null) ? false : true;
    }

    public static Long getEffectiveSlimeChunksSeed(MinimapWorld currentWorld) {
        IntegratedServer singleplayerServer = Minecraft.getInstance().getSingleplayerServer();
        if (singleplayerServer != null) {
            if (Minecraft.getInstance().level.dimension() != Level.OVERWORLD) {
                return null;
            }
            return Long.valueOf(singleplayerServer.getLevel(Level.OVERWORLD).getSeed());
        }
        if (currentWorld == null) {
            return null;
        }
        return currentWorld.getSlimeChunkSeed();
    }

    public static boolean isFairPlayForCaveMode() {
        MinimapSession session;
        boolean fairplay = HudMod.INSTANCE.isFairPlay();
        if (!fairplay) {
            return false;
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || (session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()) == null) {
            return true;
        }
        MinimapProcessor processor = session.getProcessor();
        return (processor.isConsideringNetherFairPlayMessage() && level.dimension() == Level.NETHER) ? false : true;
    }

    public static boolean hasNoCaveModeEffect() {
        Minecraft mc = Minecraft.getInstance();
        return Misc.hasEffect(mc.player, Effects.NO_CAVE_MAPS) || Misc.hasEffect(mc.player, Effects.NO_CAVE_MAPS_HARMFUL);
    }

    public static boolean getEffectiveCaveModeAllowed() {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        if (!((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED)).booleanValue()) {
            return false;
        }
        Set<ResourceLocation> allowedDimensionsLocal = (Set) configManager.getEffective(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED_DIMENSIONS);
        Set<ResourceLocation> allowedDimensionsServer = (Set) configManager.getServerSynced().getEffective(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED_DIMENSIONS);
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) {
            return allowedDimensionsLocal.isEmpty() && allowedDimensionsServer.isEmpty();
        }
        ResourceLocation currentDimensionId = clientLevel.dimension().location();
        if (allowedDimensionsLocal.isEmpty() || allowedDimensionsLocal.contains(currentDimensionId)) {
            return allowedDimensionsServer == null || allowedDimensionsServer.isEmpty() || allowedDimensionsServer.contains(currentDimensionId);
        }
        return false;
    }
}
