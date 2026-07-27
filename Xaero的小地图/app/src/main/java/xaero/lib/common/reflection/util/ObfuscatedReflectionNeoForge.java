package xaero.lib.common.reflection.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/reflection/util/ObfuscatedReflectionNeoForge.class */
public class ObfuscatedReflectionNeoForge implements IObfuscatedReflection {
    private static Field getForgeMappedField(Class<?> clazz, String name) throws ObfuscationReflectionHelper.UnableToFindFieldException {
        return ObfuscationReflectionHelper.findField(clazz, name);
    }

    private static Method getForgeMappedMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws ObfuscationReflectionHelper.UnableToFindMethodException {
        return ObfuscationReflectionHelper.findMethod(clazz, name, parameterTypes);
    }

    @Override // xaero.lib.common.reflection.util.IObfuscatedReflection
    public Class<?> getClassForName(String obfuscatedName, String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    @Override // xaero.lib.common.reflection.util.IObfuscatedReflection
    public Field getFieldReflection(Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge) {
        try {
            Field field = getForgeMappedField(c, deobfName);
            return field;
        } catch (ObfuscationReflectionHelper.UnableToFindFieldException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    @Override // xaero.lib.common.reflection.util.IObfuscatedReflection
    public Method getMethodReflection(Class<?> c, String deobfName, String obfuscatedNameFabric, String descriptor, String obfuscatedNameForge, Class<?>... parameters) {
        try {
            Method method = getForgeMappedMethod(c, deobfName, parameters);
            return method;
        } catch (ObfuscationReflectionHelper.UnableToFindMethodException e) {
            throw new RuntimeException((Throwable) e);
        }
    }
}
