package xaero.hud.minimap.radar.category.rule;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.hud.minimap.radar.util.RadarUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/rule/EntityRadarListRuleTypes.class */
public class EntityRadarListRuleTypes {
    public static final List<ObjectCategoryListRuleType<Entity, Player, ?>> TYPE_LIST = EntityRadarCategoryConstants.LIST_FACTORY.get();
    public static final Map<String, ObjectCategoryListRuleType<Entity, Player, ?>> TYPE_MAP = EntityRadarCategoryConstants.MAP_FACTORY.get();
    public static final ObjectCategoryListRuleType<Entity, Player, EntityType<?>> ENTITY_TYPE = new ObjectCategoryListRuleType<>("entity", (e, p) -> {
        return e.getType();
    }, () -> {
        return BuiltInRegistries.ENTITY_TYPE;
    }, EntityRadarCategoryConstants.getDefaultElementResolver(BuiltInRegistries.ENTITY_TYPE, s -> {
        return (EntityType) EntityType.byString(s).orElse(null);
    }, EntityType::getKey), EntityRadarCategoryConstants.DEFAULT_LIST_SERIALIZER, EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR_FIXER, EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR, TYPE_LIST, TYPE_MAP);
    public static final ObjectCategoryListRuleType<Entity, Player, Item> ITEM_TYPE;
    public static final ObjectCategoryListRuleType<Entity, Player, String> PLAYER_NAME;
    public static final ObjectCategoryListRuleType<Entity, Player, String> CUSTOM_NAME;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> LIVING;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> HOSTILE;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> TAMED;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> SAME_TEAM;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> BABY;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> VANILLA;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> ABOVE_GROUND;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> MY_GROUND;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> LIT;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> HAS_CUSTOM_NAME;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> IN_TEAM;
    public static final ObjectCategoryListRuleType<Entity, Player, Boolean> TRACKED;

    static {
        BiFunction biFunction = (e, p) -> {
            if (e instanceof ItemEntity) {
                return ((ItemEntity) e).getItem().getItem();
            }
            return null;
        };
        Supplier supplier = () -> {
            return BuiltInRegistries.ITEM;
        };
        DefaultedRegistry defaultedRegistry = BuiltInRegistries.ITEM;
        Function function = s -> {
            return (Item) BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(s)).orElse(null);
        };
        DefaultedRegistry defaultedRegistry2 = BuiltInRegistries.ITEM;
        Objects.requireNonNull(defaultedRegistry2);
        ITEM_TYPE = new ObjectCategoryListRuleType<>("item", biFunction, supplier, EntityRadarCategoryConstants.getDefaultElementResolver(defaultedRegistry, function, (v1) -> {
            return r7.getKey(v1);
        }), item -> {
            return BuiltInRegistries.ITEM.getKey(item).toString();
        }, EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR_FIXER, EntityRadarCategoryConstants.DEFAULT_LIST_STRING_VALIDATOR, TYPE_LIST, TYPE_MAP);
        PLAYER_NAME = new ObjectCategoryListRuleType<>("player", (e2, p2) -> {
            if (e2 instanceof Player) {
                return ((Player) e2).getGameProfile().getName();
            }
            return null;
        }, () -> {
            if (Minecraft.getInstance().getConnection() == null) {
                return new ArrayList();
            }
            Stream map = Minecraft.getInstance().getConnection().getOnlinePlayers().stream().map(pi -> {
                return pi.getProfile().getName();
            });
            Objects.requireNonNull(map);
            return map::iterator;
        }, xva$0 -> {
            return Lists.newArrayList(new String[]{xva$0});
        }, Function.identity(), EntityRadarCategoryConstants.PLAYER_NAME_VALIDATOR_FIXER, EntityRadarCategoryConstants.PLAYER_NAME_VALIDATOR, TYPE_LIST, TYPE_MAP);
        CUSTOM_NAME = new ObjectCategoryListRuleType<>("custom-name", (e3, p3) -> {
            return RadarUtils.getCustomName(e3, false);
        }, () -> {
            Iterable<Entity> entities;
            if (Minecraft.getInstance().level != null && (entities = Minecraft.getInstance().level.entitiesForRendering()) != null) {
                Stream<String> nameStream = StreamSupport.stream(entities.spliterator(), false).map(e4 -> {
                    return RadarUtils.getCustomName(e4, true);
                }).filter((v0) -> {
                    return Objects.nonNull(v0);
                });
                Iterator<String> iterator = nameStream.iterator();
                return !iterator.hasNext() ? Lists.newArrayList(new String[]{"example"}) : () -> {
                    return iterator;
                };
            }
            return Lists.newArrayList(new String[]{"example"});
        }, xva$02 -> {
            return Lists.newArrayList(new String[]{xva$02});
        }, Function.identity(), s2 -> {
            return s2;
        }, s3 -> {
            return true;
        }, TYPE_LIST, TYPE_MAP);
        LIVING = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_LIVING, TYPE_LIST, TYPE_MAP);
        HOSTILE = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_HOSTILE, TYPE_LIST, TYPE_MAP);
        TAMED = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_TAMED, TYPE_LIST, TYPE_MAP);
        SAME_TEAM = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_SAME_TEAM, TYPE_LIST, TYPE_MAP);
        BABY = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_BABY, TYPE_LIST, TYPE_MAP);
        VANILLA = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_VANILLA, TYPE_LIST, TYPE_MAP);
        ABOVE_GROUND = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_ABOVE_GROUND, TYPE_LIST, TYPE_MAP);
        MY_GROUND = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_MY_GROUND, TYPE_LIST, TYPE_MAP);
        LIT = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_LIT, TYPE_LIST, TYPE_MAP);
        HAS_CUSTOM_NAME = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.HAS_CUSTOM_NAME, TYPE_LIST, TYPE_MAP);
        IN_TEAM = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_IN_TEAM, TYPE_LIST, TYPE_MAP);
        TRACKED = EntityRadarCategoryConstants.createHardRuleBasedPredicateListRuleType(EntityRadarCategoryHardRules.IS_TRACKED, TYPE_LIST, TYPE_MAP);
    }
}
