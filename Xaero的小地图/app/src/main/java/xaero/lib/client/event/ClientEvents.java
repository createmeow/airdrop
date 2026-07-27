package xaero.lib.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import xaero.lib.client.gui.GuiUpdateAll;
import xaero.lib.patreon.Patreon;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/event/ClientEvents.class */
public class ClientEvents {
    public void handleDrawScreenEventPost(Screen gui) {
        if (Patreon.needsNotification() && (gui instanceof TitleScreen)) {
            Minecraft.getInstance().setScreen(new GuiUpdateAll());
        }
    }

    public void onRenderTick() {
    }
}
