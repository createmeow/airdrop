package xaero.common.events;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.effect.EffectsRegister;
import xaero.hud.minimap.common.config.primary.option.MinimapPrimaryCommonConfigOptions;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.single.SingleConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/ModCommonEventsNeoForge.class */
public class ModCommonEventsNeoForge extends ModCommonEvents {
    public ModCommonEventsNeoForge(IXaeroMinimap modMain) {
        super(modMain);
    }

    @SubscribeEvent
    public void onRegister(RegisterEvent event) {
        SingleConfigManager<Config> primaryCommonConfig = HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManager();
        boolean shouldRegisterEffects = ((Boolean) primaryCommonConfig.getEffective(MinimapPrimaryCommonConfigOptions.REGISTER_EFFECTS)).booleanValue();
        if (event.getRegistry() == BuiltInRegistries.MOB_EFFECT && shouldRegisterEffects) {
            new EffectsRegister().registerEffects(effect -> {
                return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, effect.getRegistryName(), effect);
            });
        }
    }
}
