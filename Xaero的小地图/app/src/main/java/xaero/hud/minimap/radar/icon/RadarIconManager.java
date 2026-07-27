package xaero.hud.minimap.radar.icon;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;
import xaero.common.exception.OpenGLException;
import xaero.common.icon.XaeroIcon;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.cache.RadarIconCache;
import xaero.hud.minimap.radar.icon.cache.RadarIconEntityCache;
import xaero.hud.minimap.radar.icon.cache.id.RadarIconKey;
import xaero.hud.minimap.radar.icon.cache.id.armor.RadarIconArmor;
import xaero.hud.minimap.radar.icon.cache.id.armor.RadarIconArmorHandler;
import xaero.hud.minimap.radar.icon.cache.id.variant.RadarIconVariantHandler;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.hud.minimap.radar.icon.definition.RadarIconDefinition;
import xaero.hud.minimap.radar.icon.definition.RadarIconDefinitionManager;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconBasicForms;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconForm;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/RadarIconManager.class */
public class RadarIconManager {
    public static final XaeroIcon FAILED = new XaeroIcon(null, 0, 0);
    public static final XaeroIcon DOT = new XaeroIcon(null, 0, 0);
    private boolean canPrerender;
    private final RadarIconCreator iconCreator;
    private final RadarIconModelConfig defaultModelConfig;
    private final RadarIconArmorHandler armorHandler;
    private final RadarIconDefinitionManager definitionManager = new RadarIconDefinitionManager();
    private final RadarIconVariantHandler variantHandler = new RadarIconVariantHandler();
    private final RadarIconCache iconCache = new RadarIconCache();

    public RadarIconManager(RadarIconCreator iconCreator) {
        this.iconCreator = iconCreator;
        this.definitionManager.reloadResources();
        this.defaultModelConfig = new RadarIconModelConfig();
        this.armorHandler = new RadarIconArmorHandler();
    }

    public <T extends Entity> XaeroIcon get(T entity, float scale, boolean debug, boolean debugEntityVariantIds, GuiGraphics guiGraphics, RenderTarget defaultFramebuffer) throws IllegalAccessException, NoSuchMethodException, SecurityException, OpenGLException, IllegalArgumentException {
        RadarIconForm iconForm;
        EntityType<?> entityType = entity.getType();
        RadarIconDefinition iconDefinition = this.definitionManager.get(EntityType.getKey(entityType));
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super T> entityRenderer = renderManager.getRenderer(entity);
        Object variant = this.variantHandler.getEntityVariant(iconDefinition, entity, entityRenderer);
        while (GL11.glGetError() != 0) {
        }
        if (variant == null) {
            return null;
        }
        RadarIconArmor armor = null;
        if ((entity instanceof LivingEntity) && !(entity instanceof Player)) {
            armor = this.armorHandler.getArmor((LivingEntity) entity);
        }
        RadarIconEntityCache entityIconCache = this.iconCache.getEntityCache(entityType);
        RadarIconKey iconKey = new RadarIconKey(variant, armor);
        XaeroIcon cachedValue = entityIconCache.get(iconKey);
        if (entityIconCache.isInvalidVariantClass()) {
            return FAILED;
        }
        if (cachedValue != null) {
            return cachedValue;
        }
        String entityVariantString = entityIconCache.getVariantString(iconKey);
        if (iconDefinition != null) {
            iconForm = entityVariantString == null ? null : iconDefinition.getVariantForm(entityVariantString);
            if (iconForm == null) {
                iconForm = iconDefinition.getVariantForm("default");
            }
        } else {
            iconForm = entity instanceof LivingEntity ? RadarIconBasicForms.DEFAULT_MODEL : RadarIconBasicForms.SELF_ITEM;
        }
        if (debugEntityVariantIds && entityVariantString != null && (this.canPrerender || iconForm == RadarIconBasicForms.DOT)) {
            Minecraft.getInstance().gui.getChat().addMessage(Component.literal(entityVariantString));
        }
        if (iconForm == RadarIconBasicForms.DOT) {
            entityIconCache.add(iconKey, DOT);
            return DOT;
        }
        if (!this.canPrerender) {
            return null;
        }
        RadarIconCreator.Parameters parameters = new RadarIconCreator.Parameters(variant, this.defaultModelConfig, iconForm, scale, debug);
        XaeroIcon cachedValue2 = this.iconCreator.create(guiGraphics, entityRenderer, entity, defaultFramebuffer, parameters);
        entityIconCache.add(iconKey, cachedValue2);
        this.canPrerender = false;
        return cachedValue2;
    }

    public void reset() {
        this.iconCreator.clearAtlases();
        this.iconCache.clear();
        MinimapLogs.LOGGER.info("Radar icon manager reset!");
    }

    public void resetResources() {
        this.definitionManager.reloadResources();
    }

    public void allowPrerender() {
        this.canPrerender = true;
    }

    public void onModelRenderTrace(EntityModel<?> model, VertexConsumer vertexConsumer, int color) throws IllegalAccessException, IllegalArgumentException {
        this.iconCreator.getRenderTracer().onModelRender(model, vertexConsumer, color);
    }

    public void onModelPartRenderTrace(ModelPart modelRenderer, int color) {
        this.iconCreator.getRenderTracer().onModelPartRender(modelRenderer, color);
    }
}
