package xaero.common.events;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientChatEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import xaero.common.HudMod;
import xaero.common.core.XaeroMinimapCore;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/ClientEventsNeoForge.class */
public class ClientEventsNeoForge extends ClientEvents {
    public ClientEventsNeoForge(HudMod modMain) {
        super(modMain);
    }

    @SubscribeEvent
    public void handleGuiOpenEvent(ScreenEvent.Opening event) {
        Screen guiBefore = event.getNewScreen();
        Screen gui = handleGuiOpen(guiBefore);
        if (guiBefore != null && gui == null) {
            event.setCanceled(true);
        } else {
            event.setNewScreen(gui);
        }
    }

    @SubscribeEvent
    public void handleRenderGameOverlayEventPre(RenderGuiEvent.Pre event) {
        handleRenderGameOverlayEventPre(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(true));
    }

    @SubscribeEvent
    public void handleRenderGameOverlayEventPost(RenderGuiEvent.Post event) {
        handleRenderGameOverlayEventPost();
    }

    @SubscribeEvent
    protected void handleRenderGameOverlayEventPreLayer(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(VanillaGuiLayers.EFFECTS) && handleRenderStatusEffectOverlay(event.getGuiGraphics())) {
            event.setCanceled(true);
        }
        if (event.getName().equals(VanillaGuiLayers.CROSSHAIR) && handleRenderCrosshairOverlay(event.getGuiGraphics())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    protected void handleRenderGameOverlayEventPostLayer(RenderGuiLayerEvent.Post event) {
        if (event.getName().equals(VanillaGuiLayers.EFFECTS)) {
            XaeroMinimapCore.onRenderStatusEffectOverlayPost(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public void handleClientSendChatEvent(ClientChatEvent e) {
        if (handleClientSendChatEvent(e.getMessage())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void handleClientChatReceivedEvent(ClientChatReceivedEvent e) {
        if (e.getMessage() == null || Minecraft.getInstance().getConnection() == null) {
            return;
        }
        Component text = e.getMessage();
        PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(e.getSender());
        GameProfile gameProfile = playerInfo == null ? null : playerInfo.getProfile();
        if (gameProfile != null && handleClientPlayerChatReceivedEvent(e.getBoundChatType(), text, gameProfile)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void handleRenderSolidBlocks(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            XaeroMinimapCore.onProjectionMatrix(event.getProjectionMatrix());
            XaeroMinimapCore.onWorldModelViewMatrix(event.getModelViewMatrix());
        }
    }

    @SubscribeEvent
    public void handleDrawScreenEventPost(ScreenEvent.Render.Post event) {
        handleDrawScreenEventPost(event.getScreen());
    }

    @SubscribeEvent
    public void worldUnload(LevelEvent.Unload event) {
        worldUnload(event.getLevel());
    }

    @SubscribeEvent
    public void handleClientTickEventStart(ClientTickEvent.Pre event) {
        handleClientTickStart();
    }

    @SubscribeEvent
    public void handlePlayerTickEventStart(PlayerTickEvent.Pre event) {
        handlePlayerTickStart(event.getEntity());
    }

    @SubscribeEvent
    public void handleRenderTickEventStart(RenderFrameEvent.Pre event) {
        handleRenderTickStart();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void handleBossHealthRender(CustomizeGuiOverlayEvent.BossEventProgress event) {
        XaeroMinimapCore.onBossHealthRender(event.getY() + event.getIncrement());
    }
}
