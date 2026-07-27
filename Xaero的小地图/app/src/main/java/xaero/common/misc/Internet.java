package xaero.common.misc;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import xaero.hud.minimap.MinimapLogs;
import xaero.lib.patreon.Patreon;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/misc/Internet.class */
public class Internet {
    public static Cipher cipher;

    static {
        cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.getDecoder().decode(Patreon.getPublicKeyString2().getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            PublicKey publicKey = factory.generatePublic(X509publicKey);
            cipher.init(2, publicKey);
        } catch (Exception e) {
            cipher = null;
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0132, code lost:
    
        r9.setOutdated(false);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void checkModVersion(xaero.common.IXaeroMinimap r9) {
        /*
            Method dump skipped, instructions count: 546
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.common.misc.Internet.checkModVersion(xaero.common.IXaeroMinimap):void");
    }
}
