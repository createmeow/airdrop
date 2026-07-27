package xaero.common.events;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import com.mojang.realmsclient.util.task.LongRunningTask;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.joml.Matrix4f;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.effect.Effects;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.GuiWaypoints;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.hud.HudSession;
import xaero.hud.controls.key.KeyMappingTickHandler;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.element.render.world.MinimapElementWorldRendererHandler;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.waypoint.WaypointTeleport;
import xaero.lib.common.reflection.util.ReflectionUtils;
import xaero.lib.patreon.Patreon;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/ClientEvents.class */
public class ClientEvents {
    protected HudMod modMain;
    private Screen lastGuiOpen;
    private Field realmsTaskField;
    private Field realmsTaskServerField;
    public RealmsServer latestRealm;

    public ClientEvents(HudMod modMain) {
        this.modMain = modMain;
    }

    public Screen handleGuiOpen(Screen gui) {
        if (!this.modMain.isFirstStageLoaded()) {
            return gui;
        }
        if ((gui instanceof TitleScreen) || (gui instanceof JoinMultiplayerScreen)) {
            this.modMain.getSettings().resetServerSettings();
        }
        Minecraft mc = Minecraft.getInstance();
        if (gui instanceof RealmsLongRunningMcoTaskScreen) {
            try {
                if (this.realmsTaskField == null) {
                    this.realmsTaskField = ReflectionUtils.getFieldReflection(RealmsLongRunningMcoTaskScreen.class, "queuedTasks", "field_46707", "Ljava/util/List;", "f_302752_");
                    this.realmsTaskField.setAccessible(true);
                }
                if (this.realmsTaskServerField == null) {
                    this.realmsTaskServerField = ReflectionUtils.getFieldReflection(GetServerDetailsTask.class, "server", "field_20224", "Lnet/minecraft/class_4877;", "f_90327_");
                    this.realmsTaskServerField.setAccessible(true);
                }
                RealmsLongRunningMcoTaskScreen realmsTaskScreen = (RealmsLongRunningMcoTaskScreen) gui;
                List<LongRunningTask> tasks = (List) this.realmsTaskField.get(realmsTaskScreen);
                for (LongRunningTask longRunningTask : tasks) {
                    if (longRunningTask instanceof GetServerDetailsTask) {
                        GetServerDetailsTask realmsTask = (GetServerDetailsTask) longRunningTask;
                        RealmsServer realm = (RealmsServer) this.realmsTaskServerField.get(realmsTask);
                        if (realm != null && (this.latestRealm == null || realm.id != this.latestRealm.id)) {
                            this.latestRealm = realm;
                        }
                    }
                }
            } catch (Exception e) {
                MinimapLogs.LOGGER.error("suppressed exception", e);
            }
        } else if (((gui instanceof GuiAddWaypoint) || (gui instanceof GuiWaypoints)) && (mc.player.hasEffect(Effects.NO_WAYPOINTS) || mc.player.hasEffect(Effects.NO_WAYPOINTS_HARMFUL))) {
            gui = null;
        }
        this.lastGuiOpen = gui;
        return gui;
    }

    public void handleRenderGameOverlayEventPre(GuiGraphics guiGraphics, float partialTicks) {
        if (Minecraft.getInstance().options.hideGui) {
            return;
        }
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession != null) {
            Window mainwindow = Minecraft.getInstance().getWindow();
            Matrix4f projectionMatrixBU = RenderSystem.getProjectionMatrix();
            VertexSorting vertexSortingBU = RenderSystem.getVertexSorting();
            Matrix4f ortho = new Matrix4f().setOrtho(0.0f, mainwindow.getWidth(), mainwindow.getHeight(), 0.0f, 1000.0f, 3000.0f);
            RenderSystem.setProjectionMatrix(ortho, VertexSorting.ORTHOGRAPHIC_Z);
            RenderSystem.getModelViewStack().pushMatrix();
            RenderSystem.getModelViewStack().identity();
            RenderSystem.applyModelViewMatrix();
            Minecraft mc = Minecraft.getInstance();
            Vec3 renderPos = mc.gameRenderer.getMainCamera().getPosition();
            MinimapElementWorldRendererHandler worldRendererHandler = HudMod.INSTANCE.getMinimap().getWorldRendererHandler();
            worldRendererHandler.prepareRender(XaeroMinimapCore.waypointsProjection, XaeroMinimapCore.waypointModelView);
            worldRendererHandler.render(guiGraphics, renderPos, partialTicks, null, mc.level.dimensionType().coordinateScale(), mc.level.dimension());
            RenderSystem.getModelViewStack().popMatrix();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setProjectionMatrix(projectionMatrixBU, vertexSortingBU);
        }
    }

    public void handleRenderGameOverlayEventPost() {
        if (!this.modMain.isLoadedClient()) {
            return;
        }
        this.modMain.getHud().getEventHandler().handleRenderGameOverlayEventPost();
    }

    public boolean handleClientSendChatEvent(String message) {
        if (message.startsWith("xaero_waypoint_add:")) {
            String[] args = message.split(":");
            WaypointSession minimapSession = ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getWaypointSession();
            minimapSession.getSharing().onWaypointAdd(args);
            return true;
        }
        if (message.equals(WaypointTeleport.TELEPORT_ANYWAY_COMMAND)) {
            WaypointSession minimapSession2 = ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getWaypointSession();
            minimapSession2.getTeleport().teleportAnyway();
            return true;
        }
        return false;
    }

    public boolean handleClientPlayerChatReceivedEvent(ChatType.Bound chatType, Component component, GameProfile gameProfile) {
        if (component == null) {
            return false;
        }
        return handleChatMessage(gameProfile == null ? I18n.get("gui.xaero_waypoint_somebody_shared", new Object[0]) : gameProfile.getName(), component);
    }

    public boolean handleClientSystemChatReceivedEvent(Component component) {
        if (component == null) {
            return false;
        }
        String textString = component.getString();
        if (textString.contains("§r§e§s§e§t§x§a§e§r§o")) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            minimapSession.getMinimapProcessor().setNoMinimapMessageReceived(false);
            minimapSession.getMinimapProcessor().setFairPlayOnlyMessageReceived(false);
            minimapSession.getMinimapProcessor().setConsideringNetherFairPlayMessage(false);
        }
        if (textString.contains("§n§o§m§i§n§i§m§a§p")) {
            XaeroMinimapSession minimapSession2 = XaeroMinimapSession.getCurrentSession();
            minimapSession2.getMinimapProcessor().setNoMinimapMessageReceived(true);
        }
        if (textString.contains("§x§a§e§r§o§m§m§n§e§t§h§e§r§i§s§f§a§i§r")) {
            XaeroMinimapSession minimapSession3 = XaeroMinimapSession.getCurrentSession();
            minimapSession3.getMinimapProcessor().setConsideringNetherFairPlayMessage(true);
        }
        if (textString.contains("§f§a§i§r§x§a§e§r§o")) {
            XaeroMinimapSession minimapSession4 = XaeroMinimapSession.getCurrentSession();
            minimapSession4.getMinimapProcessor().setFairPlayOnlyMessageReceived(true);
        }
        String probableName = StringUtils.substringBetween(textString, "<", ">");
        return handleChatMessage(probableName == null ? I18n.get("gui.xaero_waypoint_server_shared", new Object[0]) : probableName, component);
    }

    private boolean handleChatMessage(String playerName, Component text) {
        MinimapSession minimapSession;
        String textString = text.getString();
        if ((!textString.contains("xaero_waypoint:") && !textString.contains("xaero-waypoint:")) || (minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()) == null) {
            return false;
        }
        minimapSession.getWaypointSession().getSharing().onWaypointReceived(playerName, textString);
        return true;
    }

    public void handleDrawScreenEventPost(Screen gui) {
        if (!Patreon.needsNotification() && this.modMain.isOutdated()) {
            this.modMain.setOutdated(false);
        }
    }

    public void handlePlayerSetSpawnEvent(BlockPos newSpawnPoint, Level world) {
        if (world instanceof ClientLevel) {
            MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            if (minimapSession != null) {
                minimapSession.getWorldStateUpdater().setCurrentWorldSpawn(newSpawnPoint);
            }
            if (MinimapClientWorldDataHelper.getWorldData((ClientLevel) world).serverLevelId == null) {
            }
        }
    }

    public Object getLastGuiOpen() {
        return this.lastGuiOpen;
    }

    public void worldUnload(LevelAccessor world) {
        XaeroMinimapSession minimapSession;
        if ((world instanceof ClientLevel) && (minimapSession = XaeroMinimapSession.getCurrentSession()) != null) {
            MinimapProcessor minimap = minimapSession.getMinimapProcessor();
            minimap.getRadarSession().update(null, null, null);
        }
    }

    public void handleClientTickStart() {
        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
        if (minimapSession != null) {
            MinimapProcessor minimap = minimapSession.getMinimapProcessor();
            minimap.onClientTick();
            if (Minecraft.getInstance().screen == null) {
                minimapSession.getKeyMappingTickHandler().tick();
            }
            HudSession hudSession = HudSession.getCurrentSession();
            this.modMain.getClientEventsListener().clientTickPost(hudSession);
        }
    }

    public void handlePlayerTickStart(Player player) {
        MinimapSession minimapSession;
        if (player == Minecraft.getInstance().player && this.modMain.isLoadedClient() && (minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()) != null) {
            try {
                MinimapProcessor minimap = minimapSession.getProcessor();
                minimapSession.getWorldStateUpdater().update();
                minimap.onPlayerTick();
                Minecraft.getInstance();
                HudSession hudSession = HudSession.getCurrentSession();
                this.modMain.getClientEventsListener().playerTickPost(hudSession);
            } catch (Throwable t) {
                this.modMain.getInterfaces().getMinimapInterface().setCrashedWith(t);
            }
        }
    }

    public void handleRenderTickStart() {
        if (Minecraft.getInstance().player == null || !this.modMain.isLoadedClient()) {
            return;
        }
        this.modMain.getInterfaces().getMinimapInterface().checkCrashes();
        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
        if (minimapSession != null) {
            MinimapProcessor minimap = minimapSession.getMinimapProcessor();
            minimap.getMinimapWriter().onRender();
        }
    }

    public boolean handleRenderStatusEffectOverlay(GuiGraphics guiGraphics) {
        if (!this.modMain.isLoadedClient()) {
            return false;
        }
        return this.modMain.getClientEventsListener().handleRenderStatusEffectOverlay(guiGraphics);
    }

    public boolean handleRenderCrosshairOverlay(GuiGraphics guiGraphics) {
        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
        return minimapSession != null && minimapSession.getMinimapProcessor().isEnlargedMap() && ((Boolean) this.modMain.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.CENTERED_ENLARGED)).booleanValue();
    }

    public boolean handleForceToggleKeyMapping(ToggleKeyMapping keyMapping) {
        if (KeyMappingTickHandler.DISABLE_KEY_MAPPING_OVERRIDES) {
            return false;
        }
        return this.modMain.getClientEventsListener().handleForceToggleKeyMapping(keyMapping);
    }
}
