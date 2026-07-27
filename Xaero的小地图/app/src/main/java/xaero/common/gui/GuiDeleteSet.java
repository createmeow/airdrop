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
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiDeleteSet.class */
public class GuiDeleteSet extends ConfirmScreen {
    public GuiDeleteSet(String setName, XaeroPath worldPath, String name, Screen parent, Screen escapeScreen, IXaeroMinimap modMain, MinimapSession session) {
        super(result -> {
            confirmDeleteSet(result, worldPath, name, parent, escapeScreen, modMain, session);
        }, Component.literal(I18n.get("gui.xaero_delete_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?"), Component.translatable("gui.xaero_delete_set_message2"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void confirmDeleteSet(boolean p_confirmResult_1_, XaeroPath worldPath, String name, Screen parent, Screen escapeScreen, IXaeroMinimap modMain, MinimapSession session) {
        if (p_confirmResult_1_) {
            MinimapWorldManager waypointsManager = session.getWorldManager();
            waypointsManager.getWorld(worldPath).removeWaypointSet(name);
            waypointsManager.getWorld(worldPath).setCurrentWaypointSetId("gui.xaero_default");
            try {
                session.getWorldManagerIO().saveWorld(waypointsManager.getWorld(worldPath));
            } catch (IOException e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
            }
            Minecraft.getInstance().setScreen(new GuiWaypoints((HudMod) modMain, session, ((GuiWaypoints) parent).parent, escapeScreen));
            return;
        }
        Minecraft.getInstance().setScreen(parent);
    }
}
