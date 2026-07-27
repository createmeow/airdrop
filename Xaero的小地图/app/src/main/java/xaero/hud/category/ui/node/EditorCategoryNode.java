package xaero.hud.category.ui.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ObjectCategory;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.ConnectionLineType;
import xaero.hud.category.ui.entry.EditorListEntryCategory;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorAdderNode;
import xaero.hud.category.ui.node.EditorCategoryNode;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.EditorSettingsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorCategoryNode.class */
public abstract class EditorCategoryNode<C extends ObjectCategory<?, C>, SD extends EditorSettingsNode<?>, ED extends EditorCategoryNode<C, SD, ED>> extends EditorNode {
    private final ED self;
    private boolean cut;
    private final List<ED> subCategories;
    private final EditorAdderNode topAdder;
    private final Function<EditorAdderNode, ED> newCategorySupplier;
    private final SD settingsNode;

    protected EditorCategoryNode(@Nonnull SD settingNode, @Nonnull List<ED> subCategories, @Nonnull EditorAdderNode topAdder, @Nonnull Function<EditorAdderNode, ED> newCategorySupplier, boolean movable, int subIndex, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.self = this;
        this.settingsNode = settingNode;
        this.subCategories = subCategories;
        this.topAdder = topAdder;
        this.newCategorySupplier = newCategorySupplier;
    }

    public SD getSettingsNode() {
        return this.settingsNode;
    }

    public final List<ED> getSubCategories() {
        return this.subCategories;
    }

    public String getName() {
        return this.settingsNode.getNameOption().getResult();
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return Component.translatable(getName());
    }

    private BiConsumer<EditorAdderNode, Integer> getAdderHandler() {
        return (adder, i) -> {
            if (!adder.isConfirmed()) {
                return;
            }
            ED newCategory = this.newCategorySupplier.apply(adder);
            this.subCategories.add(i.intValue(), newCategory);
            adder.reset();
        };
    }

    private Runnable getDeletionHandler() {
        return () -> {
            Iterator<ED> subIterator = this.subCategories.iterator();
            while (subIterator.hasNext()) {
                ED subCategory = subIterator.next();
                if (subCategory.getSettingsNode().isToBeDeleted()) {
                    subIterator.remove();
                }
            }
        };
    }

    public Supplier<Boolean> getMoveAction(int subIndex, int direction, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
        return () -> {
            int newSlot = subIndex + direction;
            ED subCategoryToMove = this.subCategories.get(subIndex);
            rowList.setLastExpandedData(subCategoryToMove);
            if (newSlot < 0) {
                this.subCategories.remove(subCategoryToMove);
                this.subCategories.add(subCategoryToMove);
                return true;
            }
            if (newSlot >= this.subCategories.size()) {
                this.subCategories.remove(subCategoryToMove);
                this.subCategories.add(0, subCategoryToMove);
                return true;
            }
            rowList.restoreScrollAfterUpdate();
            ED subCategoryToReplace = this.subCategories.get(newSlot);
            this.subCategories.set(subIndex, subCategoryToReplace);
            this.subCategories.set(newSlot, subCategoryToMove);
            return true;
        };
    }

    public Supplier<Boolean> getDuplicateAction(int subIndex, GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowList) {
        return () -> {
            if (subIndex < 0 || subIndex >= this.subCategories.size()) {
                return false;
            }
            ED subCategoryToDuplicate = this.subCategories.get(subIndex);
            GuiCategoryEditor screenToRestore = (GuiCategoryEditor) Minecraft.getInstance().screen;
            Minecraft.getInstance().setScreen(new ConfirmScreen(z -> {
                if (!z) {
                    Minecraft.getInstance().setScreen(screenToRestore);
                    return;
                }
                EditorCategoryNode editorCategoryNodeConvert = rowList.getDataConverter().convert(rowList.getDataConverter().convert(subCategoryToDuplicate), false);
                editorCategoryNodeConvert.removeProtectionRecursive();
                this.subCategories.add(subIndex + 1, editorCategoryNodeConvert);
                Minecraft.getInstance().setScreen(screenToRestore);
                GuiCategoryEditor.SettingRowList rowList2 = screenToRestore.getRowList();
                rowList2.setLastExpandedData(editorCategoryNodeConvert);
                rowList2.updateEntries();
            }, Component.translatable("gui.xaero_category_duplicate_confirm"), subCategoryToDuplicate.getDisplayName().copy().withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)))));
            return true;
        };
    }

    public Supplier<Boolean> getCutAction(ED parent, GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowList) {
        return () -> {
            rowList.setCutCategory(this.self, parent);
            rowList.setLastExpandedData(this);
            rowList.restoreScrollAfterUpdate();
            return true;
        };
    }

    public Supplier<Boolean> getPasteAction(GuiCategoryEditor<C, ED, ?, ?, ?, ?>.SettingRowList rowList) {
        return () -> {
            rowList.pasteTo(this.self);
            rowList.restoreScrollAfterUpdate();
            return true;
        };
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        BiConsumer<EditorAdderNode, Integer> adderHandler = getAdderHandler();
        adderHandler.accept(this.topAdder, 0);
        getDeletionHandler().run();
        List<EditorNode> result = new ArrayList<>(this.subCategories);
        result.add(0, this.topAdder);
        result.add(0, this.settingsNode);
        return result;
    }

    public void removeProtectionRecursive() {
        getSettingsNode().setProtected(false);
        for (ED sub : this.subCategories) {
            sub.removeProtectionRecursive();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorCategoryNode$Builder.class */
    public static abstract class Builder<C extends ObjectCategory<?, C>, ED extends EditorCategoryNode<C, SD, ED>, SD extends EditorSettingsNode<?>, SDB extends EditorSettingsNode.Builder<SD, SDB>, EDB extends Builder<C, ED, SD, SDB, EDB>> extends EditorNode.Builder<EDB> {
        protected final EDB self;
        protected String name;
        protected final SDB settingsDataBuilder;
        protected final List<EDB> subCategoryBuilders;
        protected final ListFactory listFactory;
        protected final EditorAdderNode.Builder topAdderBuilder;
        protected Function<EditorAdderNode, ED> newCategorySupplier;
        protected int subIndex;

        protected Builder(ListFactory listFactory, SDB settingsDataBuilder) {
            if (settingsDataBuilder == null) {
                throw new IllegalStateException("settings data builder cannot be null!");
            }
            this.settingsDataBuilder = settingsDataBuilder;
            this.subCategoryBuilders = listFactory.get();
            this.listFactory = listFactory;
            this.topAdderBuilder = EditorAdderNode.Builder.begin(listFactory);
            this.self = this;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EDB setDefault() {
            super.setDefault();
            setName(null);
            this.settingsDataBuilder.setDefault();
            this.subCategoryBuilders.clear();
            this.topAdderBuilder.setDisplayName(Component.translatable("gui.xaero_category_add_subcategory"));
            setMovable(true);
            setSubIndex(0);
            setTooltipSupplier((parent, data) -> {
                Component displayNameComponent = data.getDisplayName();
                Tooltip tooltip = new Tooltip((Component) Component.translatable("gui.xaero_box_category", new Object[]{displayNameComponent}));
                tooltip.setAutoLinebreak(false);
                return tooltip;
            });
            return this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry mainEntryFactory(EditorNode data, EditorNode parent, int index, ConnectionLineType lineType, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, int screenWidth, boolean isFinalExpanded) {
            return new EditorListEntryCategory(screenWidth, index, rowList, lineType, (EditorCategoryNode) data, (EditorCategoryNode) parent, data.getTooltipSupplier(parent), isFinalExpanded);
        }

        public EDB setNewCategorySupplier(Function<EditorAdderNode, ED> newCategorySupplier) {
            this.newCategorySupplier = newCategorySupplier;
            return this.self;
        }

        public EDB setSubIndex(int subIndex) {
            this.subIndex = subIndex;
            return this.self;
        }

        public EDB setName(String name) {
            this.name = name;
            return this.self;
        }

        public SDB getSettingDataBuilder() {
            return this.settingsDataBuilder;
        }

        public EDB addSubCategoryBuilder(EDB subCategory) {
            subCategory.setSubIndex(this.subCategoryBuilders.size());
            this.subCategoryBuilders.add(subCategory);
            return this.self;
        }

        protected List<ED> buildSubCategories() {
            Stream<R> map = this.subCategoryBuilders.stream().map((v0) -> {
                return v0.build();
            });
            ListFactory listFactory = this.listFactory;
            Objects.requireNonNull(listFactory);
            return (List) map.collect(listFactory::get, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public ED build() {
            if (this.name == null || this.newCategorySupplier == null) {
                throw new IllegalStateException("required fields not set!");
            }
            this.settingsDataBuilder.getNameOptionBuilder().setInput(this.name);
            this.settingsDataBuilder.getNameOptionBuilder().setDisplayName(Component.translatable("gui.xaero_category_name"));
            this.settingsDataBuilder.getNameOptionBuilder().setMaxLength(200);
            return (ED) super.build();
        }
    }
}
