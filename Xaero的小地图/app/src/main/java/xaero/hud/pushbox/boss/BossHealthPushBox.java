package xaero.hud.pushbox.boss;

import xaero.hud.pushbox.PushBox;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/pushbox/boss/BossHealthPushBox.class */
public class BossHealthPushBox extends PushBox implements IBossHealthPushBox {
    public BossHealthPushBox() {
        super(-92, 0, 184, 0, 0.5f, 0.0f, 0);
    }

    @Override // xaero.hud.pushbox.PushBox
    public void postUpdate() {
        super.postUpdate();
        this.h = 0;
        this.active = false;
    }

    @Override // xaero.hud.pushbox.boss.IBossHealthPushBox
    public void setLastBossHealthHeight(int h) {
        this.h = h;
    }
}
