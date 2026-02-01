/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author
 */
public class Lista {
    public NodoLista inicio;

    /**
     * Constructor por defecto que inicializa una lista vacía.
     */
    public Lista() {
        inicio = null;
    }
    
    
    /**
     * Inserta un nuevo nodo al inicio de la lista con la clave especificada.
     *
     * @param x Nombre de usuario que se insertará como clave en el nuevo nodo.
     */
    public void insertar(String x) {
        inicio = new NodoLista(x, inicio);
    }

     /**
     * Elimina el primer nodo de la lista cuya clave sea igual al valor especificado.
     * Si la clave no existe en la lista, no se realiza ninguna modificación.
     *
     * @param x Nombre de usuario que se desea eliminar.
     */
    public void eliminar(String x) {
        NodoLista actual = inicio, anterior = null;
        while (actual != null && actual.getDato() != x) {
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
    
    

}
