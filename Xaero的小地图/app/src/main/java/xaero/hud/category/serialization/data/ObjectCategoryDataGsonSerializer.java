package xaero.hud.category.serialization.data;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import xaero.hud.category.serialization.data.ObjectCategoryData;
import xaero.hud.category.serialization.data.ObjectCategoryDataSerializer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataGsonSerializer.class */
public final class ObjectCategoryDataGsonSerializer<D extends ObjectCategoryData<D>> extends ObjectCategoryDataSerializer<D, String> {
    private final Gson gson;
    private final Class<D> dataClass;

    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public /* bridge */ /* synthetic */ String serialize(ObjectCategoryData objectCategoryData) {
        return serialize((ObjectCategoryDataGsonSerializer<D>) objectCategoryData);
    }

    private ObjectCategoryDataGsonSerializer(@Nonnull Gson gson, Class<D> dataClass) {
        this.gson = gson;
        this.dataClass = dataClass;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public String serialize(D data) {
        return this.gson.toJson(data);
    }

    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public D deserialize(String serializedData) {
        return (D) this.gson.fromJson(serializedData, this.dataClass);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataGsonSerializer$Builder.class */
    public static final class Builder<D extends ObjectCategoryData<D>> extends ObjectCategoryDataSerializer.Builder<D, String> {
        private final Gson gson;
        private final Class<D> dataClass;

        public Builder(Gson gson, Class<D> dataClass) {
            this.gson = gson;
            this.dataClass = dataClass;
        }

        @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer.Builder
        public Builder<D> setDefault() {
            super.setDefault();
            return this;
        }

        @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer.Builder
        public ObjectCategoryDataGsonSerializer<D> build() {
            return new ObjectCategoryDataGsonSerializer<>(this.gson, this.dataClass);
        }

        public static <D extends ObjectCategoryData<D>> Builder<D> begin(Gson gson, Class<D> dataClass) {
            return new Builder(gson, dataClass).setDefault();
        }
    }
}
