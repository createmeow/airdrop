package xaero.hud.category.serialization.data;

import java.util.Iterator;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import xaero.hud.category.rule.ExcludeListMode;
import xaero.hud.category.serialization.data.FilterObjectCategoryData;
import xaero.hud.category.serialization.data.FilterObjectCategoryData.Builder;
import xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/FilterObjectCategoryDataNbtSerializer.class */
public class FilterObjectCategoryDataNbtSerializer<D extends FilterObjectCategoryData<D>, DB extends FilterObjectCategoryData.Builder<D, DB>> extends ObjectCategoryDataNbtSerializer<D, DB> {
    protected FilterObjectCategoryDataNbtSerializer(Supplier<DB> builderSupplier) {
        super(builderSupplier);
    }

    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer, xaero.hud.category.serialization.data.ObjectCategoryDataSerializer
    public CompoundTag serialize(D data) {
        CompoundTag resultTag = super.serialize((FilterObjectCategoryDataNbtSerializer<D, DB>) data);
        resultTag.putString("h", data.getHardInclude());
        resultTag.putBoolean("i", data.getIncludeListInSuperCategory());
        resultTag.putString("m", data.getExcludeMode().name());
        ListTag includeListTag = new ListTag();
        data.getIncludeListIterator().forEachRemaining(includeEntry -> {
            includeListTag.add(StringTag.valueOf(includeEntry));
        });
        resultTag.put("l", includeListTag);
        ListTag excludeListTag = new ListTag();
        data.getExcludeListIterator().forEachRemaining(excludeEntry -> {
            excludeListTag.add(StringTag.valueOf(excludeEntry));
        });
        resultTag.put("e", excludeListTag);
        return resultTag;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer
    public DB getConfiguredBuilder(CompoundTag serializedData) {
        DB db = (DB) super.getConfiguredBuilder(serializedData);
        db.setHardInclude(serializedData.getString("h"));
        db.setIncludeListInSuperCategory(serializedData.getBoolean("i"));
        db.setExcludeMode(ExcludeListMode.valueOf(serializedData.getString("m")));
        ListTag includeListTag = serializedData.getList("l", 8);
        Iterator it = includeListTag.iterator();
        while (it.hasNext()) {
            Tag includeEntryTag = (Tag) it.next();
            db.addToIncludeList(includeEntryTag.getAsString());
        }
        ListTag excludeListTag = serializedData.getList("e", 8);
        Iterator it2 = excludeListTag.iterator();
        while (it2.hasNext()) {
            Tag excludeEntryTag = (Tag) it2.next();
            db.addToExcludeList(excludeEntryTag.getAsString());
        }
        return db;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/FilterObjectCategoryDataNbtSerializer$Builder.class */
    public static abstract class Builder<D extends FilterObjectCategoryData<D>, DB extends FilterObjectCategoryData.Builder<D, DB>, B extends Builder<D, DB, B>> extends ObjectCategoryDataNbtSerializer.Builder<D, DB, B> {
        protected Builder() {
        }

        @Override // xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer.Builder
        public FilterObjectCategoryDataNbtSerializer<D, DB> build() {
            return (FilterObjectCategoryDataNbtSerializer) super.build();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/serialization/data/FilterObjectCategoryDataNbtSerializer$FinalBuilder.class */
    public static final class FinalBuilder<D extends FilterObjectCategoryData<D>, DB extends FilterObjectCategoryData.Builder<D, DB>> extends Builder<D, DB, FinalBuilder<D, DB>> {
        private FinalBuilder() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer.Builder
        public FilterObjectCategoryDataNbtSerializer<D, DB> buildInternally() {
            return new FilterObjectCategoryDataNbtSerializer<>(this.builderSupplier);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static <D extends FilterObjectCategoryData<D>, DB extends FilterObjectCategoryData.Builder<D, DB>> FinalBuilder<D, DB> begin() {
            return (FinalBuilder) new FinalBuilder().setDefault();
        }
    }
}
