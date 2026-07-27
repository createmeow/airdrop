package xaero.hud.minimap.radar.icon.definition.form.item;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import xaero.hud.minimap.radar.icon.definition.RadarIconDefinition;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconBasicForms;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconForm;
import xaero.hud.minimap.radar.icon.definition.form.type.RadarIconFormType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/form/item/RadarIconItemForm.class */
public class RadarIconItemForm extends RadarIconForm {
    private final ResourceLocation itemKey;

    public RadarIconItemForm(RadarIconFormType type, @Nullable ResourceLocation itemKey) {
        super(type);
        this.itemKey = itemKey;
    }

    public ResourceLocation getItemKey() {
        return this.itemKey;
    }

    public static RadarIconItemForm read(RadarIconFormType type, String[] args, RadarIconDefinition iconDefinition) {
        if (args.length == 1) {
            return RadarIconBasicForms.SELF_ITEM;
        }
        if (args.length > 3) {
            return null;
        }
        ResourceLocation itemKey = args.length == 2 ? ResourceLocation.parse(args[1]) : ResourceLocation.fromNamespaceAndPath(args[1], args[2]);
        return new RadarIconItemForm(type, itemKey);
    }
}
