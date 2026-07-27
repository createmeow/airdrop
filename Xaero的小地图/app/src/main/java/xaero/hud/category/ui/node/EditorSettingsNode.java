package xaero.hud.category.ui.node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListEntryWidget;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.entry.widget.EditorButton;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.options.EditorSimpleButtonNode;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder;
import xaero.hud.category.ui.node.options.text.EditorTextFieldOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.hud.category.ui.setting.SettingNodeBuilderFactoryManager;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSettingsNode.class */
public class EditorSettingsNode<SETTING_DATA extends EditorOptionsNode<?> & IEditorSettingNode<?>> extends EditorNode {
    private final Map<ObjectCategorySetting<?>, SETTING_DATA> settings;
    private final List<SETTING_DATA> settingList;
    private boolean toBeDeleted;
    private final EditorSimpleButtonNode deleteButton;
    private final EditorSimpleButtonNode protectionButton;
    private final EditorTextFieldOptionsNode nameOption;
    private final ListFactory listFactory;
    private final boolean rootSettings;
    private boolean protection;

    protected EditorSettingsNode(@Nonnull Map<ObjectCategorySetting<?>, SETTING_DATA> settings, @Nonnull List<SETTING_DATA> settingList, @Nonnull EditorSimpleButtonNode deleteButton, @Nonnull EditorSimpleButtonNode protectionButton, @Nonnull EditorTextFieldOptionsNode nameOption, @Nonnull ListFactory listFactory, boolean rootSettings, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, boolean protection) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.settings = settings;
        this.settingList = settingList;
        this.listFactory = listFactory;
        this.rootSettings = rootSettings;
        this.deleteButton = deleteButton;
        this.protectionButton = protectionButton;
        this.nameOption = nameOption;
        this.protection = protection;
    }

    public Map<ObjectCategorySetting<?>, SETTING_DATA> getSettings() {
        return this.settings;
    }

    public IEditorSettingNode<?> getSettingData(ObjectCategorySetting<?> setting) {
        return (IEditorSettingNode) this.settings.get(setting);
    }

    public boolean isRootSettings() {
        return this.rootSettings;
    }

    public boolean isToBeDeleted() {
        return this.toBeDeleted;
    }

    public void setToBeDeleted() {
        this.toBeDeleted = true;
    }

    public boolean getProtection() {
        return this.protection;
    }

    public void setProtected(boolean protection) {
        this.protection = protection;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        List<EditorNode> result = this.listFactory.get();
        result.addAll(this.settingList);
        if (!this.protection) {
            result.add(this.nameOption);
        }
        result.add(this.deleteButton);
        result.add(this.protectionButton);
        return result;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return Component.translatable("gui.xaero_category_settings");
    }

    public EditorTextFieldOptionsNode getNameOption() {
        return this.nameOption;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSettingsNode$Builder.class */
    public static abstract class Builder<SD extends EditorSettingsNode<?>, SDB extends Builder<SD, SDB>> extends EditorNode.Builder<Builder<SD, SDB>> {
        protected final Map<ObjectCategorySetting<?>, IEditorSettingNodeBuilder<?, ?>> settingMap;
        protected final List<IEditorSettingNodeBuilder<?, ?>> settingList;
        protected final EditorTextFieldOptionsNode.Builder nameOptionBuilder;
        protected final MapFactory mapFactory;
        protected final ListFactory listFactory;
        protected boolean rootSettings;
        protected boolean protection;
        protected final SDB self = this;
        protected final EditorSimpleButtonNode.Builder deleteButtonBuilder = EditorSimpleButtonNode.Builder.begin();
        protected final EditorSimpleButtonNode.Builder protectionButtonBuilder = EditorSimpleButtonNode.Builder.begin();

        protected abstract SD buildInternally(List<IEditorSettingNode<?>> list, Map<ObjectCategorySetting<?>, IEditorSettingNode<?>> map);

        protected Builder(MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings, SettingNodeBuilderFactoryManager settingNodeBuilderFactoryManager) {
            this.settingMap = mapFactory.get();
            this.settingList = listFactory.get();
            this.nameOptionBuilder = EditorTextFieldOptionsNode.Builder.begin(listFactory);
            this.mapFactory = mapFactory;
            this.listFactory = listFactory;
            Iterator<ObjectCategorySetting<?>> it = allSettings.iterator();
            while (it.hasNext()) {
                addSetting((ObjectCategorySetting) it.next(), settingNodeBuilderFactoryManager);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private <V> void addSetting(ObjectCategorySetting<V> setting, SettingNodeBuilderFactoryManager settingNodeBuilderFactoryManager) {
            Object setting2 = settingNodeBuilderFactoryManager.get(setting).apply(this.listFactory).setSetting(setting);
            this.settingMap.put(setting, setting2);
            this.settingList.add(setting2);
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public SDB setDefault() {
            super.setDefault();
            for (IEditorSettingNodeBuilder<?, ?> builder : this.settingList) {
                builder.setSettingValue(null);
            }
            setRootSettings(false);
            this.nameOptionBuilder.setDefault();
            this.deleteButtonBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_category_delete")).setCallback((parent, bd, rl) -> {
                EditorSettingsNode<?> settings = (EditorSettingsNode) parent;
                Minecraft mc = Minecraft.getInstance();
                Screen configScreen = mc.screen;
                mc.setScreen(new ConfirmScreen(result -> {
                    if (result) {
                        settings.setToBeDeleted();
                    }
                    mc.setScreen(configScreen);
                }, Component.translatable("gui.xaero_category_delete_confirm"), Component.translatable(settings.getNameOption().getResult()).withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)))));
            }).setIsActiveSupplier((parent2, data, rowList) -> {
                return (rowList.readOnly || ((EditorSettingsNode) parent2).getProtection()) ? false : true;
            });
            this.protectionButtonBuilder.setDefault().setDisplayName(Component.literal("")).setCallback((parent3, bd2, rl2) -> {
                EditorSettingsNode<?> settings = (EditorSettingsNode) parent3;
                boolean currentlyProtected = settings.getProtection();
                Minecraft mc = Minecraft.getInstance();
                Screen configScreen = mc.screen;
                MutableComponent mutableComponentTranslatable = Component.translatable(currentlyProtected ? "gui.xaero_category_disable_protection_confirm" : "gui.xaero_category_enable_protection_confirm");
                ChatFormatting confirmSecondLineColor = currentlyProtected ? ChatFormatting.RED : ChatFormatting.GREEN;
                mc.setScreen(new ConfirmScreen(result -> {
                    if (result) {
                        settings.setProtected(!settings.getProtection());
                    }
                    mc.setScreen(configScreen);
                }, mutableComponentTranslatable, Component.translatable(settings.getNameOption().getResult()).withStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(confirmSecondLineColor)))));
            }).setMessageSupplier((parent4, bd3) -> {
                return () -> {
                    String str;
                    if (((EditorSettingsNode) parent4).getProtection()) {
                        str = "gui.xaero_category_disable_protection";
                    } else {
                        str = "gui.xaero_category_enable_protection";
                    }
                    return Component.translatable(str);
                };
            }).setIsActiveSupplier((parent5, bd4, rowList2) -> {
                return (rowList2.readOnly || ((EditorSettingsNode) parent5).isRootSettings()) ? false : true;
            }).setTooltipSupplier((parent6, bd5) -> {
                return new Tooltip((Component) Component.translatable("gui.xaero_box_category_protection"));
            });
            setTooltipSupplier((parent7, data2) -> {
                if (!(parent7 instanceof EditorCategoryNode)) {
                    return null;
                }
                EditorCategoryNode<?, ?, ?> category = (EditorCategoryNode) parent7;
                Component displayNameComponent = category.getDisplayName();
                Tooltip tooltip = new Tooltip((Component) Component.translatable("gui.xaero_box_category_settings", new Object[]{displayNameComponent}));
                tooltip.setAutoLinebreak(false);
                return tooltip;
            });
            return this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            return (x, y, width, height, root) -> {
                EditorButton button = new EditorButton(parent, true, 216, 20, data, rowList);
                return new EditorListEntryWidget(x, y, width, height, index, rowList, root, button, data.getTooltipSupplier(parent));
            };
        }

        public <T> SDB setSettingValue(ObjectCategorySetting<T> setting, T value) {
            this.settingMap.get(setting).setSettingValue(value);
            return this.self;
        }

        public SDB setRootSettings(boolean rootSettings) {
            this.rootSettings = rootSettings;
            return this.self;
        }

        public SDB setProtection(boolean protection) {
            this.protection = protection;
            return this.self;
        }

        public EditorTextFieldOptionsNode.Builder getNameOptionBuilder() {
            return this.nameOptionBuilder;
        }

        public EditorSimpleButtonNode.Builder getDeleteButtonBuilder() {
            return this.deleteButtonBuilder;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorNode buildInternally() {
            if (this.nameOptionBuilder.needsInputStringValidator()) {
                this.nameOptionBuilder.setInputStringValidator(s -> {
                    return true;
                });
            }
            Stream map = this.settingList.stream().map(b -> {
                return b.setRootSettings(this.rootSettings);
            }).map((v0) -> {
                return v0.build();
            });
            ListFactory listFactory = this.listFactory;
            Objects.requireNonNull(listFactory);
            List<IEditorSettingNode<?>> builtSettingData = (List) map.collect(listFactory::get, (rec$, x$0) -> {
                ((List) rec$).add(x$0);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
            Map<ObjectCategorySetting<?>, IEditorSettingNode<?>> builtSettingsDataMap = this.mapFactory.get();
            for (IEditorSettingNode<?> sd : builtSettingData) {
                if (!(sd instanceof EditorOptionsNode)) {
                    throw new IllegalStateException("illegal setting data class! " + String.valueOf(sd.getClass()));
                }
                builtSettingsDataMap.put(sd.getSetting(), sd);
            }
            return buildInternally(builtSettingData, builtSettingsDataMap);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSettingsNode$FinalBuilder.class */
    public static final class FinalBuilder extends Builder<EditorSettingsNode<?>, FinalBuilder> {
        private FinalBuilder(MapFactory mapFactory, ListFactory listFactory, List<ObjectCategorySetting<?>> allSettings, SettingNodeBuilderFactoryManager settingNodeBuilderFactoryManager) {
            super(mapFactory, listFactory, allSettings, settingNodeBuilderFactoryManager);
        }

        @Override // xaero.hud.category.ui.node.EditorSettingsNode.Builder
        protected EditorSettingsNode<?> buildInternally(List<IEditorSettingNode<?>> builtSettingData, Map<ObjectCategorySetting<?>, IEditorSettingNode<?>> builtSettingsDataMap) {
            EditorSettingsNode<?> result = new EditorSettingsNode<>(builtSettingsDataMap, builtSettingData, this.deleteButtonBuilder.build(), this.protectionButtonBuilder.build(), this.nameOptionBuilder.build(), this.listFactory, this.rootSettings, this.movable, this.listEntryFactory, this.tooltipSupplier, this.protection);
            return result;
        }
    }
}
