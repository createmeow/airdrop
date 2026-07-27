package xaero.common.events;

import java.util.Objects;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import xaero.common.IXaeroMinimap;
import xaero.common.interfaces.InterfaceManager;
import xaero.hud.controls.ControlsRegister;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/ModClientEventsNeoForge.class */
public class ModClientEventsNeoForge extends ModClientEvents {
    public ModClientEventsNeoForge(IXaeroMinimap modMain) {
        super(modMain);
    }

    @SubscribeEvent
    public void handleTextureStitchEventPost(TextureAtlasStitchedEvent event) {
        handleTextureStitchEventPost(event.getAtlas());
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelEvent.BakingCompleted event) {
        InterfaceManager interfaceManager = this.modMain.getInterfaces();
        if (interfaceManager != null) {
            interfaceManager.getMinimapInterface().getMinimapFBORenderer().resetEntityIconsResources();
        }
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        this.modMain.ensureControlsRegister();
        ControlsRegister controlsRegister = this.modMain.getControlsRegister();
        Objects.requireNonNull(event);
        controlsRegister.registerKeybindings(event::register);
    }

    @SubscribeEvent
    public void registerOverlay(RegisterGuiLayersEvent event) {
        LayeredDraw.Layer overlay = this::handleRenderModOverlay;
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath("xaerohud", "hud"), overlay);
    }
}
