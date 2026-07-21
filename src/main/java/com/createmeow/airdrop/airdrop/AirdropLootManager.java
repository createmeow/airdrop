package com.createmeow.airdrop.airdrop;

import com.createmeow.airdrop.airDrop;
import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AirdropLootManager {
    private static final Map<AirdropData.Tier, List<LootEntry>> LOOT_TABLES = new HashMap<>();
    private static final Path CONFIG_DIR = Path.of("config", airDrop.MODID);
    private static final Path LOOT_FILE = CONFIG_DIR.resolve("loot.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Random RANDOM = new Random();

    private static final Set<String> BASECORE_PART_ITEMS = Set.of(
            "basecore:part",
            "basecore:small_part_bundle",
            "basecore:medium_part_bundle",
            "basecore:large_part_bundle"
    );
    private static final Set<String> BASECORE_MODULE_ITEMS = Set.of(
            "basecore:module_substrate",
            "basecore:range_module",
            "basecore:secure_module",
            "basecore:def_module",
            "basecore:auto_repair_module",
            "basecore:thorns_module",
            "basecore:exp_def_module",
            "basecore:basecore_counter_reconnaissance_module",
            "basecore:strength_module",
            "basecore:jump_boost_module",
            "basecore:regeneration_module",
            "basecore:resistance_module",
            "basecore:dig_speed_module",
            "basecore:movement_speed_module",
            "basecore:dig_slowdown_module",
            "basecore:weakness_module",
            "basecore:movement_slowdown_module"
    );
    private static Boolean baseCoreComponentMode = null;

    public static void init() {
        LOOT_TABLES.clear();
        for (AirdropData.Tier tier : AirdropData.Tier.values()) {
            LOOT_TABLES.put(tier, new ArrayList<>());
        }
        loadLoot();
    }

    public static void loadLoot() {
        try {
            Files.createDirectories(CONFIG_DIR);
            if (!Files.exists(LOOT_FILE)) {
                createDefaultLoot();
                saveLoot();
            } else {
                String json = Files.readString(LOOT_FILE);
                JsonObject root = GSON.fromJson(json, JsonObject.class);
                for (AirdropData.Tier tier : AirdropData.Tier.values()) {
                    List<LootEntry> entries = new ArrayList<>();
                    if (root.has(tier.getSerializedName())) {
                        JsonArray arr = root.getAsJsonArray(tier.getSerializedName());
                        for (JsonElement elem : arr) {
                            JsonObject obj = elem.getAsJsonObject();
                            String itemId = obj.get("item").getAsString();
                            int min = obj.get("min").getAsInt();
                            int max = obj.get("max").getAsInt();
                            int weight = obj.get("weight").getAsInt();
                            String enchantment = obj.has("enchantment") ? obj.get("enchantment").getAsString() : null;
                            int enchantmentLevel = obj.has("enchantment_level") ? obj.get("enchantment_level").getAsInt() : 1;
                            if (enchantment != null) {
                                entries.add(new LootEntry(itemId, min, max, weight, enchantment, enchantmentLevel));
                            } else {
                                entries.add(new LootEntry(itemId, min, max, weight));
                            }
                        }
                    }
                    LOOT_TABLES.put(tier, entries);
                }
            }
            airDrop.LOGGER.info("AirDrop loot tables loaded");
        } catch (Exception e) {
            airDrop.LOGGER.error("Failed to load loot config", e);
        }
    }

    private static void createDefaultLoot() {
        List<LootEntry> common = LOOT_TABLES.computeIfAbsent(AirdropData.Tier.COMMON, k -> new ArrayList<>());
        common.add(new LootEntry("minecraft:iron_ingot", 1, 4, 10));
        common.add(new LootEntry("minecraft:gold_ingot", 1, 2, 5));
        common.add(new LootEntry("minecraft:bread", 2, 5, 10));
        common.add(new LootEntry("minecraft:arrow", 4, 16, 8));
        common.add(new LootEntry("minecraft:leather", 1, 3, 6));
        common.add(new LootEntry("minecraft:coal", 2, 8, 8));

        // BaseCore - 基础材料
        common.add(new LootEntry("basecore:copper_wire", 3, 8, 8));
        common.add(new LootEntry("basecore:wire", 2, 5, 6));
        common.add(new LootEntry("basecore:chip", 1, 3, 5));
        common.add(new LootEntry("basecore:antenna", 1, 2, 4));
        common.add(new LootEntry("basecore:small_part_bundle", 1, 1, 5));
        common.add(new LootEntry("basecore:medium_part_bundle", 1, 1, 3));
        common.add(new LootEntry("basecore:part", 10, 25, 8));

        // RealityValue - 医疗与生存
        common.add(new LootEntry("reality_value:herb", 2, 5, 8));
        common.add(new LootEntry("reality_value:tattered_cloth", 2, 4, 6));
        common.add(new LootEntry("reality_value:bandage", 1, 3, 6));
        common.add(new LootEntry("reality_value:high_sugar_chocolate", 1, 3, 5));

        // ColdSweat - 动物掉落材料
        common.add(new LootEntry("cold_sweat:goat_fur", 1, 3, 6));
        common.add(new LootEntry("cold_sweat:hoglin_hide", 1, 3, 6));
        common.add(new LootEntry("cold_sweat:chameleon_molt", 1, 2, 4));

        // ThirstWasTaken - 基础容器
        common.add(new LootEntry("thirst:clay_bowl", 1, 3, 6));
        common.add(new LootEntry("thirst:terracotta_bowl", 1, 2, 5));
        common.add(new LootEntry("thirst:terracotta_water_bowl", 1, 2, 5));

        // CmMsg - 通讯器
        common.add(new LootEntry("cm_msg:communicator", 1, 1, 2));

        // Create - 基础材料
        common.add(new LootEntry("create:andesite_alloy", 2, 5, 6));
        common.add(new LootEntry("create:copper_sheet", 1, 3, 5));
        common.add(new LootEntry("create:iron_sheet", 1, 3, 5));

        List<LootEntry> medium = LOOT_TABLES.computeIfAbsent(AirdropData.Tier.MEDIUM, k -> new ArrayList<>());
        medium.add(new LootEntry("minecraft:diamond", 1, 3, 8));
        medium.add(new LootEntry("minecraft:iron_ingot", 3, 8, 10));
        medium.add(new LootEntry("minecraft:gold_ingot", 2, 5, 8));
        medium.add(new LootEntry("minecraft:ender_pearl", 1, 3, 6));
        medium.add(new LootEntry("minecraft:enchanted_book", 1, 1, 3));
        medium.add(new LootEntry("minecraft:experience_bottle", 2, 6, 5));

        // BaseCore - 进阶材料
        medium.add(new LootEntry("basecore:refined_chip", 1, 2, 4));
        medium.add(new LootEntry("basecore:electronic_component", 1, 2, 3));
        medium.add(new LootEntry("basecore:module_substrate", 1, 1, 3));
        medium.add(new LootEntry("basecore:medium_part_bundle", 1, 1, 5));
        medium.add(new LootEntry("basecore:part", 16, 40, 8));

        // RealityValue - 医疗用品
        medium.add(new LootEntry("reality_value:rum", 1, 2, 4));
        medium.add(new LootEntry("reality_value:antibiotics", 1, 2, 4));
        medium.add(new LootEntry("reality_value:first_aid_injection", 1, 1, 3));

        // ColdSweat - 实用工具
        medium.add(new LootEntry("cold_sweat:thermometer", 1, 1, 3));
        medium.add(new LootEntry("cold_sweat:waterskin", 1, 1, 4));
        medium.add(new LootEntry("cold_sweat:filled_waterskin", 1, 2, 4));
        medium.add(new LootEntry("cold_sweat:soulspring_lamp", 1, 1, 2));

        // CmMsg - 通讯器
        medium.add(new LootEntry("cm_msg:communicator", 1, 1, 3));

        // 信号枪
        medium.add(new LootEntry("airdrop:signal_gun", 1, 1, 3));
        medium.add(new LootEntry("airdrop:signal_gun_adv", 1, 1, 2));

        // Create - 中级材料与组件
        medium.add(new LootEntry("create:brass_ingot", 1, 3, 5));
        medium.add(new LootEntry("create:rose_quartz", 1, 2, 4));
        medium.add(new LootEntry("create:shaft", 4, 8, 6));
        medium.add(new LootEntry("create:cogwheel", 2, 4, 5));
        medium.add(new LootEntry("create:large_cogwheel", 1, 2, 4));
        medium.add(new LootEntry("create:wrench", 1, 1, 3));

        List<LootEntry> advanced = LOOT_TABLES.computeIfAbsent(AirdropData.Tier.ADVANCED, k -> new ArrayList<>());
        advanced.add(new LootEntry("minecraft:diamond", 2, 5, 10));
        advanced.add(new LootEntry("minecraft:netherite_scrap", 1, 1, 3));
        advanced.add(new LootEntry("minecraft:enchanted_golden_apple", 1, 1, 4));
        advanced.add(new LootEntry("minecraft:ender_pearl", 2, 6, 6));
        advanced.add(new LootEntry("minecraft:enchanted_book", 1, 2, 5));
        advanced.add(new LootEntry("minecraft:diamond_block", 1, 1, 2));

        // Create - 高级组件
        advanced.add(new LootEntry("create:brass_casing", 1, 2, 4));
        advanced.add(new LootEntry("create:andesite_casing", 1, 2, 4));
        advanced.add(new LootEntry("create:copper_casing", 1, 2, 3));
        advanced.add(new LootEntry("create:precision_mechanism", 1, 1, 3));
        advanced.add(new LootEntry("create:electron_tube", 1, 2, 4));
        advanced.add(new LootEntry("create:brass_sheet", 2, 4, 5));

        // BaseCore - 高级模块与装置
        advanced.add(new LootEntry("basecore:range_module", 1, 1, 3));
        advanced.add(new LootEntry("basecore:secure_module", 1, 1, 3));
        advanced.add(new LootEntry("basecore:def_module", 1, 1, 3));
        advanced.add(new LootEntry("basecore:auto_repair_module", 1, 1, 2));
        advanced.add(new LootEntry("basecore:thorns_module", 1, 1, 2));
        advanced.add(new LootEntry("basecore:disguise", 1, 1, 3));
        advanced.add(new LootEntry("basecore:advanced_disguise_device", 1, 1, 2));
        advanced.add(new LootEntry("basecore:protection_signal_shield", 1, 1, 2));
        advanced.add(new LootEntry("basecore:large_part_bundle", 1, 1, 5));
        advanced.add(new LootEntry("basecore:part", 25, 58, 8));

        // NightVisionDevice
        advanced.add(new LootEntry("night_vision_device:night_vision_device", 1, 1, 2));

        // Soulbound - 灵魂绑定附魔书
        advanced.add(new LootEntry("minecraft:enchanted_book", 1, 1, 2, "soulbound:soulbound", 1));

        // CmMsg - 通讯器
        advanced.add(new LootEntry("cm_msg:communicator", 1, 1, 4));

        // 信号枪
        advanced.add(new LootEntry("airdrop:signal_gun", 1, 1, 2));
        advanced.add(new LootEntry("airdrop:signal_gun_adv", 1, 1, 3));
    }

    public static void saveLoot() {
        try {
            Files.createDirectories(CONFIG_DIR);
            JsonObject root = new JsonObject();
            for (var entry : LOOT_TABLES.entrySet()) {
                JsonArray arr = new JsonArray();
                for (LootEntry loot : entry.getValue()) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("item", loot.itemId);
                    obj.addProperty("min", loot.minCount);
                    obj.addProperty("max", loot.maxCount);
                    obj.addProperty("weight", loot.weight);
                    if (loot.enchantmentId != null) {
                        obj.addProperty("enchantment", loot.enchantmentId);
                        obj.addProperty("enchantment_level", loot.enchantmentLevel);
                    }
                    arr.add(obj);
                }
                root.add(entry.getKey().getSerializedName(), arr);
            }
            Files.writeString(LOOT_FILE, GSON.toJson(root));
        } catch (IOException e) {
            airDrop.LOGGER.error("Failed to save loot config", e);
        }
    }

    public static List<LootEntry> getEntries(AirdropData.Tier tier) {
        return LOOT_TABLES.getOrDefault(tier, Collections.emptyList());
    }

    public static void addEntry(AirdropData.Tier tier, String itemId, int min, int max, int weight) {
        List<LootEntry> entries = LOOT_TABLES.computeIfAbsent(tier, k -> new ArrayList<>());
        entries.add(new LootEntry(itemId, min, max, weight));
        saveLoot();
    }

    public static boolean removeEntry(AirdropData.Tier tier, String itemId) {
        List<LootEntry> entries = LOOT_TABLES.get(tier);
        if (entries == null) return false;
        boolean removed = entries.removeIf(e -> e.itemId.equals(itemId));
        if (removed) saveLoot();
        return removed;
    }

    public static List<ItemStack> generateLoot(AirdropData.Tier tier, Level level) {
        List<LootEntry> entries = LOOT_TABLES.getOrDefault(tier, Collections.emptyList());
        if (entries.isEmpty()) return Collections.emptyList();

        boolean componentMode = isBaseCoreComponentMode();
        List<LootEntry> effectiveEntries = entries.stream()
                .filter(e -> {
                    if (BASECORE_PART_ITEMS.contains(e.itemId)) return componentMode;
                    if (BASECORE_MODULE_ITEMS.contains(e.itemId)) return !componentMode;
                    return true;
                })
                .toList();
        if (effectiveEntries.isEmpty()) return Collections.emptyList();

        List<ItemStack> loot = new ArrayList<>();
        RandomSource random = level.getRandom();

        int totalWeight = effectiveEntries.stream().mapToInt(e -> e.weight).sum();
        int rolls = switch (tier) {
            case COMMON -> 12 + random.nextInt(6);
            case MEDIUM -> 14 + random.nextInt(8);
            case ADVANCED -> 16 + random.nextInt(10);
        };

        for (int i = 0; i < rolls; i++) {
            int roll = random.nextInt(totalWeight);
            int cumulative = 0;
            for (LootEntry entry : effectiveEntries) {
                cumulative += entry.weight;
                if (roll < cumulative) {
                    Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.itemId));
                    if (item != null) {
                        int count = entry.minCount + random.nextInt(entry.maxCount - entry.minCount + 1);
                        ItemStack stack = new ItemStack(item, count);
                        if (entry.enchantmentId != null) {
                            applyEnchantment(stack, entry.enchantmentId, entry.enchantmentLevel, level);
                        } else if (entry.itemId.equals("minecraft:enchanted_book")) {
                            applyRandomEnchantment(stack, level, random);
                        }
                        loot.add(stack);
                    }
                    break;
                }
            }
        }

        if (loot.isEmpty()) {
            loot.add(findFallbackItem(tier, random));
        }

        return loot;
    }

    private static boolean isBaseCoreComponentMode() {
        if (baseCoreComponentMode == null) {
            try {
                Class<?> configClass = Class.forName("dev.anye.mc.basecore.config.BasecoreConfig");
                Method method = configClass.getMethod("isComponentMode");
                baseCoreComponentMode = (Boolean) method.invoke(null);
            } catch (Exception e) {
                airDrop.LOGGER.debug("BaseCore not available or mode unknown, filtering mode-specific BaseCore items");
                baseCoreComponentMode = false;
            }
        }
        return baseCoreComponentMode;
    }

    private static void applyEnchantment(ItemStack stack, String enchantmentId, int level, Level levelAccess) {
        try {
            var registry = levelAccess.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            var holder = registry.getHolder(ResourceLocation.parse(enchantmentId));
            holder.ifPresent(h -> stack.enchant(h, level));
        } catch (Exception e) {
            airDrop.LOGGER.error("Failed to apply enchantment {} to {}", enchantmentId, stack.getItem(), e);
        }
    }

    private static void applyRandomEnchantment(ItemStack stack, Level level, RandomSource random) {
        try {
            var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            var holders = registry.holders()
                    .filter(h -> h.key().location().getNamespace().equals("minecraft"))
                    .toList();
            if (!holders.isEmpty()) {
                var holder = holders.get(random.nextInt(holders.size()));
                int maxLevel = holder.value().getMaxLevel();
                int enchLevel = 1 + (maxLevel > 1 ? random.nextInt(maxLevel) : 0);
                stack.enchant(holder, enchLevel);
            }
        } catch (Exception e) {
            airDrop.LOGGER.error("Failed to apply random enchantment to {}", stack.getItem(), e);
        }
    }

    private static ItemStack findFallbackItem(AirdropData.Tier tier, RandomSource random) {
        return switch (tier) {
            case COMMON -> new ItemStack(net.minecraft.world.item.Items.IRON_INGOT, 1 + random.nextInt(3));
            case MEDIUM -> new ItemStack(net.minecraft.world.item.Items.DIAMOND, 1 + random.nextInt(2));
            case ADVANCED -> new ItemStack(net.minecraft.world.item.Items.DIAMOND, 2 + random.nextInt(3));
        };
    }

    public static class LootEntry {
        public final String itemId;
        public final int minCount;
        public final int maxCount;
        public final int weight;
        public final String enchantmentId;
        public final int enchantmentLevel;

        public LootEntry(String itemId, int minCount, int maxCount, int weight) {
            this(itemId, minCount, maxCount, weight, null, 1);
        }

        public LootEntry(String itemId, int minCount, int maxCount, int weight, String enchantmentId, int enchantmentLevel) {
            this.itemId = itemId;
            this.minCount = minCount;
            this.maxCount = maxCount;
            this.weight = weight;
            this.enchantmentId = enchantmentId;
            this.enchantmentLevel = enchantmentLevel;
        }
    }
}