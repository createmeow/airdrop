package xaero.hud.category.ui.node.rule;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.ConnectionLineType;
import xaero.hud.category.ui.entry.EditorListEntryWidget;
import xaero.hud.category.ui.entry.EditorListEntryWrapper;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.entry.widget.EditorButton;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.EditorSimpleDeletableWrapperNode;
import xaero.hud.category.ui.node.options.text.EditorTextFieldOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.common.gui.widget.TooltipInfo;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/rule/EditorListNode.class */
public abstract class EditorListNode extends EditorNode {
    protected final List<EditorSimpleDeletableWrapperNode<String>> list;
    private final EditorTextFieldOptionsNode topAdder;
    private final EditorTextFieldOptionsNode bottomAdder;
    private final ListFactory listFactory;
    private final EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback;
    private final IEditorDataTooltipSupplier helpTooltipSupplier;

    protected EditorListNode(@Nonnull List<EditorSimpleDeletableWrapperNode<String>> list, @Nonnull ListFactory listFactory, @Nonnull EditorTextFieldOptionsNode topAdder, @Nonnull EditorTextFieldOptionsNode bottomAdder, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, @Nonnull EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback, @Nonnull IEditorDataTooltipSupplier helpTooltipSupplier) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.list = list;
        this.listFactory = listFactory;
        this.topAdder = topAdder;
        this.bottomAdder = bottomAdder;
        this.deletionCallback = deletionCallback;
        this.helpTooltipSupplier = helpTooltipSupplier;
    }

    public List<EditorSimpleDeletableWrapperNode<String>> getList() {
        return this.list;
    }

    public EditorSimpleDeletableWrapperNode.DeletionCallback getDeletionCallback() {
        return this.deletionCallback;
    }

    private Consumer<EditorTextFieldOptionsNode> getAdderHandler() {
        return adder -> {
            String adderRequest = adder.getResult();
            if (adderRequest.isEmpty()) {
                return;
            }
            EditorSimpleDeletableWrapperNode<String> element = EditorSimpleDeletableWrapperNode.Builder.begin().setElement(adderRequest).setDeletionCallback(getDeletionCallback()).build();
            int sortedIndex = Collections.binarySearch(this.list, element);
            if (sortedIndex < 0) {
                this.list.add(sortedIndex ^ (-1), element);
            }
            adder.resetInput("");
        };
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        Consumer<EditorTextFieldOptionsNode> adderHandler = getAdderHandler();
        adderHandler.accept(this.topAdder);
        adderHandler.accept(this.bottomAdder);
        List<EditorNode> result = this.listFactory.get();
        if (this.list.size() > 0) {
            result.add(this.topAdder);
        }
        result.addAll(this.list);
        result.add(this.bottomAdder);
        return result;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/rule/EditorListNode$Builder.class */
    public static abstract class Builder<E, P, ED extends EditorListNode, B extends Builder<E, P, ED, B>> extends EditorNode.Builder<B> {
        private final B self = this;
        protected final List<EditorSimpleDeletableWrapperNode.Builder<String>> list;
        protected final EditorTextFieldOptionsNode.Builder adderBuilder;
        protected ListFactory listFactory;
        protected EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback;
        private Predicate<String> inputRuleTypeStringValidator;
        protected IEditorDataTooltipSupplier helpTooltipSupplier;
        private ObjectCategoryListRuleType<E, P, ?> defaultListRuleType;
        private Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes;
        private String listRuleTypePrefixSeparator;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public abstract ED buildInternally();

        protected Builder(ListFactory listFactory) {
            this.list = listFactory.get();
            this.listFactory = listFactory;
            this.adderBuilder = EditorTextFieldOptionsNode.Builder.begin(listFactory);
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public B setDefault() {
            super.setDefault();
            this.list.clear();
            setDeletionCallback(null);
            this.adderBuilder.setDefault().setAllowCustomInput(false).setAutoConfirm(false).setDisplayName(Component.translatable("gui.xaero_category_list_add"));
            setDeletionCallback((parent, element, rowList) -> {
                EditorListNode listData = (EditorListNode) parent;
                if (listData.getList().remove(element)) {
                    rowList.restoreScrollAfterUpdate();
                    return true;
                }
                return false;
            });
            setHelpTooltipSupplier((parent2, data) -> {
                return null;
            });
            setDefaultListRuleType(null);
            setListRuleTypes(null);
            setListRuleTypePrefixSeparator(null);
            setInputRuleTypeStringValidator(null);
            return this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry mainEntryFactory(EditorNode data, EditorNode parent, int index, ConnectionLineType lineType, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, int screenWidth, boolean isFinalExpanded) {
            EditorListNode elData = (EditorListNode) data;
            return new EditorListEntryWrapper(getCenteredEntryFactory(data, parent, index, rowList), screenWidth, index, rowList, lineType, data, elData.helpTooltipSupplier.apply(parent, elData));
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            return (x, y, width, height, root) -> {
                EditorButton button = new EditorButton(parent, true, 216, 20, data, rowList);
                return new EditorListEntryWidget(x, y, width, height, index, rowList, root, button, data.getTooltipSupplier(parent));
            };
        }

        public B addListElement(String element) {
            this.list.add((EditorSimpleDeletableWrapperNode.Builder) EditorSimpleDeletableWrapperNode.Builder.begin().setElement(element));
            return this.self;
        }

        public B setDeletionCallback(EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback) {
            this.deletionCallback = deletionCallback;
            return this.self;
        }

        public EditorTextFieldOptionsNode.Builder getAdderBuilder() {
            return this.adderBuilder;
        }

        protected List<EditorSimpleDeletableWrapperNode<String>> buildList() {
            Stream streamSorted = this.list.stream().map(builder -> {
                return builder.setDeletionCallback(this.deletionCallback).build();
            }).sorted();
            ListFactory listFactory = this.listFactory;
            Objects.requireNonNull(listFactory);
            return (List) streamSorted.collect(listFactory::get, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
        }

        public B setInputRuleTypeStringValidator(Predicate<String> inputRuleTypeStringValidator) {
            this.inputRuleTypeStringValidator = inputRuleTypeStringValidator;
            return this.self;
        }

        public B setHelpTooltipSupplier(IEditorDataTooltipSupplier helpTooltipSupplier) {
            this.helpTooltipSupplier = helpTooltipSupplier;
            return this.self;
        }

        public B setHelpTooltipInfoSupplier(BiFunction<EditorNode, EditorNode, TooltipInfo> biFunction) {
            return (B) setHelpTooltipSupplier((parent, data) -> {
                return new Tooltip((TooltipInfo) biFunction.apply(parent, data));
            });
        }

        public B setDefaultListRuleType(ObjectCategoryListRuleType<E, P, ?> defaultListRuleType) {
            this.defaultListRuleType = defaultListRuleType;
            return this.self;
        }

        public B setListRuleTypes(Iterable<ObjectCategoryListRuleType<E, P, ?>> listRuleTypes) {
            this.listRuleTypes = listRuleTypes;
            return this.self;
        }

        public B setListRuleTypePrefixSeparator(String listRuleTypePrefixSeparator) {
            this.listRuleTypePrefixSeparator = listRuleTypePrefixSeparator;
            return this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorNode build() {
            if (this.deletionCallback == null || this.helpTooltipSupplier == null || this.defaultListRuleType == null || this.listRuleTypes == null || this.listRuleTypePrefixSeparator == null) {
                throw new IllegalStateException("required fields not set!");
            }
            String str = this.listRuleTypePrefixSeparator;
            Predicate<String> predicate = this.inputRuleTypeStringValidator;
            Iterable<ObjectCategoryListRuleType<E, P, ?>> iterable = this.listRuleTypes;
            Predicate predicate2 = s -> {
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    ObjectCategoryListRuleType<E, P, ?> listRuleType = (ObjectCategoryListRuleType) it.next();
                    if (listRuleType.getStringValidator().test(s)) {
                        return true;
                    }
                }
                return false;
            };
            if (this.adderBuilder.needsInputStringValidator()) {
                this.adderBuilder.setInputStringValidator(s2 -> {
                    int separatorIndex = s2.indexOf(str);
                    if (separatorIndex == -1) {
                        return predicate2.test(s2);
                    }
                    String listRuleTypeString = s2.substring(0, separatorIndex);
                    if (predicate != null && !predicate.test(listRuleTypeString)) {
                        return false;
                    }
                    String elementString = s2.substring(separatorIndex + 1);
                    return predicate2.test(elementString);
                });
            }
            Iterator<ObjectCategoryListRuleType<E, P, ?>> it = iterable.iterator();
            while (it.hasNext()) {
                ObjectCategoryListRuleType<E, P, ?> next = it.next();
                addSuggestionsForListRuleType(next, next == this.defaultListRuleType ? "" : next.getId() + str);
            }
            return super.build();
        }

        private <S> void addSuggestionsForListRuleType(ObjectCategoryListRuleType<E, P, S> listRuleType, String prefix) {
            listRuleType.getAllElementSupplier().get().forEach(e -> {
                this.adderBuilder.addOptionBuilderFor(prefix + ((String) listRuleType.getSerializer().apply(e)));
            });
        }
    }
}
