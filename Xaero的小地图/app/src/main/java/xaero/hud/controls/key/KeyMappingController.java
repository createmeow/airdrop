package xaero.hud.controls.key;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.KeyMapping;
import xaero.hud.controls.key.function.KeyMappingFunction;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/controls/key/KeyMappingController.class */
public class KeyMappingController implements Iterable<KeyMappingFunction> {
    private final KeyMapping keyMapping;
    private final boolean xaeroKey;
    private final Set<KeyMappingFunction> functions = new HashSet();
    private boolean pressed;

    public KeyMappingController(KeyMapping keyMapping, boolean xaeroKey) {
        this.keyMapping = keyMapping;
        this.xaeroKey = xaeroKey;
    }

    public void add(KeyMappingFunction function) {
        this.functions.add(function);
    }

    public KeyMapping getKeyMapping() {
        return this.keyMapping;
    }

    public Iterable<KeyMappingFunction> getFunctions() {
        return this.functions;
    }

    public boolean isXaeroKey() {
        return this.xaeroKey;
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override // java.lang.Iterable
    public Iterator<KeyMappingFunction> iterator() {
        return this.functions.iterator();
    }
}
