package xaero.common.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiClearSet.class */
public class GuiClearSet extends ConfirmScreen {
    public GuiClearSet(String setName, XaeroPath worldPath, String name, GuiWaypoints parent, Screen escapeScreen, IXaeroMinimap modMain, MinimapSession session) {
        super(result -> {
            confirmClearSet(result, worldPath, name, parent, escapeScreen, modMain, session);
        }, Component.literal(I18n.get("gui.xaero_clear_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?"), Component.translatable("gui.xaero_clear_set_message2"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void confirmClearSet(boolean p_confirmResult_1_, XaeroPath worldPath, String name, GuiWaypoints parent, Screen escapeScreen, IXaeroMinimap modMain, MinimapSession session) {
        if (p_confirmResult_1_) {
            WaypointSet set = session.getWorldManager().getWorld(worldPath).getWaypointSet(name);
            if (set != null) {
                set.clear();
            }
            try {
                session.getWorldManagerIO().saveWorld(session.getWorldManager().getWorld(worldPath));
            } catch (IOException e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
            }
        }
        Minecraft.getInstance().setScreen(new GuiWaypoints((HudMod) modMain, session, parent.parent, escapeScreen));
    }
}
