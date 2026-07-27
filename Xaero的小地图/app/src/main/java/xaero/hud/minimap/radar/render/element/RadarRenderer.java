package xaero.hud.minimap.radar.render.element;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xaero.common.HudMod;
import xaero.common.exception.OpenGLException;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.icon.XaeroIcon;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.RadarSession;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.hud.minimap.radar.color.RadarColor;
import xaero.hud.minimap.radar.icon.RadarIconManager;
import xaero.hud.minimap.radar.state.RadarList;
import xaero.hud.minimap.radar.util.RadarUtils;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.single.SingleConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/render/element/RadarRenderer.class */
public abstract class RadarRenderer extends MinimapElementRenderer<Entity, RadarRenderContext> {
    private final RadarIconManager radarIconManager;
    private final Minimap minimap;
    private RadarSession radarSession;
    private EntityRadarCategoryManager categoryManager;
    private RadarList previousList;
    private double maxDistanceSquared;
    private double labelScale;
    private boolean smoothDots;
    private boolean debugEntityIcons;
    private boolean debugEntityVariantIds;
    private int dotsStyle;
    private int heightLimit;
    private boolean heightBasedFade;
    private int startFadingAt;
    private boolean displayNameWhenIconFails;
    private boolean alwaysNameTags;
    private RadarColor radarColor;
    private RadarColor fallbackColor;
    private int displayY;
    private int nameSettingForCategory;
    private boolean namesForCategory;
    private boolean name;
    private boolean iconsAllowed;
    private boolean labelsAllowed;
    private RenderType dotsRenderType;
    private MultiBufferSource.BufferSource minimapBufferSource;
    private VertexConsumer dotsBufferBuilder;
    private VertexConsumer labelBgBuilder;
    private MultiTextureRenderTypeRenderer iconsRenderer;
    private MinimapRendererHelper helper;
    private final RadarRenderProvider radarRenderProvider;

    protected RadarRenderer(RadarIconManager radarIconManager, Minimap minimap, RadarElementReader elementReader, RadarRenderProvider provider, RadarRenderContext context) {
        super(elementReader, provider, context);
        this.radarIconManager = radarIconManager;
        this.minimap = minimap;
        this.radarRenderProvider = provider;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public void preRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        this.radarIconManager.allowPrerender();
        HudMod.INSTANCE.getSettings();
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        SingleConfigManager<Config> primaryConfigManager = configManager.getPrimaryConfigManager();
        this.iconsAllowed = true;
        this.labelsAllowed = true;
        ((RadarRenderContext) this.context).reversedOrder = ModSettings.keyReverseEntityRadar.isDown();
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        this.radarSession = session.getRadarSession();
        this.categoryManager = this.radarSession.getCategoryManager();
        this.previousList = null;
        this.labelScale = ((Double) configManager.getEffective(MinimapProfiledConfigOptions.RADAR_NAME_SCALE)).doubleValue() * (Minecraft.getInstance().isEnforceUnicode() ? 2.0d : 1.0d);
        this.smoothDots = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.RADAR_SMOOTH_DOTS)).booleanValue();
        this.debugEntityIcons = ((Boolean) primaryConfigManager.getEffective(MinimapPrimaryClientConfigOptions.DEBUG_ENTITY_ICONS)).booleanValue();
        this.debugEntityVariantIds = ((Boolean) primaryConfigManager.getEffective(MinimapPrimaryClientConfigOptions.DEBUG_ENTITY_VARIANT_IDS)).booleanValue();
        this.dotsStyle = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.RADAR_DOTS_STYLE)).intValue();
        this.dotsRenderType = this.smoothDots ? CustomRenderTypes.GUI_BILINEAR : CustomRenderTypes.GUI_NEAREST;
        RenderSystem.disableDepthTest();
        vanillaBufferSource.endBatch();
        this.minimapBufferSource = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
        this.dotsBufferBuilder = null;
        this.labelBgBuilder = this.minimapBufferSource.getBuffer(CustomRenderTypes.RADAR_NAME_BGS);
        this.iconsRenderer = multiTextureRenderTypeRenderers.getRenderer(t -> {
            RenderSystem.setShaderTexture(0, t);
        }, MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_BILINEAR);
        this.helper = HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper();
        double playerDimDiv = renderInfo.backgroundCoordinateScale / renderInfo.renderEntityDimensionScale;
        int shapeConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.SHAPE)).intValue();
        this.maxDistanceSquared = RadarUtils.getMaxDistance(session.getProcessor(), shapeConfig == 1) * playerDimDiv * playerDimDiv;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean renderElement(Entity e, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) throws IllegalAccessException, NoSuchMethodException, SecurityException, OpenGLException, IllegalArgumentException {
        double figureScale;
        if (renderInfo.location == MinimapElementRenderLocation.IN_MINIMAP) {
            double offX = e.getX() - renderInfo.renderEntityPos.x;
            if (offX * offX > this.maxDistanceSquared) {
                return false;
            }
            double offY = e.getZ() - renderInfo.renderEntityPos.z;
            if (offY * offY > this.maxDistanceSquared) {
                return false;
            }
        }
        if (((RadarRenderContext) this.context).radarList == null) {
            EntityRadarCategory rootCategory = this.categoryManager.getRootCategory();
            EntityRadarCategory syncedRootCategory = this.categoryManager.getEffectiveSyncedRootCategory();
            ((RadarRenderContext) this.context).radarList = RadarList.Builder.getDefault().build();
            ((RadarRenderContext) this.context).radarList.setClientCategory((EntityRadarCategory) this.categoryManager.getRuleResolver().resolve(rootCategory, e, renderInfo.player));
            if (((RadarRenderContext) this.context).radarList.getClientCategory() == null) {
                if (!((RadarRenderContext) this.context).isMainDot) {
                    return false;
                }
                ((RadarRenderContext) this.context).radarList.setClientCategory(rootCategory);
            }
            if (syncedRootCategory != null) {
                ((RadarRenderContext) this.context).radarList.setSyncedCategory((EntityRadarCategory) this.categoryManager.getRuleResolver().resolve(syncedRootCategory, e, renderInfo.player));
                if (!((RadarRenderContext) this.context).isMainDot && ((RadarRenderContext) this.context).radarList.getSyncedCategory() == null) {
                    return false;
                }
            }
            if (((RadarRenderContext) this.context).radarList == null) {
                return false;
            }
        }
        if (((RadarRenderContext) this.context).radarList != this.previousList) {
            setupRenderForList(((RadarRenderContext) this.context).radarList);
            this.previousList = ((RadarRenderContext) this.context).radarList;
        }
        setupRenderForEntity(e);
        if (e instanceof Player) {
            confirmTrackedPlayerRadarRender((Player) e);
        }
        Entity renderEntity = renderInfo.renderEntity;
        boolean cave = renderInfo.cave;
        float optionalScaleAdjust = renderInfo.location == MinimapElementRenderLocation.OVER_MINIMAP ? 0.5f : 1.0f;
        float optionalScale2 = optionalScale * optionalScaleAdjust;
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        boolean icon = this.iconsAllowed && ((RadarRenderContext) this.context).icon;
        boolean name = this.name;
        if (highlighted && this.nameSettingForCategory > 0) {
            name = true;
        }
        XaeroIcon entityIcon = null;
        if (icon) {
            entityIcon = this.radarIconManager.get(e, (float) ((RadarRenderContext) this.context).iconScale, this.debugEntityIcons, this.debugEntityVariantIds, guiGraphics, renderInfo.framebuffer);
        }
        if (entityIcon == RadarIconManager.DOT) {
            entityIcon = null;
            icon = false;
        }
        boolean usableIcon = (entityIcon == null || entityIcon == RadarIconManager.FAILED) ? false : true;
        float offY2 = (float) (renderEntity.getY() - e.getY());
        int labelOffsetX = 0;
        int labelOffsetY = 0;
        matrixStack.translate(partialX, partialY, 0.0d);
        if (usableIcon) {
            figureScale = ((RadarRenderContext) this.context).iconScale;
            renderIcon(entityIcon, optionalScale2, figureScale, offY2, cave, matrixStack);
        } else {
            boolean smooth = this.smoothDots;
            if (!smooth) {
                optionalScale2 = (float) Math.ceil(optionalScale2);
            }
            double dotActualScale = optionalScale2;
            figureScale = ((RadarRenderContext) this.context).dotScale;
            if (this.dotsStyle == 1) {
                if (!smooth) {
                    figureScale = (int) figureScale;
                }
                dotActualScale *= figureScale;
            }
            float dotOffset = renderDot(e, renderInfo.player, smooth, optionalScale2, figureScale, offY2, cave, matrixStack);
            if (!smooth) {
                double dotRadius = (-dotOffset) * dotActualScale;
                double dotRadiusPartial = dotRadius - ((int) dotRadius);
                labelOffsetX = partialX - dotRadiusPartial <= -0.5d ? -1 : 0;
                labelOffsetY = partialY - dotRadiusPartial < -0.5d ? -1 : 0;
            }
            if (icon && this.displayNameWhenIconFails && entityIcon == RadarIconManager.FAILED) {
                name = true;
            }
        }
        matrixStack.popPose();
        if (!this.labelsAllowed) {
            return true;
        }
        if (!name && this.displayY <= 0) {
            return true;
        }
        matrixStack.translate(labelOffsetX, labelOffsetY + ((int) Math.round((usableIcon ? 11 : 5) * figureScale * optionalScale2)), optionalDepth + 0.10000000149011612d);
        if (optionalScale2 < 1.0f) {
            optionalScale2 = 1.0f;
        }
        renderLabel(e, renderEntity, name, optionalScale2, matrixStack);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public void postRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        if (((RadarRenderContext) this.context).reversedOrder && this.dotsBufferBuilder != null) {
            this.minimapBufferSource.endBatch(this.dotsRenderType);
        }
        multiTextureRenderTypeRenderers.draw(this.iconsRenderer);
        if (!((RadarRenderContext) this.context).reversedOrder && this.dotsBufferBuilder != null) {
            this.minimapBufferSource.endBatch(this.dotsRenderType);
        }
        this.minimapBufferSource.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        this.iconsRenderer = null;
        this.previousList = null;
    }

    private void renderIcon(XaeroIcon entityIcon, double optionalScale, double figureScale, float offY, boolean cave, PoseStack matrixStack) {
        double clampedScale = Math.max(1.0d, figureScale * optionalScale);
        matrixStack.scale((float) clampedScale, (float) clampedScale, 1.0f);
        float brightness = !this.heightBasedFade ? 1.0f : this.radarSession.getColorHelper().getEntityHeightFade(offY, this.heightLimit, this.startFadingAt);
        float opacity = 1.0f;
        if (cave) {
            opacity = brightness;
            brightness = 1.0f;
        }
        this.helper.prepareMyTexturedColoredModalRect(matrixStack.last().pose(), -31.0f, -31.0f, entityIcon.getOffsetX() + 1, entityIcon.getOffsetY() + 1, 62.0f, 62.0f, 62.0f, entityIcon.getTextureAtlas().getWidth(), entityIcon.getTextureAtlas().getTextureId(), brightness, brightness, brightness, opacity, this.iconsRenderer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private float renderDot(Entity e, Player player, boolean smooth, float optionalScale, double figureScale, float offY, boolean cave, PoseStack matrixStack) {
        float dotOffset;
        int dotTextureY;
        int dotTextureH;
        int dotTextureW;
        matrixStack.scale(optionalScale, optionalScale, 1.0f);
        int color = this.radarSession.getColorHelper().getEntityColor(e, offY, cave, this.heightLimit, this.startFadingAt, this.heightBasedFade, this.radarColor, this.fallbackColor);
        float r = ((color >> 16) & 255) / 255.0f;
        float g = ((color >> 8) & 255) / 255.0f;
        float b = (color & 255) / 255.0f;
        float a = ((color >> 24) & 255) / 255.0f;
        int dotTextureX = 0;
        if (this.dotsStyle == 1) {
            if (smooth) {
                dotTextureX = 1;
                dotTextureY = 88;
            } else {
                dotTextureX = 9;
                dotTextureY = 77;
            }
            dotOffset = -3.5f;
            dotTextureH = 8;
            dotTextureW = 8;
            matrixStack.scale((float) figureScale, (float) figureScale, 1.0f);
        } else {
            switch (((RadarRenderContext) this.context).dotSize) {
                case 1:
                    dotOffset = -4.5f;
                    dotTextureY = 108;
                    dotTextureH = 9;
                    dotTextureW = 9;
                    break;
                case 2:
                default:
                    dotOffset = -5.5f;
                    dotTextureY = 117;
                    dotTextureH = 11;
                    dotTextureW = 11;
                    break;
                case 3:
                    dotOffset = -7.5f;
                    dotTextureY = 128;
                    dotTextureH = 15;
                    dotTextureW = 15;
                    break;
                case 4:
                    dotOffset = -10.5f;
                    dotTextureY = 160;
                    dotTextureH = 21;
                    dotTextureW = 21;
                    break;
            }
        }
        if (this.dotsBufferBuilder == null) {
            this.dotsBufferBuilder = this.minimapBufferSource.getBuffer(this.dotsRenderType);
        }
        RenderBufferUtil.addTexturedColoredRect(matrixStack.last().pose(), this.dotsBufferBuilder, dotOffset, dotOffset, dotTextureX, dotTextureY, dotTextureW, dotTextureH, r, g, b, a, 256.0f);
        return dotOffset;
    }

    private void renderLabel(Entity e, Entity renderEntity, boolean name, double optionalScale, PoseStack matrixStack) {
        String yValueString;
        double dotNameScale = this.labelScale * optionalScale;
        matrixStack.scale((float) dotNameScale, (float) dotNameScale, 1.0f);
        String yValueString2 = null;
        if (this.displayY > 0) {
            int yInt = (int) Math.floor(e.getY());
            int pYInt = (int) Math.floor(renderEntity.getY());
            if (this.displayY == 1) {
                yValueString = yInt;
            } else if (this.displayY == 2) {
                yValueString = (yInt - pYInt);
            } else {
                yValueString = "";
            }
            yValueString2 = yValueString + (yInt > pYInt ? "↑" : yInt != pYInt ? "↓" : "");
            if (yValueString2.length() == 0) {
                yValueString2 = "-";
            }
        }
        Font font = Minecraft.getInstance().font;
        String label = null;
        if (name) {
            Component component = Misc.getFixedDisplayName(e);
            if (component == null) {
                return;
            }
            label = component.getString();
            if (this.displayY > 0) {
                label = label + "(" + yValueString2 + ")";
            }
        } else if (this.displayY > 0) {
            label = yValueString2;
        }
        if (label == null) {
            return;
        }
        int labelW = font.width(label);
        RenderBufferUtil.addColoredRect(matrixStack.last().pose(), this.labelBgBuilder, ((-labelW) / 2) - 2, -1.0f, labelW + 3, 10, 0.0f, 0.0f, 0.0f, 0.3529412f);
        Misc.drawNormalText(matrixStack, label, (-labelW) / 2, 0.0f, -1, false, this.minimapBufferSource);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setupRenderForList(RadarList radarList) {
        if (!this.radarRenderProvider.isUsed()) {
            this.radarRenderProvider.setupContextForList(radarList, (RadarRenderContext) this.context);
        }
        this.heightLimit = ((Double) radarList.getEffective(EntityRadarCategorySettings.HEIGHT_LIMIT)).intValue();
        this.heightBasedFade = ((Boolean) radarList.getEffective(EntityRadarCategorySettings.HEIGHT_FADE)).booleanValue();
        this.startFadingAt = ((Double) radarList.getEffective(EntityRadarCategorySettings.START_FADING_AT)).intValue();
        this.displayNameWhenIconFails = ((Boolean) radarList.getEffective(EntityRadarCategorySettings.ICON_NAME_FALLBACK)).booleanValue();
        this.alwaysNameTags = ((Boolean) radarList.getEffective(EntityRadarCategorySettings.ALWAYS_NAMETAGS)).booleanValue();
        this.radarColor = RadarColor.fromIndex(((Double) radarList.getEffective(EntityRadarCategorySettings.COLOR)).intValue());
        this.fallbackColor = this.radarSession.getColorHelper().getFallbackColor(radarList);
        this.displayY = ((Double) radarList.getEffective(EntityRadarCategorySettings.DISPLAY_Y)).intValue();
        this.nameSettingForCategory = ((Double) radarList.getEffective(EntityRadarCategorySettings.NAMES)).intValue();
        this.namesForCategory = (this.nameSettingForCategory == 1 && ((RadarRenderContext) this.context).playerListDown) || this.nameSettingForCategory == 2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setupRenderForEntity(Entity entity) {
        if (!this.radarRenderProvider.isUsed()) {
            this.radarRenderProvider.setupContextForEntity(entity, (RadarRenderContext) this.context);
        }
        boolean name = this.namesForCategory;
        if (!name && !(entity instanceof Player)) {
            name = this.alwaysNameTags && entity.hasCustomName();
        }
        this.name = name;
    }

    private void confirmTrackedPlayerRadarRender(Player e) {
        if (HudMod.INSTANCE.getTrackedPlayerRenderer().getCollector().playerExists(e.getUUID())) {
            HudMod.INSTANCE.getTrackedPlayerRenderer().getCollector().confirmPlayerRadarRender(e);
        }
        if (!HudMod.INSTANCE.getSupportMods().worldmap()) {
            return;
        }
        HudMod.INSTANCE.getSupportMods().worldmapSupport.confirmPlayerRadarRender(e);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void renderSingleEntity(Entity entity, boolean cave, boolean highlighted, float optionalScale, boolean allowIcon, boolean allowLabel, MinimapElementRenderLocation location, RenderTarget defaultFramebuffer, GuiGraphics guiGraphics) {
        ((RadarRenderContext) this.context).radarList = null;
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapElementRenderInfo renderInfo = new MinimapElementRenderInfo(location, entity, Minecraft.getInstance().player, entity.position(), cave, 1.0f, defaultFramebuffer, 1.0d, entity.level().dimension());
        MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = session.getMultiTextureRenderTypeRenderers();
        MultiBufferSource.BufferSource vanillaBufferSource = guiGraphics.bufferSource();
        ((RadarRenderContext) this.context).isMainDot = entity == Minecraft.getInstance().getCameraEntity();
        preRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
        this.iconsAllowed = allowIcon;
        this.labelsAllowed = allowLabel;
        renderElement(entity, highlighted, false, 0.0d, optionalScale, 0.0d, 0.0d, renderInfo, guiGraphics, vanillaBufferSource);
        postRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
        ((RadarRenderContext) this.context).isMainDot = false;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean shouldRender(MinimapElementRenderLocation location) {
        if (!this.minimap.usingFBO()) {
            return false;
        }
        if (location == MinimapElementRenderLocation.WORLD_MAP || location == MinimapElementRenderLocation.WORLD_MAP_MENU) {
            return true;
        }
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        return ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DISPLAY_RADAR)).booleanValue();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/render/element/RadarRenderer$Builder.class */
    public static final class Builder {
        private RadarIconManager radarIconManager;
        private Minimap minimap;

        private Builder() {
        }

        public Builder setDefault() {
            setRadarIconManager(null);
            return this;
        }

        public Builder setRadarIconManager(RadarIconManager radarIconManager) {
            this.radarIconManager = radarIconManager;
            return this;
        }

        public Builder setMinimap(Minimap minimap) {
            this.minimap = minimap;
            return this;
        }

        public RadarRenderer build() {
            if (this.radarIconManager == null || this.minimap == null) {
                throw new IllegalStateException();
            }
            RadarElementReader elementReader = new xaero.common.minimap.render.radar.element.RadarElementReader();
            RadarRenderProvider provider = new xaero.common.minimap.render.radar.element.RadarRenderProvider();
            RadarRenderContext context = new xaero.common.minimap.render.radar.element.RadarRenderContext();
            return new xaero.common.minimap.render.radar.element.RadarRenderer(this.radarIconManager, this.minimap, elementReader, provider, context);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
