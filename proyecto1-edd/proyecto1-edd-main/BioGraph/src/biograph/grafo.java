package biograph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase principal que implementa el grafo de interacciones proteicas
 * @author Gianfranco, Adrian, Alirio
 */
public class grafo {
    /**
     * Atributos
     */
    private boolean dirigido;
    private int capacidadMaxima;
    private int numProteinas;
    private String[] nombresProteinas;      // Arreglo de nombres
    private boolean[] activa;               // Proteína eliminada o activa
    private Lista<Arista>[] listaAdy;       // Lista de adyacencia
    private String archivoActual;            // Ruta del archivo actualmente cargado
    
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
        this.archivoActual = null;
        
        for (int i = 0; i < capacidad; i++) {
            listaAdy[i] = new Lista<Arista>();
            activa[i] = false;  // Inicialmente no hay proteínas
        }
    }
    
    /**
     * Busca el indice de una proteina por nombre (busqueda lineal).
     * 
     * @param nombre Nombre de la proteína a buscar
     * @return Índice de la proteína o -1 si no se encuentra
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
     * Obtiene el índice de una proteína por su nombre (público)
     * @param nombre Nombre de la proteína
     * @return Índice o -1 si no existe
     */
    public int getIndiceProteina(String nombre) {
        return buscarIndiceProteina(nombre);
    }
    
    /**
     * Agrega una nueva proteína si hay espacio.
     * 
     * @param nombre Nombre de la proteina a agregar
     * @return indice asignado a la proteína
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
     * 
     * @param proteinaA Nombre de la primera proteína
     * @param proteinaB Nombre de la segunda proteína
     * @param peso Peso o costo de la interacción
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
     * Elimina una interacción entre dos proteínas
     * @param proteinaA Nombre de la primera proteína
     * @param proteinaB Nombre de la segunda proteína
     */
    public void eliminarInteraccion(String proteinaA, String proteinaB) {
        int idxA = buscarIndiceProteina(proteinaA);
        int idxB = buscarIndiceProteina(proteinaB);
        
        if (idxA == -1 || idxB == -1) return;
        
        eliminarArista(idxA, idxB);
        if (!dirigido) {
            eliminarArista(idxB, idxA);
        }
    }
    
    /**
     * Verifica si existe una interacción entre dos proteínas
     * @param proteinaA Nombre de la primera proteína
     * @param proteinaB Nombre de la segunda proteína
     * @return true si existe la interacción, false en caso contrario
     */
    public boolean existeInteraccion(String proteinaA, String proteinaB) {
        int idxA = buscarIndiceProteina(proteinaA);
        int idxB = buscarIndiceProteina(proteinaB);
        
        if (idxA == -1 || idxB == -1) {
            return false;
        }
        
        Lista<Arista> adyacentes = listaAdy[idxA];
        NodoLista<Arista> actual = adyacentes.obtenerInicio();
        
        while (actual != null) {
            if (actual.getDato().getDestino() == idxB) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    /**
     * Verifica si existe arista entre dos índices.
     * 
     * @param origen indice del vertice origen
     * @param destino indice del vertice destino
     * @return true si existe la arista, false en caso contrario
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
     * 
     * @param nombre Nombre de la proteina a eliminar
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
     * 
     * @param origen indice del vertice origen
     * @param destino indice del vertice destino
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
     * 
     * 
     * @return Arreglo de nombres de proteínas activas
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
     * 
     * @return Numero de proteinas activas
     */
    public int contarProteinasActivas() {
        int count = 0;
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) count++;
        }
        return count;
    }
    
    /**
     * Calcula grado de un vértice.
     * 
     * @param indice indice del vertice
     * @return Grado del vertice
     */
    public int calcularGrado(int indice) {
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
     * 
     * @param lista Lista a contar
     * @return Numero de elementos en la lista
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
     * 
     * @param cantidad Numero maximo de hubs a identificar
     * @return Lista de nombres de las proteinas identificadas como hubs
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
     * Encuentra todos los complejos proteicos (componentes conexos) en el grafo
     * Utiliza el algoritmo BFS para identificar grupos de proteínas conectadas
     * 
     * @return Lista de listas, donde cada sublista representa un complejo proteico
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
     * algoritmo BFS (Busqueda en Anchura).
     * 
     * @param inicio indice del vertice inicial
     * @param visitado Arreglo de vertices visitados
     * @param componente Lista donde se almacenarán los vertices del componente
     */
    private void bfs(int inicio, boolean[] visitado, Lista<String> componente) {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(inicio);
        visitado[inicio] = true;
        
        while (!cola.esVacia()) {
            int actual = cola.desencolarConRetorno();
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
    
    /**
     * Encuentra la ruta más corta entre dos proteínas usando el algoritmo de Dijkstra.
     * 
     * @param origen Nombre de la proteína origen
     * @param destino Nombre de la proteína destino
     * @return Lista con la secuencia de proteínas en la ruta más corta
     */
    public Lista<String> encontrarRutaMasCorta(String origen, String destino) {
        int idxOrigen = buscarIndiceProteina(origen);
        int idxDestino = buscarIndiceProteina(destino);
        
        if (idxOrigen == -1 || idxDestino == -1) {
            return new Lista<>();
        }
        
        int[] distancias = new int[capacidadMaxima];
        int[] predecesores = new int[capacidadMaxima];
        boolean[] procesado = new boolean[capacidadMaxima];
        
        // Inicializar
        for (int i = 0; i < capacidadMaxima; i++) {
            distancias[i] = Integer.MAX_VALUE;
            predecesores[i] = -1;
            procesado[i] = false;
        }
        distancias[idxOrigen] = 0;
        
        // Dijkstra
        for (int count = 0; count < contarProteinasActivas(); count++) {
            // Encontrar vértice no procesado con distancia mínima
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            for (int i = 0; i < numProteinas; i++) {
                if (activa[i] && !procesado[i] && distancias[i] < minDist) {
                    minDist = distancias[i];
                    u = i;
                }
            }
            
            if (u == -1 || u == idxDestino) break;
            procesado[u] = true;
            
            // Relajar aristas
            NodoLista<Arista> nodo = listaAdy[u].obtenerInicio();
            while (nodo != null) {
                Arista arista = nodo.getDato();
                int v = arista.getDestino();
                if (activa[v] && !procesado[v]) {
                    int nuevaDist = distancias[u] + arista.getPeso();
                    if (nuevaDist < distancias[v]) {
                        distancias[v] = nuevaDist;
                        predecesores[v] = u;
                    }
                }
                nodo = nodo.getSiguiente();
            }
        }
        
        // Reconstruir ruta
        Lista<String> ruta = new Lista<>();
        int actual = idxDestino;
        
        // Verificar si hay ruta
        if (predecesores[actual] == -1 && actual != idxOrigen) {
            return new Lista<>(); // No hay ruta
        }
        
        // Construir la ruta desde destino a origen
        while (actual != -1) {
            ruta.insertar(nombresProteinas[actual]);
            actual = predecesores[actual];
        }
        
        return ruta;
    }
    
    // ====================================================
    // MÉTODOS PARA ARCHIVOS Y PERSISTENCIA
    // ====================================================
    
    /**
     * Carga un archivo CSV con las interacciones
     * @param archivo Archivo a cargar
     * @throws IOException Si hay error de lectura
     */
    public void cargarArchivo(File archivo) throws IOException {
        // Limpiar el grafo actual
        limpiarGrafo();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                String[] partes = linea.split(",");
                if (partes.length >= 3) {
                    String protA = partes[0].trim();
                    String protB = partes[1].trim();
                    int peso = Integer.parseInt(partes[2].trim());
                    
                    agregarInteraccion(protA, protB, peso);
                }
            }
        }
        
        this.archivoActual = archivo.getAbsolutePath();
    }
    
    /**
     * Guarda el grafo actual en un archivo CSV
     * @param archivo Archivo donde guardar
     * @throws IOException Si hay error de escritura
     */
    public void guardarArchivo(File archivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            // Para evitar duplicados, llevamos registro de aristas ya escritas
            boolean[][] escrita = new boolean[numProteinas][numProteinas];
            
            for (int i = 0; i < numProteinas; i++) {
                if (activa[i]) {
                    NodoLista<Arista> nodo = listaAdy[i].obtenerInicio();
                    while (nodo != null) {
                        Arista arista = nodo.getDato();
                        int j = arista.getDestino();
                        
                        // Escribir solo si no se ha escrito antes (para no dirigidos)
                        if (!escrita[i][j] && !escrita[j][i]) {
                            pw.println(nombresProteinas[i] + "," + 
                                      nombresProteinas[j] + "," + 
                                      arista.getPeso());
                            escrita[i][j] = true;
                            if (!dirigido) {
                                escrita[j][i] = true;
                            }
                        }
                        nodo = nodo.getSiguiente();
                    }
                }
            }
        }
        
        this.archivoActual = archivo.getAbsolutePath();
    }
    
    /**
     * Limpia todo el grafo
     */
    private void limpiarGrafo() {
        this.numProteinas = 0;
        this.nombresProteinas = new String[capacidadMaxima];
        this.activa = new boolean[capacidadMaxima];
        this.listaAdy = new Lista[capacidadMaxima];
        
        for (int i = 0; i < capacidadMaxima; i++) {
            listaAdy[i] = new Lista<Arista>();
            activa[i] = false;
        }
    }
    
    /**
     * Obtiene el nombre de una proteína por su índice
     * @param indice Índice de la proteína
     * @return Nombre de la proteína o null si no está activa
     */
    public String getNombreProteina(int indice) {
        if (indice >= 0 && indice < numProteinas && activa[indice]) {
            return nombresProteinas[indice];
        }
        return null;
    }
    
    /**
     * Obtiene la lista de adyacencia de un nodo
     * @param indice Índice de la proteína
     * @return Lista de adyacencia
     */
    public Lista<Arista> getListaAdyacencia(int indice) {
        if (indice >= 0 && indice < numProteinas && activa[indice]) {
            return listaAdy[indice];
        }
        return null;
    }
    
    /**
     * Verifica si el grafo tiene datos
     * @return true si tiene al menos una proteína activa
     */
    public boolean tieneDatos() {
        return contarProteinasActivas() > 0;
    }
    
    /**
     * Obtiene la ruta del archivo actual
     * @return Ruta del archivo o null si no hay
     */
    public String getArchivoActual() {
        return archivoActual;
    }
    
    /**
     * Convierte una lista a String para mostrar
     * @param lista Lista a convertir
     * @return String con los elementos de la lista
     */
    public String listaToString(Lista<?> lista) {
        if (lista == null || lista.obtenerInicio() == null) {
            return "Vacío";
        }
        
        StringBuilder sb = new StringBuilder();
        NodoLista<?> actual = lista.obtenerInicio();
        while (actual != null) {
            sb.append(actual.getDato().toString());
            actual = actual.getSiguiente();
            if (actual != null) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
    
    /**
     * Representación en String del grafo
     * @return String con la información del grafo
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GRAFO DE INTERACCIONES PROTEICAS ===\n");
        sb.append("Proteínas activas: ").append(contarProteinasActivas()).append("\n\n");
        
        for (int i = 0; i < numProteinas; i++) {
            if (activa[i]) {
                sb.append(nombresProteinas[i]).append(" (").append(i).append(") -> ");
                
                NodoLista<Arista> nodo = listaAdy[i].obtenerInicio();
                if (nodo == null) {
                    sb.append("sin conexiones");
                } else {
                    boolean primero = true;
                    while (nodo != null) {
                        if (!primero) sb.append(", ");
                        Arista arista = nodo.getDato();
                        if (arista.getDestino() >= 0 && arista.getDestino() < numProteinas) {
                            sb.append(nombresProteinas[arista.getDestino()])
                              .append("(").append(arista.getPeso()).append(")");
                        }
                        primero = false;
                        nodo = nodo.getSiguiente();
                    }
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
}