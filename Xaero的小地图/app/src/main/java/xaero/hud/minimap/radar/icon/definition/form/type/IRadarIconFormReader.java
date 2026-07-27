package xaero.hud.minimap.radar.icon.definition.form.type;

import xaero.hud.minimap.radar.icon.definition.RadarIconDefinition;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconForm;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/form/type/IRadarIconFormReader.class */
public interface IRadarIconFormReader {
    RadarIconForm read(RadarIconFormType radarIconFormType, String[] strArr, RadarIconDefinition radarIconDefinition);
}
