package xaero.common.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import xaero.minimap.XaeroMinimap;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/effect/MinimapStatusEffect.class */
public class MinimapStatusEffect extends MobEffect {
    private ResourceLocation id;

    protected MinimapStatusEffect(MobEffectCategory type, int color, String idPrefix) {
        super(type, color);
        String suffix = type == MobEffectCategory.HARMFUL ? "_harmful" : type == MobEffectCategory.BENEFICIAL ? "_beneficial" : "";
        setRegistryName(ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, idPrefix + suffix));
    }

    protected void setRegistryName(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getRegistryName() {
        return this.id;
    }
}
