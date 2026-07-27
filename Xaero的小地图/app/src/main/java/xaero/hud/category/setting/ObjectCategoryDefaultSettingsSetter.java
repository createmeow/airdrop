package xaero.hud.category.setting;

import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import xaero.hud.category.ObjectCategory;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/setting/ObjectCategoryDefaultSettingsSetter.class */
public final class ObjectCategoryDefaultSettingsSetter {
    private final Map<String, ObjectCategorySetting<?>> settings;

    private ObjectCategoryDefaultSettingsSetter(@Nonnull Map<String, ObjectCategorySetting<?>> settings) {
        this.settings = settings;
    }

    public boolean setDefaultsFor(ObjectCategory<?, ?> category, boolean onlyNew) {
        boolean changedSomething = false;
        Iterator<ObjectCategorySetting<?>> it = this.settings.values().iterator();
        while (it.hasNext()) {
            ObjectCategorySetting<T> objectCategorySetting = (ObjectCategorySetting) it.next();
            if (!onlyNew || category.getSettingValue(objectCategorySetting) == null) {
                setForSetting(category, objectCategorySetting);
                changedSomething = true;
            }
        }
        return changedSomething;
    }

    private <T> void setForSetting(ObjectCategory<?, ?> category, ObjectCategorySetting<T> setting) {
        category.setSettingValue(setting, setting.getDefaultValue());
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/setting/ObjectCategoryDefaultSettingsSetter$Builder.class */
    public static final class Builder {
        private Map<String, ObjectCategorySetting<?>> settings;

        private Builder() {
        }

        public Builder setDefault() {
            setSettings(null);
            return this;
        }

        public Builder setSettings(Map<String, ObjectCategorySetting<?>> settings) {
            this.settings = settings;
            return this;
        }

        public ObjectCategoryDefaultSettingsSetter build() {
            if (this.settings == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return new ObjectCategoryDefaultSettingsSetter(this.settings);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
