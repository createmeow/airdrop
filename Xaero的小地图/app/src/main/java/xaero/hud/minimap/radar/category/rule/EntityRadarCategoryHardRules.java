package xaero.hud.minimap.radar.category.rule;

import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.XaeroMinimapCore;
import xaero.hud.category.rule.ObjectCategoryHardRule;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.hud.minimap.radar.util.RadarUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/rule/EntityRadarCategoryHardRules.class */
public final class EntityRadarCategoryHardRules {
    public static final List<ObjectCategoryHardRule<Entity, Player>> HARD_RULES_LIST = EntityRadarCategoryConstants.LIST_FACTORY.get();
    public static final Map<String, ObjectCategoryHardRule<Entity, Player>> HARD_RULES = EntityRadarCategoryConstants.MAP_FACTORY.get();
    public static final ObjectCategoryHardRule<Entity, Player> IS_NOTHING = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_NOTHING).setPredicate((e, p) -> {
        return false;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_ANYTHING = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_ANYTHING).setPredicate((e, p) -> {
        return true;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_LIVING = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_LIVING).setPredicate((e, p) -> {
        return e instanceof LivingEntity;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_PLAYER = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_PLAYERS).setPredicate((e, p) -> {
        return e instanceof Player;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_SAME_TEAM = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_SAME_TEAM).setPredicate((e, p) -> {
        return p.getTeam() == e.getTeam();
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_HOSTILE = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_HOSTILE).setPredicate((e, p) -> {
        return RadarUtils.isHostile(e);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_TAMED = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_TAMED).setPredicate((e, p) -> {
        return RadarUtils.isTamed(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_ITEM = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_ITEMS).setPredicate((e, p) -> {
        return e instanceof ItemEntity;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_FRIENDLY = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_FRIENDLY).setPredicate((e, p) -> {
        return !IS_HOSTILE.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_OTHER_TEAMS = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_OTHER_TEAMS).setPredicate((e, p) -> {
        return !IS_SAME_TEAM.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_BABY = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_BABY).setPredicate((e, p) -> {
        return (e instanceof LivingEntity) && ((LivingEntity) e).isBaby();
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_VANILLA = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_VANILLA).setPredicate((e, p) -> {
        EntityType<?> type = e.getType();
        ResourceLocation resourceLocation = type == null ? null : EntityType.getKey(type);
        if (resourceLocation == null) {
            return false;
        }
        return resourceLocation.getNamespace().equals("minecraft");
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_MODDED = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_MODDED).setPredicate((e, p) -> {
        return !IS_VANILLA.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_ABOVE_GROUND = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_ABOVE_GROUND).setPredicate((e, p) -> {
        return e.level().getBrightness(LightLayer.SKY, e.blockPosition()) == 15;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_BELOW_GROUND = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_BELOW_GROUND).setPredicate((e, p) -> {
        return !IS_ABOVE_GROUND.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_MY_GROUND = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_MY_GROUND).setPredicate((e, p) -> {
        if (Minecraft.getInstance().cameraEntity == null) {
            return true;
        }
        boolean caveMode = XaeroMinimapSession.getCurrentSession().getMinimapProcessor().isCaveModeDisplayed();
        return IS_ABOVE_GROUND.isFollowedBy(e, p) == (!caveMode);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_NOT_MY_GROUND = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_NOT_MY_GROUND).setPredicate((e, p) -> {
        return !IS_MY_GROUND.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_LIT = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_LIT).setPredicate((e, p) -> {
        return e.level().getBrightness(LightLayer.BLOCK, e.blockPosition()) > 0;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_UNLIT = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_UNLIT).setPredicate((e, p) -> {
        return !IS_LIT.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> HAS_CUSTOM_NAME = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_CUSTOM_NAME).setPredicate((e, p) -> {
        return e.hasCustomName();
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> NO_CUSTOM_NAME = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_NO_CUSTOM_NAME).setPredicate((e, p) -> {
        return !HAS_CUSTOM_NAME.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_TRACKED = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_TRACKED).setPredicate((e, p) -> {
        if (!(e instanceof Player)) {
            return false;
        }
        return XaeroMinimapCore.modMain.getTrackedPlayerRenderer().getCollector().playerExists(e.getUUID());
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_IN_TEAM = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_IN_TEAM).setPredicate((e, p) -> {
        return e.getTeam() != null;
    }).build(HARD_RULES, HARD_RULES_LIST);
    public static final ObjectCategoryHardRule<Entity, Player> IS_TEAMLESS = new ObjectCategoryHardRule.Builder().setName(EntityRadarCategoryConstants.HARD_TEAMLESS).setPredicate((e, p) -> {
        return !IS_IN_TEAM.isFollowedBy(e, p);
    }).build(HARD_RULES, HARD_RULES_LIST);
}
