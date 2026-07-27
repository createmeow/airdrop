package xaero.hud.preset.action;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/preset/action/IPresetAction.class */
public interface IPresetAction<M> {
    void apply(M m);

    void confirm(M m);

    void cancel(M m);
}
