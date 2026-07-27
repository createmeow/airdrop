package xaero.hud.minimap.player.tracker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;
import xaero.hud.entity.EntityUtils;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/PlayerTrackerMinimapElementReader.class */
public class PlayerTrackerMinimapElementReader extends MinimapElementReader<PlayerTrackerMinimapElement<?>, PlayerTrackerMinimapElementRenderContext> {
    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isHidden(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context) {
        return (context.renderEntityDimId == element.getDimension() || context.mapDimId == element.getDimension()) ? false : true;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderX(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Player clientPlayer = mc.level.getPlayerByUUID(element.getPlayerId());
        return clientPlayer == null ? element.getX() : EntityUtils.getEntityX(clientPlayer, partialTicks);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderY(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Player clientPlayer = mc.level.getPlayerByUUID(element.getPlayerId());
        return clientPlayer == null ? element.getY() : EntityUtils.getEntityY(clientPlayer, partialTicks);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderZ(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Player clientPlayer = mc.level.getPlayerByUUID(element.getPlayerId());
        return clientPlayer == null ? element.getZ() : EntityUtils.getEntityZ(clientPlayer, partialTicks);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getCoordinateScale(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, MinimapElementRenderInfo renderInfo) {
        if (element.getDimension() == renderInfo.renderEntityDimension) {
            return renderInfo.renderEntityDimensionScale;
        }
        return renderInfo.backgroundCoordinateScale;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxLeft(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return -10;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxRight(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return 10;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxTop(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return -10;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxBottom(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return 10;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxLeft(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return -20;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxRight(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return 20;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxTop(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return -20;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxBottom(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
        return 20;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getLeftSideLength(PlayerTrackerMinimapElement<?> element, Minecraft mc) {
        PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(element.getPlayerId());
        if (info == null) {
            return 9;
        }
        return 9 + mc.font.width(info.getProfile().getName());
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public String getMenuName(PlayerTrackerMinimapElement<?> element) {
        PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(element.getPlayerId());
        if (info == null) {
            return String.valueOf(element.getPlayerId());
        }
        return info.getProfile().getName();
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public String getFilterName(PlayerTrackerMinimapElement<?> element) {
        return getMenuName(element);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getMenuTextFillLeftPadding(PlayerTrackerMinimapElement<?> element) {
        return 0;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRightClickTitleBackgroundColor(PlayerTrackerMinimapElement<?> element) {
        return -11184641;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean shouldScaleBoxWithOptionalScale() {
        return true;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public float getBoxScale(MinimapElementRenderLocation location, PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context) {
        return context.iconScale;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isInteractable(MinimapElementRenderLocation location, PlayerTrackerMinimapElement<?> element) {
        return true;
    }
}
