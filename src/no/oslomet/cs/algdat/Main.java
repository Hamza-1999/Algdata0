package no.oslomet.cs.algdat;

import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {

        LinkedList<Integer> li = new LinkedList<>();
        li.add(1);
        li.add(2);
        li.remove(1);
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        liste.leggInn(3);
        liste.leggInn(5);
        liste.leggInn(7);
        System.out.println(liste);
        int i = liste.indeksTil(null);

    }
}
