package xaero.common.minimap.highlight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/highlight/HighlighterRegistry.class */
public class HighlighterRegistry {
    private List<AbstractHighlighter> highlighters = new ArrayList();

    public void register(AbstractHighlighter highlighter) {
        this.highlighters.add(highlighter);
    }

    public void end() {
        this.highlighters = Collections.unmodifiableList(this.highlighters);
    }

    public List<AbstractHighlighter> getHighlighters() {
        return this.highlighters;
    }
}
