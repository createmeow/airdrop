package xaero.hud.category.rule;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryHardRule.class */
public final class ObjectCategoryHardRule<E, P> extends ObjectCategoryRule<E, P> {
    private final Predicate<E, P> predicate;
    private final boolean reversed;

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryHardRule$Predicate.class */
    public interface Predicate<E, P> {
        boolean test(E e, P p);
    }

    private ObjectCategoryHardRule(@Nonnull String name, boolean reversed, @Nonnull Predicate<E, P> predicate) {
        super(name);
        this.reversed = reversed;
        this.predicate = predicate;
    }

    @Override // xaero.hud.category.rule.ObjectCategoryRule
    public boolean isFollowedBy(E object, P context) {
        if (this.reversed) {
            return !this.predicate.test(object, context);
        }
        return this.predicate.test(object, context);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryHardRule$Builder.class */
    public static final class Builder<E, P> {
        private String name;
        private Predicate<E, P> predicate;
        private boolean reversed;

        public Builder<E, P> setDefault() {
            setName(null);
            setPredicate(null);
            setReversed(false);
            return this;
        }

        public Builder<E, P> setName(String name) {
            this.name = name;
            return this;
        }

        public Builder<E, P> setPredicate(Predicate<E, P> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder<E, P> setReversed(boolean reversed) {
            this.reversed = reversed;
            return this;
        }

        public ObjectCategoryHardRule<E, P> build(Map<String, ObjectCategoryHardRule<E, P>> destinationMap, List<ObjectCategoryHardRule<E, P>> destinationList) {
            if (this.name == null || this.predicate == null) {
                throw new IllegalStateException("required fields not set!");
            }
            ObjectCategoryHardRule<E, P> rule = new ObjectCategoryHardRule<>(this.name, this.reversed, this.predicate);
            destinationMap.put(rule.getName(), rule);
            destinationList.add(rule);
            return rule;
        }
    }
}
