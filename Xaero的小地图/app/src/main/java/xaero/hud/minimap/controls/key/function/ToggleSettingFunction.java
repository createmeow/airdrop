package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import java.util.function.Supplier;
import xaero.common.HudMod;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.option.BooleanConfigOption;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/ToggleSettingFunction.class */
public class ToggleSettingFunction extends KeyMappingFunction {
    private final Supplier<BooleanConfigOption> settingSupplier;

    protected ToggleSettingFunction(Supplier<BooleanConfigOption> settingSupplier) {
        super(false);
        this.settingSupplier = settingSupplier;
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() throws InterruptedException, IOException {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        ConfigProfile currentProfile = configManager.getCurrentProfile();
        BooleanConfigOption option = this.settingSupplier.get();
        currentProfile.set(option, Boolean.valueOf(!((Boolean) currentProfile.get(option)).booleanValue()));
        HudMod.INSTANCE.getHudConfigs().getClientConfigProfileIO().save(currentProfile);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
