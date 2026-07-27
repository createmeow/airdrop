package xaero.hud.minimap.radar.icon.definition;

import com.google.gson.annotations.Expose;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconBasicForms;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconForm;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;
import xaero.hud.minimap.radar.icon.definition.form.type.RadarIconFormType;
import xaero.hud.minimap.radar.icon.definition.form.type.RadarIconFormTypes;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/RadarIconDefinition.class */
public class RadarIconDefinition {
    private ResourceLocation entityId;

    @Expose
    private HashMap<String, String> variants;

    @Expose
    private ArrayList<RadarIconModelConfig> modelConfigs;
    private HashMap<String, RadarIconForm> variantForms;

    @Expose
    private String variantMethod;
    private Method variantMethodReflect;

    @Expose
    private String variantIdMethod;
    private Method variantIdMethodReflect;

    @Expose
    private String variantIdBuilderMethod;
    private Method variantIdBuilderMethodReflect;

    public RadarIconForm getVariantForm(String variantId) {
        if (this.variantForms == null) {
            return RadarIconBasicForms.DEFAULT_MODEL;
        }
        return this.variantForms.get(variantId);
    }

    public void construct(ResourceLocation entityId) {
        this.entityId = entityId;
        if (this.variantMethod != null) {
            this.variantMethodReflect = convertStringToMethod(this.variantMethod, entityId.toString(), "variant", null, ResourceLocation.class, EntityRenderer.class, Entity.class);
        }
        if (this.variantIdBuilderMethod != null) {
            this.variantIdBuilderMethodReflect = convertStringToMethod(this.variantIdBuilderMethod, entityId.toString(), "variant ID builder", Void.TYPE, StringBuilder.class, EntityRenderer.class, Entity.class);
        }
        if (this.variantIdMethod != null) {
            this.variantIdMethodReflect = convertStringToMethod(this.variantIdMethod, entityId.toString(), "variant ID", String.class, EntityRenderer.class, Entity.class);
        }
        if (this.variants == null) {
            return;
        }
        for (Map.Entry<String, String> entry : this.variants.entrySet()) {
            String value = entry.getValue();
            RadarIconForm form = constructForm(value);
            if (form == null) {
                MinimapLogs.LOGGER.info("Skipping invalid icon form: " + value + " for " + String.valueOf(entityId));
            } else {
                if (this.variantForms == null) {
                    this.variantForms = new HashMap<>();
                }
                this.variantForms.put(entry.getKey(), form);
            }
        }
        if (this.variantForms == null || this.variantForms.containsKey("default")) {
            return;
        }
        this.variantForms.put("default", RadarIconBasicForms.DEFAULT_MODEL);
    }

    private RadarIconForm constructForm(String value) {
        String[] valueSplit = value.split(":");
        RadarIconFormType formType = RadarIconFormTypes.readType(valueSplit[0]);
        if (formType == null) {
            return null;
        }
        return formType.readForm(this, valueSplit);
    }

    public String getVariantMethodString() {
        return this.variantMethod;
    }

    public Method getVariantMethod() {
        return this.variantMethodReflect;
    }

    public void setVariantMethod(Method variantMethod) {
        this.variantMethodReflect = variantMethod;
    }

    public String getVariantIdBuilderMethodString() {
        return this.variantIdBuilderMethod;
    }

    public Method getVariantIdBuilderMethod() {
        return this.variantIdBuilderMethodReflect;
    }

    public void setVariantIdBuilderMethod(Method variantIdBuilderMethodReflect) {
        this.variantIdBuilderMethodReflect = variantIdBuilderMethodReflect;
    }

    private Method convertStringToMethod(String methodPath, String entityId, String methodDisplayName, Class<?> returnType, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (methodPath == null) {
            return null;
        }
        Method result = null;
        int lastDot = methodPath.lastIndexOf(46);
        String classPath = methodPath.substring(0, lastDot);
        String methodName = methodPath.substring(lastDot + 1);
        try {
            Class<?> c = Class.forName(classPath);
            result = c.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            MinimapLogs.LOGGER.error(String.format("Could not find %s method %s defined for %s", methodDisplayName, methodPath, entityId), e);
        }
        if (returnType == null) {
            return result;
        }
        if (result.getReturnType() != returnType) {
            MinimapLogs.LOGGER.info(String.format("The return type of the %s method for %s is not %s. Can't use it.", methodDisplayName, entityId, returnType));
            return null;
        }
        return result;
    }

    public String getOldVariantIdMethodString() {
        return this.variantIdMethod;
    }

    public Method getOldVariantIdMethod() {
        return this.variantIdMethodReflect;
    }

    public void setOldVariantIdMethod(Method variantIdMethodReflect) {
        this.variantIdMethodReflect = variantIdMethodReflect;
    }

    public RadarIconModelConfig getModelConfig(int index) {
        if (this.modelConfigs == null || index < 0 || index >= this.modelConfigs.size()) {
            return null;
        }
        return this.modelConfigs.get(index);
    }

    public ResourceLocation getEntityId() {
        return this.entityId;
    }
}
