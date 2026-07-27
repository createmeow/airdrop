package xaero.hud.minimap.radar.icon.creator.render.trace;

import java.util.HashMap;
import java.util.Set;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import xaero.common.graphics.CustomRenderTypes;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/trace/ModelRenderTrace.class */
public class ModelRenderTrace {
    public final EntityModel<?> model;
    public final ResourceLocation renderTexture;
    public final TextureAtlasSprite renderAtlasSprite;
    public final CustomRenderTypes.EntityIconLayerPhases layerPhases;
    public int color;
    public boolean allVisible;
    private HashMap<ModelPart, ModelPartRenderTrace> visibleParts;

    public ModelRenderTrace(EntityModel<?> model, ResourceLocation renderTexture, TextureAtlasSprite renderAtlasSprite, CustomRenderTypes.EntityIconLayerPhases layerPhases, int color) {
        this.model = model;
        this.renderTexture = renderTexture;
        this.renderAtlasSprite = renderAtlasSprite;
        this.layerPhases = layerPhases;
        this.color = color;
    }

    public String toString() {
        return String.valueOf(this.model) + " " + String.valueOf(this.layerPhases.texture);
    }

    public void addVisibleModelPart(ModelPart part, int color) {
        if (this.visibleParts == null) {
            this.visibleParts = new HashMap<>();
        }
        this.visibleParts.put(part, new ModelPartRenderTrace(part, color));
    }

    public ModelPartRenderTrace getModelPartRenderInfo(ModelPart part) {
        ModelPartRenderTrace mprdi = this.visibleParts == null ? null : this.visibleParts.get(part);
        if (mprdi == null && this.allVisible) {
            mprdi = new ModelPartRenderTrace(part, this.color);
        }
        return mprdi;
    }

    public boolean isEmpty() {
        return !this.allVisible && (this.visibleParts == null || this.visibleParts.isEmpty());
    }

    public boolean sameVisibility(ModelRenderTrace other) {
        HashMap<ModelPart, ModelPartRenderTrace> otherVisibleParts = other.visibleParts;
        if ((this.visibleParts == null) != (otherVisibleParts == null)) {
            return false;
        }
        if (this.visibleParts == null) {
            return true;
        }
        if (this.visibleParts.size() != otherVisibleParts.size()) {
            return false;
        }
        Set<ModelPart> keySet = this.visibleParts.keySet();
        for (ModelPart key : keySet) {
            if (!otherVisibleParts.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
