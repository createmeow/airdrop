package xaero.hud.event;

import xaero.common.XaeroMinimapSession;
import xaero.hud.Hud;
import xaero.hud.module.HudModule;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/event/HudEventHandler.class */
public class HudEventHandler {
    private Hud hud;

    public void setHud(Hud hud) {
        if (this.hud != null) {
            throw new IllegalStateException();
        }
        this.hud = hud;
    }

    public void handleRenderGameOverlayEventPost() {
        if (XaeroMinimapSession.getCurrentSession() == null) {
            return;
        }
        for (HudModule<?> module : this.hud.getModuleManager().getModules()) {
            module.getCurrentSession().onPostGameOverlay();
        }
    }
}
