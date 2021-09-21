package algo.datatype;

import java.util.ArrayList;

public class StatsArrayList<E> extends ArrayList<E> {
    private int gets;
    private int sets;

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

    public int getGets() {
        return gets;
    }

    public int getSets() {
        return sets;
    }

}
