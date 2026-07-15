package com.createmeow.airdrop.network;

import com.createmeow.airdrop.airDrop;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AirdropNetwork {
    private static boolean registered = false;

    public static void register(IEventBus modEventBus) {
        if (registered) return;
        registered = true;
        modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            PayloadRegistrar registrar = event.registrar(airDrop.MODID).versioned("1");
            registrar.playToClient(
                    WaypointCreatePayload.TYPE,
                    WaypointCreatePayload.STREAM_CODEC,
                    AirdropNetwork::handleWaypointCreate
            );
            registrar.playToClient(
                    WaypointRemovePayload.TYPE,
                    WaypointRemovePayload.STREAM_CODEC,
                    AirdropNetwork::handleWaypointRemove
            );
            airDrop.LOGGER.info("Airdrop network payloads registered (dedicated server compatible)");
        });
    }

    public static void sendWaypointCreateToAll(MinecraftServer server, BlockPos pos, String name, boolean isTimed) {
        if (server == null || server.getPlayerList() == null) {
            airDrop.LOGGER.warn("Cannot send waypoint create: server or player list is null");
            return;
        }
        var payload = new WaypointCreatePayload(pos.getX(), pos.getY(), pos.getZ(), name, isTimed);
        int count = 0;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.connection != null) {
                PacketDistributor.sendToPlayer(player, payload);
                count++;
            }
        }
        airDrop.LOGGER.debug("Sent waypoint create to {} players for airdrop at ({}, {}, {})",
                count, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void sendWaypointRemoveToAll(MinecraftServer server, BlockPos pos) {
        if (server == null || server.getPlayerList() == null) {
            airDrop.LOGGER.warn("Cannot send waypoint remove: server or player list is null");
            return;
        }
        var payload = new WaypointRemovePayload(pos.getX(), pos.getY(), pos.getZ());
        int count = 0;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.connection != null) {
                PacketDistributor.sendToPlayer(player, payload);
                count++;
            }
        }
        airDrop.LOGGER.debug("Sent waypoint remove to {} players for airdrop at ({}, {}, {})",
                count, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void handleWaypointCreate(WaypointCreatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                airDrop.LOGGER.warn("WaypointCreate handler: player is null");
                return;
            }
            BlockPos pos = new BlockPos(payload.x, payload.y, payload.z);
            airDrop.LOGGER.debug("Client received waypoint create at ({}, {}, {}): {}",
                    payload.x, payload.y, payload.z, payload.name);
            XaeroWaypointHandler.createWaypoint(player, pos, payload.name, payload.isTimed);
        });
    }

    public static void handleWaypointRemove(WaypointRemovePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                airDrop.LOGGER.warn("WaypointRemove handler: player is null");
                return;
            }
            BlockPos pos = new BlockPos(payload.x, payload.y, payload.z);
            airDrop.LOGGER.debug("Client received waypoint remove at ({}, {}, {})",
                    payload.x, payload.y, payload.z);
            XaeroWaypointHandler.removeWaypoint(player, pos);
        });
    }

    public record WaypointCreatePayload(int x, int y, int z, String name, boolean isTimed) implements CustomPacketPayload {
        public static final Type<WaypointCreatePayload> TYPE = new Type<>(airDrop.id("waypoint_create"));

        public static final StreamCodec<ByteBuf, WaypointCreatePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, WaypointCreatePayload::x,
                ByteBufCodecs.INT, WaypointCreatePayload::y,
                ByteBufCodecs.INT, WaypointCreatePayload::z,
                ByteBufCodecs.STRING_UTF8, WaypointCreatePayload::name,
                ByteBufCodecs.BOOL, WaypointCreatePayload::isTimed,
                WaypointCreatePayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record WaypointRemovePayload(int x, int y, int z) implements CustomPacketPayload {
        public static final Type<WaypointRemovePayload> TYPE = new Type<>(airDrop.id("waypoint_remove"));

        public static final StreamCodec<ByteBuf, WaypointRemovePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, WaypointRemovePayload::x,
                ByteBufCodecs.INT, WaypointRemovePayload::y,
                ByteBufCodecs.INT, WaypointRemovePayload::z,
                WaypointRemovePayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}