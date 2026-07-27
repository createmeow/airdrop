package xaero.hud.pushbox.effect;

import xaero.hud.pushbox.FullHeightShiftPushBox;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/pushbox/effect/PotionEffectsShiftPushBox.class */
public class PotionEffectsShiftPushBox extends FullHeightShiftPushBox implements IPotionEffectsPushBox {
    private boolean hasNegative;

    public PotionEffectsShiftPushBox() {
        super(0, 0, 1.0f);
    }

    @Override // xaero.hud.pushbox.PushBox
    public int getX(int width, int height) {
        return super.getX(width, height) - getW(width, height);
    }

    @Override // xaero.hud.pushbox.FullHeightShiftPushBox
    protected int getShift() {
        return this.hasNegative ? 53 : 27;
    }

    @Override // xaero.hud.pushbox.FullHeightShiftPushBox, xaero.hud.pushbox.PushBox
    public void update() {
        super.update();
        this.hasNegative = false;
        this.w = PotionEffectsPushBox.calculatePotionDisplayWidth(this);
    }

    @Override // xaero.hud.pushbox.PushBox
    public void postUpdate() {
        super.postUpdate();
        this.active = false;
    }

    @Override // xaero.hud.pushbox.effect.IPotionEffectsPushBox
    public void setHasNegative(boolean b) {
        this.hasNegative = b;
    }
}
