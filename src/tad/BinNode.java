package tad;

/**
 * Clase BinNode, representa un &aacute;rbol binomial
 * @implements Comparable
 * @author <a href="mailto:j.vargas.munoz@gmail.com">Jos&eacute; de Jes&uacute;s Vargas Mu&ntilde;oz</a>
 * @version 23/11/2013
 */
public class BinNode<T extends Comparable<T>> implements Comparable<BinNode<T>> {
    
    /**
     * Valor del nodo
     */
    public T value;

    /**
     * Referencia al primer hijo
     */
    public BinNode<T> firstChild;

    /**
     * Referencia al 'hermano' derecho
     */
    public BinNode<T> right;

    /**
     * Referencia al padre
     */
    public BinNode<T> parent;


    /**
     * Constructor
     * @param value El valor del nodo actual
     * @param firstChild Referencia al primer hijo
     * @param right Referencia al hermano derecho
     * @param parent Referencia al padre
     */
    public BinNode(T value, BinNode<T> firstChild, BinNode<T> right, BinNode<T> parent) {
        this.value = value;
        this.firstChild = firstChild;
        this.right = right;
        this.parent = parent;
    }


    /**
     * Constructor
     * @param value El valor del nodo actual
     */
    public BinNode(T value) {
        this(value, null, null, null);
    }


    /**
     * Indica el orden entre dos nodos de &aacute;rbol binomial
     * @param o Un nodo a comparar
     * @return int Un entero negativo, cero, o un entero positivo si este objeto es menor que, igual, o mayor que el objeto especificado
     */
    @Override
    public int compareTo(BinNode<T> o) {
        return value.compareTo(o.value);
    }


    /**
     * Devuelve una cadena con la representaci&oacute;n del arbol
     * Solo indica el valor de la raiz y el valor de su hijo, de tenerlo
     * @return String La representaci&oacute;n del objeto
     */
    public String toString() {
        return "Parent: " + value + " First child: " + (firstChild == null ? "null" : firstChild.value);
    }
}