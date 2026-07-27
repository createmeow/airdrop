package xaero.lib.client.gui.widget;

import java.util.Objects;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/CycleButtonOption.class */
public class CycleButtonOption<T> {
    private final T value;

    public CycleButtonOption(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CycleButtonOption<?> that = (CycleButtonOption) o;
        return Objects.equals(this.value, that.value);
    }

    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
