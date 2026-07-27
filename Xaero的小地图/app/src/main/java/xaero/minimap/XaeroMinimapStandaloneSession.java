package xaero.minimap;

import java.io.IOException;
import net.minecraft.client.multiplayer.ClientPacketListener;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.minimap.controls.MinimapControlsHandler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/minimap/XaeroMinimapStandaloneSession.class */
public class XaeroMinimapStandaloneSession extends XaeroMinimapSession {
    public XaeroMinimapStandaloneSession(HudMod modMain) {
        super(modMain);
    }

    @Override // xaero.common.XaeroMinimapSession, xaero.hud.HudSession
    public void init(ClientPacketListener connection) throws IOException {
        super.init(connection);
        this.controls = new MinimapControlsHandler(this.modMain, this);
    }
}
