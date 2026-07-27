package xaero.lib.client.config.option.ui.type;

import xaero.lib.client.config.option.ui.factory.StandardConfigWidgetFactories;
import xaero.lib.client.config.option.ui.type.ConfigOptionUIType;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.IndexedConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/type/BuiltInConfigOptionUITypes.class */
public class BuiltInConfigOptionUITypes {
    public static ConfigOptionUIType<IndexedConfigOption<Boolean>> TOGGLE = getIndexedButton();
    public static ConfigOptionUIType<IndexedConfigOption<Integer>> INT_INDEXED_BUTTON = getIndexedButton();
    public static ConfigOptionUIType<IndexedConfigOption<Double>> DOUBLE_INDEXED_BUTTON = getIndexedButton();
    public static ConfigOptionUIType<IndexedConfigOption<Integer>> INT_INDEXED_SLIDER = getIndexedSlider();
    public static ConfigOptionUIType<IndexedConfigOption<Double>> DOUBLE_INDEXED_SLIDER = getIndexedSlider();
    public static ConfigOptionUIType<ConfigOption<String>> STRING_STRING_EDIT = getStringEdit();
    public static ConfigOptionUIType<ConfigOption<Integer>> INT_STRING_EDIT = getStringEdit();
    public static ConfigOptionUIType<ConfigOption<Double>> DOUBLE_STRING_EDIT = getStringEdit();

    public static <T, CT extends IndexedConfigOption<T>> ConfigOptionUIType<CT> getIndexedButton() {
        return ConfigOptionUIType.Builder.begin().setWidgetFactory(StandardConfigWidgetFactories.getIndexedCycleButtonFactory()).build();
    }

    public static <T, CT extends IndexedConfigOption<T>> ConfigOptionUIType<CT> getIndexedSlider() {
        return ConfigOptionUIType.Builder.begin().setWidgetFactory(StandardConfigWidgetFactories.getIndexedSliderFactory()).build();
    }

    public static <CT extends ConfigOption<?>> ConfigOptionUIType<CT> getStringEdit() {
        return ConfigOptionUIType.Builder.begin().setWidgetFactory(StandardConfigWidgetFactories.getStringEditFactory()).build();
    }
}
