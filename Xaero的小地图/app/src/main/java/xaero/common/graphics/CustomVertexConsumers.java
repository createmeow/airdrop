package xaero.common.graphics;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SortedMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/CustomVertexConsumers.class */
public class CustomVertexConsumers {
    private final SortedMap<RenderType, ByteBufferBuilder> builders = (SortedMap) Util.make(new Object2ObjectLinkedOpenHashMap(), map -> {
        checkedAddToMap(map, CustomRenderTypes.GUI_NEAREST, new ByteBufferBuilder(256));
        checkedAddToMap(map, CustomRenderTypes.GUI_BILINEAR, new ByteBufferBuilder(256));
        checkedAddToMap(map, CustomRenderTypes.GUI_BILINEAR_PREMULTIPLIED, new ByteBufferBuilder(256));
        checkedAddToMap(map, CustomRenderTypes.COLORED_WAYPOINTS_BGS, new ByteBufferBuilder(256));
        checkedAddToMap(map, CustomRenderTypes.MAP_CHUNK_OVERLAY, new ByteBufferBuilder(256));
        checkedAddToMap(map, CustomRenderTypes.MAP_LINES, new ByteBufferBuilder(256));
        checkedAddToMap(map, CustomRenderTypes.RADAR_NAME_BGS, new ByteBufferBuilder(256));
    });
    private MultiBufferSource.BufferSource betterPVPRenderTypeBuffers = MultiBufferSource.immediateWithBuffers(this.builders, new ByteBufferBuilder(256));

    public MultiBufferSource.BufferSource getBetterPVPRenderTypeBuffers() {
        return this.betterPVPRenderTypeBuffers;
    }

    private static void checkedAddToMap(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map, RenderType layer, ByteBufferBuilder bb) {
        if (map.containsKey(layer)) {
            throw new RuntimeException("Duplicate render layers!");
        }
        map.put(layer, bb);
    }
}
