package xaero.hud.minimap.radar.icon.creator.render.form.model.resolver;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ListModel;
import xaero.hud.util.SeparatedKeysParser;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/resolver/RadarIconModelFieldResolver.class */
public class RadarIconModelFieldResolver {
    public static final SeparatedKeysParser KEYS_PARSER = new SeparatedKeysParser(c -> {
        return c.charValue() == ',' || c.charValue() == ';';
    });

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/resolver/RadarIconModelFieldResolver$FieldReferenceElementGetter.class */
    private interface FieldReferenceElementGetter<T> {
        Object get(T t, Object[] objArr, String str);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/resolver/RadarIconModelFieldResolver$Listener.class */
    public interface Listener {
        boolean isFieldAllowed(Field field);

        boolean shouldStop();

        void onFieldResolved(Object[] objArr, String str);
    }

    public static Object[] handleDeclaredField(Field f, Object currentChainNode, String matchedFilterElement, Object[] oneResultArray) throws IllegalAccessException, IllegalArgumentException {
        Object referencedObject = ReflectionUtils.getReflectFieldValue(currentChainNode, f);
        if (referencedObject == null) {
            return null;
        }
        FieldReferenceType<?> referenceType = getReferenceType(referencedObject);
        Object[] collectionArray = referenceType.getArray(referencedObject, oneResultArray);
        if (collectionArray.length == 0) {
            return collectionArray;
        }
        if (matchedFilterElement == null || !matchedFilterElement.endsWith("]")) {
            return collectionArray;
        }
        int lastStartBracket = matchedFilterElement.lastIndexOf(91);
        if (lastStartBracket == -1) {
            throw new IllegalArgumentException("Field name " + matchedFilterElement + " ends with ] but is missing [!");
        }
        try {
            String keysString = matchedFilterElement.substring(lastStartBracket + 1, matchedFilterElement.length() - 1);
            String[] keys = KEYS_PARSER.parseKeys(keysString);
            Object[] result = keys.length == 1 ? oneResultArray : (Object[]) Array.newInstance(oneResultArray.getClass().getComponentType(), keys.length);
            for (int i = 0; i < keys.length; i++) {
                String keyString = keys[i];
                Object element = referenceType.getElement(referencedObject, collectionArray, keyString);
                result[i] = element;
            }
            return result;
        } catch (Exception nfe) {
            throw new IllegalArgumentException("Invalid element index/indices in " + matchedFilterElement + "!", nfe);
        }
    }

    public static void searchSuperclassFields(Object currentChainNode, List<String> filter, Listener listener, Object[] oneResultArray) {
        Class<?> nodeClass = currentChainNode.getClass();
        while (nodeClass != EntityModel.class && nodeClass != AgeableListModel.class && nodeClass != ListModel.class && nodeClass != Object.class) {
            Field[] declaredModelFields = nodeClass.getDeclaredFields();
            handleFields(currentChainNode, declaredModelFields, filter, listener, oneResultArray);
            if (!listener.shouldStop()) {
                Class<? super Object> superclass = nodeClass.getSuperclass();
                nodeClass = superclass;
                if (superclass == null) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004e A[Catch: Exception -> 0x0078, PHI: r15
  0x004e: PHI (r15v1 'matchedFilterElement' java.lang.String) = (r15v0 'matchedFilterElement' java.lang.String), (r15v2 'matchedFilterElement' java.lang.String) binds: [B:9:0x003f, B:11:0x004b] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {Exception -> 0x0078, blocks: (B:8:0x0027, B:10:0x0042, B:12:0x004e, B:14:0x005f, B:15:0x0069), top: B:23:0x0027 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void handleFields(java.lang.Object r5, java.lang.reflect.Field[] r6, java.util.List<java.lang.String> r7, xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver.Listener r8, java.lang.Object[] r9) {
        /*
            r0 = r6
            r10 = r0
            r0 = r10
            int r0 = r0.length
            r11 = r0
            r0 = 0
            r12 = r0
        Lb:
            r0 = r12
            r1 = r11
            if (r0 >= r1) goto L8c
            r0 = r10
            r1 = r12
            r0 = r0[r1]
            r13 = r0
            r0 = r8
            r1 = r13
            boolean r0 = r0.isFieldAllowed(r1)
            if (r0 != 0) goto L27
            goto L86
        L27:
            r0 = r13
            java.lang.Class r0 = r0.getDeclaringClass()     // Catch: java.lang.Exception -> L78
            java.lang.String r0 = r0.getName()     // Catch: java.lang.Exception -> L78
            r1 = r13
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Exception -> L78
            java.lang.String r0 = r0 + ";" + r1     // Catch: java.lang.Exception -> L78
            r14 = r0
            r0 = 0
            r15 = r0
            r0 = r7
            if (r0 == 0) goto L4e
            r0 = r14
            r1 = r7
            java.lang.String r0 = passesFilter(r0, r1)     // Catch: java.lang.Exception -> L78
            r1 = r0
            r15 = r1
            if (r0 == 0) goto L75
        L4e:
            r0 = r13
            r1 = r5
            r2 = r15
            r3 = r9
            java.lang.Object[] r0 = handleDeclaredField(r0, r1, r2, r3)     // Catch: java.lang.Exception -> L78
            r16 = r0
            r0 = r16
            if (r0 == 0) goto L69
            r0 = r8
            r1 = r16
            r2 = r15
            r0.onFieldResolved(r1, r2)     // Catch: java.lang.Exception -> L78
        L69:
            r0 = r8
            boolean r0 = r0.shouldStop()     // Catch: java.lang.Exception -> L78
            if (r0 == 0) goto L75
            goto L8c
        L75:
            goto L86
        L78:
            r14 = move-exception
            org.apache.logging.log4j.Logger r0 = xaero.hud.minimap.MinimapLogs.LOGGER
            java.lang.String r1 = "suppressed exception"
            r2 = r14
            r0.error(r1, r2)
        L86:
            int r12 = r12 + 1
            goto Lb
        L8c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver.handleFields(java.lang.Object, java.lang.reflect.Field[], java.util.List, xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver$Listener, java.lang.Object[]):void");
    }

    private static String passesFilter(String entry, List<String> filter) {
        for (String f : filter) {
            if (f.equals(entry)) {
                return f;
            }
            int indexOfBracket = f.lastIndexOf(91);
            if (indexOfBracket != -1 && f.substring(0, indexOfBracket).equals(entry)) {
                return f;
            }
        }
        return null;
    }

    private static FieldReferenceType<?> getReferenceType(Object o) {
        if (o instanceof Object[]) {
            return FieldReferenceType.ARRAY;
        }
        if (o instanceof Collection) {
            return FieldReferenceType.COLLECTION;
        }
        if (o instanceof Map) {
            return FieldReferenceType.MAP;
        }
        return FieldReferenceType.SINGLE;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/resolver/RadarIconModelFieldResolver$FieldReferenceType.class */
    private static class FieldReferenceType<T> {
        public static FieldReferenceType<Object> SINGLE = new FieldReferenceType<>((o, a, k) -> {
            throw new RuntimeException(String.format("%s is not an array/collection!", new Object[0]));
        }, (o2, ora) -> {
            ora[0] = o2;
            return ora;
        });
        public static FieldReferenceType<Object[]> ARRAY = new FieldReferenceType<>((o, a, k) -> {
            return o[Integer.parseInt(k.trim())];
        }, (o2, ora) -> {
            return o2;
        });
        public static FieldReferenceType<Collection<?>> COLLECTION = new FieldReferenceType<>((o, a, k) -> {
            return a[Integer.parseInt(k.trim())];
        }, (o2, ora) -> {
            return o2.toArray(ora);
        });
        public static FieldReferenceType<Map<?, ?>> MAP = new FieldReferenceType<>((o, a, k) -> {
            Object result = o.get(k);
            if (result == null) {
                try {
                    int integerAttemptKey = Integer.parseInt(k.trim());
                    result = o.get(Integer.valueOf(integerAttemptKey));
                } catch (NumberFormatException e) {
                }
            }
            return result;
        }, (o2, ora) -> {
            return o2.values().toArray(ora);
        });
        private FieldReferenceElementGetter<T> elementGetter;
        private BiFunction<T, Object[], Object[]> arrayGetter;

        private FieldReferenceType(FieldReferenceElementGetter<T> elementGetter, BiFunction<T, Object[], Object[]> arrayGetter) {
            this.elementGetter = elementGetter;
            this.arrayGetter = arrayGetter;
        }

        public Object[] getArray(Object referencedObject, Object[] oneResultArray) {
            return this.arrayGetter.apply(referencedObject, oneResultArray);
        }

        public Object getElement(Object referencedObject, Object[] array, String key) {
            return this.elementGetter.get(referencedObject, array, key);
        }
    }
}
