package xaero.hud.category.ui;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.ObjectCategory.Builder;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.node.EditorCategoryNode;
import xaero.hud.category.ui.node.EditorCategoryNode.Builder;
import xaero.hud.category.ui.node.EditorSettingsNode;
import xaero.hud.category.ui.node.EditorSettingsNode.Builder;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/EditorCategoryNodeConverter.class */
public abstract class EditorCategoryNodeConverter<C extends ObjectCategory<?, C>, ED extends EditorCategoryNode<C, SD, ED>, CB extends ObjectCategory.Builder<C, CB>, SD extends EditorSettingsNode<?>, SDB extends EditorSettingsNode.Builder<SD, SDB>, EDB extends EditorCategoryNode.Builder<C, ED, SD, SDB, EDB>> {
    private final Supplier<CB> categoryBuilderFactory;
    private final Supplier<EDB> editorNodeBuilderFactory;

    public EditorCategoryNodeConverter(@Nonnull Supplier<CB> categoryBuilderFactory, @Nonnull Supplier<EDB> editorNodeBuilderFactory) {
        this.categoryBuilderFactory = categoryBuilderFactory;
        this.editorNodeBuilderFactory = editorNodeBuilderFactory;
    }

    public ED convert(C c, boolean z) {
        return (ED) getConfiguredBuilder(c, z).build();
    }

    protected EDB getConfiguredBuilder(C category, boolean canBeRoot) {
        EDB editorNodeBuilder = this.editorNodeBuilderFactory.get();
        editorNodeBuilder.setName(category.getName());
        EditorSettingsNode.Builder settingDataBuilder = editorNodeBuilder.getSettingDataBuilder();
        category.getSettingOverridesIterator().forEachRemaining(e -> {
            setSettingValue((EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB>) settingDataBuilder, (ObjectCategorySetting) e.getKey(), e.getValue());
        });
        settingDataBuilder.setRootSettings(canBeRoot && category.getSuperCategory() == null);
        settingDataBuilder.setProtection(category.getProtection());
        category.getDirectSubCategoryIterator().forEachRemaining(sc -> {
            editorNodeBuilder.addSubCategoryBuilder(getConfiguredBuilder(sc, canBeRoot));
        });
        return editorNodeBuilder;
    }

    private <T> void setSettingValue(SDB settingOverridesBuilder, ObjectCategorySetting<T> setting, Object value) {
        settingOverridesBuilder.setSettingValue(setting, value);
    }

    public C convert(ED ed) {
        return (C) getConfiguredBuilder(ed).build();
    }

    protected CB getConfiguredBuilder(ED editorNode) {
        CB categoryBuilder = this.categoryBuilderFactory.get();
        categoryBuilder.setName(editorNode.getName());
        categoryBuilder.setProtection(editorNode.getSettingsNode().getProtection());
        editorNode.getSettingsNode().getSettings().forEach((k, editorOptionsNode) -> {
            setSettingValue((EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB>) categoryBuilder, k, ((IEditorSettingNode) editorOptionsNode).getSettingValue());
        });
        editorNode.getSubCategories().forEach(sed -> {
            categoryBuilder.addSubCategoryBuilder(getConfiguredBuilder(sed));
        });
        return categoryBuilder;
    }

    private <T> void setSettingValue(CB categoryBuilder, ObjectCategorySetting<T> setting, Object value) {
        categoryBuilder.setSettingValue(setting, value);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/EditorCategoryNodeConverter$Builder.class */
    public static abstract class Builder<C extends ObjectCategory<?, C>, ED extends EditorCategoryNode<C, SD, ED>, CB extends ObjectCategory.Builder<C, CB>, SD extends EditorSettingsNode<?>, SDB extends EditorSettingsNode.Builder<SD, SDB>, EDB extends EditorCategoryNode.Builder<C, ED, SD, SDB, EDB>, B extends Builder<C, ED, CB, SD, SDB, EDB, B>> {
        protected final B self = this;
        protected final Supplier<CB> categoryBuilderFactory;
        protected final Supplier<EDB> editorDataBuilderFactory;

        protected abstract EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> buildInternally();

        protected Builder(Supplier<CB> categoryBuilderFactory, Supplier<EDB> editorDataBuilderFactory) {
            this.categoryBuilderFactory = categoryBuilderFactory;
            this.editorDataBuilderFactory = editorDataBuilderFactory;
        }

        protected B setDefault() {
            return this.self;
        }

        public EditorCategoryNodeConverter<C, ED, CB, SD, SDB, EDB> build() {
            return buildInternally();
        }
    }
}
