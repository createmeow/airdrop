package xaero.hud.minimap.radar.icon.creator.render.form.model.part;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/part/ModelPartUtil.class */
public class ModelPartUtil {
    private static final Field CUBES_FIELD = ReflectionUtils.getFieldReflection(ModelPart.class, "cubes", "field_3663", "Ljava/util/List;", "f_104212_");
    private static final Field CHILDREN_FIELD = ReflectionUtils.getFieldReflection(ModelPart.class, "children", "field_3661", "Ljava/util/Map;", "f_104213_");

    public static List<ModelPart.Cube> getCubes(ModelPart modelRenderer) {
        return (List) ReflectionUtils.getReflectFieldValue(modelRenderer, CUBES_FIELD);
    }

    public static Map<String, ModelPart> getChildren(ModelPart modelRenderer) {
        return (Map) ReflectionUtils.getReflectFieldValue(modelRenderer, CHILDREN_FIELD);
    }

    public static boolean hasDirectCubes(ModelPart part) {
        List<ModelPart.Cube> cubes = getCubes(part);
        return (cubes == null || cubes.isEmpty()) ? false : true;
    }

    public static boolean hasCubes(ModelPart part) {
        if (hasDirectCubes(part)) {
            return true;
        }
        Map<String, ModelPart> children = getChildren(part);
        for (ModelPart child : children.values()) {
            if (hasCubes(child)) {
                return true;
            }
        }
        return false;
    }

    public static ModelPart.Cube getBiggestCuboid(ModelPart part) {
        List<ModelPart.Cube> mainCubeList = getCubes(part);
        if (mainCubeList == null || mainCubeList.isEmpty()) {
            return null;
        }
        float biggestSize = 0.0f;
        ModelPart.Cube biggestCuboid = null;
        for (ModelPart.Cube cuboid : mainCubeList) {
            float size = Math.abs((cuboid.maxX - cuboid.minX) * (cuboid.maxY - cuboid.minY) * (cuboid.maxZ - cuboid.minZ));
            if (size >= biggestSize) {
                biggestCuboid = cuboid;
                biggestSize = size;
            }
        }
        return biggestCuboid;
    }
}
