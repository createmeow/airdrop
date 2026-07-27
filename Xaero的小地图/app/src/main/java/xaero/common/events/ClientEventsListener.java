package xaero.common.events;

import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import xaero.hud.HudSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/ClientEventsListener.class */
public class ClientEventsListener {
    public void playerTickPost(HudSession hudSession) {
    }

    public void clientTickPost(HudSession hudSession) {
    }

    public boolean handleRenderStatusEffectOverlay(GuiGraphics guiGraphics) {
        return false;
    }

    public boolean handleForceToggleKeyMapping(ToggleKeyMapping keyMapping) {
        return false;
    }
}
