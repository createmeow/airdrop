package xaero.hud.category.serialization.data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;
import xaero.hud.category.serialization.data.ObjectCategoryData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryData.class */
public abstract class ObjectCategoryData<D extends ObjectCategoryData<D>> {
    private final String name;
    private final boolean protection;
    private final Map<String, Object> settingOverrides;
    private final List<D> subCategories;

    protected ObjectCategoryData(@Nonnull String name, @Nonnull Map<String, Object> settingOverrides, @Nonnull List<D> subCategories, boolean protection) {
        this.name = name;
        this.settingOverrides = settingOverrides;
        this.subCategories = subCategories;
        this.protection = protection;
    }

    public String getName() {
        return this.name;
    }

    public Iterator<Map.Entry<String, Object>> getSettingOverrideIterator() {
        return this.settingOverrides.entrySet().iterator();
    }

    public Iterator<D> getSubCategoryIterator() {
        return this.subCategories.iterator();
    }

    public boolean getProtection() {
        return this.protection;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryData$Builder.class */
    public static abstract class Builder<D extends ObjectCategoryData<D>, B extends Builder<D, B>> {
        protected final B self = this;
        protected String name;
        protected final Map<String, Object> settingOverrides;
        private final ListFactory listFactory;
        protected final List<B> subCategoryBuilders;
        protected boolean protection;

        protected abstract D buildInternally(List<D> list);

        public Builder(@Nonnull ListFactory listFactory, @Nonnull MapFactory mapFactory) {
            this.settingOverrides = mapFactory.get();
            this.subCategoryBuilders = listFactory.get();
            this.listFactory = listFactory;
        }

        public B setDefault() {
            setName(null);
            setProtection(false);
            this.settingOverrides.clear();
            return this.self;
        }

        public B setName(String name) {
            this.name = name;
            return this.self;
        }

        public B setSettingOverride(String key, Object value) {
            this.settingOverrides.put(key, value);
            return this.self;
        }

        public B addSubCategoryBuilder(B builder) {
            this.subCategoryBuilders.add(builder);
            return this.self;
        }

        public B setProtection(boolean protection) {
            this.protection = protection;
            return this.self;
        }

        private List<D> buildSubCategories() {
            Stream<R> map = this.subCategoryBuilders.stream().map((v0) -> {
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

        public D build() {
            if (this.name == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (D) buildInternally(buildSubCategories());
        }
    }
}
