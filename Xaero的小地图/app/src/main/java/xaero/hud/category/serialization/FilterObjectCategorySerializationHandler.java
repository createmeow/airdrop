package xaero.hud.category.serialization;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.FilterObjectCategory.Builder;
import xaero.hud.category.rule.ObjectCategoryExcludeList;
import xaero.hud.category.rule.ObjectCategoryHardRule;
import xaero.hud.category.rule.ObjectCategoryIncludeList;
import xaero.hud.category.rule.ObjectCategoryListRule;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.rule.ObjectCategoryRule;
import xaero.hud.category.serialization.ObjectCategorySerializationHandler;
import xaero.hud.category.serialization.data.FilterObjectCategoryData;
import xaero.hud.category.serialization.data.FilterObjectCategoryData.Builder;
import xaero.hud.category.serialization.data.ObjectCategoryDataSerializer;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.io.HudIO;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/FilterObjectCategorySerializationHandler.class */
public abstract class FilterObjectCategorySerializationHandler<E, P, D extends FilterObjectCategoryData<D>, C extends FilterObjectCategory<E, P, D, C>, B extends FilterObjectCategory.Builder<E, P, C, B>, DB extends FilterObjectCategoryData.Builder<D, DB>> extends ObjectCategorySerializationHandler<D, C, B, DB> {
    private final Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter;
    private final ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
    private final Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
    private final String listRuleTypePrefixSeparator;

    protected FilterObjectCategorySerializationHandler(ObjectCategoryDataSerializer<D, String> serializer, Supplier<DB> dataBuilderFactory, Supplier<B> objectCategoryBuilderFactory, Function<String, ObjectCategorySetting<?>> settingTypeGetter, Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter, ObjectCategoryListRuleType<E, P, ?> defaultListRuleType, Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter, String listRuleTypePrefixSeparator) {
        super(serializer, dataBuilderFactory, objectCategoryBuilderFactory, settingTypeGetter);
        this.hardRuleGetter = hardRuleGetter;
        this.defaultListRuleType = defaultListRuleType;
        this.listRuleTypeGetter = listRuleTypeGetter;
        this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.hud.category.serialization.ObjectCategorySerializationHandler
    public DB getConfiguredDataBuilderForCategory(C category) {
        DB db = (DB) super.getConfiguredDataBuilderForCategory((FilterObjectCategorySerializationHandler<E, P, D, C, B, DB>) category);
        ObjectCategoryRule<E, P> baseRule = category.getBaseRule();
        db.setHardInclude(baseRule == null ? EntityRadarCategoryConstants.HARD_NOTHING : baseRule.getName());
        db.setExcludeMode(category.getExcludeMode());
        db.setIncludeListInSuperCategory(category.getIncludeInSuperCategory());
        for (ObjectCategoryIncludeList<E, P, ?> includeList : category.getIncludeLists()) {
            String prefix = getListRulePrefix(includeList);
            includeList.forEach(el -> {
                db.addToIncludeList(prefix + el);
            });
        }
        for (ObjectCategoryExcludeList<E, P, ?> excludeList : category.getExcludeLists()) {
            String prefix2 = getListRulePrefix(excludeList);
            excludeList.forEach(el2 -> {
                db.addToExcludeList(prefix2 + el2);
            });
        }
        return db;
    }

    private String getListRulePrefix(ObjectCategoryListRule<E, P, ?> listRule) {
        if (listRule.getType() == this.defaultListRuleType) {
            return "";
        }
        return listRule.getType().getId() + this.listRuleTypePrefixSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.hud.category.serialization.ObjectCategorySerializationHandler
    public B getConfiguredCategoryBuilderForData(D data) {
        B b = (B) super.getConfiguredCategoryBuilderForData((FilterObjectCategorySerializationHandler<E, P, D, C, B, DB>) data);
        String hardInclude = data.getHardInclude();
        ObjectCategoryHardRule<E, P> serializedHardRule = this.hardRuleGetter == null ? null : this.hardRuleGetter.apply(hardInclude);
        if (serializedHardRule != null) {
            b.setBaseRule(serializedHardRule);
        }
        b.setExcludeMode(data.getExcludeMode());
        b.setIncludeInSuperCategory(data.getIncludeListInSuperCategory());
        data.getIncludeListIterator().forEachRemaining(s -> {
            Objects.requireNonNull(b);
            handleListRuleSerializedElement(s, b::getIncludeListBuilder);
        });
        data.getExcludeListIterator().forEachRemaining(s2 -> {
            Objects.requireNonNull(b);
            handleListRuleSerializedElement(s2, b::getExcludeListBuilder);
        });
        return b;
    }

    public void handleListRuleSerializedElement(String s, Function<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryListRule.Builder<E, P, ?, ?>> listBuilderGetter) {
        handleListRuleSerializedElement(s, listBuilderGetter, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator);
    }

    public static <E, P> void handleListRuleSerializedElement(String s, Function<ObjectCategoryListRuleType<E, P, ?>, ObjectCategoryListRule.Builder<E, P, ?, ?>> listBuilderGetter, ObjectCategoryListRuleType<E, P, ?> defaultListRuleType, Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter, String listRuleTypePrefixSeparator) {
        ObjectCategoryListRuleType<E, P, ?> entryListRuleType = defaultListRuleType;
        if (s.contains(listRuleTypePrefixSeparator)) {
            ObjectCategoryListRuleType<E, P, ?> specifiedListRuleType = listRuleTypeGetter.apply(s.substring(0, s.indexOf(listRuleTypePrefixSeparator)));
            if (specifiedListRuleType != null) {
                entryListRuleType = specifiedListRuleType;
            }
            s = s.substring(s.indexOf(listRuleTypePrefixSeparator) + 1);
        }
        listBuilderGetter.apply(entryListRuleType).addListElement(s);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/FilterObjectCategorySerializationHandler$Builder.class */
    public static abstract class Builder<E, P, D extends FilterObjectCategoryData<D>, C extends FilterObjectCategory<E, P, D, C>, B extends FilterObjectCategory.Builder<E, P, C, B>, DB extends FilterObjectCategoryData.Builder<D, DB>, SH extends FilterObjectCategorySerializationHandler<E, P, D, C, B, DB>, SHB extends Builder<E, P, D, C, B, DB, SH, SHB>> extends ObjectCategorySerializationHandler.Builder<D, C, B, DB, SH, SHB> {
        protected Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter;
        protected ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
        protected Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
        protected String listRuleTypePrefixSeparator;

        public Builder(ObjectCategoryDataSerializer<D, String> serializer) {
            super(serializer);
        }

        @Override // xaero.hud.category.serialization.ObjectCategorySerializationHandler.Builder
        public SHB setDefault() {
            super.setDefault();
            setHardRuleGetter(null);
            setDefaultListRuleType(null);
            setListRuleTypeGetter(null);
            setListRuleTypePrefixSeparator(HudIO.SEPARATOR);
            return (SHB) this.self;
        }

        public SHB setDefaultListRuleType(ObjectCategoryListRuleType<E, P, ?> defaultListRuleType) {
            this.defaultListRuleType = defaultListRuleType;
            return (SHB) this.self;
        }

        public SHB setHardRuleGetter(Function<String, ObjectCategoryHardRule<E, P>> hardRuleGetter) {
            this.hardRuleGetter = hardRuleGetter;
            return (SHB) this.self;
        }

        public SHB setListRuleTypeGetter(Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter) {
            this.listRuleTypeGetter = listRuleTypeGetter;
            return (SHB) this.self;
        }

        public SHB setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
            this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
            return (SHB) this.self;
        }

        @Override // xaero.hud.category.serialization.ObjectCategorySerializationHandler.Builder
        public SH build() {
            if (this.hardRuleGetter == null || this.defaultListRuleType == null || this.listRuleTypeGetter == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (SH) super.build();
        }
    }
}
