package biograph;

/**
 * Grafo para modelar interacciones proteicas con restricción de no usar Collections.
 */
public class grafo {
    private boolean dirigido;
    private int capacidadMaxima;
    private int numProteinas;
    private String[] nombresProteinas;      // Arreglo de nombres
    private boolean[] activa;               // Proteína eliminada o activa
    private Lista<Arista>[] listaAdy;       // Lista de adyacencia
    
    /**
     * Constructor del grafo proteico.
     * @param capacidad Capacidad máxima de proteínas
     * @param dirigido true si el grafo es dirigido
     */
    public grafo(int capacidad, boolean dirigido) {
        this.dirigido = dirigido;
        this.capacidadMaxima = capacidad;
        this.numProteinas = 0;
        this.nombresProteinas = new String[capacidad];
        this.activa = new boolean[capacidad];
        this.listaAdy = new Lista[capacidad];
        
        for (int i = 0; i < capacidad; i++) {
            listaAdy[i] = new Lista<Arista>();
            activa[i] = false;  // Inicialmente no hay proteínas
        }
    }
    
    /**
     * Busca el índice de una proteína por nombre (búsqueda lineal).
     */
    private int buscarIndiceProteina(String nombre) {
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i] && nombresProteinas[i].equals(nombre)) {
                return i;
            }
        }
        return -1;  // No encontrado
    }
    
    /**
     * Agrega una nueva proteína si hay espacio.
     */
    public int agregarProteina(String nombre) {
        // Verificar si ya existe
        int idxExistente = buscarIndiceProteina(nombre);
        if (idxExistente != -1) {
            return idxExistente;
        }
        
        // Buscar slot disponible (alguna proteína eliminada)
        for (int i = 0; i < numProteinas; i++) {
            if (!activa[i]) {
                nombresProteinas[i] = nombre;
                activa[i] = true;
                listaAdy[i] = new Lista<Arista>(); // Reiniciar lista
                return i;
            }
        }
        
        // Agregar al final si hay capacidad
        if (numProteinas < capacidadMaxima) {
            nombresProteinas[numProteinas] = nombre;
            activa[numProteinas] = true;
            listaAdy[numProteinas] = new Lista<Arista>();
            return numProteinas++;
        }
        
        throw new RuntimeException("Capacidad máxima del grafo excedida");
    }
    
    /**
     * Agrega una interacción entre dos proteínas con peso.
     */
    public void agregarInteraccion(String proteinaA, String proteinaB, int peso) {
        int idxA = agregarProteina(proteinaA);
        int idxB = agregarProteina(proteinaB);
        
        // Verificar si la arista ya existe
        if (!existeArista(idxA, idxB)) {
            listaAdy[idxA].insertar(new Arista(idxB, peso));
            if (!dirigido) {
                listaAdy[idxB].insertar(new Arista(idxA, peso));
            }
        }
    }
    
    /**
     * Verifica si existe arista entre dos índices.
     */
    private boolean existeArista(int origen, int destino) {
        Lista<Arista> adyacentes = listaAdy[origen];
        NodoLista<Arista> actual = adyacentes.obtenerInicio();
        
        while (actual != null) {
            if (actual.getDato().getDestino() == destino) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    /**
     * Elimina una proteína (simula efecto de fármaco).
     */
    public void eliminarProteina(String nombre) {
        int idx = buscarIndiceProteina(nombre);
        if (idx == -1) return;
        
        // Marcar como inactiva
        activa[idx] = false;
        
        // Eliminar todas las aristas que apuntan a esta proteína
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) {
                eliminarArista(i, idx);
            }
        }
    }
    
    /**
     * Elimina arista de origen a destino.
     */
    private void eliminarArista(int origen, int destino) {
        Lista<Arista> lista = listaAdy[origen];
        NodoLista<Arista> actual = lista.obtenerInicio();
        NodoLista<Arista> anterior = null;
        
        while (actual != null) {
            if (actual.getDato().getDestino() == destino) {
                if (anterior == null) {
                    lista.inicio = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                return;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
    }
    
    
}