package xaero.lib.common.reflection.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import xaero.lib.XaeroLib;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/reflection/util/ReflectionUtils.class */
public class ReflectionUtils {
    public static Class<?> getClassForName(String obfuscatedName, String deobfName) throws ClassNotFoundException {
        IObfuscatedReflection obfuscatedReflection = Services.PLATFORM.getObfuscatedReflection();
        return obfuscatedReflection.getClassForName(obfuscatedName, deobfName);
    }

    public static Field getFieldReflection(Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge) {
        IObfuscatedReflection obfuscatedReflection = Services.PLATFORM.getObfuscatedReflection();
        return obfuscatedReflection.getFieldReflection(c, deobfName, obfuscatedNameFabric, descriptor, obfuscatedNameForge);
    }

    public static <A, B> B getReflectFieldValue(A a, Field field) throws IllegalAccessException, IllegalArgumentException {
        boolean zIsAccessible = field.isAccessible();
        field.setAccessible(true);
        Object obj = null;
        try {
            obj = field.get(a);
        } catch (Exception e) {
            XaeroLib.LOGGER.error("suppressed exception", e);
        }
        field.setAccessible(zIsAccessible);
        return (B) obj;
    }

    public static <A, B> void setReflectFieldValue(A parentObject, Field field, B value) throws IllegalAccessException, IllegalArgumentException {
        boolean accessibleBU = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(parentObject, value);
        } catch (Exception e) {
            XaeroLib.LOGGER.error("suppressed exception", e);
        }
        field.setAccessible(accessibleBU);
    }

    public static Method getMethodReflection(Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge, Class<?>... parameters) {
        IObfuscatedReflection obfuscatedReflection = Services.PLATFORM.getObfuscatedReflection();
        return obfuscatedReflection.getMethodReflection(c, deobfName, obfuscatedNameFabric, descriptor, obfuscatedNameForge, parameters);
    }

    public static <A, B> B getReflectMethodValue(A a, Method method, Object... objArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean zIsAccessible = method.isAccessible();
        method.setAccessible(true);
        Object objInvoke = null;
        try {
            objInvoke = method.invoke(a, objArr);
        } catch (Exception e) {
            XaeroLib.LOGGER.error("suppressed exception", e);
        }
        method.setAccessible(zIsAccessible);
        return (B) objInvoke;
    }
}
