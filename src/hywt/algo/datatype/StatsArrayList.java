package hywt.algo.datatype;

import java.util.ArrayList;

public class StatsArrayList<E> extends ArrayList<E> {
    private long gets;
    private long sets;

    public E statsGet(int index) {
        gets++;
        return get(index);
    }

    public E statsSet(int index, E element) {
        sets++;
        return set(index, element);
    }

    public void statsSwap(int i1, int i2) {
        E temp = statsGet(i1);
        statsSet(i1, statsGet(i2));
        statsSet(i2, temp);
    }

    public long getGets() {
        return gets;
    }

    public long getSets() {
        return sets;
    }

}
