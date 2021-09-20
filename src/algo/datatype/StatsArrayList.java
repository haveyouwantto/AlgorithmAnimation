package algo.datatype;

import java.util.ArrayList;

public class StatsArrayList<E> extends ArrayList<E> {
    private int gets;
    private int sets;

    public E statsGet(int index) {
        gets++;
        return super.get(index);
    }

    public E statsSet(int index, E element) {
        sets++;
        return super.set(index, element);
    }

    public int getGets() {
        return gets;
    }

    public int getSets() {
        return sets;
    }

}
