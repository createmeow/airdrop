package xaero.hud.category.serialization.data;

import xaero.hud.category.serialization.data.ObjectCategoryData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataSerializer.class */
public abstract class ObjectCategoryDataSerializer<D extends ObjectCategoryData<D>, S> {
    public abstract S serialize(D d);

    public abstract D deserialize(S s);

    protected ObjectCategoryDataSerializer() {
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataSerializer$Builder.class */
    public static abstract class Builder<D extends ObjectCategoryData<D>, S> {
        public abstract ObjectCategoryDataSerializer<D, S> build();

        public Builder<D, S> setDefault() {
            return this;
        }
    }
}
