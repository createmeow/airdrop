package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.lib.common.util.KeySortableByOther;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/SwitchWaypointSetFunction.class */
public class SwitchWaypointSetFunction extends KeyMappingFunction {
    protected SwitchWaypointSetFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapWorld currentWorld = session.getWorldManager().getCurrentWorld();
        if (currentWorld == null) {
            return;
        }
        List<KeySortableByOther<String>> keysList = new ArrayList<>();
        for (WaypointSet set : currentWorld.getIterableWaypointSets()) {
            String key = set.getName();
            keysList.add(new KeySortableByOther<>(key, I18n.get(key, new Object[0]).toLowerCase()));
        }
        Collections.sort(keysList);
        boolean foundCurrent = false;
        String firstSetKey = null;
        Iterator<KeySortableByOther<String>> it = keysList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            KeySortableByOther<String> sortedSet = it.next();
            String setKey = sortedSet.getKey();
            if (firstSetKey == null) {
                firstSetKey = setKey;
            }
            if (setKey != null && setKey.equals(currentWorld.getCurrentWaypointSetId())) {
                foundCurrent = true;
            } else if (foundCurrent) {
                foundCurrent = false;
                currentWorld.setCurrentWaypointSetId(setKey);
                break;
            }
        }
        if (foundCurrent) {
            currentWorld.setCurrentWaypointSetId(firstSetKey);
        }
        session.getWorldStateUpdater().update();
        session.getWaypointSession().setSetChangedTime(System.currentTimeMillis());
        try {
            session.getWorldManagerIO().saveWorld(currentWorld);
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
