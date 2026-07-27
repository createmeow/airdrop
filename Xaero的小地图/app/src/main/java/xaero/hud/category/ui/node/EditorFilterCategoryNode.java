package xaero.hud.category.ui.node;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import xaero.common.misc.ListFactory;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorCategoryNode;
import xaero.hud.category.ui.node.EditorFilterCategoryNode;
import xaero.hud.category.ui.node.EditorFilterSettingsNode;
import xaero.hud.category.ui.node.EditorSettingsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.hud.io.HudIO;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorFilterCategoryNode.class */
public abstract class EditorFilterCategoryNode<C extends FilterObjectCategory<?, ?, ?, C>, SD extends EditorSettingsNode<?>, ED extends EditorFilterCategoryNode<C, SD, ED>> extends EditorCategoryNode<C, SD, ED> {
    protected EditorFilterCategoryNode(@Nonnull SD settingOverrides, @Nonnull List<ED> subCategories, @Nonnull EditorAdderNode topAdder, @Nonnull Function<EditorAdderNode, ED> newCategorySupplier, boolean movable, int subIndex, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier) {
        super(settingOverrides, subCategories, topAdder, newCategorySupplier, movable, subIndex, listEntryFactory, tooltipSupplier);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorFilterCategoryNode$Builder.class */
    public static abstract class Builder<C extends FilterObjectCategory<?, ?, ?, C>, ED extends EditorFilterCategoryNode<C, SD, ED>, SD extends EditorFilterSettingsNode<?, ?, ?>, SDB extends EditorFilterSettingsNode.Builder<?, ?, SD, SDB>, EDB extends Builder<C, ED, SD, SDB, EDB>> extends EditorCategoryNode.Builder<C, ED, SD, SDB, EDB> {
        private String listRuleTypePrefixSeparator;
        private Predicate<String> inputRuleTypeStringValidator;

        protected Builder(ListFactory listFactory, SDB settingsDataBuilder) {
            super(listFactory, settingsDataBuilder);
        }

        @Override // xaero.hud.category.ui.node.EditorCategoryNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EDB setDefault() {
            super.setDefault();
            setListRuleTypePrefixSeparator(HudIO.SEPARATOR);
            setInputRuleTypeStringValidator(s -> {
                return s.matches("[a-z_0-9\\-]+");
            });
            return (EDB) this.self;
        }

        public EDB setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
            this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
            return (EDB) this.self;
        }

        public EDB setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
            this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
            return (EDB) this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorCategoryNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public ED build() {
            if (this.listRuleTypePrefixSeparator == null) {
                throw new IllegalStateException();
            }
            ((EditorFilterSettingsNode.Builder) this.settingsDataBuilder).setListRuleTypePrefixSeparator(this.listRuleTypePrefixSeparator).setInputRuleTypeStringValidator(this.inputRuleTypeStringValidator);
            return (ED) super.build();
        }
    }
}
