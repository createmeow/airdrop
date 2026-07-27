package xaero.hud.pushbox;

import xaero.hud.pushbox.PushboxHandler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/pushbox/FullHeightShiftPushBox.class */
public abstract class FullHeightShiftPushBox extends PushBox {
    protected int shift;

    protected abstract int getShift();

    public FullHeightShiftPushBox(int x, int w, float anchorX) {
        super(x, 0, w, 0, anchorX, 0.0f, 0);
    }

    @Override // xaero.hud.pushbox.PushBox
    public void update() {
        super.update();
        this.shift = getShift();
    }

    @Override // xaero.hud.pushbox.PushBox
    public int getH(int width, int height) {
        return height;
    }

    @Override // xaero.hud.pushbox.PushBox
    public void push(PushboxHandler.State state, int pushX, int pushY) {
        super.push(state, 0, this.shift);
    }
}
