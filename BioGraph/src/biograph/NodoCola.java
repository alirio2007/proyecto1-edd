/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author Gianfranco, Adrian, Alirio
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
