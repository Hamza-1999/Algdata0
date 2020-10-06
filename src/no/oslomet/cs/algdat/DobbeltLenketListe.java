package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;



public class DobbeltLenketListe<T> implements no.oslomet.cs.algdat.Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
        public <T> T getVerdi(){
            return (T) verdi;
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        //throw new UnsupportedOperationException();
    }

    private Node<T> finnNode(int indeks) {
        Node<T> returnNode = null;


        if (indeks < antall/2) { // hvis indeksen er mindre enn antall/2, skal denne koden kjøres
            Node<T> returNode;

            if (indeks < antall/2) {                 // Hvis indeksen er mindre enn antall / 2, søker fra hode
                returNode = hode;
                int x = 0;

                // Setter returnNode lik neste verdi helt til indeksen stemmer
                while (x < indeks) {
                    returNode = returNode.neste;
                    x++;
                }
            } else {
                returNode = hale;
                int x = antall - 1;

                while (x > indeks) {
                    returNode = returNode.forrige;
                    x--;
                }
            }


        }
        return returnNode;
    }


    public DobbeltLenketListe(T[] a) {
        Objects.requireNonNull(a,"Tabell a er NULL");

        for(T i : a){
            if(i != null){

                if(a.length == 0){
                    hode = null;
                    hale = null;
                }
                Node<T> verdi = new Node<>(i);
                if(a.length == 1){
                    hode = hale = verdi;
                }else {
                    if(antall == 0){
                        hode = hale = new Node<>(i, null, null);
                    }else {
                        hale = hale.neste = new Node<>(i, hale, null);
                    }
                }
                antall++;

            }
        }

    }

    public no.oslomet.cs.algdat.Liste<T> subliste(int fra, int til){


        //startet fra node x(finnnode)
        Node<T> x = finnNode(fra);

        fratilKontroll(antall, fra, til);

        DobbeltLenketListe<T> subListe = new DobbeltLenketListe<>();


        //bruker  en løkke til å legge til alle nodene mellom fra og til
        for (int i=fra;i<til;i++){


            subListe = (DobbeltLenketListe<T>) x.verdi; // legger alt fra-til intervallet i sublisten
            x = x.neste; // starter fra og går videre

        }

        //retunerer sublisten
        return subListe;

    }


    private void fratilKontroll ( int antall, int fra, int til){
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > tablengde(" + antall + ")");

        if (fra > til)                                // fra er større enn til
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi,"Verdi er lik NULL");
        if(verdi != null) {
            if (antall == 0) {
                hode = hale = new Node<>(verdi, null, null);
                antall++;
            } else {
                hale = hale.neste = new Node<>(verdi, hale, null);
                antall++;
            }
        }

        return true;
    }

    private void settInn(Node<T> p, Node<T> q, Node<T> r) { //Hentet fra forelesningen
        //metode som får pekerene til å peke frem og tilbake på korrekt måte
        p.neste = q;
        q.neste = r;
        r.forrige = q;
        q.forrige = p;
    }

    @Override
    public void leggInn(int indeks, T verdi) {

        indeksKontroll(indeks, true); // Arvet fra Liste.java
        if (verdi == null){
            throw new NullPointerException("Ikke tillat med null verdier");

        } else {

            if (indeks == 0){  // Hvis indeksen er 0 sett verdi først og flytter hode pekeren

                if (antall() == 0){ // Hvis listen er tom peker hode og hale til samme node
                    hale = hode = new Node<>(verdi, null, null);

                } else { // Lager en ny node som settes først og flytter hode pekeren en bak
                    hode = hode.forrige = new Node<>(verdi,null, hode);
                }

            } else if (indeks == antall() && antall() > 0) { // Hvis antall > 0 og indeks = antall settes verdien bakerst

                // Lager en ny node bakerst og flytter hale pekeren en bak
                hale = hale.neste = new Node<>(verdi, hale,null);

            } else if (indeks <= antall()/2) {

                Node<T> current = hode;
                for (int i = 1; i < indeks; i++) {
                    current = current.neste;
                }
                //Lager en ny node som plasseres i mellom indeks-1 og indeks slik at den nye noden får plassen til indeks
                current.neste = new Node<>(verdi, current, current.neste);
                //Sørger for at den nye nodene peker på neste og forrige node
                settInn(current, current.neste, current.neste.neste);

            } else {
                Node<T> current = hale;
                for (int i = (antall()-1); i >= indeks ; i--) {
                    current = current.forrige;
                }
                //Lager en ny node som plasseres i mellom indeks-1 og indeks slik at den nye noden får plassen til indeks
                current.neste = new Node<>(verdi, current, current.neste);
                //Sørger for at den nye nodene peker på neste og forrige node
                settInn(current, current.neste, current.neste.neste);
            }

            antall++;       // Øker antallet i listen
            endringer ++;   // Teller opp endringer gjort i listen
        }
    }

    @Override
    public boolean inneholder(T verdi) {

        int index = indeksTil(verdi); // legger metode kallet i en variabel

        if (index == -1){ // hvis metoden inntreffer -1, betyr det at den ikke innholder verdi
            return false; // returnerer dermed naturligvis false.
        }
        return true; // retunerer true hvis det stemmer
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks,false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {

        int indeks = 0;
        if(verdi == null){ // om T verdi er lik null
            for(Node<T> x = hode; x!= null; x=x.neste){
                if(x.verdi == null){ // sjekker om Liste.verdi = null
                    return indeks; // returnere indeks til første null Liste.verdi
                }
                indeks++;
            }
        }else {
            for(Node<T> i = hode; i != null; i= i.neste){ // itererer liste
                if(verdi.equals(i.verdi)){  // om verdi er like Liste.verdi
                    return indeks; // returnerer indkes
                }
                indeks++;
            }
        }
        return -1; // returnerer -1 om verdi finnes ikke
    }


    @Override
    public T oppdater(int indeks, T nyverdi) {



        Objects.requireNonNull(nyverdi, "Ikke tillatt med null-verdier!");

        indeksKontroll(indeks, false);  // Se Liste, false: indeks = antall er ulovlig

        Node<T> p = finnNode(indeks);
        T gammelVerdi = p.verdi;

        p.verdi = nyverdi;
        return gammelVerdi;

    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null){
            return false;
        }


        boolean valueFound = false;
        Node<T> peker = hode;
        while (valueFound == false && peker.neste != null) {

            if (peker.verdi == verdi) {
                valueFound = true;

                Node<T> pekerEtter = peker.forrige;
                Node<T> pekerForan = peker.neste;
                pekerEtter.forrige = pekerForan;
                pekerForan.neste = pekerEtter;
                //peker = null;
                antall--;
                endringer++;
                break;

            }
            peker = peker.neste;

        }

        return valueFound;


    }

    public Node<T> getFromIndeks (int indeks){
        Node<T> peker = hode;

        for (int i = 0; i < indeks; i++){
            peker = peker.neste;


        }
        return peker;

    }

    @Override
    public T fjern(int indeks) {

        indeksKontroll(indeks,false); //Sjekker om indeks finnes i listen

        Node<T> peker = hode;
        Node<T> next = peker.neste;
        Node<T> prev = peker.forrige;
        int index = 0;
        for(Node<T> p = hode; p != null; p = p.neste){
            if(indeks == index){
                peker.forrige = null;
                peker.neste = null;
                next.forrige = prev;
                prev.neste = next;
                break;
            }
            index++;
            next = p.neste;
            prev = p.forrige;
        }
        return (T) peker;




/*
        Node <T> peker = getFromIndeks(indeks);
        Node <T> pekerEtter = getFromIndeks(indeks+1);
        Node <T> pekerForan= getFromIndeks(indeks-1);

        pekerForan.neste = pekerEtter;
        pekerEtter.forrige = pekerForan;
        T indeksVerdi = peker.verdi;
        peker = null;
        antall --;
        return indeksVerdi;

 */
    }

    @Override
    public void nullstill() {
        hode = null;
        hale = null;
        endringer = 0;
        antall = 0;

    }

    @Override
    public String toString() {
        StringBuilder ut = new StringBuilder();
        ut.append("[");

        for(Node<T> denne = hode; denne != null;){
            ut.append(denne.getVerdi().toString());

            if(denne.neste != null){
                ut.append(", ");
            }
            denne = denne.neste;
        }

        ut.append("]");
        return ut.toString();
    }

    public String omvendtString() {
        StringBuilder ut = new StringBuilder();
        ut.append("[");

        for(Node<T> denne = hale; denne !=null;){
            ut.append(denne.getVerdi().toString());

            if(denne.forrige != null){
                ut.append(", ");
            }
            denne = denne.forrige;
        }
        ut.append("]");
        return ut.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator(); // Returnerer en instans av iterator klassen
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, true);
        return new DobbeltLenketListeIterator(indeks); // Returnerer en instans av iterator klassen
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){ //Ferdig og skal IKKE endres!
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            denne = finnNode(indeks); // p starter på node_indeks
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        @Override
        public boolean hasNext(){ //Ferdig og skal IKKE endres!
            return denne != null;
        }

        @Override
        public T next(){
            if (iteratorendringer != endringer){
                throw new ConcurrentModificationException();
            }else if(!hasNext()){
                throw new NoSuchElementException();
            }else{ //Antar at det er her koden er ment å plasseres
                fjernOK = true;
                T denneVerdi = denne.verdi;
                denne = denne.neste;
                return denneVerdi;
            }
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(no.oslomet.cs.algdat.Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe
