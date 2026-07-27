package xaero.hud.minimap.radar.category;

import net.minecraft.world.entity.EntityType;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.hud.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/EntityRadarDefaultCategories.class */
public final class EntityRadarDefaultCategories {
    private final boolean forServer;

    public EntityRadarDefaultCategories(boolean forServer) {
        this.forServer = forServer;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public EntityRadarCategory setupDefault(ModSettings settings) {
        EntityRadarBackwardsCompatibilityConfig compatibilityConfig = settings.getEntityRadarBackwardsCompatibilityConfig();
        EntityRadarCategory.Builder builder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_ROOT)).setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING).setProtection(true);
        if (!this.forServer && (!settings.foundOldRadarSettings() || !compatibilityConfig.itemFramesOnRadar)) {
            builder.getExcludeListBuilder(EntityRadarListRuleTypes.ENTITY_TYPE).addListElement(EntityType.getKey(EntityType.ITEM_FRAME).toString());
            builder.getExcludeListBuilder(EntityRadarListRuleTypes.ENTITY_TYPE).addListElement(EntityType.getKey(EntityType.GLOW_ITEM_FRAME).toString());
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            builder.setSettingValue(EntityRadarCategorySettings.ENTITY_NUMBER, Double.valueOf(compatibilityConfig.entityAmount * 100.0d));
            builder.setSettingValue(EntityRadarCategorySettings.DOT_SIZE, Double.valueOf(compatibilityConfig.dotsSize));
            builder.setSettingValue(EntityRadarCategorySettings.ICON_SCALE, Double.valueOf(compatibilityConfig.headsScale));
            builder.setSettingValue(EntityRadarCategorySettings.HEIGHT_FADE, Boolean.valueOf(compatibilityConfig.showEntityHeight));
            builder.setSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT, Double.valueOf(compatibilityConfig.heightLimit));
            builder.setSettingValue(EntityRadarCategorySettings.ALWAYS_NAMETAGS, Boolean.valueOf(compatibilityConfig.alwaysEntityNametags));
            builder.setSettingValue(EntityRadarCategorySettings.ICON_NAME_FALLBACK, Boolean.valueOf(compatibilityConfig.displayNameWhenIconFails));
        }
        EntityRadarCategory.Builder livingBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_LIVING)).setBaseRule(EntityRadarCategoryHardRules.IS_LIVING).setProtection(true);
        if (!this.forServer) {
            livingBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(2.0d));
            livingBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(14.0d));
        }
        livingBuilder.getExcludeListBuilder(EntityRadarListRuleTypes.ENTITY_TYPE).addListElement(EntityType.getKey(EntityType.ARMOR_STAND).toString());
        EntityRadarCategory.Builder hostileBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_HOSTILE)).setBaseRule(EntityRadarCategoryHardRules.IS_HOSTILE).setProtection(true);
        if (!this.forServer) {
            hostileBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(3.0d));
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showHostile) {
                hostileBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            if (compatibilityConfig.hostileColor != 14) {
                hostileBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.hostileColor));
            }
            if (compatibilityConfig.hostileIcons != 1) {
                hostileBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(compatibilityConfig.hostileIcons));
            }
            if (compatibilityConfig.hostileMobNames != 0) {
                hostileBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.hostileMobNames));
            }
        }
        EntityRadarCategory.Builder friendlyBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_FRIENDLY)).setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING).setProtection(true);
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showMobs) {
                friendlyBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            if (compatibilityConfig.mobsColor != 14) {
                friendlyBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.mobsColor));
            }
            if (compatibilityConfig.mobIcons != 1) {
                friendlyBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(compatibilityConfig.mobIcons));
            }
            if (compatibilityConfig.friendlyMobNames != 0) {
                friendlyBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.friendlyMobNames));
            }
        }
        EntityRadarCategory.Builder playersBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_PLAYERS)).setBaseRule(EntityRadarCategoryHardRules.IS_PLAYER).setProtection(true);
        if (!this.forServer) {
            playersBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(6.0d));
            playersBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(15.0d));
            int lastHeightLimitIndex = EntityRadarCategorySettings.HEIGHT_LIMIT.getUiLastOption();
            playersBuilder.setSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT, EntityRadarCategorySettings.HEIGHT_LIMIT.getIndexReader().apply(lastHeightLimitIndex));
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showPlayers) {
                playersBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            if (compatibilityConfig.playersColor != 14) {
                playersBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.playersColor));
            }
            if (compatibilityConfig.playerIcons != 1) {
                playersBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(compatibilityConfig.playerIcons));
            }
            if (compatibilityConfig.playerNames != 0) {
                playersBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.playerNames));
            }
        }
        EntityRadarCategory.Builder friendsBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_FRIENDS)).setBaseRule(EntityRadarCategoryHardRules.IS_NOTHING).setProtection(true);
        EntityRadarCategory.Builder playersTrackedBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_TRACKED)).setBaseRule(EntityRadarCategoryHardRules.IS_TRACKED).setProtection(true);
        if (!this.forServer) {
            playersTrackedBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(2.0d));
        }
        EntityRadarCategory.Builder playersTeamBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_SAME_TEAM)).setBaseRule(EntityRadarCategoryHardRules.IS_SAME_TEAM).setProtection(true);
        EntityRadarCategory.Builder playersOtherTeamsBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_OTHER_TEAMS)).setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING).setProtection(true);
        if (!this.forServer) {
            playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(7.0d));
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showOtherTeam) {
                playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            if (compatibilityConfig.otherTeamColor != -1) {
                playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.otherTeamColor));
            }
            if (compatibilityConfig.otherTeamsNames != 3) {
                playersOtherTeamsBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.otherTeamsNames));
            }
        }
        EntityRadarCategory.Builder tamedHostileBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_HOSTILE_TAMED)).setBaseRule(EntityRadarCategoryHardRules.IS_TAMED).setProtection(true);
        if (!this.forServer) {
            tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(5.0d));
        }
        EntityRadarCategory.Builder tamedFriendlyBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_FRIENDLY_TAMED)).setBaseRule(EntityRadarCategoryHardRules.IS_TAMED).setProtection(true);
        if (!this.forServer) {
            tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(4.0d));
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showTamed) {
                tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
                tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            if (compatibilityConfig.tamedMobsColor != -1) {
                tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.tamedMobsColor));
                tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.tamedMobsColor));
            }
            if (compatibilityConfig.tamedIcons != 3) {
                tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(compatibilityConfig.tamedIcons));
                tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.ICONS, Double.valueOf(compatibilityConfig.tamedIcons));
            }
            if (compatibilityConfig.tamedMobNames != 3) {
                tamedFriendlyBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.tamedMobNames));
                tamedHostileBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.tamedMobNames));
            }
        }
        EntityRadarCategory.Builder itemsBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_ITEMS)).setBaseRule(EntityRadarCategoryHardRules.IS_ITEM).setProtection(true);
        if (!this.forServer) {
            itemsBuilder.setSettingValue(EntityRadarCategorySettings.RENDER_ORDER, Double.valueOf(1.0d));
            itemsBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(12.0d));
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showItems) {
                itemsBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            itemsBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.itemsColor));
            if (compatibilityConfig.itemNames != 0) {
                itemsBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.itemNames));
            }
        }
        EntityRadarCategory.Builder otherBuilder = (EntityRadarCategory.Builder) ((EntityRadarCategory.Builder) EntityRadarCategory.Builder.begin().setName(EntityRadarCategoryConstants.CATEGORY_OTHER)).setBaseRule(EntityRadarCategoryHardRules.IS_ANYTHING).setProtection(true);
        if (!this.forServer) {
            otherBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(5.0d));
        }
        if (!this.forServer && settings.foundOldRadarSettings()) {
            if (!compatibilityConfig.showOther) {
                otherBuilder.setSettingValue(EntityRadarCategorySettings.DISPLAYED, false);
            }
            otherBuilder.setSettingValue(EntityRadarCategorySettings.COLOR, Double.valueOf(compatibilityConfig.otherColor));
            if (compatibilityConfig.otherNames != 0) {
                otherBuilder.setSettingValue(EntityRadarCategorySettings.NAMES, Double.valueOf(compatibilityConfig.otherNames));
            }
        }
        builder.addSubCategoryBuilder(livingBuilder);
        builder.addSubCategoryBuilder(itemsBuilder);
        builder.addSubCategoryBuilder(otherBuilder);
        livingBuilder.addSubCategoryBuilder(playersBuilder);
        livingBuilder.addSubCategoryBuilder(hostileBuilder);
        livingBuilder.addSubCategoryBuilder(friendlyBuilder);
        hostileBuilder.addSubCategoryBuilder(tamedHostileBuilder);
        friendlyBuilder.addSubCategoryBuilder(tamedFriendlyBuilder);
        playersBuilder.addSubCategoryBuilder(friendsBuilder);
        playersBuilder.addSubCategoryBuilder(playersTrackedBuilder);
        playersBuilder.addSubCategoryBuilder(playersTeamBuilder);
        playersBuilder.addSubCategoryBuilder(playersOtherTeamsBuilder);
        EntityRadarCategory root = builder.build();
        return root;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/EntityRadarDefaultCategories$Builder.class */
    public static final class Builder {
        private boolean forServer;

        private Builder() {
        }

        public Builder setDefault() {
            setForServer(false);
            return this;
        }

        public Builder setForServer(boolean forServer) {
            this.forServer = forServer;
            return this;
        }

        public EntityRadarDefaultCategories build() {
            return new EntityRadarDefaultCategories(this.forServer);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
