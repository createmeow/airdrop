package xaero.hud.minimap.radar.icon.definition.form.model.config;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/form/model/config/RadarIconModelConfig.class */
public class RadarIconModelConfig {

    @Expose
    public float rotationY;

    @Expose
    public float rotationX;

    @Expose
    public float rotationZ;

    @Expose
    public float offsetX;

    @Expose
    public float offsetY;

    @Expose
    public Boolean renderingFullModel;

    @Expose
    public ArrayList<String> modelMainPartFieldAliases;

    @Expose
    public ArrayList<String> modelPartsFields;

    @Expose
    public ArrayList<ArrayList<String>> modelRootPath;

    @Expose
    public float baseScale = 1.0f;

    @Expose
    public boolean modelPartsRotationReset = true;

    @Expose
    public boolean layersAllowed = true;
}
