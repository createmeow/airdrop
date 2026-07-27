package xaero.hud.minimap.player.tracker;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.hud.entity.EntityUtils;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/PlayerTrackerMinimapElementRenderer.class */
public final class PlayerTrackerMinimapElementRenderer extends MinimapElementRenderer<PlayerTrackerMinimapElement<?>, PlayerTrackerMinimapElementRenderContext> {
    private final double WORLD_MINIMUM_DISTANCE = 10.0d;
    private final double WORLD_FADING_LENGTH = 10.0d;
    private MultiBufferSource.BufferSource minimapBufferSource;
    private final PlayerTrackerMinimapElementCollector elementCollector;
    private final PlayerTrackerIconRenderer playerTrackerIconRenderer;
    private final IXaeroMinimap modMain;
    private float nameScale;

    private PlayerTrackerMinimapElementRenderer(PlayerTrackerMinimapElementCollector elementCollector, IXaeroMinimap modMain, PlayerTrackerMinimapElementRenderContext context, PlayerTrackerMinimapElementRenderProvider<PlayerTrackerMinimapElementRenderContext> provider, PlayerTrackerMinimapElementReader reader, PlayerTrackerIconRenderer playerTrackerIconRenderer) {
        super(reader, provider, context);
        this.WORLD_MINIMUM_DISTANCE = 10.0d;
        this.WORLD_FADING_LENGTH = 10.0d;
        this.elementCollector = elementCollector;
        this.modMain = modMain;
        this.playerTrackerIconRenderer = playerTrackerIconRenderer;
    }

    public ResourceLocation getPlayerSkin(Player player, PlayerInfo info) {
        ResourceLocation skinTextureLocation = player instanceof AbstractClientPlayer ? ((AbstractClientPlayer) player).getSkin().texture() : info.getSkin().texture();
        if (skinTextureLocation == null) {
            skinTextureLocation = DefaultPlayerSkin.get(player.getUUID()).texture();
        }
        return skinTextureLocation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public void preRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider rendererProvider) {
        float f;
        RenderSystem.disableDepthTest();
        vanillaBufferSource.endBatch();
        this.minimapBufferSource = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
        ((PlayerTrackerMinimapElementRenderContext) this.context).coloredBackgroundConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
        ((PlayerTrackerMinimapElementRenderContext) this.context).uniqueTextureUIObjectRenderer = rendererProvider.getRenderer(t -> {
            RenderSystem.setShaderTexture(0, t);
        }, MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_NEAREST);
        ((PlayerTrackerMinimapElementRenderContext) this.context).renderEntityDimId = renderInfo.renderEntityDimension;
        ((PlayerTrackerMinimapElementRenderContext) this.context).mapDimId = renderInfo.mapDimension;
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        float trackedPlayerWorldIconScale = MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.TRACKED_PLAYER_WORLD_ICON_SCALE);
        float trackedPlayerMinimapIconScale = MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.TRACKED_PLAYER_MINIMAP_ICON_SCALE);
        PlayerTrackerMinimapElementRenderContext playerTrackerMinimapElementRenderContext = (PlayerTrackerMinimapElementRenderContext) this.context;
        if (renderInfo.location == MinimapElementRenderLocation.IN_WORLD) {
            f = trackedPlayerWorldIconScale;
        } else {
            f = trackedPlayerMinimapIconScale;
        }
        playerTrackerMinimapElementRenderContext.iconScale = f;
        this.nameScale = MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.TRACKED_PLAYER_WORLD_NAME_SCALE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public void postRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider rendererProvider) {
        rendererProvider.draw(((PlayerTrackerMinimapElementRenderContext) this.context).uniqueTextureUIObjectRenderer);
        this.minimapBufferSource.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        this.elementCollector.resetRenderedOnRadarFlags();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean renderElement(PlayerTrackerMinimapElement<?> e, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        if (!outOfBounds && renderInfo.location != MinimapElementRenderLocation.IN_WORLD && e.wasRenderedOnRadar()) {
            return false;
        }
        PoseStack matrixStack = guiGraphics.pose();
        Minecraft mc = Minecraft.getInstance();
        PlayerInfo info = mc.getConnection().getPlayerInfo(e.getPlayerId());
        if (info == null) {
            return false;
        }
        Player clientPlayer = mc.level.getPlayerByUUID(e.getPlayerId());
        double trackedX = clientPlayer == null ? e.getX() : EntityUtils.getEntityX(clientPlayer, renderInfo.partialTicks);
        double trackedY = clientPlayer == null ? e.getY() : EntityUtils.getEntityY(clientPlayer, renderInfo.partialTicks);
        double trackedZ = clientPlayer == null ? e.getZ() : EntityUtils.getEntityZ(clientPlayer, renderInfo.partialTicks);
        double offX = trackedX - renderInfo.renderEntityPos.x;
        double offY = trackedY - renderInfo.renderEntityPos.y;
        double offZ = trackedZ - renderInfo.renderEntityPos.z;
        double distance = Math.sqrt((offX * offX) + (offY * offY) + (offZ * offZ));
        if (distance < 10.0d) {
            return false;
        }
        matrixStack.pushPose();
        matrixStack.translate(0.0d, 0.0d, optionalDepth);
        boolean inWorld = renderInfo.location == MinimapElementRenderLocation.IN_WORLD;
        float alpha = inWorld ? 0.5f : 1.0f;
        if (highlighted && inWorld) {
            alpha = 0.8f;
        }
        if (!highlighted && inWorld && distance < 20.0d) {
            alpha *= (float) ((distance - 10.0d) / 10.0d);
        }
        matrixStack.translate(0.0d, 0.0d, 0.01d);
        matrixStack.pushPose();
        matrixStack.scale(((PlayerTrackerMinimapElementRenderContext) this.context).iconScale, ((PlayerTrackerMinimapElementRenderContext) this.context).iconScale, 1.0f);
        RenderBufferUtil.addColoredRect(matrixStack.last().pose(), ((PlayerTrackerMinimapElementRenderContext) this.context).coloredBackgroundConsumer, -5.0f, -5.0f, 10, 10, 1.0f, 1.0f, 1.0f, alpha);
        this.playerTrackerIconRenderer.renderIcon(mc, ((PlayerTrackerMinimapElementRenderContext) this.context).uniqueTextureUIObjectRenderer, matrixStack, clientPlayer, getPlayerSkin(clientPlayer, info), alpha);
        matrixStack.popPose();
        if (highlighted && inWorld) {
            matrixStack.translate((-5.0f) * ((PlayerTrackerMinimapElementRenderContext) this.context).iconScale, 0.0f, 0.0f);
            matrixStack.scale(this.nameScale, this.nameScale, 1.0f);
            String playerName = info.getProfile().getName();
            int playerNameWidth = mc.font.width(playerName);
            RenderBufferUtil.addColoredRect(matrixStack.last().pose(), ((PlayerTrackerMinimapElementRenderContext) this.context).coloredBackgroundConsumer, (-playerNameWidth) - 1, -5.0f, playerNameWidth + 1, 10, 0.0f, 0.0f, 0.0f, 0.3529412f);
            mc.font.drawInBatch(playerName, -playerNameWidth, -4.0f, -1, false, matrixStack.last().pose(), this.minimapBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
        matrixStack.popPose();
        return true;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean shouldRender(MinimapElementRenderLocation location) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        return (location != MinimapElementRenderLocation.IN_WORLD && ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.TRACKED_PLAYERS_ON_MINIMAP)).booleanValue()) || (location == MinimapElementRenderLocation.IN_WORLD && ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.TRACKED_PLAYERS_IN_WORLD)).booleanValue());
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public int getOrder() {
        return 100;
    }

    public PlayerTrackerMinimapElementCollector getCollector() {
        return this.elementCollector;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/PlayerTrackerMinimapElementRenderer$Builder.class */
    public static final class Builder {
        private final IXaeroMinimap modMain;

        private Builder(IXaeroMinimap modMain) {
            this.modMain = modMain;
        }

        private Builder setDefault() {
            return this;
        }

        public PlayerTrackerMinimapElementRenderer build() {
            PlayerTrackerMinimapElementCollector collector = new PlayerTrackerMinimapElementCollector(this.modMain.getPlayerTrackerSystemManager());
            return new PlayerTrackerMinimapElementRenderer(collector, this.modMain, new PlayerTrackerMinimapElementRenderContext(), new PlayerTrackerMinimapElementRenderProvider(collector), new PlayerTrackerMinimapElementReader(), new PlayerTrackerIconRenderer());
        }

        public static Builder begin(IXaeroMinimap modMain) {
            return new Builder(modMain).setDefault();
        }
    }
}
