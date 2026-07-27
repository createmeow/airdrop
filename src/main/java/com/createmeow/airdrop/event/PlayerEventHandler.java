package com.createmeow.airdrop.event;

import com.createmeow.airdrop.airdrop.AirdropScheduler;
import com.createmeow.airdrop.network.XaeroWaypointHandler;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 同步当前所有活跃空投的路径点
            AirdropScheduler.onPlayerJoin(player);
            XaeroWaypointHandler.onPlayerJoin(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 清理玩家的路径点缓存
            XaeroWaypointHandler.onPlayerLeave(player.getUUID());
        }
    }
}