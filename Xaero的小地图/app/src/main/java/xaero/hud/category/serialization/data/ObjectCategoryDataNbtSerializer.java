package xaero.hud.category.serialization.data;

import java.util.Iterator;
import java.util.function.Supplier;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import xaero.hud.category.serialization.data.ObjectCategoryData;
import xaero.hud.category.serialization.data.ObjectCategoryData.Builder;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataNbtSerializer.class */
public class ObjectCategoryDataNbtSerializer<D extends ObjectCategoryData<D>, DB extends ObjectCategoryData.Builder<D, DB>> extends ObjectCategoryDataSerializer<D, CompoundTag> {
    private final Supplier<DB> builderSupplier;

    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public /* bridge */ /* synthetic */ CompoundTag serialize(ObjectCategoryData objectCategoryData) {
        return serialize((ObjectCategoryDataNbtSerializer<D, DB>) objectCategoryData);
    }

    protected ObjectCategoryDataNbtSerializer(Supplier<DB> builderSupplier) {
        this.builderSupplier = builderSupplier;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public CompoundTag serialize(D data) {
        CompoundTag resultTag = new CompoundTag();
        resultTag.putString("n", data.getName());
        resultTag.putBoolean("p", data.getProtection());
        CompoundTag settingOverrides = new CompoundTag();
        data.getSettingOverrideIterator().forEachRemaining(entry -> {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                return;
            }
            if (value instanceof Boolean) {
                settingOverrides.putBoolean(key, ((Boolean) value).booleanValue());
            } else if (value instanceof Double) {
                settingOverrides.putDouble(key, ((Double) value).doubleValue());
            } else {
                if (value instanceof Integer) {
                    settingOverrides.putInt(key, ((Integer) value).intValue());
                    return;
                }
                throw new IllegalArgumentException("Unsupported category setting type: " + String.valueOf(value.getClass()));
            }
        });
        resultTag.put("v", settingOverrides);
        ListTag subCategoriesTag = new ListTag();
        data.getSubCategoryIterator().forEachRemaining(sub -> {
            CompoundTag subCategoryTag = serialize((ObjectCategoryDataNbtSerializer<D, DB>) sub);
            subCategoriesTag.add(subCategoryTag);
        });
        resultTag.put("s", subCategoriesTag);
        return resultTag;
    }

    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public final D deserialize(CompoundTag compoundTag) {
        return (D) getConfiguredBuilder(compoundTag).build();
    }

    protected DB getConfiguredBuilder(CompoundTag serializedData) {
        DB builder = this.builderSupplier.get();
        builder.setName(serializedData.getString("n"));
        builder.setProtection(serializedData.getBoolean("p"));
        CompoundTag settingOverrides = serializedData.getCompound("v");
        for (String key : settingOverrides.getAllKeys()) {
            IntTag intTag = settingOverrides.get(key);
            if (intTag instanceof ByteTag) {
                builder.setSettingOverride(key, Boolean.valueOf(((ByteTag) intTag).getAsByte() == 1));
            } else if (intTag instanceof DoubleTag) {
                builder.setSettingOverride(key, Double.valueOf(((DoubleTag) intTag).getAsDouble()));
            } else if (intTag instanceof IntTag) {
                builder.setSettingOverride(key, Integer.valueOf(intTag.getAsInt()));
            } else {
                throw new IllegalArgumentException("Unsupported category setting NBT tag type: " + String.valueOf(intTag.getClass()));
            }
        }
        ListTag subCategoriesTag = serializedData.getList("s", 10);
        Iterator it = subCategoriesTag.iterator();
        while (it.hasNext()) {
            builder.addSubCategoryBuilder(getConfiguredBuilder((Tag) it.next()));
        }
        return builder;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataNbtSerializer$Builder.class */
    public static abstract class Builder<D extends ObjectCategoryData<D>, DB extends ObjectCategoryData.Builder<D, DB>, B extends Builder<D, DB, B>> {
        protected final B self = this;
        protected Supplier<DB> builderSupplier;

        protected abstract ObjectCategoryDataNbtSerializer<D, DB> buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setBuilderSupplier(null);
            return this.self;
        }

        public B setBuilderSupplier(Supplier<DB> builderSupplier) {
            this.builderSupplier = builderSupplier;
            return this.self;
        }

        public ObjectCategoryDataNbtSerializer<D, DB> build() {
            if (this.builderSupplier == null) {
                throw new IllegalStateException();
            }
            return buildInternally();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/ObjectCategoryDataNbtSerializer$FinalBuilder.class */
    public static final class FinalBuilder<D extends ObjectCategoryData<D>, DB extends ObjectCategoryData.Builder<D, DB>> extends Builder<D, DB, FinalBuilder<D, DB>> {
        private FinalBuilder() {
        }

        @Override // xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer.Builder
        protected ObjectCategoryDataNbtSerializer<D, DB> buildInternally() {
            return new ObjectCategoryDataNbtSerializer<>(this.builderSupplier);
        }

        public static <D extends ObjectCategoryData<D>, DB extends ObjectCategoryData.Builder<D, DB>> FinalBuilder<D, DB> begin() {
            return (FinalBuilder) new FinalBuilder().setDefault();
        }
    }
}
