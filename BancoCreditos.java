// Clase que representa a cada persona en la fila
class Nodo {
    int turno;
    String tipo;
    int prioridad; // 0 para normal, 1 para prioridad
    String nombre;
    Nodo siguiente;

    public Nodo(int turno, String tipo, int prioridad, String nombre) {
        this.turno = turno;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.nombre = nombre;
        this.siguiente = null;
    }
}

public class BancoCreditos {

    // === INSERCIÓN NORMAL ===
    static void insertarNormal(Nodo[] cola, int turno, String tipo, String nombre) {
        Nodo nuevo = new Nodo(turno, tipo, 0, nombre);
        
        if (cola[0] == null) {
            cola[0] = nuevo;
            return;
        }
        
        Nodo actual = cola[0];
        while (actual.siguiente != null) {
            actual = actual.siguiente;
        }
        actual.siguiente = nuevo;
    }

    // === INSERCIÓN PRIORITARIA ===
    static void insertarPrioridad(Nodo[] cola, int turno, String tipo, String nombre) {
        Nodo nuevo = new Nodo(turno, tipo, 1, nombre);
        
        // Si la cola está vacía o el primero no tiene prioridad, insertamos al principio
        if (cola[0] == null || cola[0].prioridad < 1) {
            nuevo.siguiente = cola[0];
            cola[0] = nuevo;
            return;
        }
        
        // Buscamos el último nodo que tenga prioridad
        Nodo actual = cola[0];
        while (actual.siguiente != null && actual.siguiente.prioridad >= 1) {
            actual = actual.siguiente;
        }
        
        // Insertamos el nuevo nodo después del último nodo con prioridad
        nuevo.siguiente = actual.siguiente;
        actual.siguiente = nuevo;
    }

    // === FUNCIÓN PARA MOSTRAR LA COLA ===
    static void imprimirCola(Nodo[] cola) {
        if (cola[0] == null) {
            System.out.println("La fila está vacía.\n");
            return;
        }
        
        Nodo actual = cola[0];
        System.out.print("Estado de la fila: ");
        while (actual != null) {
            String etiquetaPrioridad = actual.prioridad == 1 ? "[⭐ Prioridad]" : "[Normal]";
            System.out.print(etiquetaPrioridad + " " + actual.nombre + " (Turno " + actual.turno + ") -> ");
            actual = actual.siguiente;
        }
        System.out.println("FIN\n");
    }

    // === EJECUCIÓN DEL PROGRAMA ===
    public static void main(String[] args) {
        // En Java, usamos un arreglo de 1 posición para poder modificar 
        // la referencia a la cabeza (inicio) de la lista desde los métodos.
        Nodo[] colaSectorCreditos = new Nodo[1];

        System.out.println("--- SISTEMA DE TURNOS: SECTOR CRÉDITOS ---\n");

        System.out.println("Paso 1: Llega Juan (Cliente Normal)");
        insertarNormal(colaSectorCreditos, 1, "Creditos", "Juan");
        imprimirCola(colaSectorCreditos);

        System.out.println("Paso 2: Llega Pedro (Cliente Normal)");
        insertarNormal(colaSectorCreditos, 2, "Creditos", "Pedro");
        imprimirCola(colaSectorCreditos);

        System.out.println("Paso 3: Llega María (Cliente Embarazada - Prioridad)");
        insertarPrioridad(colaSectorCreditos, 3, "Creditos", "María");
        imprimirCola(colaSectorCreditos);

        System.out.println("Paso 4: Llega Carlos (Cliente Normal)");
        insertarNormal(colaSectorCreditos, 4, "Creditos", "Carlos");
        imprimirCola(colaSectorCreditos);

        System.out.println("Paso 5: Llega Ana (Jubilada - Prioridad)");
        insertarPrioridad(colaSectorCreditos, 5, "Creditos", "Ana");
        imprimirCola(colaSectorCreditos);
    }
}