package xaero.hud.pushbox.boss;

import xaero.hud.pushbox.FullHeightShiftPushBox;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/pushbox/boss/BossHealthShiftPushBox.class */
public class BossHealthShiftPushBox extends FullHeightShiftPushBox implements IBossHealthPushBox {
    public int lastBossHealthHeight;

    public BossHealthShiftPushBox() {
        super(-92, 184, 0.5f);
    }

    @Override // xaero.hud.pushbox.FullHeightShiftPushBox
    protected int getShift() {
        return this.lastBossHealthHeight;
    }

    @Override // xaero.hud.pushbox.PushBox
    public void postUpdate() {
        super.postUpdate();
        this.lastBossHealthHeight = 0;
        this.active = false;
    }

    @Override // xaero.hud.pushbox.boss.IBossHealthPushBox
    public void setLastBossHealthHeight(int h) {
        this.lastBossHealthHeight = h;
    }
}
