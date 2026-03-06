/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 * Implementación de una Cola genérica utilizando nodos enlazados
 * @author Gianfranco, Adrian, Alirio
 * @param <T> Tipo de datos que almacenará la cola
 */
public class Cola<T> {
    /**
     * Atributos
     */
    private NodoCola<T> pFirst;
    private NodoCola<T> pLast;
    private int size;

    /**
     * Constructor que inicializa una cola vacía
     */
    public Cola() {
        pFirst = null;
        pLast = null;
        size = 0;

    }
    
    /**
     * Verifica si la cola está vacía.
     * @return true si está vacía, false en caso contrario.
     */
    public boolean esVacia() {
        return size == 0;
    }
    
    /**
     * Inserta un elemento al final de la cola.
     * @param x Valor a insertar.
     */
    public void encolar(T x) {
        NodoCola<T> nuevo = new NodoCola<>(x);
        if (esVacia()) {
            pFirst = nuevo;
        } else {
            pLast.siguiente = nuevo;
        }
        pLast = nuevo;
        size++;
    }
    
    /**
     * Elimina el primer elemento de la cola.
     * Lanza excepción si está vacía.
     */
    public void desencolar() {
        if (esVacia()) {
            throw new IllegalStateException("La cola está vacia");
        }
        NodoCola<T> temp = pFirst;
        pFirst = pFirst.siguiente;
        temp.siguiente = null;
        if (pFirst == null) {
            pLast = null;
        }
        size--;
    }
    
    /**
     * Retorna el número de elementos en la cola.
     * @return Tamaño de la cola.
     */
    public int tamanio() {
        return size;
    }
    
    /**
     * Elimina y retorna el primer elemento de la cola.
     * 
     * @return El primer elemento de la cola, o null si la cola está vacía
     */
    public T desencolarConRetorno() {
        if (esVacia()) {
            return null;
        }
        T valor = pFirst.valor;
        pFirst = pFirst.siguiente;
        size--;
        if (esVacia()) {
            pLast = null;
        }
        return valor;
    }
    
    /**
     * Obtiene el primer elemento sin eliminarlo
     * @return El primer elemento o null si la cola está vacía
     */
    public T obtenerFrente() {
        if (esVacia()) {
            return null;
        }
        return pFirst.valor;
    }
}