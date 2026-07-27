package xaero.hud.controls.key.function;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/controls/key/function/KeyMappingFunction.class */
public abstract class KeyMappingFunction {
    private final boolean held;

    public abstract void onPress();

    public abstract void onRelease();

    protected KeyMappingFunction(boolean held) {
        this.held = held;
    }

    public boolean isHeld() {
        return this.held;
    }
}
