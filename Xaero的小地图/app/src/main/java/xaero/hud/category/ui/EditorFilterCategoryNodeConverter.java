package xaero.hud.category.ui;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.FilterObjectCategory.Builder;
import xaero.hud.category.rule.ObjectCategoryExcludeList;
import xaero.hud.category.rule.ObjectCategoryIncludeList;
import xaero.hud.category.rule.ObjectCategoryListRule;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.serialization.FilterObjectCategorySerializationHandler;
import xaero.hud.category.ui.EditorCategoryNodeConverter;
import xaero.hud.category.ui.node.EditorFilterCategoryNode;
import xaero.hud.category.ui.node.EditorFilterCategoryNode.Builder;
import xaero.hud.category.ui.node.EditorFilterSettingsNode;
import xaero.hud.category.ui.node.EditorFilterSettingsNode.Builder;
import xaero.hud.category.ui.node.rule.EditorExcludeListNode;
import xaero.hud.category.ui.node.rule.EditorIncludeListNode;
import xaero.hud.io.HudIO;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/EditorFilterCategoryNodeConverter.class */
public abstract class EditorFilterCategoryNodeConverter<E, P, C extends FilterObjectCategory<E, P, ?, C>, ED extends EditorFilterCategoryNode<C, SD, ED>, CB extends FilterObjectCategory.Builder<E, P, C, CB>, SD extends EditorFilterSettingsNode<E, P, ?>, SDB extends EditorFilterSettingsNode.Builder<E, P, SD, SDB>, EDB extends EditorFilterCategoryNode.Builder<C, ED, SD, SDB, EDB>> extends EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> {
    private final ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
    private final Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
    private final String listRuleTypePrefixSeparator;
    private final Predicate<String> inputRuleTypeStringValidator;

    public EditorFilterCategoryNodeConverter(@Nonnull Supplier<CB> categoryBuilderFactory, @Nonnull Supplier<EDB> editorDataBuilderFactory, ObjectCategoryListRuleType<E, P, ?> defaultListRuleType, Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter, String listRuleTypePrefixSeparator, Predicate<String> inputRuleTypeStringValidator) {
        super(categoryBuilderFactory, editorDataBuilderFactory);
        this.defaultListRuleType = defaultListRuleType;
        this.listRuleTypeGetter = listRuleTypeGetter;
        this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
        this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.hud.category.ui.EditorCategoryNodeConverter
    public EDB getConfiguredBuilder(C category, boolean canBeRoot) {
        EDB edb = (EDB) super.getConfiguredBuilder((EditorFilterCategoryNodeConverter<E, P, C, ED, CB, SD, SDB, EDB>) category, canBeRoot);
        EditorFilterSettingsNode.Builder builder = (EditorFilterSettingsNode.Builder) edb.getSettingDataBuilder();
        builder.setBaseRule(category.getBaseRule());
        EditorIncludeListNode.Builder<E, P> includeListBuilder = builder.getIncludeListBuilder();
        EditorExcludeListNode.Builder<E, P> excludeListBuilder = builder.getExcludeListBuilder();
        for (ObjectCategoryIncludeList<E, P, ?> includeList : category.getIncludeLists()) {
            String prefix = getListRulePrefix(includeList);
            includeList.forEach(el -> {
                includeListBuilder.addListElement(prefix + el);
            });
        }
        for (ObjectCategoryExcludeList<E, P, ?> excludeList : category.getExcludeLists()) {
            String prefix2 = getListRulePrefix(excludeList);
            excludeList.forEach(el2 -> {
                excludeListBuilder.addListElement(prefix2 + el2);
            });
        }
        edb.setListRuleTypePrefixSeparator(this.listRuleTypePrefixSeparator).setInputRuleTypeStringValidator(this.inputRuleTypeStringValidator);
        includeListBuilder.getIncludeInSuperToggleDataBuilder().setCurrentValue(Boolean.valueOf(category.getIncludeInSuperCategory()));
        excludeListBuilder.setExcludeMode(category.getExcludeMode());
        return edb;
    }

    private String getListRulePrefix(ObjectCategoryListRule<E, P, ?> list) {
        if (list.getType() == this.defaultListRuleType) {
            return "";
        }
        return list.getType().getId() + this.listRuleTypePrefixSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // xaero.hud.category.ui.EditorCategoryNodeConverter
    public CB getConfiguredBuilder(ED editorNode) {
        CB cb = (CB) super.getConfiguredBuilder((EditorFilterCategoryNodeConverter<E, P, C, ED, CB, SD, SDB, EDB>) editorNode);
        EditorFilterSettingsNode editorFilterSettingsNode = (EditorFilterSettingsNode) editorNode.getSettingsNode();
        cb.setBaseRule(editorFilterSettingsNode.getBaseRule());
        cb.setIncludeInSuperCategory(editorFilterSettingsNode.getIncludeList().getIncludeInSuper());
        cb.setExcludeMode(editorFilterSettingsNode.getExcludeList().getExcludeMode());
        editorFilterSettingsNode.getIncludeList().getList().forEach(led -> {
            String str = (String) led.getElement();
            Objects.requireNonNull(cb);
            FilterObjectCategorySerializationHandler.handleListRuleSerializedElement(str, cb::getIncludeListBuilder, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator);
        });
        editorFilterSettingsNode.getExcludeList().getList().forEach(led2 -> {
            String str = (String) led2.getElement();
            Objects.requireNonNull(cb);
            FilterObjectCategorySerializationHandler.handleListRuleSerializedElement(str, cb::getExcludeListBuilder, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator);
        });
        return cb;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/EditorFilterCategoryNodeConverter$Builder.class */
    public static abstract class Builder<E, P, C extends FilterObjectCategory<E, P, ?, C>, ED extends EditorFilterCategoryNode<C, SD, ED>, CB extends FilterObjectCategory.Builder<E, P, C, CB>, SD extends EditorFilterSettingsNode<E, P, ?>, SDB extends EditorFilterSettingsNode.Builder<E, P, SD, SDB>, EDB extends EditorFilterCategoryNode.Builder<C, ED, SD, SDB, EDB>, B extends Builder<E, P, C, ED, CB, SD, SDB, EDB, B>> extends EditorCategoryNodeConverter.Builder<C, ED, CB, SD, SDB, EDB, B> {
        protected ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
        protected Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter;
        protected String listRuleTypePrefixSeparator;
        protected Predicate<String> inputRuleTypeStringValidator;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.EditorCategoryNodeConverter.Builder
        public abstract EditorFilterCategoryNodeConverter<E, P, C, ED, CB, SD, SDB, EDB> buildInternally();

        protected Builder(Supplier<CB> categoryBuilderFactory, Supplier<EDB> editorDataBuilderFactory) {
            super(categoryBuilderFactory, editorDataBuilderFactory);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.EditorCategoryNodeConverter.Builder
        public B setDefault() {
            setDefaultListRuleType(null);
            setListRuleTypeGetter(null);
            setListRuleTypePrefixSeparator(HudIO.SEPARATOR);
            setInputRuleTypeStringValidator(s -> {
                return s.matches("[a-z_0-9\\-]+");
            });
            return (B) super.setDefault();
        }

        public B setDefaultListRuleType(ObjectCategoryListRuleType<E, P, ?> defaultListRuleType) {
            this.defaultListRuleType = defaultListRuleType;
            return (B) this.self;
        }

        public B setListRuleTypeGetter(Function<String, ObjectCategoryListRuleType<E, P, ?>> listRuleTypeGetter) {
            this.listRuleTypeGetter = listRuleTypeGetter;
            return (B) this.self;
        }

        public B setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
            this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
            return (B) this.self;
        }

        public B setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
            this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
            return (B) this.self;
        }

        @Override // xaero.hud.category.ui.EditorCategoryNodeConverter.Builder
        public EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> build() {
            if (this.defaultListRuleType == null || this.listRuleTypeGetter == null) {
                throw new IllegalStateException();
            }
            return super.build();
        }
    }
}
