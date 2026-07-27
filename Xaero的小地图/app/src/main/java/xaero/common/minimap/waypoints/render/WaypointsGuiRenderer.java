package xaero.common.minimap.waypoints.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderContext;
import xaero.hud.minimap.world.MinimapWorldManager;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/render/WaypointsGuiRenderer.class */
public final class WaypointsGuiRenderer extends xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer {
    private MinimapElementRenderInfo compatibleRenderInfo;
    private boolean temporaryWaypointsGlobal;

    public WaypointsGuiRenderer(WaypointReader elementReader, WaypointRenderProvider provider, WaypointGuiRenderContext context) {
        super(elementReader, provider, context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public void updateWaypointCollection(IXaeroMinimap modMain) {
        super.updateWaypointCollection();
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapWorldManager manager = session.getWorldManager();
        Camera activeRender = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = activeRender.getPosition();
        ((WaypointMapRenderContext) this.context).dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(manager.getCurrentWorld());
        double cameraPosMultiplier = Minecraft.getInstance().level.dimensionType().coordinateScale() / ((WaypointMapRenderContext) this.context).dimCoordinateScale;
        Waypoint.RENDER_SORTING_POS = new Vec3(cameraPos.x * cameraPosMultiplier, cameraPos.y, cameraPos.z * cameraPosMultiplier);
    }

    @Deprecated
    public void drawIconOnGUI(GuiGraphics guiGraphics, MinimapRendererHelper rendererHelper, Waypoint w, ModSettings settings, int drawX, int drawY, MultiBufferSource.BufferSource renderTypeBuffer, VertexConsumer waypointBackgroundConsumer) {
        super.drawIconOnGUI(guiGraphics, rendererHelper, w, drawX, drawY, renderTypeBuffer, waypointBackgroundConsumer, renderTypeBuffer.getBuffer(CustomRenderTypes.GUI_NEAREST));
    }

    @Override // xaero.hud.minimap.waypoint.render.WaypointMapRenderer
    @Deprecated
    public void drawSetChange(WaypointsManager waypointsManager, GuiGraphics guiGraphics, Window res) {
        drawSetChange((MinimapSession) waypointsManager, guiGraphics, res);
    }

    @Override // xaero.hud.minimap.waypoint.render.WaypointMapRenderer
    @Deprecated
    public void drawSetChange(MinimapSession session, GuiGraphics guiGraphics, Window res) {
        super.drawSetChange(session, guiGraphics, res);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public boolean shouldRender(int location) {
        return super.shouldRender(MinimapElementRenderLocation.fromIndex(location));
    }

    @Override // xaero.hud.minimap.waypoint.render.WaypointMapRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public int getOrder() {
        return super.getOrder();
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public boolean renderElement(int location, boolean highlit, boolean outOfBounds, GuiGraphics guiGraphics, MultiBufferSource.BufferSource renderTypeBuffers, Font font, RenderTarget framebuffer, MinimapRendererHelper helper, Entity renderEntity, Player player, double renderX, double renderY, double renderZ, int elementIndex, double optionalDepth, float optionalScale, Waypoint element, double partialX, double partialY, boolean cave, float partialTicks) {
        if (this.compatibleRenderInfo == null) {
            MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            this.compatibleRenderInfo = new MinimapElementRenderInfo(MinimapElementRenderLocation.fromIndex(location), renderEntity, player, new Vec3(renderX, renderY, renderZ), cave, partialTicks, framebuffer, session.getProcessor().getLastMapDimensionScale(), session.getProcessor().getLastMapDimension());
        }
        return renderElement(element, highlit, outOfBounds, optionalDepth, optionalScale, partialX, partialY, this.compatibleRenderInfo, guiGraphics, renderTypeBuffers);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public void preRender(int location, Entity renderEntity, Player player, double renderX, double renderY, double renderZ, IXaeroMinimap modMain, MultiBufferSource.BufferSource renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        preRender(getPreInfo(location, renderEntity, player, renderX, renderY, renderZ), renderTypeBuffers, multiTextureRenderTypeRenderers);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public void postRender(int location, Entity renderEntity, Player player, double renderX, double renderY, double renderZ, IXaeroMinimap modMain, MultiBufferSource.BufferSource renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        if (this.compatibleRenderInfo == null) {
            this.compatibleRenderInfo = getPreInfo(location, renderEntity, player, renderX, renderY, renderZ);
        }
        postRender(this.compatibleRenderInfo, renderTypeBuffers, multiTextureRenderTypeRenderers);
        this.compatibleRenderInfo = null;
    }
}
