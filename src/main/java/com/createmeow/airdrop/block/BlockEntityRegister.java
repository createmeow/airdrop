package com.createmeow.airdrop.block;

import com.createmeow.airdrop.airDrop;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, airDrop.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirDropBlockEntity>> AIR_DROP_BE =
            BLOCK_ENTITIES.register("air_drop", AirDropBlockEntity::createType);

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}