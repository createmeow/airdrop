package xaero.hud.minimap.info.codec;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/codec/InfoDisplayCommonStateCodecs.class */
public class InfoDisplayCommonStateCodecs {

    @Deprecated
    public static final InfoDisplayStateCodec<Boolean> BOOLEAN = new InfoDisplayStateCodec<>(s -> {
        return Boolean.valueOf(s.equals("true"));
    }, (v0) -> {
        return v0.toString();
    }, 5);

    @Deprecated
    public static final InfoDisplayStateCodec<Integer> INTEGER = new InfoDisplayStateCodec<>(s -> {
        return Integer.valueOf(Integer.parseInt(s));
    }, (v0) -> {
        return v0.toString();
    }, "-2147483648".length());
}
