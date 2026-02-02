/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package biograph;

/**
 *
 * @author 
 */
public class grafo {
    private boolean dirigido;
    private int maxNodos;
    private int numVertices;
    private Lista[] listaAdy;
    
    /**
     * Constructor de la clase Grafo.
     * 
     * @param n La capacidad maxima inicial de vertices del grafo
     */
    public grafo(int n) {
        this.dirigido = false;
        this.maxNodos = n;
        this.numVertices = 0;
        this.listaAdy = new Lista[n];
    }

    public boolean isDirigido() {
        return dirigido;
    }

    public void setDirigido(boolean dirigido) {
        this.dirigido = dirigido;
    }

    public int getMaxNodos() {
        return maxNodos;
    }

    public void setMaxNodos(int maxNodos) {
        this.maxNodos = maxNodos;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    public Lista[] getListaAdy() {
        return listaAdy;
    }

    public void setListaAdy(Lista[] listaAdy) {
        this.listaAdy = listaAdy;
    }
    
    
    
    public void insertaVertice(int n) {
        if (n > maxNodos - numVertices) {
            System.out.println("Error, se supera el numero de nodos maximo del grafo");
        } else {
            for (int i = numVertices; i < numVertices + n; i++) {
                listaAdy[i] = new Lista();
            }
            numVertices += n;
        }
    }

    public void insertaArista(int i, int j) {
        if (i >= numVertices || j >= numVertices) {
            System.out.println("Error, el vertice no es valido");
            return;
        }
        listaAdy[i].insertar(j);
        if (!dirigido) listaAdy[j].insertar(i);
    }
    
}
