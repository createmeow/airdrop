package xaero.hud.minimap.radar.icon.cache.id.armor;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/id/armor/RadarIconArmorHandler.class */
public class RadarIconArmorHandler {
    public RadarIconArmor getArmor(LivingEntity livingEntity) {
        EquipmentSlot relevantArmourSlot = livingEntity instanceof Horse ? EquipmentSlot.CHEST : EquipmentSlot.HEAD;
        ItemStack armorItemStack = livingEntity.getItemBySlot(relevantArmourSlot);
        if (armorItemStack == null || armorItemStack == ItemStack.EMPTY) {
            return null;
        }
        Item armorItem = armorItemStack.getItem();
        if (!armorItemStack.is(ItemTags.TRIMMABLE_ARMOR)) {
            return new RadarIconArmor(armorItem, null, null);
        }
        if (!armorItemStack.has(DataComponents.TRIM)) {
            return new RadarIconArmor(armorItem, null, null);
        }
        ArmorTrim trim = (ArmorTrim) armorItemStack.get(DataComponents.TRIM);
        TrimMaterial trimMaterial = (TrimMaterial) trim.material().value();
        TrimPattern trimPattern = (TrimPattern) trim.pattern().value();
        return new RadarIconArmor(armorItem, trimMaterial, trimPattern);
    }
}
