/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author
 */
public class Pila<T> {
    private NodoLista<T> cima;
    private int tamaño;
    
    public Pila() {
        cima = null;
        tamaño = 0;
    }
    
    public boolean estaVacia() {
        return cima == null;
    }
    
    public void push(T valor) {
        NodoLista<T> nuevo = new NodoLista<>(valor, cima);
        cima = nuevo;
        tamaño++;
    }
    
    public T pop() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        T valor = cima.getDato();
        cima = cima.getSiguiente();
        tamaño--;
        return valor;
    }
    
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        return cima.getDato();
    }
    
    public int tamaño() {
        return tamaño;
    }
}
