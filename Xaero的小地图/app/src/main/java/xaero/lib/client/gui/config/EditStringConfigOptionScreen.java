package xaero.lib.client.gui.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/config/EditStringConfigOptionScreen.class */
public class EditStringConfigOptionScreen<T> extends ScreenBase {
    private EditBox editBox;
    private Button confirmButton;
    private final Config config;
    private final Config enforcedConfig;
    private final ConfigOption<T> configOption;
    private final boolean allowEmpty;
    private final boolean emptyMeansNull;
    private final Runnable postConfirmAction;
    private boolean valid;
    private String currentValue;
    private Component enforcedValueLabel;
    protected boolean shouldRenderEscapeScreen;

    /* JADX WARN: Multi-variable type inference failed */
    public EditStringConfigOptionScreen(Screen screen, Screen screen2, Config config, Config config2, ConfigOption<T> configOption, boolean z, boolean z2, Runnable runnable) {
        super(screen, screen2, configOption.getDisplayName());
        this.config = config;
        this.enforcedConfig = config2;
        this.configOption = configOption;
        this.allowEmpty = z;
        this.emptyMeansNull = z2;
        this.postConfirmAction = runnable;
        Object obj = config.get(configOption);
        this.currentValue = obj == null ? "" : configOption.getValueType().getIoCodec().encode(obj, null, configOption);
        this.shouldRenderEscapeScreen = false;
        this.canSkipWorldRender = false;
        if (screen instanceof EditConfigScreen) {
            EditConfigScreen editConfigScreen = (EditConfigScreen) screen;
            this.shouldRenderEscapeScreen = editConfigScreen.shouldRenderEscapeScreen;
            this.canSkipWorldRender = editConfigScreen.canSkipWorldRender();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        Object obj = this.enforcedConfig == null ? null : this.enforcedConfig.get(this.configOption);
        this.enforcedValueLabel = obj == null ? null : Component.translatable("gui.xaero_config_value_enforced_string_edit", new Object[]{this.configOption.getValueType().getIoCodec().encode(obj, null, this.configOption)}).withStyle(ChatFormatting.YELLOW);
        this.editBox = new EditBox(this.font, (this.width / 2) - 100, ((this.height / 7) + 29) - 2, 200, 20, this.configOption.getDisplayName());
        this.editBox.setMaxLength(this.configOption.getValueType().getIoCodec().getMaxStringLength());
        setFocused(this.editBox);
        this.editBox.setFocused(true);
        this.editBox.setValue(this.currentValue);
        this.editBox.setResponder(this::onChange);
        addRenderableWidget(this.editBox);
        Button buttonBuild = Button.builder(Component.translatable("gui.xaero_confirm"), b -> {
            confirm();
        }).bounds((this.width / 2) - 105, (this.height / 7) + 29 + 48, 100, 20).build();
        this.confirmButton = buttonBuild;
        addRenderableWidget(buttonBuild);
        addRenderableWidget(Button.builder(Component.translatable("gui.xaero_cancel"), b2 -> {
            goBack();
        }).bounds((this.width / 2) + 5, (this.height / 7) + 29 + 48, 100, 20).build());
        this.valid = true;
        updateConfirmButton();
    }

    private void onChange(String s) {
        this.currentValue = s;
        this.valid = true;
        if (this.editBox.getValue().isEmpty()) {
            this.valid = this.allowEmpty;
            updateConfirmButton();
        } else {
            try {
                T value = this.configOption.getValueType().getIoCodec().decode(s, null, this.configOption);
                this.valid = this.configOption.isValidValue(value);
            } catch (Throwable th) {
                this.valid = false;
            }
            updateConfirmButton();
        }
    }

    private boolean canConfirm() {
        return this.valid;
    }

    private void updateConfirmButton() {
        this.confirmButton.active = canConfirm();
    }

    public void confirm() {
        if (!canConfirm()) {
            return;
        }
        T value = (this.editBox.getValue().isEmpty() && this.emptyMeansNull) ? null : this.configOption.getValueType().getIoCodec().decode(this.editBox.getValue(), null, this.configOption);
        if (value != null && !this.configOption.isValidValue(value)) {
            return;
        }
        this.config.set(this.configOption, value);
        this.postConfirmAction.run();
        goBack();
    }

    public boolean keyPressed(int keyCode, int par2, int par3) {
        boolean result = super.keyPressed(keyCode, par2, par3);
        if (keyCode == 257 && canConfirm()) {
            this.confirmButton.onClick(0.0d, 0.0d);
            return true;
        }
        return result;
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRenderEscapeScreen) {
            renderEscapeScreen(guiGraphics, mouseX, mouseY, partial);
        }
        super.render(guiGraphics, mouseX, mouseY, partial);
        if (this.enforcedValueLabel != null) {
            guiGraphics.drawCenteredString(this.font, this.enforcedValueLabel, this.width / 2, ((this.height / 7) + 29) - 22, 16777215);
        }
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, ((this.height / 7) + 29) - 42, 16777215);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void onClose() {
        onExit(this.parent);
    }
}
