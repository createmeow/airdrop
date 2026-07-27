package xaero.hud.path;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/path/XaeroPathReader.class */
public class XaeroPathReader {
    public XaeroPath read(String pathString) {
        return read(pathString, false);
    }

    public XaeroPath read(String pathString, boolean caseSensitive) {
        String[] pathStringNodes = pathString.split("/");
        if (pathStringNodes.length == 0) {
            return XaeroPath.root("", caseSensitive);
        }
        XaeroPath result = XaeroPath.root(pathStringNodes[0], caseSensitive);
        for (int i = 1; i < pathStringNodes.length; i++) {
            result = result.resolve(pathStringNodes[i]);
        }
        return result;
    }
}
