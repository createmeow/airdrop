package xaero.common.pool;

import java.util.ArrayList;
import java.util.List;
import xaero.common.pool.PoolUnit;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/pool/MapPool.class */
public abstract class MapPool<T extends PoolUnit> {
    private int maxSize;
    private List<T> units = new ArrayList();

    protected abstract T construct(Object... objArr);

    public MapPool(int maxSize) {
        this.maxSize = maxSize;
    }

    public T get(Object... objArr) {
        PoolUnit poolUnitTakeFromPool = null;
        synchronized (this.units) {
            if (!this.units.isEmpty()) {
                poolUnitTakeFromPool = takeFromPool();
            }
        }
        if (poolUnitTakeFromPool == null) {
            return (T) construct(objArr);
        }
        poolUnitTakeFromPool.create(objArr);
        return (T) poolUnitTakeFromPool;
    }

    public boolean addToPool(T unit) {
        synchronized (this.units) {
            if (this.units.size() < this.maxSize) {
                this.units.add(unit);
                return true;
            }
            return false;
        }
    }

    private T takeFromPool() {
        return this.units.remove(this.units.size() - 1);
    }

    public int size() {
        return this.units.size();
    }
}
