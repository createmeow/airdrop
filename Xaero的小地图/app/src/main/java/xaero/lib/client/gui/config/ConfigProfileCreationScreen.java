package xaero.lib.client.gui.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.client.gui.widget.MySmallButton;
import xaero.lib.client.gui.widget.dropdown.DropDownWidget;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/config/ConfigProfileCreationScreen.class */
public final class ConfigProfileCreationScreen extends ScreenBase {
    private static final Component PROFILE_TO_COPY_COMPONENT = Component.translatable("gui.xaero_config_profile_to_copy");
    private DropDownWidget profileToCopyDropdown;
    private EditBox editBox;
    private Button confirmButton;
    private String currentValue;
    private final IEditConfigScreenContext context;
    private final ConfigChannel channel;
    private final Runnable onConfirm;
    private ConfigProfileDropdownContext dropdownContext;
    private String selectedProfileToCopy;

    protected ConfigProfileCreationScreen(Screen parent, IEditConfigScreenContext context, ConfigChannel channel, Runnable onConfirm) {
        super(parent, parent, Component.translatable("gui.xaero_config_profile_creation_screen"));
        this.context = context;
        this.channel = channel;
        this.onConfirm = onConfirm;
        this.currentValue = "";
        this.selectedProfileToCopy = context.getSelectedProfileId(channel);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void init() {
        super.init();
        this.dropdownContext = new ConfigProfileDropdownContext(this.context, this.channel, null, this.selectedProfileToCopy, false);
        this.profileToCopyDropdown = DropDownWidget.Builder.begin().setX((this.width / 2) - 100).setY((this.height / 7) + 15).setW(200).setCallback(this::onDropdownSelection).setOptions(this.dropdownContext.getOptions()).setSelected(Integer.valueOf(this.dropdownContext.getInitialSelection())).setNarrationTitle(PROFILE_TO_COPY_COMPONENT).setContainer(this).build();
        addWidget(this.profileToCopyDropdown);
        this.editBox = new EditBox(this.font, (this.width / 2) - 100, (this.height / 7) + 55, 200, 20, BuiltInProfiledConfigOptions.PROFILE_NAME.getDisplayName());
        this.editBox.setMaxLength(50);
        setFocused(this.editBox);
        this.editBox.setFocused(true);
        this.editBox.setValue(this.currentValue);
        this.editBox.setResponder(this::onChange);
        addRenderableWidget(this.editBox);
        MySmallButton mySmallButton = new MySmallButton(200, (this.width / 2) - 155, (this.height / 6) + 168, Component.translatable("gui.xaero_confirm"), b -> {
            confirm();
        });
        this.confirmButton = mySmallButton;
        addRenderableWidget(mySmallButton);
        addRenderableWidget(new MySmallButton(201, (this.width / 2) + 5, (this.height / 6) + 168, Component.translatable("gui.xaero_cancel"), b2 -> {
            goBack();
        }));
        updateConfirmButton();
    }

    private boolean onDropdownSelection(DropDownWidget menu, int selected) {
        if (!this.context.getSyncStatus(this.channel)) {
            return false;
        }
        this.selectedProfileToCopy = this.dropdownContext.getProfiles()[selected].getId();
        return true;
    }

    private void onChange(String s) {
        this.currentValue = s;
        updateConfirmButton();
    }

    private boolean canConfirm() {
        return !this.editBox.getValue().isEmpty() && BuiltInProfiledConfigOptions.PROFILE_NAME.isValidValue(this.editBox.getValue());
    }

    private void updateConfirmButton() {
        this.confirmButton.active = canConfirm();
    }

    public void confirm() {
        if (!canConfirm()) {
            return;
        }
        String autoId = ConfigUtils.getAutoProfileIdForName(this::profileExists, this.editBox.getValue());
        createProfile(autoId, this.editBox.getValue(), this.selectedProfileToCopy);
        this.context.setCurrentProfile(autoId, this.channel);
        this.onConfirm.run();
        goBack();
    }

    private boolean profileExists(String id) {
        return this.context.profileExists(id, this.channel);
    }

    private void createProfile(String id, String name, String profileToCopy) {
        this.context.createProfile(id, name, this.channel, profileToCopy);
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
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partial);
        guiGraphics.drawCenteredString(this.font, PROFILE_TO_COPY_COMPONENT, this.width / 2, this.height / 7, -1);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, (this.height / 7) + 40, -1);
    }

    @Override // xaero.lib.client.gui.ScreenBase
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        super.render(guiGraphics, mouseX, mouseY, partial);
    }
}
