package xaero.common.mods.pac;

import net.minecraft.resources.ResourceLocation;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.highlight.DimensionHighlighterHandler;
import xaero.common.minimap.write.MinimapWriter;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.tracker.api.IClaimsManagerListenerAPI;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/pac/ClientClaimChangeListener.class */
public class ClientClaimChangeListener implements IClaimsManagerListenerAPI {
    public void onWholeRegionChange(ResourceLocation dimension, int regionX, int regionZ) {
        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
        MinimapWriter write = minimapSession.getMinimapProcessor().getMinimapWriter();
        DimensionHighlighterHandler dimHighlightHandler = write.getDimensionHighlightHandler();
        if (dimHighlightHandler != null) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if ((i == 0 && j == 0) || i * i != j * j) {
                        dimHighlightHandler.requestRefresh(regionX + i, regionZ + j);
                    }
                }
            }
        }
    }

    public void onChunkChange(ResourceLocation dimension, int chunkX, int chunkZ, IPlayerChunkClaimAPI claim) {
        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
        MinimapWriter write = minimapSession.getMinimapProcessor().getMinimapWriter();
        DimensionHighlighterHandler dimHighlightHandler = write.getDimensionHighlightHandler();
        if (dimHighlightHandler != null) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if ((i == 0 && j == 0) || i * i != j * j) {
                        dimHighlightHandler.requestRefresh((chunkX + i) >> 5, (chunkZ + j) >> 5);
                    }
                }
            }
        }
    }

    public void onDimensionChange(ResourceLocation dimension) {
        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
        MinimapWriter write = minimapSession.getMinimapProcessor().getMinimapWriter();
        DimensionHighlighterHandler dimHighlightHandler = write.getDimensionHighlightHandler();
        if (dimHighlightHandler != null) {
            dimHighlightHandler.requestRefresh();
        }
    }
}
