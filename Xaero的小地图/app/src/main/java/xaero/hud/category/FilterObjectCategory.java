package xaero.hud.category;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.rule.ExcludeListMode;
import xaero.hud.category.rule.ObjectCategoryExcludeList;
import xaero.hud.category.rule.ObjectCategoryIncludeList;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.rule.ObjectCategoryRule;
import xaero.hud.category.serialization.data.FilterObjectCategoryData;
import xaero.hud.category.setting.ObjectCategorySetting;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/FilterObjectCategory.class */
public abstract class FilterObjectCategory<E, P, D extends FilterObjectCategoryData<D>, C extends FilterObjectCategory<E, P, D, C>> extends ObjectCategory<D, C> {
    private final C self;
    private ObjectCategoryRule<E, P> baseRule;
    private final Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryIncludeList<E, P, ?>> includeLists;
    private final Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryExcludeList<E, P, ?>> excludeLists;
    private final List<ObjectCategoryIncludeList<E, P, ?>> includeListsIndexed;
    private final List<ObjectCategoryExcludeList<E, P, ?>> excludeListsIndexed;
    private final ExcludeListMode excludeMode;
    private final boolean includeInSuperCategory;

    protected FilterObjectCategory(@Nonnull String name, @Nonnull C superCategory, @Nonnull ObjectCategoryRule<E, P> baseRule, @Nonnull Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryIncludeList<E, P, ?>> includeLists, @Nonnull Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryExcludeList<E, P, ?>> excludeLists, @Nonnull List<ObjectCategoryIncludeList<E, P, ?>> includeListsIndexed, @Nonnull List<ObjectCategoryExcludeList<E, P, ?>> excludeListsIndexed, @Nonnull Map<ObjectCategorySetting<?>, Object> settingOverrides, @Nonnull List<C> subCategories, boolean protection, ExcludeListMode excludeMode, boolean includeInSuperCategory) {
        super(name, superCategory, settingOverrides, subCategories, protection);
        this.self = this;
        this.baseRule = baseRule;
        this.includeLists = includeLists;
        this.excludeLists = excludeLists;
        this.includeListsIndexed = includeListsIndexed;
        this.excludeListsIndexed = excludeListsIndexed;
        this.excludeMode = excludeMode;
        this.includeInSuperCategory = includeInSuperCategory;
    }

    public ObjectCategoryRule<E, P> getBaseRule() {
        return this.baseRule;
    }

    public <S> ObjectCategoryIncludeList<E, P, S> getIncludeList(ObjectCategoryListRuleType<E, P, S> objectCategoryListRuleType) {
        return this.includeLists.get(objectCategoryListRuleType);
    }

    public <S> ObjectCategoryExcludeList<E, P, S> getExcludeList(ObjectCategoryListRuleType<E, P, S> objectCategoryListRuleType) {
        return this.excludeLists.get(objectCategoryListRuleType);
    }

    public List<ObjectCategoryIncludeList<E, P, ?>> getIncludeLists() {
        return this.includeListsIndexed;
    }

    public List<ObjectCategoryExcludeList<E, P, ?>> getExcludeLists() {
        return this.excludeListsIndexed;
    }

    public ExcludeListMode getExcludeMode() {
        return this.excludeMode;
    }

    public boolean getIncludeInSuperCategory() {
        return this.includeInSuperCategory;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/FilterObjectCategory$Builder.class */
    public static abstract class Builder<E, P, C extends FilterObjectCategory<E, P, ?, C>, B extends Builder<E, P, C, B>> extends ObjectCategory.Builder<C, B> {
        protected ObjectCategoryRule<E, P> baseRule;
        protected final Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryIncludeList.Builder<E, P, ?>> includeListBuilders;
        protected final Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryExcludeList.Builder<E, P, ?>> excludeListBuilders;
        protected ExcludeListMode excludeMode;
        protected boolean includeInSuperCategory;

        protected abstract C buildUncheckedFilter(List<C> list, Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryIncludeList<E, P, ?>> map, Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryExcludeList<E, P, ?>> map2, List<ObjectCategoryIncludeList<E, P, ?>> list2, List<ObjectCategoryExcludeList<E, P, ?>> list3);

        protected Builder(ListFactory listFactory, MapFactory mapFactory, Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes) {
            super(listFactory, mapFactory);
            this.includeListBuilders = mapFactory.get();
            this.excludeListBuilders = mapFactory.get();
            for (ObjectCategoryListRuleType<E, P, ?> type : listRuleTypes) {
                this.includeListBuilders.put(type, ObjectCategoryIncludeList.Builder.begin(listFactory, type));
                this.excludeListBuilders.put(type, ObjectCategoryExcludeList.Builder.begin(listFactory, type));
            }
        }

        @Override // xaero.hud.category.ObjectCategory.Builder
        public B setDefault() {
            super.setDefault();
            this.includeListBuilders.forEach((k, v) -> {
                v.setDefault();
            });
            this.excludeListBuilders.forEach((k2, v2) -> {
                v2.setDefault();
            });
            setBaseRule(null);
            setExcludeMode(ExcludeListMode.ONLY);
            setIncludeInSuperCategory(true);
            return (B) this.self;
        }

        public <S> ObjectCategoryIncludeList.Builder<E, P, S> getIncludeListBuilder(ObjectCategoryListRuleType<E, P, S> objectCategoryListRuleType) {
            return this.includeListBuilders.get(objectCategoryListRuleType);
        }

        public <S> ObjectCategoryExcludeList.Builder<E, P, S> getExcludeListBuilder(ObjectCategoryListRuleType<E, P, S> objectCategoryListRuleType) {
            return this.excludeListBuilders.get(objectCategoryListRuleType);
        }

        public B setBaseRule(ObjectCategoryRule<E, P> baseRule) {
            this.baseRule = baseRule;
            return (B) this.self;
        }

        public B setExcludeMode(ExcludeListMode excludeMode) {
            this.excludeMode = excludeMode;
            return (B) this.self;
        }

        public B setIncludeInSuperCategory(boolean includeInSuperCategory) {
            this.includeInSuperCategory = includeInSuperCategory;
            return (B) this.self;
        }

        @Override // xaero.hud.category.ObjectCategory.Builder
        public C build() {
            if (this.baseRule == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (C) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ObjectCategory.Builder
        public final C buildUnchecked(List<C> list) {
            Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryIncludeList<E, P, ?>> map = this.mapFactory.get();
            Map<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryExcludeList<E, P, ?>> map2 = this.mapFactory.get();
            List<ObjectCategoryIncludeList<E, P, ?>> list2 = this.listFactory.get();
            List<ObjectCategoryExcludeList<E, P, ?>> list3 = this.listFactory.get();
            for (Map.Entry<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryIncludeList.Builder<E, P, ?>> entry : this.includeListBuilders.entrySet()) {
                ObjectCategoryIncludeList<E, P, ?> objectCategoryIncludeListBuild = entry.getValue().build(list);
                map.put(entry.getKey(), objectCategoryIncludeListBuild);
                list2.add(objectCategoryIncludeListBuild);
            }
            for (Map.Entry<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryExcludeList.Builder<E, P, ?>> entry2 : this.excludeListBuilders.entrySet()) {
                ObjectCategoryExcludeList<E, P, ?> objectCategoryExcludeListBuild = entry2.getValue().setExcludeMode(this.excludeMode).build(list);
                map2.put(entry2.getKey(), objectCategoryExcludeListBuild);
                list3.add(objectCategoryExcludeListBuild);
            }
            return (C) buildUncheckedFilter(list, map, map2, list2, list3);
        }
    }
}
