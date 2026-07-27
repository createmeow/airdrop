package xaero.hud.category.rule.resolver;

import java.util.Iterator;
import java.util.List;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.rule.ExcludeListMode;
import xaero.hud.category.rule.ObjectCategoryExcludeList;
import xaero.hud.category.rule.ObjectCategoryIncludeList;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/resolver/ObjectCategoryRuleResolver.class */
public final class ObjectCategoryRuleResolver {
    private ObjectCategoryRuleResolver() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E, P, C extends FilterObjectCategory<E, P, ?, C>> C resolve(C c, E e, P p) {
        if (!followsRules(c, e, p)) {
            return null;
        }
        Iterator directSubCategoryIterator = c.getDirectSubCategoryIterator();
        while (directSubCategoryIterator.hasNext()) {
            C c2 = (C) resolve((FilterObjectCategory) directSubCategoryIterator.next(), e, p);
            if (c2 != null) {
                return c2;
            }
        }
        return c;
    }

    private <E, P, C extends FilterObjectCategory<E, P, ?, C>> boolean followsRules(C category, E element, P context) {
        boolean result = category.getBaseRule().isFollowedBy(element, context);
        if (!result) {
            List<ObjectCategoryIncludeList<E, P, ?>> includeLists = category.getIncludeLists();
            Iterator<ObjectCategoryIncludeList<E, P, ?>> it = includeLists.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ObjectCategoryIncludeList<E, P, ?> includeList = it.next();
                if (includeList.isFollowedBy(element, context)) {
                    result = true;
                    break;
                }
            }
        }
        if (result) {
            List<ObjectCategoryExcludeList<E, P, ?>> excludeLists = category.getExcludeLists();
            if (category.getExcludeMode() == ExcludeListMode.ALL_BUT) {
                result = false;
            }
            Iterator<ObjectCategoryExcludeList<E, P, ?>> it2 = excludeLists.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                ObjectCategoryExcludeList<E, P, ?> excludeList = it2.next();
                if (result != excludeList.isFollowedBy(element, context)) {
                    result = !result;
                }
            }
        }
        return result;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/rule/resolver/ObjectCategoryRuleResolver$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public ObjectCategoryRuleResolver build() {
            return new ObjectCategoryRuleResolver();
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
