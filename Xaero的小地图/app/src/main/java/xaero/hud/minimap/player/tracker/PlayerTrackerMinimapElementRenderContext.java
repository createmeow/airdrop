package xaero.hud.minimap.player.tracker;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/PlayerTrackerMinimapElementRenderContext.class */
public class PlayerTrackerMinimapElementRenderContext {
    public VertexConsumer coloredBackgroundConsumer;
    public MultiTextureRenderTypeRenderer uniqueTextureUIObjectRenderer;
    public ResourceKey<Level> renderEntityDimId;
    public ResourceKey<Level> mapDimId;
    public float iconScale = 1.0f;
}
