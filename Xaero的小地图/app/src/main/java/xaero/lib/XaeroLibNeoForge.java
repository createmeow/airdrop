package xaero.lib;

import java.util.Iterator;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import xaero.lib.client.event.ClientEvents;
import xaero.lib.client.event.ClientEventsNeoForge;
import xaero.lib.client.event.ModClientEventsNeoForge;
import xaero.lib.common.event.CommonEventNeoForge;
import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.common.packet.PacketHandlerNeoForge;
import xaero.lib.common.packet.PacketHandlerRegistry;
import xaero.lib.common.permission.system.NeoForgePermissionSystem;
import xaero.lib.common.permission.system.PermissionSystemRegistry;

@Mod(XaeroLib.MOD_ID)
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/XaeroLibNeoForge.class */
public class XaeroLibNeoForge extends XaeroLib {
    public XaeroLibNeoForge(IEventBus fmlEventBus) {
        registerEvents(fmlEventBus);
    }

    private void registerEvents(IEventBus fmlEventBus) {
        fmlEventBus.addListener(this::loadCommonNeoForge);
        fmlEventBus.addListener(this::loadClientNeoForge);
        fmlEventBus.addListener(this::loadServerNeoForge);
        fmlEventBus.addListener(this::registerPayloadHandlers);
        NeoForge.EVENT_BUS.register(new CommonEventNeoForge());
        if (FMLLoader.getDist() == Dist.CLIENT) {
            registerClientEvents(fmlEventBus);
        }
    }

    private void registerClientEvents(IEventBus fmlEventBus) {
        ClientEvents clientEvents = new ClientEventsNeoForge();
        NeoForge.EVENT_BUS.register(clientEvents);
        fmlEventBus.register(new ModClientEventsNeoForge(clientEvents));
    }

    private void loadCommonNeoForge(FMLCommonSetupEvent event) {
        loadCommon();
    }

    private void loadClientNeoForge(FMLClientSetupEvent event) {
        loadClient();
        event.enqueueWork(this::deferredTaskClient);
    }

    private void loadServerNeoForge(FMLDedicatedServerSetupEvent event) {
        loadServer();
        event.enqueueWork(this::deferredTaskServer);
    }

    public void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        Iterator<IPacketHandler> it = PacketHandlerRegistry.INSTANCE.iterator();
        while (it.hasNext()) {
            IPacketHandler packetHandler = it.next();
            if (packetHandler instanceof PacketHandlerNeoForge) {
                PacketHandlerNeoForge packetHandlerNeoForge = (PacketHandlerNeoForge) packetHandler;
                packetHandlerNeoForge.registerPayloadHandler(event);
            }
        }
    }

    @Override // xaero.lib.XaeroLib
    public void loadCommonLater() {
        PermissionSystemRegistry.INSTANCE.register(new NeoForgePermissionSystem());
        super.loadCommonLater();
    }

    private void deferredTaskClient() {
        loadCommonLater();
        loadClientLater();
    }

    private void deferredTaskServer() {
        loadCommonLater();
        loadServerLater();
    }
}
