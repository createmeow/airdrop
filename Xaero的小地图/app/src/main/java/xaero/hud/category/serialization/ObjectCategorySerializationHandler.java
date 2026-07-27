package xaero.hud.category.serialization;

import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.ObjectCategory.Builder;
import xaero.hud.category.serialization.data.ObjectCategoryData;
import xaero.hud.category.serialization.data.ObjectCategoryData.Builder;
import xaero.hud.category.serialization.data.ObjectCategoryDataSerializer;
import xaero.hud.category.setting.ObjectCategorySetting;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/ObjectCategorySerializationHandler.class */
public abstract class ObjectCategorySerializationHandler<D extends ObjectCategoryData<D>, C extends ObjectCategory<D, C>, B extends ObjectCategory.Builder<C, B>, DB extends ObjectCategoryData.Builder<D, DB>> {
    private final ObjectCategoryDataSerializer<D, String> serializer;
    private final Supplier<DB> dataBuilderFactory;
    private final Supplier<B> objectCategoryBuilderFactory;
    private final Function<String, ObjectCategorySetting<?>> settingTypeGetter;

    protected ObjectCategorySerializationHandler(@Nonnull ObjectCategoryDataSerializer<D, String> serializer, @Nonnull Supplier<DB> dataBuilderFactory, @Nonnull Supplier<B> objectCategoryBuilderFactory, @Nonnull Function<String, ObjectCategorySetting<?>> settingTypeGetter) {
        this.serializer = serializer;
        this.dataBuilderFactory = dataBuilderFactory;
        this.objectCategoryBuilderFactory = objectCategoryBuilderFactory;
        this.settingTypeGetter = settingTypeGetter;
    }

    public String serialize(C category) {
        String serializedData = this.serializer.serialize(getConfiguredDataBuilderForCategory(category).build());
        return serializedData;
    }

    public D convertToData(C c) {
        return (D) getConfiguredDataBuilderForCategory(c).build();
    }

    protected DB getConfiguredDataBuilderForCategory(C c) {
        DB db = (DB) this.dataBuilderFactory.get().setDefault();
        db.setName(c.getName());
        db.setProtection(c.getProtection());
        c.getSettingOverridesIterator().forEachRemaining(e -> {
            db.setSettingOverride(((ObjectCategorySetting) e.getKey()).getId(), e.getValue());
        });
        c.getDirectSubCategoryIterator().forEachRemaining(c2 -> {
            db.addSubCategoryBuilder(getConfiguredDataBuilderForCategory(c2));
        });
        return db;
    }

    public C convertFromData(D d) {
        return (C) getConfiguredCategoryBuilderForData(d).build();
    }

    public C deserialize(String str) {
        return (C) getConfiguredCategoryBuilderForData(this.serializer.deserialize(str)).build();
    }

    protected B getConfiguredCategoryBuilderForData(D d) {
        B b = (B) this.objectCategoryBuilderFactory.get().setDefault();
        b.setName(d.getName());
        b.setProtection(d.getProtection());
        d.getSettingOverrideIterator().forEachRemaining(e -> {
            ObjectCategorySetting<T> objectCategorySetting = (ObjectCategorySetting) this.settingTypeGetter.apply((String) e.getKey());
            if (objectCategorySetting != 0) {
                setSettingValue(b, objectCategorySetting, e.getValue());
            }
        });
        d.getSubCategoryIterator().forEachRemaining(subCategory -> {
            b.addSubCategoryBuilder(getConfiguredCategoryBuilderForData(subCategory));
        });
        return b;
    }

    private <T> void setSettingValue(B objectCategoryBuilder, ObjectCategorySetting<T> setting, Object value) {
        objectCategoryBuilder.setSettingValue(setting, value);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/ObjectCategorySerializationHandler$Builder.class */
    public static abstract class Builder<D extends ObjectCategoryData<D>, C extends ObjectCategory<D, C>, B extends ObjectCategory.Builder<C, B>, DB extends ObjectCategoryData.Builder<D, DB>, SH extends ObjectCategorySerializationHandler<D, C, B, DB>, SHB extends Builder<D, C, B, DB, SH, SHB>> {
        protected final SHB self = this;
        protected final ObjectCategoryDataSerializer<D, String> serializer;
        protected Supplier<DB> dataBuilderFactory;
        protected Supplier<B> objectCategoryBuilderFactory;
        protected Function<String, ObjectCategorySetting<?>> settingTypeGetter;

        protected abstract SH buildInternally();

        public Builder(ObjectCategoryDataSerializer<D, String> serializer) {
            this.serializer = serializer;
        }

        public SHB setDefault() {
            setDataBuilderFactory(null);
            setObjectCategoryBuilderFactory(null);
            setSettingTypeGetter(null);
            return this.self;
        }

        public SHB setDataBuilderFactory(Supplier<DB> dataBuilderFactory) {
            this.dataBuilderFactory = dataBuilderFactory;
            return this.self;
        }

        public SHB setObjectCategoryBuilderFactory(Supplier<B> objectCategoryBuilderFactory) {
            this.objectCategoryBuilderFactory = objectCategoryBuilderFactory;
            return this.self;
        }

        public SHB setSettingTypeGetter(Function<String, ObjectCategorySetting<?>> settingTypeGetter) {
            this.settingTypeGetter = settingTypeGetter;
            return this.self;
        }

        public SH build() {
            if (this.dataBuilderFactory == null || this.objectCategoryBuilderFactory == null || this.settingTypeGetter == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (SH) buildInternally();
        }
    }
}
