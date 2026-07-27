package xaero.common.mods.pac.party;

import java.util.Iterator;
import xaero.common.mods.pac.SupportOpenPartiesAndClaims;
import xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker;
import xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mods/pac/party/OPACPlayerTrackerSystem.class */
public class OPACPlayerTrackerSystem implements IRenderedPlayerTracker<IPartyMemberDynamicInfoSyncableAPI> {
    private final SupportOpenPartiesAndClaims opac;
    private final OPACTrackedPlayerReader reader = new OPACTrackedPlayerReader();

    public OPACPlayerTrackerSystem(SupportOpenPartiesAndClaims opac) {
        this.opac = opac;
    }

    @Override // xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker
    public ITrackedPlayerReader<IPartyMemberDynamicInfoSyncableAPI> getReader() {
        return this.reader;
    }

    @Override // xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker
    public Iterator<IPartyMemberDynamicInfoSyncableAPI> getTrackedPlayerIterator() {
        return this.opac.getAllyIterator();
    }
}
