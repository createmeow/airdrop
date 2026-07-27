package xaero.lib.common.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import xaero.lib.XaeroLib;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/ImageIOUtils.class */
public class ImageIOUtils {
    public static BufferedImage getImageThroughZipError(ImageInputStream input, String debugName) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
        if (!readers.hasNext()) {
            return null;
        }
        ImageReader reader = readers.next();
        reader.setInput(input);
        ImageReadParam param = reader.getDefaultReadParam();
        ImageTypeSpecifier type = (ImageTypeSpecifier) reader.getImageTypes(0).next();
        int width = reader.getWidth(0);
        int height = reader.getHeight(0);
        BufferedImage image = type.createBufferedImage(width, height);
        param.setDestination(image);
        try {
            reader.read(0, param);
        } catch (IOException ioe) {
            if (!(ioe.getCause() instanceof ZipException)) {
                throw ioe;
            }
            XaeroLib.LOGGER.error("Suppressed ZIP exception loading PNG {}: {}", debugName, ioe.getCause().getMessage());
        }
        return image;
    }
}
