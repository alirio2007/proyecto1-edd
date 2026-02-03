/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author
 */
public class Cola<T> {
    private NodoCola<T> pFirst;
    private NodoCola<T> pLast;
    private int iN;

    /**
     * Constructor
     */
    public Cola() {
        pFirst = null;
        pLast = null;
        iN = 0;

    }
    
    public boolean esVacia() {
        return iN == 0;
    }
    
    public void encolar(T x) {
        NodoCola<T> nuevo = new NodoCola<>(x);
        if (esVacia()) {
            pFirst = nuevo;
        } else {
            pLast.siguiente = nuevo;
        }
        pLast = nuevo;
        iN++;
    }
    
    public void desencolar() {
        if (esVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        NodoCola<T> temp = pFirst;
        pFirst = pFirst.siguiente;
        temp.siguiente = null;
        if (pFirst == null) {
            pLast = null;
        }
        iN--;
    }
    
    public int tamanio() {
        return iN;
    }
}
