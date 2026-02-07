/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author
 */
public class Lista<T> {
    public NodoLista<T> inicio;

    /**
     * Constructor por defecto que inicializa una lista vacía.
     */
    public Lista() {
        inicio = null;
    }
    
    
    /**
     * Inserta un nuevo nodo al inicio de la lista con la clave especificada.
     *
     * @param x clave en el nuevo nodo.
     */
    public void insertar(T x) {
        inicio = new NodoLista<T>(x, inicio);
    }

     /**
     * Elimina el primer nodo de la lista cuya clave sea igual al valor especificado.
     * Si la clave no existe en la lista, no se realiza ninguna modificación.
     *
     * @param x clave que se desea eliminar.
     */
    public void eliminar(T x) {
        NodoLista<T> actual = inicio, anterior = null;
        while (actual != null && !actual.getDato().equals(x)) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual != null) {
            if (anterior == null) {
                inicio = actual.getSiguiente();
            } else {
                anterior.setSiguiente(actual.getSiguiente());
            }
        }
    }
    
    public boolean busqueda(T x) {
        NodoLista<T> actual = inicio;
        while (actual != null) {
            if (actual.getDato().equals(x)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public NodoLista<T> obtenerInicio() {
        return inicio;
    }
}