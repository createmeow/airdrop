package com.createmeow.airdrop;

import com.createmeow.airdrop.airdrop.AirdropScheduler;
import com.createmeow.airdrop.block.AirDropBlock;
import com.createmeow.airdrop.block.BlockEntityRegister;
import com.createmeow.airdrop.command.AirdropCommand;
import com.createmeow.airdrop.command.AirdropConfigCommand;
import com.createmeow.airdrop.item.SignalGunItem;
import com.createmeow.airdrop.network.AirdropNetwork;
import com.createmeow.airdrop.particle.ParticleRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(airDrop.MODID)
public class airDrop {
    public static final String MODID = "airdrop";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> AIR_DROP_BLOCK = BLOCKS.register("air_drop",
            () -> new AirDropBlock(Block.Properties.of().strength(-1.0F, 3600000.0F).noOcclusion().sound(SoundType.WOOL)));

    public static final DeferredItem<BlockItem> AIR_DROP_BLOCK_ITEM = ITEMS.register("air_drop",
            () -> new BlockItem(AIR_DROP_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<SignalGunItem> SIGNAL_GUN = ITEMS.register("signal_gun",
            () -> new SignalGunItem(new Item.Properties().stacksTo(16), false));

    public static final DeferredItem<SignalGunItem> SIGNAL_GUN_ADV = ITEMS.register("signal_gun_adv",
            () -> new SignalGunItem(new Item.Properties().stacksTo(16), true));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AIRDROP_TAB = CREATIVE_MODE_TABS.register("airdrop_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.airdrop"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> SIGNAL_GUN.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(SIGNAL_GUN.get());
                        output.accept(SIGNAL_GUN_ADV.get());
                        output.accept(AIR_DROP_BLOCK_ITEM.get());
                    }).build());

    public airDrop(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BlockEntityRegister.register(modEventBus);
        ParticleRegister.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        AirdropNetwork.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("AirDrop mod common setup complete");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("AirDrop mod server starting");
        AirdropScheduler.init(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        AirdropScheduler.onServerStop();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        AirdropScheduler.tick(event.getServer());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        AirdropCommand.register(event.getDispatcher());
        AirdropConfigCommand.register(event.getDispatcher());
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}