package xaero.hud.pushbox;

import java.util.HashSet;
import java.util.Set;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/pushbox/PushboxManager.class */
public class PushboxManager {
    private final Set<PushBox> pushBoxes = new HashSet();

    public void add(PushBox pushBox) {
        this.pushBoxes.add(pushBox);
    }

    public Iterable<PushBox> getAll() {
        return this.pushBoxes;
    }
}
