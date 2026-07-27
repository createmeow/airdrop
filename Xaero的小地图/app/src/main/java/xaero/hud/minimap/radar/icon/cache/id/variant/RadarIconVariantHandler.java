package xaero.hud.minimap.radar.icon.cache.id.variant;

import java.lang.reflect.Method;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.definition.BuiltInRadarIconDefinitions;
import xaero.hud.minimap.radar.icon.definition.RadarIconDefinition;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/variant/RadarIconVariantHandler.class */
public class RadarIconVariantHandler {
    private final StringBuilder legacyEntityStringBuilder = new StringBuilder();

    public <T extends Entity> Object getEntityVariant(RadarIconDefinition iconDefinition, T entity, EntityRenderer<? super T> entityRenderer) {
        Object variant = null;
        ResourceLocation entityTexture = null;
        try {
            ResourceLocation entityTextureUnchecked = entityRenderer.getTextureLocation(entity);
            entityTexture = entityTextureUnchecked;
        } catch (Throwable e) {
            MinimapLogs.LOGGER.error("Exception while fetching entity texture to build its variant ID for " + String.valueOf(EntityType.getKey(entity.getType())));
            MinimapLogs.LOGGER.error("The exception is most likely on another mod's end and suppressing it here could lead to more issues. Please report to appropriate mod devs.", e);
        }
        if (iconDefinition != null) {
            Method variantMethod = iconDefinition.getVariantMethod();
            if (variantMethod != null) {
                try {
                    variant = variantMethod.invoke(null, entityTexture, entityRenderer, entity);
                } catch (Throwable e2) {
                    ResourceLocation entityId = EntityType.getKey(entity.getType());
                    MinimapLogs.LOGGER.error("Exception while using the variant ID method " + iconDefinition.getVariantMethodString() + " defined for " + String.valueOf(entityId));
                    MinimapLogs.LOGGER.error("If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", e2);
                    iconDefinition.setVariantMethod(null);
                }
            } else {
                variant = getLegacyVariantId(iconDefinition, entity, entityRenderer);
            }
        }
        if (variant == null) {
            variant = BuiltInRadarIconDefinitions.getVariant(entityTexture, entityRenderer, entity);
        }
        return variant;
    }

    private <T extends Entity> String getLegacyVariantId(RadarIconDefinition iconDefinition, T entity, EntityRenderer<? super T> entityRenderer) {
        Method variantIdBuilderMethod = iconDefinition.getVariantIdBuilderMethod();
        if (variantIdBuilderMethod != null && !variantIdBuilderMethod.equals(BuiltInRadarIconDefinitions.BUILD_VARIANT_ID_STRING_METHOD)) {
            this.legacyEntityStringBuilder.setLength(0);
            try {
                variantIdBuilderMethod.invoke(null, this.legacyEntityStringBuilder, entityRenderer, entity);
                return this.legacyEntityStringBuilder.toString();
            } catch (Throwable e) {
                ResourceLocation entityId = EntityType.getKey(entity.getType());
                MinimapLogs.LOGGER.error("Exception while using the variant builder ID method " + iconDefinition.getVariantIdBuilderMethodString() + " defined for " + String.valueOf(entityId));
                MinimapLogs.LOGGER.error("If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", e);
                iconDefinition.setVariantIdBuilderMethod(null);
                return null;
            }
        }
        Method variantOldIdMethod = iconDefinition.getOldVariantIdMethod();
        if (variantOldIdMethod == null || variantOldIdMethod.equals(BuiltInRadarIconDefinitions.GET_VARIANT_ID_STRING_METHOD)) {
            return null;
        }
        try {
            return (String) variantOldIdMethod.invoke(null, entityRenderer, entity);
        } catch (Throwable e2) {
            ResourceLocation entityId2 = EntityType.getKey(entity.getType());
            MinimapLogs.LOGGER.error("Exception while using the variant ID method " + iconDefinition.getOldVariantIdMethodString() + " defined for " + String.valueOf(entityId2));
            MinimapLogs.LOGGER.error("If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", e2);
            iconDefinition.setOldVariantIdMethod(null);
            return null;
        }
    }
}
