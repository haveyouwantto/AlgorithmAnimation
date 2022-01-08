package hywt.algo;

import hywt.algo.datatype.OrderedPriorityQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Test {
    public static void main(String[] args) {
        OrderedPriorityQueue<Integer> p = new OrderedPriorityQueue<>();
        p.add(3);
        p.add(1);
        p.add(6);
        p.add(4);
        p.add(5);
        p.add(3);
        p.add(1);

        for (Integer i : p){
            System.out.print(i+" ");
        }

        List<Integer> list = new ArrayList<>();
        p.fetch(list);
        System.out.println(list);
    }
}
