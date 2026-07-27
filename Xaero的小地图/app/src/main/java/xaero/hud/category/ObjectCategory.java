package xaero.hud.category;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.serialization.data.ObjectCategoryData;
import xaero.hud.category.setting.ObjectCategorySetting;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ObjectCategory.class */
public abstract class ObjectCategory<D extends ObjectCategoryData<D>, C extends ObjectCategory<D, C>> {
    private final C self = this;
    private C superCategory;
    private final String name;
    private final boolean protection;
    private final Map<ObjectCategorySetting<?>, Object> settingOverrides;
    private final List<C> subCategories;

    protected ObjectCategory(@Nonnull String name, C superCategory, @Nonnull Map<ObjectCategorySetting<?>, Object> settingOverrides, @Nonnull List<C> subCategories, boolean protection) {
        this.name = name;
        this.superCategory = superCategory;
        this.settingOverrides = settingOverrides;
        this.subCategories = subCategories;
        this.protection = protection;
    }

    public String getName() {
        return this.name;
    }

    public C getSuperCategory() {
        return this.superCategory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSuperCategory(C superCategory) {
        this.superCategory = superCategory;
    }

    public Iterator<C> getDirectSubCategoryIterator() {
        return this.subCategories.iterator();
    }

    public <T> T getSettingValue(ObjectCategorySetting<T> objectCategorySetting) {
        T t = (T) this.settingOverrides.get(objectCategorySetting);
        if (t != null) {
            return t;
        }
        if (this.superCategory == null) {
            return null;
        }
        return (T) this.superCategory.getSettingValue(objectCategorySetting);
    }

    public <T> void setSettingValue(ObjectCategorySetting<T> setting, T value) {
        this.settingOverrides.put(setting, value);
    }

    public Iterator<Map.Entry<ObjectCategorySetting<?>, Object>> getSettingOverridesIterator() {
        return this.settingOverrides.entrySet().iterator();
    }

    public boolean getProtection() {
        return this.protection;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ObjectCategory$Builder.class */
    public static abstract class Builder<C extends ObjectCategory<?, C>, B extends Builder<C, B>> {
        protected final B self = this;
        protected String name;
        protected C superCategory;
        protected final List<B> subCategories;
        protected boolean protection;
        protected final Map<ObjectCategorySetting<?>, Object> settingOverrides;
        protected final ListFactory listFactory;
        protected final MapFactory mapFactory;

        protected abstract C buildUnchecked(List<C> list);

        public Builder(@Nonnull ListFactory listFactory, @Nonnull MapFactory mapFactory) {
            this.subCategories = listFactory.get();
            this.settingOverrides = mapFactory.get();
            this.listFactory = listFactory;
            this.mapFactory = mapFactory;
        }

        public B setDefault() {
            setName(null);
            this.subCategories.clear();
            this.settingOverrides.clear();
            setSuperCategory(null);
            setProtection(false);
            return this.self;
        }

        public B setName(String name) {
            this.name = name;
            return this.self;
        }

        public B setSuperCategory(C superCategory) {
            this.superCategory = superCategory;
            return this.self;
        }

        public B addSubCategoryBuilder(B subCategoryBuilder) {
            this.subCategories.add(subCategoryBuilder);
            return this.self;
        }

        public <T> B setSettingValue(ObjectCategorySetting<T> setting, T value) {
            this.settingOverrides.put(setting, value);
            return this.self;
        }

        public B setProtection(boolean protection) {
            this.protection = protection;
            return this.self;
        }

        public C build() {
            if (this.name == null) {
                throw new IllegalStateException("required fields not set!");
            }
            C c = (C) buildUnchecked(buildSubCategories());
            c.getDirectSubCategoryIterator().forEachRemaining(c2 -> {
                c2.setSuperCategory(c);
            });
            return c;
        }

        private List<C> buildSubCategories() {
            Stream<R> map = this.subCategories.stream().map((v0) -> {
                return v0.build();
            });
            ListFactory listFactory = this.listFactory;
            Objects.requireNonNull(listFactory);
            return (List) map.collect(listFactory::get, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
        }
    }
}
