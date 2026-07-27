package xaero.common;

import java.io.IOException;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.waypoints.WaypointSharingHandler;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.HudSession;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/XaeroMinimapSession.class */
public class XaeroMinimapSession extends HudSession {
    public XaeroMinimapSession(HudMod modMain) {
        super(modMain);
    }

    @Override // xaero.hud.HudSession
    public void init(ClientPacketListener connection) throws IOException {
        super.init(connection);
    }

    @Override // xaero.hud.HudSession
    protected void cleanup() {
        super.cleanup();
    }

    public WaypointsManager getWaypointsManager() {
        return (WaypointsManager) BuiltInHudModules.MINIMAP.getCurrentSession();
    }

    public WaypointSharingHandler getWaypointSharing() {
        return (WaypointSharingHandler) ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getWaypointSession().getSharing();
    }

    public MinimapProcessor getMinimapProcessor() {
        return ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getProcessor();
    }

    public static XaeroMinimapSession getCurrentSession() {
        return (XaeroMinimapSession) HudSession.getCurrentSession();
    }

    public static XaeroMinimapSession getForPlayer(LocalPlayer player) {
        return (XaeroMinimapSession) HudSession.getForPlayer(player);
    }

    public IXaeroMinimap getModMain() {
        return getHudMod();
    }
}
