package xaero.common.gui;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiDropdownHelper.class */
public class GuiDropdownHelper<T> {
    protected int current;
    protected int auto;
    protected T[] keys;
    protected String[] options;

    public T getCurrentKey() {
        return this.keys[this.current];
    }

    public String getCurrentName() {
        return this.options[this.current];
    }
}
