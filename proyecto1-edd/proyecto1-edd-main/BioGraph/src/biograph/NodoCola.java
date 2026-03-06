/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 * Nodo para la implementación de una Cola
 * @author Gianfranco, Adrian, Alirio
 * @param <T> Tipo de dato que almacena el nodo
 */
class NodoCola<T> {
    /**
     * Atributos
     */
    T valor;
    NodoCola<T> siguiente;

    /**
     * Constructor
     */
    public NodoCola(T valor) {
        this.valor = valor;
        this.siguiente = null;
    }
}