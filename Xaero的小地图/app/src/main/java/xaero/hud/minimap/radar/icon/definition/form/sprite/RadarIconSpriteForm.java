package xaero.hud.minimap.radar.icon.definition.form.sprite;

import net.minecraft.resources.ResourceLocation;
import xaero.hud.minimap.radar.icon.definition.RadarIconDefinition;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconForm;
import xaero.hud.minimap.radar.icon.definition.form.type.RadarIconFormType;
import xaero.minimap.XaeroMinimap;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/definition/form/sprite/RadarIconSpriteForm.class */
public class RadarIconSpriteForm extends RadarIconForm {
    private final ResourceLocation spriteLocation;

    public RadarIconSpriteForm(RadarIconFormType type, ResourceLocation spriteLocation) {
        super(type);
        this.spriteLocation = spriteLocation;
    }

    public ResourceLocation getSpriteLocation() {
        return this.spriteLocation;
    }

    public static RadarIconSpriteForm read(RadarIconFormType type, String[] args, RadarIconDefinition iconDefinition) {
        if (args.length != 2) {
            return null;
        }
        ResourceLocation sprite = ResourceLocation.fromNamespaceAndPath(XaeroMinimap.MOD_ID, "entity/icon/sprite/" + args[1]);
        return new RadarIconSpriteForm(type, sprite);
    }
}
