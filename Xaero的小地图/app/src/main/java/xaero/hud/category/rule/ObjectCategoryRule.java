package xaero.hud.category.rule;

import javax.annotation.Nonnull;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/ObjectCategoryRule.class */
public abstract class ObjectCategoryRule<E, P> {
    private final String name;

    public abstract boolean isFollowedBy(E e, P p);

    ObjectCategoryRule(@Nonnull String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return String.format("include(%s)", this.name);
    }
}
