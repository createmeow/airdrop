package xaero.hud.minimap.radar.icon.creator.render.form.model.resolver;

import java.lang.reflect.Field;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/resolver/ResolvedFieldModelRootPathListener.class */
public class ResolvedFieldModelRootPathListener implements RadarIconModelFieldResolver.Listener {
    private Object resolvedObject;
    private boolean stop;
    private boolean failed;

    public void prepare() {
        this.resolvedObject = null;
        this.stop = false;
        this.failed = false;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver.Listener
    public boolean isFieldAllowed(Field f) {
        return true;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver.Listener
    public boolean shouldStop() {
        return this.stop;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver.Listener
    public void onFieldResolved(Object[] resolved, String matchedFilterElement) {
        this.stop = true;
        if (resolved.length != 1) {
            MinimapLogs.LOGGER.warn("Only exactly 1 object can be referenced with a model root path step but {} were referenced with {}", Integer.valueOf(resolved.length), matchedFilterElement);
            this.failed = true;
        } else {
            this.resolvedObject = resolved[0];
        }
    }

    public Object getCurrentNode() {
        return this.resolvedObject;
    }

    public boolean failed() {
        return this.failed;
    }
}
