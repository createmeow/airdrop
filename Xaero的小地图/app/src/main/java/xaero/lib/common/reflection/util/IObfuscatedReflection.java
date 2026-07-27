package xaero.lib.common.reflection.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/reflection/util/IObfuscatedReflection.class */
public interface IObfuscatedReflection {
    Class<?> getClassForName(String str, String str2) throws ClassNotFoundException;

    Field getFieldReflection(Class<?> cls, String str, String str2, String str3, String str4);

    Method getMethodReflection(Class<?> cls, String str, String str2, String str3, String str4, Class<?>... clsArr);
}
