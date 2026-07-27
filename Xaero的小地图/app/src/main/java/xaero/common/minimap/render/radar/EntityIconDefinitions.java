package xaero.common.minimap.render.radar;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.definition.BuiltInRadarIconDefinitions;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/render/radar/EntityIconDefinitions.class */
public class EntityIconDefinitions {
    private static StringBuilder VARIANT_STRING_BUILDER = new StringBuilder();

    public static <E extends Entity> Object getVariant(ResourceLocation entityTexture, EntityRenderer<? super E> entityRenderer, E entity) {
        return BuiltInRadarIconDefinitions.getVariant(entityTexture, entityRenderer, entity);
    }

    public static void buildVariantIdString(StringBuilder stringBuilder, EntityRenderer entityRenderer, Entity entity) {
        ResourceLocation entityTexture = null;
        try {
            ResourceLocation entityTextureUnchecked = entityRenderer.getTextureLocation(entity);
            entityTexture = entityTextureUnchecked;
        } catch (Throwable e) {
            MinimapLogs.LOGGER.error("Exception while fetching entity texture to build its variant ID for " + String.valueOf(EntityType.getKey(entity.getType())));
            MinimapLogs.LOGGER.error("The exception is most likely on another mod's end and suppressing it here could lead to more issues. Please report to appropriate mod devs.", e);
        }
        if (entityTexture == null) {
            return;
        }
        stringBuilder.append(getVariant(entityTexture, entityRenderer, entity));
    }

    public static String getVariantString(EntityRenderer entityRenderer, Entity entity) {
        StringBuilder stringBuilder = VARIANT_STRING_BUILDER;
        stringBuilder.setLength(0);
        buildVariantIdString(stringBuilder, entityRenderer, entity);
        return stringBuilder.toString();
    }
}
