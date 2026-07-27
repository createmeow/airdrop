package xaero.lib.common.util;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/KeySortableByOther.class */
public class KeySortableByOther<T> implements Comparable<KeySortableByOther> {
    private T key;
    private Comparable[] dataToSortBy;

    public KeySortableByOther(T key, Comparable... dataToSortBy) {
        this.key = key;
        this.dataToSortBy = dataToSortBy;
    }

    public T getKey() {
        return this.key;
    }

    public Comparable[] getDataToSortBy() {
        return this.dataToSortBy;
    }

    @Override // java.lang.Comparable
    public int compareTo(KeySortableByOther arg0) {
        Comparable[] otherData = arg0.getDataToSortBy();
        for (int i = 0; i < this.dataToSortBy.length; i++) {
            int comparison = this.dataToSortBy[i].compareTo(otherData[i]);
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }
}
