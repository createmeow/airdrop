package xaero.common.mods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.mods.pac.SupportOpenPartiesAndClaims;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/SupportMods.class */
public abstract class SupportMods {
    public SupportXaeroWorldmap worldmapSupport;
    public SupportOpenPartiesAndClaims xaeroPac;
    private IXaeroMinimap modMain;
    public boolean optifine;
    public boolean vivecraft;
    public boolean iris;
    public boolean ftbTeams;
    public SupportIris supportIris;
    public SupportFramedBlocks supportFramedBlocks;

    public boolean worldmap() {
        return this.worldmapSupport != null;
    }

    public boolean pac() {
        return this.xaeroPac != null;
    }

    public boolean shouldUseWorldMapChunks() {
        return worldmap() && ((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.DISPLAY_WORLD_MAP_CHUNKS)).booleanValue();
    }

    public boolean shouldUseWorldMapCaveChunks() {
        return shouldUseWorldMapChunks() && this.worldmapSupport.caveLayersAreUsable();
    }

    public boolean framedBlocks() {
        return this.supportFramedBlocks != null;
    }

    public static void checkForMinimapDuplicates(String otherModMainClass) throws ClassNotFoundException {
        try {
            Class.forName(otherModMainClass);
            throw new RuntimeException("Better PVP contains Xaero's Minimap by default. Do not install Better PVP and Xaero's Minimap together!");
        } catch (ClassNotFoundException e) {
        }
    }

    public SupportMods(IXaeroMinimap modMain) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        this.worldmapSupport = null;
        this.modMain = modMain;
        try {
            Class.forName("xaero.map.WorldMap");
            this.worldmapSupport = new SupportXaeroWorldmap(modMain);
            MinimapLogs.LOGGER.info("Xaero's Minimap: World Map found!");
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("xaero.pac.OpenPartiesAndClaims");
            this.xaeroPac = new SupportOpenPartiesAndClaims(modMain);
            this.xaeroPac.register();
            MinimapLogs.LOGGER.info("Xaero's Minimap: Open Parties And Claims found!");
        } catch (ClassNotFoundException e2) {
        }
        try {
            Class.forName("optifine.Patcher");
            this.optifine = true;
            MinimapLogs.LOGGER.info("Optifine!");
        } catch (ClassNotFoundException e3) {
            this.optifine = false;
            MinimapLogs.LOGGER.info("No Optifine!");
        }
        try {
            Class.forName("org.vivecraft.api.VRData");
            this.vivecraft = true;
            try {
                Class<?> vrStateClass = Class.forName("org.vivecraft.VRState");
                Method checkVRMethod = vrStateClass.getDeclaredMethod("checkVR", new Class[0]);
                this.vivecraft = ((Boolean) checkVRMethod.invoke(null, new Object[0])).booleanValue();
            } catch (ClassNotFoundException e4) {
            } catch (IllegalAccessException e5) {
            } catch (IllegalArgumentException e6) {
            } catch (NoSuchMethodException e7) {
            } catch (InvocationTargetException e8) {
            }
        } catch (ClassNotFoundException e9) {
        }
        if (this.vivecraft) {
            MinimapLogs.LOGGER.info("Xaero's Minimap: Vivecraft!");
        } else {
            MinimapLogs.LOGGER.info("Xaero's Minimap: No Vivecraft!");
        }
        try {
            Class.forName("xfacthd.framedblocks.FramedBlocks");
            this.supportFramedBlocks = new SupportFramedBlocks();
            MinimapLogs.LOGGER.info("Xaero's Minimap: Framed Blocks found!");
        } catch (ClassNotFoundException e10) {
        }
        try {
            Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            this.supportIris = new SupportIris();
            this.iris = true;
            MinimapLogs.LOGGER.info("Xaero's Minimap: Iris found!");
        } catch (Exception e11) {
        }
    }
}
