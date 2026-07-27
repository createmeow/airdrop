package xaero.hud.pushbox.effect;

import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import xaero.hud.pushbox.PushBox;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/pushbox/effect/PotionEffectsPushBox.class */
public class PotionEffectsPushBox extends PushBox implements IPotionEffectsPushBox {
    private boolean hasNegative;

    public PotionEffectsPushBox() {
        super(0, 0, 0, 0, 1.0f, 0.0f, 53);
    }

    @Override // xaero.hud.pushbox.PushBox
    public int getX(int width, int height) {
        return super.getX(width, height) - getW(width, height);
    }

    @Override // xaero.hud.pushbox.PushBox
    public void update() {
        super.update();
        this.hasNegative = false;
        this.w = calculatePotionDisplayWidth(this);
        this.h = this.hasNegative ? 53 : 27;
    }

    @Override // xaero.hud.pushbox.PushBox
    public void postUpdate() {
        super.postUpdate();
        this.active = false;
    }

    protected static int calculatePotionDisplayWidth(IPotionEffectsPushBox potionEffectBox) {
        Holder<MobEffect> effect;
        Minecraft mc = Minecraft.getInstance();
        Collection<MobEffectInstance> collection = mc.player.getActiveEffects();
        if (collection == null || collection.isEmpty()) {
            return 0;
        }
        int positiveCount = 0;
        int negativeCount = 0;
        for (MobEffectInstance effectInstance : collection) {
            if (effectInstance.showIcon() && (effect = effectInstance.getEffect()) != null) {
                if (!((MobEffect) effect.value()).isBeneficial()) {
                    potionEffectBox.setHasNegative(true);
                    negativeCount++;
                } else {
                    positiveCount++;
                }
            }
        }
        if (positiveCount + negativeCount == 0) {
            return 0;
        }
        return (Math.max(positiveCount, negativeCount) * 25) + 1;
    }

    @Override // xaero.hud.pushbox.effect.IPotionEffectsPushBox
    public void setHasNegative(boolean b) {
        this.hasNegative = b;
    }
}
