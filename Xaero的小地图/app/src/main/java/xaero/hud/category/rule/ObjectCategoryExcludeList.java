package xaero.hud.category.rule;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.rule.ObjectCategoryListRule;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryExcludeList.class */
public final class ObjectCategoryExcludeList<E, P, S> extends ObjectCategoryListRule<E, P, S> {
    private ExcludeListMode excludeMode;

    private ObjectCategoryExcludeList(@Nonnull ObjectCategoryListRuleType<E, P, S> type, @Nonnull List<String> stringList, @Nonnull Set<S> set, @Nonnull ExcludeListMode excludeMode) {
        super(type, "exclude list", stringList, set);
        this.excludeMode = excludeMode;
    }

    @Override // xaero.hud.category.rule.ObjectCategoryRule
    public boolean isFollowedBy(E object, P context) {
        boolean inList = inList(object, context);
        return (this.excludeMode == ExcludeListMode.ALL_BUT && inList) || (this.excludeMode == ExcludeListMode.ONLY && !inList);
    }

    public ExcludeListMode getExcludeMode() {
        return this.excludeMode;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryExcludeList$Builder.class */
    public static final class Builder<E, P, S> extends ObjectCategoryListRule.Builder<E, P, S, Builder<E, P, S>> {
        private ExcludeListMode excludeMode;

        private Builder(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
            super(listFactory, type);
        }

        @Override // xaero.hud.category.rule.ObjectCategoryListRule.Builder
        public Builder<E, P, S> setDefault() {
            super.setDefault();
            setExcludeMode(ExcludeListMode.ONLY);
            return (Builder) this.self;
        }

        public Builder<E, P, S> setExcludeMode(ExcludeListMode excludeMode) {
            this.excludeMode = excludeMode;
            return (Builder) this.self;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.rule.ObjectCategoryListRule.Builder
        public <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryExcludeList<E, P, S> build(List<C> subCategories, Function<C, ObjectCategoryListRule<E, P, S>> subListGetter, Function<C, ObjectCategoryListRule<E, P, S>> subListExceptionsGetter) {
            return (ObjectCategoryExcludeList) super.build((List) subCategories, (Function) subListGetter, (Function) subListExceptionsGetter);
        }

        public <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryExcludeList<E, P, S> build(List<C> subCategories) {
            return build((List) subCategories, (Function) null, (Function) null);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.rule.ObjectCategoryListRule.Builder
        public ObjectCategoryExcludeList<E, P, S> buildInternally(Set<S> effectiveSet) {
            return new ObjectCategoryExcludeList<>(this.type, this.stringList, effectiveSet, this.excludeMode);
        }

        public static <E, P, S> Builder<E, P, S> begin(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
            return new Builder(listFactory, type).setDefault();
        }
    }
}
