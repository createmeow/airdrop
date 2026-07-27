package xaero.common;

import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.events.ClientEvents;
import xaero.common.events.CommonEvents;
import xaero.common.events.ModClientEvents;
import xaero.common.events.ModCommonEvents;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.write.MinimapWriter;
import xaero.common.mods.SupportMods;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/PlatformContext.class */
public abstract class PlatformContext {
    public abstract ClientEvents createClientEvents(HudMod hudMod);

    public abstract CommonEvents createCommonEvents(HudMod hudMod);

    public abstract PlatformContextLoaderClientOnly getLoaderClientOnly();

    public abstract PlatformContextLoaderCommon getLoaderCommon();

    public abstract ModClientEvents createModClientEvents(IXaeroMinimap iXaeroMinimap);

    public abstract SupportMods createSupportMods(IXaeroMinimap iXaeroMinimap);

    public abstract ModCommonEvents createModCommonEvents(IXaeroMinimap iXaeroMinimap);

    public abstract MinimapWriter createMinimapWriter(IXaeroMinimap iXaeroMinimap, MinimapSession minimapSession, BlockStateShortShapeCache blockStateShortShapeCache, HighlighterRegistry highlighterRegistry);

    public abstract String getModInfoVersion();
}
