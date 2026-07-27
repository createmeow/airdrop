package xaero.lib.common.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/InternetUtils.class */
public class InternetUtils {
    public static void download(BufferedOutputStream output, InputStream input) throws IOException {
        byte[] buffer = new byte[256];
        while (true) {
            int read = input.read(buffer, 0, buffer.length);
            if (read >= 0) {
                output.write(buffer, 0, read);
            } else {
                output.flush();
                input.close();
                output.close();
                return;
            }
        }
    }
}
