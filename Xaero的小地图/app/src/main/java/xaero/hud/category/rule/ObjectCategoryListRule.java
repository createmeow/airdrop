package xaero.hud.category.rule;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.hud.category.FilterObjectCategory;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryListRule.class */
public abstract class ObjectCategoryListRule<E, P, S> extends ObjectCategoryRule<E, P> implements Iterable<String> {
    private final List<String> stringList;
    private final Set<S> set;
    private final ObjectCategoryListRuleType<E, P, S> type;

    ObjectCategoryListRule(@Nonnull ObjectCategoryListRuleType<E, P, S> type, @Nonnull String name, @Nonnull List<String> stringList, @Nonnull Set<S> set) {
        super(name);
        this.type = type;
        this.stringList = stringList;
        this.set = set;
    }

    public boolean inList(E object, P context) {
        S s;
        return (this.set.isEmpty() || (s = this.type.getGetter().apply(object, context)) == null || !this.set.contains(s)) ? false : true;
    }

    @Override // java.lang.Iterable
    public Iterator<String> iterator() {
        return this.stringList.iterator();
    }

    public Predicate<String> getStringValidator() {
        return this.type.getStringValidator();
    }

    public ObjectCategoryListRuleType<E, P, S> getType() {
        return this.type;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryListRule$Builder.class */
    public static abstract class Builder<E, P, S, B extends Builder<E, P, S, B>> {
        protected final B self = this;
        protected final List<String> stringList;
        protected final ObjectCategoryListRuleType<E, P, S> type;

        protected abstract ObjectCategoryListRule<E, P, S> buildInternally(Set<S> set);

        protected Builder(ListFactory listFactory, ObjectCategoryListRuleType<E, P, S> type) {
            this.stringList = listFactory.get();
            this.type = type;
        }

        public B setDefault() {
            this.stringList.clear();
            return this.self;
        }

        public B addListElement(String element) {
            this.stringList.add(element);
            return this.self;
        }

        public ObjectCategoryListRuleType<E, P, S> getType() {
            return this.type;
        }

        protected <C extends FilterObjectCategory<E, P, ?, C>> ObjectCategoryListRule<E, P, S> build(List<C> subCategories, Function<C, ObjectCategoryListRule<E, P, S>> subListGetter, Function<C, ObjectCategoryListRule<E, P, S>> subListExceptionsGetter) {
            if (this.stringList == null) {
                throw new IllegalStateException("required fields not set!");
            }
            Set<S> effectiveSet = new HashSet<>();
            for (String stringElement : this.stringList) {
                String validatedString = this.type.getStringFixer().apply(stringElement);
                List<S> resolvedElement = this.type.getElementResolver().apply(validatedString);
                if (resolvedElement != null && !resolvedElement.isEmpty()) {
                    effectiveSet.addAll(resolvedElement);
                }
            }
            if (subListGetter == null) {
                return buildInternally(effectiveSet);
            }
            for (C subCategory : subCategories) {
                if (subCategory.getIncludeInSuperCategory()) {
                    ObjectCategoryListRule<E, P, S> subList = subListGetter.apply(subCategory);
                    ObjectCategoryListRule<E, P, S> subListExceptions = subListExceptionsGetter == null ? null : subListExceptionsGetter.apply(subCategory);
                    Stream<S> streamFilter = ((ObjectCategoryListRule) subList).set.stream().filter(s -> {
                        return subListExceptions == null || !subListExceptions.set.contains(s);
                    });
                    Objects.requireNonNull(effectiveSet);
                    streamFilter.forEach(effectiveSet::add);
                }
            }
            return buildInternally(effectiveSet);
        }
    }
}
