package xaero.lib.common.config.util;

import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.SteppedConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/util/ConfigUtils.class */
public class ConfigUtils {
    public static Component getDisplayForSimpleNumber(ConfigOption<? extends Number> option, Number value, Component unit) {
        String valueString;
        if (option instanceof SteppedConfigOption) {
            int precision = ((SteppedConfigOption) option).getPrecision();
            String format = "%." + precision + "f";
            valueString = String.format(format, value);
        } else {
            valueString = String.valueOf(value);
        }
        MutableComponent valueComponent = Component.literal(valueString);
        if (unit != null) {
            valueComponent.getSiblings().add(Component.literal(" "));
            valueComponent.getSiblings().add(unit);
        }
        return valueComponent;
    }

    public static Component getDisplayForSimpleNumber(ConfigOption<? extends Number> option, Number value) {
        return getDisplayForSimpleNumber(option, value, null);
    }

    public static Component getDisplayForBoolean(ConfigOption<Boolean> option, Boolean value) {
        return value.booleanValue() ? ConfigConstants.ON : ConfigConstants.OFF;
    }

    public static Component getDisplayForString(ConfigOption<String> stringConfigOption, String s) {
        return Component.literal(s);
    }

    public static String getAutoProfileIdForName(Predicate<String> exists, String profileName) {
        String id = profileName.toLowerCase().trim().replaceAll("\\s", "_").replaceAll("[^a-z0-9_]", "");
        if (!id.isEmpty() && !exists.test(id)) {
            return id;
        }
        int i = 0;
        while (true) {
            String id2 = "profile_" + i;
            if (!exists.test(id2)) {
                return id2;
            }
            i++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Component getEffectiveValueName(ClientOptionValueRedirectorManager clientOptionValueRedirectorManager, ConfigOption<T> configOption, Config config, Config config2) {
        T value;
        if (clientOptionValueRedirectorManager != null && clientOptionValueRedirectorManager.shouldRedirect(configOption)) {
            Component name = clientOptionValueRedirectorManager.getName(configOption);
            if (name != null) {
                return name;
            }
            value = clientOptionValueRedirectorManager.getValue(configOption);
        } else {
            value = config.get(configOption);
        }
        MutableComponent mutableComponent = value == null ? ConfigConstants.UNSPECIFIED : (Component) configOption.getDisplayGetter().apply(configOption, value);
        if (config2 == null) {
            return mutableComponent;
        }
        Object obj = config2.get(configOption);
        if (obj == null) {
            return mutableComponent;
        }
        if (Objects.equals(value, obj)) {
            return mutableComponent;
        }
        return Component.translatable("gui.xaero_config_value_enforced", new Object[]{mutableComponent.copy().withStyle(ChatFormatting.WHITE), ((Component) configOption.getDisplayGetter().apply(configOption, obj)).copy().withStyle(ChatFormatting.YELLOW)}).withStyle(ChatFormatting.YELLOW);
    }
}
