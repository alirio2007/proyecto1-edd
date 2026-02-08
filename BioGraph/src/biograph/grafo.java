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
    
    /**
     * Obtiene todas las proteínas activas.
     */
    public String[] obtenerProteinasActivas() {
        int count = 0;
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) count++;
        }
        
        String[] activas = new String[count];
        int j = 0;
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) {
                activas[j++] = nombresProteinas[i];
            }
        }
        return activas;
    }
    
    /**
     * Cuenta proteínas activas.
     */
    private int contarProteinasActivas() {
        int count = 0;
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) count++;
        }
        return count;
    }
    
    /**
     * Calcula grado de un vértice.
     */
    private int calcularGrado(int indice) {
        int grado = 0;
        NodoLista<Arista> actual = listaAdy[indice].obtenerInicio();
        while (actual != null) {
            grado++;
            actual = actual.getSiguiente();
        }
        return grado;
    }
    
    
    
    /**
     * Cuenta elementos en una lista.
     */
    private int contarElementos(Lista<String> lista) {
        int count = 0;
        NodoLista<String> actual = lista.obtenerInicio();
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        return count;
    }
    
    
    /**
     * Identific proteinas con mayor grado - hubs.
     */
    public Lista<String> identificarHubs(int cantidad) {
        // Arreglo para almacenar (nombre, grado)
        String[] nombresHubs = new String[contarProteinasActivas()];
        int[] grados = new int[contarProteinasActivas()];
        int idx = 0;
        
        // Calcular grados
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) {
                nombresHubs[idx] = nombresProteinas[i];
                grados[idx] = calcularGrado(i);
                idx++;
            }
        }
        
        // Ordenar por grado descendente
        for (int i = 0; i < idx - 1; i++) {
            for (int j = 0; j < idx - i - 1; j++) {
                if (grados[j] < grados[j + 1]) {
                    // Intercambiar grados
                    int tempGrado = grados[j];
                    grados[j] = grados[j + 1];
                    grados[j + 1] = tempGrado;
                    
                    // Intercambiar nombres
                    String tempNombre = nombresHubs[j];
                    nombresHubs[j] = nombresHubs[j + 1];
                    nombresHubs[j + 1] = tempNombre;
                }
            }
        }
        
        // Crear lista resultado
        Lista<String> resultado = new Lista<>();
        for (int i = 0; i < Math.min(cantidad, idx); i++) {
            resultado.insertar(nombresHubs[i]);
        }
        
        return resultado;
    }

    /**
     * BFS para encontrar complejos proteicos - componentes conexos.
     */

    public Lista<Lista<String>> encontrarComplejosProteicos() {
        boolean[] visitado = new boolean[capacidadMaxima];
        Lista<Lista<String>> complejos = new Lista<>();
        
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i] && !visitado[i]) {
                Lista<String> complejo = new Lista<>();
                bfs(i, visitado, complejo);
                
                if (contarElementos(complejo) > 1) {
                    complejos.insertar(complejo);
                }
            }
        }
        return complejos;
    }
    
    /**
     * BFS.
     */
    private void bfs(int inicio, boolean[] visitado, Lista<String> componente) {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(inicio);
        visitado[inicio] = true;
        
        while (!cola.esVacia()) {
            int actual = ((Cola<Integer>) cola).desencolarConRetorno();
            componente.insertar(nombresProteinas[actual]);
            
            NodoLista<Arista> nodo = listaAdy[actual].obtenerInicio();
            while (nodo != null) {
                int vecino = nodo.getDato().getDestino();
                if (activa[vecino] && !visitado[vecino]) {
                    visitado[vecino] = true;
                    cola.encolar(vecino);
                }
                nodo = nodo.getSiguiente();
            }
        }
    }

    
    
}