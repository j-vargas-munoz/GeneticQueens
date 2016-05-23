package tad;


import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Clase BinomialQ, una cola binomial
 * @author <a href="mailto:j.vargas.munoz@gmail.com">Jos&eacute; de Jes&uacute;s Vargas Mu&ntilde;oz</a>
 * @version 23/11/2013
 */
public class BinomialQ<T extends Comparable<T>> {

    private int size;

    /**
     * Bosque de &aacute;rboles binomiales
     */
    public ArrayList<BinNode<T>> forest;


    /**
     * Constructor
     * @param arrayLength Tama&ntilde;o disponible para el bosque
     */
    public BinomialQ(int arrayLength) {
        forest = new ArrayList<BinNode<T>>(arrayLength+1);
        for (int i = 0; i <= arrayLength; i++)
            forest.add(null);
        size = 0;
    }

 
    /**
     * Constructor
     * @param arrayLength Tama&ntilde;o disponible para el bosque
     * @param value El primer elemento de la cola binomial
     */
    public BinomialQ(int arrayLength, T value) {
        forest = new ArrayList<BinNode<T>>(arrayLength + 1);
        forest.add(0, new BinNode<T>(value));
        for (int i = 0; i <= arrayLength; i++)
            forest.add(null);
        forest.add(0, new BinNode<T>(value));
        size = 1;
    }


    /**
     * Nos dice si la cola binomial está vacía
     * @return el Minimo elemento del tipo generico
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Regresa el tamaño de la estructura
     * @return regresa el tamaño de la estructura
     */
    public int getSize() {
        return size;
    }


    /**
     * Estipula el nuevo tama&ntilde;o de la cola binomial
     * @param size El nuevo tama&ntilde;o
     */
    public void setSize(int size) {
        this.size = size;
    }


    /**
     * Devuelve al elemento m&iacute;nimo de la cola de prioridades
     * @return T El elemento m&iacute;nimo
     */
    public T findMin() {
        return forest.get(findMinIndex()).value; 
    }


    /**
     * Borra al elemento m&iacute;nimo de la cola de prioridades
     * @return T El elemento eliminado
     */
    public T deleteMin() {
        if(isEmpty())
            return null;
        int minIndex = findMinIndex();
        T minItem = forest.get(minIndex).value;
        BinNode<T> deletedTree = forest.get(minIndex).firstChild;

        BinomialQ<T> deletedQueue = new BinomialQ<T>(forest.size());
        deletedQueue.setSize(( 1 << minIndex ) - 1);
        for(int j = minIndex - 1; j >= 0; j-- ) {
            deletedQueue.forest.set(j, deletedTree);
            deletedTree = deletedTree.right;
            deletedQueue.forest.get(j).right = null;
        }
        forest.set(minIndex, null);
        size -= deletedQueue.size + 1;
        meld((BinomialQ<T>)deletedQueue);
        return minItem;
    }


    /**
     * Devuelve el &iacute;ndice de la cola con el elemento m&iacute;nimo
     * @return int El &iacute;indice
     */
    private int findMinIndex() {
        int i, minIndex;
        for (i = 0; forest.get(i) == null; i++);
        for (minIndex = i; i < forest.size(); i++)
            if (forest.get(i) != null && forest.get(i).compareTo(forest.get(minIndex)) < 0)
                minIndex = i;
        return minIndex;
    }


    /**
     * Inserta el elemento en la cola binomial
     * @param object El elemento a insertar
     */
    public void insert(T object) {
        BinomialQ<T> singleton = new BinomialQ<T>(1);
        singleton.setSize(1);
        singleton.forest.set(0, new BinNode<T>(object));
        meld((BinomialQ<T>)singleton);
    }


    /**
     * Devuelve la posici&oacute; del &uacute;ltimo arbol binomial cuyo valor es distinto de <code>null</code>
     * @return int Indice
     */
    public int sizeOfForest() {
        for (int i = forest.size()-1; i >= 0; i--)
            if (forest.get(i) != null)
                return i+1;
        return 0;
    }


    /**
     * Decrementa la llave de un elemento en la cola binomial
     * @param val El elemento cuya llave ser&aacute; reemplazada
     * @param key La nueva llave
     */
    public void decreaseKey(T val, T key) {
        for (int i = 0; i < sizeOfForest(); i++) {
            if (forest.get(i) == null)
                continue;
            if (forest.get(i).value.compareTo(val) > 0)
                continue;
            if (shift(forest.get(i), val, key))
                return;
        }
    }


    /**
     * Busca un elemento en el &aacute;rbol binomial enraizado en <code>node</code>. 
     * De encontrarlo, cambia su llave y lo intercambia con su padre las veces necesarias para seguir cumpliendo con la 
     * propiedad de &aacute;rbol binomial
     * @param node El nodo actual de la cola
     * @param val El valor a reemplazar
     * @param key El nuevo valor del nodo
     * @return boolean Indica si el nodo fue encontrado y reemplazado exitosamente
     */
    private boolean shift(BinNode<T> node, T val, T key) {
        if (node.value.equals(val)) {
            if (key.compareTo(node.value) < 0) {
                node.value = key;
                BinNode<T> tmp = node;
                while (tmp.parent != null && tmp.value.compareTo(tmp.parent.value) < 0) {
                    T aux = tmp.value;
                    tmp.value = tmp.parent.value;
                    tmp.parent.value = aux;
                    tmp = tmp.parent;
                }
            }
            return true;
        }
        BinNode<T> tmp = node.firstChild;
        if (tmp == null)
            return false;
        if (shift(tmp, val, key))
            return true;
        tmp = tmp.right;
        while (tmp != null) {
            if (shift(tmp, val, key))
                return true;
            tmp = tmp.right;
        }
        return false;
    }


    /**
     * Funde dos colas binomiales en una
     * @param binq La cola binomial a fundir
     * @return BinomialQ La cola binomial fundida
     */
    public BinomialQ<T> meld(BinomialQ<T> binq) {
        BinomialQ<T> bq = (BinomialQ<T>)binq;
        BinNode<T> carry = null;
        forest.ensureCapacity(forest.size() + 2);
        size += bq.getSize();
        for (int i = 0, j = 1; j <= size; i++, j *= 2 ) {
            BinNode<T> t1 = forest.get(i);
            BinNode<T> t2 = bq.forest.size() <= i ? null : bq.forest.get(i);
            int whichCase = t1 == null ? 0 : 1;
            whichCase += t2 == null ? 0 : 2;
            whichCase += carry == null ? 0 : 4;
            switch( whichCase ) {
                  case 0:   break;
                  case 1:   break;
                  case 2:   if (forest.size() == 0)
                                forest.add(i,t2);
                            else
                                forest.set(i, t2);
                            bq.forest.set(i,null);
                            break;
                  case 4:   forest.set(i,carry);
                            carry = null;
                            break;
                  case 3:   carry = combineTrees(t1, t2);
                            bq.forest.set(i, null);
                            forest.set(i, null);
                            break;
                  case 5:   carry = combineTrees(t1, carry);
                            forest.set(i, null);
                            break;
                  case 6:   carry = combineTrees(t2, carry);
                            bq.forest.set(i, null);
                            break;
                  case 7:   forest.set(i, carry);
                            carry = combineTrees(t1, t2);
                            bq.forest.set(i, null);
                            break;
                default : break;
            }
        }
        return this;
    }


    /**
     * Une las ra&iacute;ces de dos &aacute;rboles binomiales para formar uno nuevo de &iacute;ndice mayor
     * @param b1 Un &aacute;rbol a unir
     * @param b2 Un &aacute;rbol a unir
     * @return BinNode El &aacute;rbol que representa la uni&oacute;n
     */
    private BinNode<T> combineTrees(BinNode<T> b1, BinNode<T> b2) {
        if (b1.value.compareTo(b2.value) > 0) {
            b1.right = b2.firstChild;
            b2.firstChild = b1;
            b1.parent = b2;
            return b2;
        } else {
            b2.right = b1.firstChild;
            b1.firstChild = b2;
            b2.parent = b1;
            return b1;
        }
    }



    /**
     * Devuelve una cadena con la representaci&oacute;n de la cola binomial
     * @return String Representaci&oacute;n de la cola
     */
    public String toString() {
        String ret = "";
        ListIterator<BinNode<T>> it = forest.listIterator();
        while (it.hasNext()) {
            BinNode<T> tmp = it.next();
            ret += (tmp == null ? "" : "Root of current tree: " + tmp) + " \n";
        }
        return ret;
    }

}