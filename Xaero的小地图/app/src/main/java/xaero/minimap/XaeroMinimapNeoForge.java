package xaero.minimap;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import xaero.common.PlatformContext;
import xaero.common.PlatformContextNeoForge;

@Mod(XaeroMinimap.MOD_ID)
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/minimap/XaeroMinimapNeoForge.class */
public class XaeroMinimapNeoForge extends XaeroMinimap {
    public static String fileLayoutID = "minimap_neoforge";
    private final PlatformContextNeoForge platformContextNeoForge = (PlatformContextNeoForge) this.platformContext;

    public XaeroMinimapNeoForge(IEventBus modEventBus) {
        this.platformContextNeoForge.registerEvents(modEventBus);
    }

    @Override // xaero.common.HudMod
    protected PlatformContext createPlatformContext() {
        return new PlatformContextNeoForge(this);
    }

    @Override // xaero.common.IXaeroMinimap
    public String getFileLayoutID() {
        return fileLayoutID;
    }
}
