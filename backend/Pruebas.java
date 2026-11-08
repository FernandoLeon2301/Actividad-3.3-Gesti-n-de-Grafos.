package backend;

import java.util.Arrays;
import java.util.List;

/**
 * Batería de pruebas automáticas del grafo ponderado.
 */
public class Pruebas {

    private interface Operacion {
        void ejecutar() throws Exception;
    }

    public static String ejecutarSuite() {
        StringBuilder sb = new StringBuilder();
        int[] cont = new int[2];

        sb.append("===== PRUEBAS DE FUNCIONAMIENTO =====\n");

        // 1. Crear grafo vacío
        GrafoPonderado g = new GrafoPonderado();
        registrar(
                sb,
                cont,
                "Crear grafo vacío",
                g.estaVacio() && g.cantidadVertices() == 0 && g.cantidadAristas() == 0
        );

        // 2. Insertar varios vértices
        g.agregarVertice(new Vertice("1", "CD Norte", "Centro de Distribución", "Norte", 150));
        g.agregarVertice(new Vertice("2", "Terminal Sur", "Terminal", "Sur", 80));
        g.agregarVertice(new Vertice("3", "Almacén Este", "Almacén", "Este", 120));

        registrar(
                sb,
                cont,
                "Insertar varios vértices con IDs diferentes",
                g.cantidadVertices() == 3
        );

        // 3. Intentar insertar ID duplicado
        registrar(
                sb,
                cont,
                "Rechazar identificador duplicado",
                captura(() -> {
                    g.agregarVertice(new Vertice("1", "Duplicado", "Terminal", "Norte", 10));
                })
        );

        // 4. Conectar vértices con aristas
        g.agregarArista("1", "2", 12.5);
        g.agregarArista("2", "3", 7.0);
        g.agregarArista("1", "3", 20.0);

        double[][] m = g.getMatrizAdyacencia();

        registrar(
                sb,
                cont,
                "Conectar vértices con aristas y pesos",
                g.cantidadAristas() == 3 && m[0][1] == 12.5 && m[1][2] == 7.0
        );

        // 5. Buscar existente e inexistente
        registrar(
                sb,
                cont,
                "Buscar vértice existente e inexistente",
                g.buscarVertice("2") != null && g.buscarVertice("99") == null
        );

        // 6. Eliminar vértice aislado
        g.agregarVertice(new Vertice("4", "Cliente A", "Cliente", "Oeste", 10));
        g.eliminarVertice("4");

        registrar(
                sb,
                cont,
                "Eliminar vértice aislado",
                !g.existeVertice("4") && g.cantidadVertices() == 3
        );

        // 7. Eliminar vértice con aristas conectadas
        GrafoPonderado cascada = new GrafoPonderado();
        agregarVertices(cascada, "10", "20", "30");

        cascada.agregarArista("10", "20", 5);
        cascada.agregarArista("30", "10", 6);

        cascada.eliminarVertice("10");

        registrar(
                sb,
                cont,
                "Eliminar vértice con aristas conectadas (cascada)",
                !cascada.existeVertice("10") && cascada.cantidadAristas() == 0
        );

        // 8. BFS y DFS
        GrafoPonderado recorridos = new GrafoPonderado();
        agregarVertices(recorridos, "1", "2", "3", "4");

        recorridos.agregarArista("1", "2", 1);
        recorridos.agregarArista("1", "3", 1);
        recorridos.agregarArista("2", "4", 1);
        recorridos.agregarArista("3", "4", 1);

        List<String> bfs = recorridos.bfs("1");
        List<String> dfs = recorridos.dfs("1");

        registrar(
                sb,
                cont,
                "BFS desde el nodo 1",
                bfs.equals(Arrays.asList("1", "2", "3", "4"))
        );

        registrar(
                sb,
                cont,
                "DFS desde el nodo 1",
                dfs.equals(Arrays.asList("1", "2", "4", "3"))
        );

        // 9. Conectividad fuerte
        GrafoPonderado fuerte = new GrafoPonderado();
        agregarVertices(fuerte, "1", "2", "3");

        fuerte.agregarArista("1", "2", 1);
        fuerte.agregarArista("2", "3", 1);
        fuerte.agregarArista("3", "1", 1);

        registrar(
                sb,
                cont,
                "Conectividad: fuertemente conectado",
                fuerte.evaluarConectividad().equals("Fuertemente conectado")
        );

        // 10. Conectividad débil
        GrafoPonderado debil = new GrafoPonderado();
        agregarVertices(debil, "1", "2", "3");

        debil.agregarArista("1", "2", 1);
        debil.agregarArista("2", "3", 1);

        registrar(
                sb,
                cont,
                "Conectividad: débilmente conectado",
                debil.evaluarConectividad().equals("Débilmente conectado")
        );

        // 11. Grafo desconectado
        GrafoPonderado desconectado = new GrafoPonderado();
        agregarVertices(desconectado, "1", "2", "3");

        desconectado.agregarArista("1", "2", 1);

        registrar(
                sb,
                cont,
                "Conectividad: desconectado",
                desconectado.evaluarConectividad().equals("Desconectado")
        );

        // 12. Destruir grafo
        GrafoPonderado destruir = new GrafoPonderado();
        agregarVertices(destruir, "A", "B");

        destruir.agregarArista("A", "B", 2);
        destruir.destruirGrafo();

        registrar(
                sb,
                cont,
                "Destruir grafo y quedar vacío",
                destruir.estaVacio() && destruir.cantidadAristas() == 0
        );

        // 13. Validaciones
        boolean validaciones =
                captura(() -> {
                    new Vertice(" ", "X", "Y", "Z", 5);
                })
                && captura(() -> {
                    new Vertice("5", " ", "Y", "Z", 5);
                })
                && captura(() -> {
                    new Vertice("5", "X", "Y", "Z", -1);
                })
                && captura(() -> {
                    GrafoPonderado gp = new GrafoPonderado();
                    gp.agregarVertice(new Vertice("a", "A", "Terminal", "Norte", 1));
                    gp.agregarArista("a", "a", -3);
                })
                && captura(() -> {
                    GrafoPonderado gp = new GrafoPonderado();
                    gp.agregarVertice(new Vertice("a", "A", "Terminal", "Norte", 1));
                    gp.agregarArista("a", "b", 2);
                })
                && captura(() -> {
                    new GrafoPonderado().bfs("1");
                });

        registrar(
                sb,
                cont,
                "Validaciones de datos y operaciones sobre grafo vacío",
                validaciones
        );

        sb.append("\nResumen: ")
          .append(cont[0])
          .append(" pruebas pasaron, ")
          .append(cont[1])
          .append(" fallaron.\n");

        return sb.toString();
    }

    private static void agregarVertices(GrafoPonderado g, String... ids) {
        for (String id : ids) {
            g.agregarVertice(new Vertice(
                    id,
                    "Nodo " + id,
                    "Terminal",
                    "Zona " + id,
                    10.0
            ));
        }
    }

    private static boolean captura(Operacion op) {
        try {
            op.ejecutar();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private static void registrar(StringBuilder sb, int[] cont, String nombre, boolean ok) {
        sb.append(ok ? "[PASS] " : "[FAIL] ").append(nombre).append('\n');

        if (ok) {
            cont[0]++;
        } else {
            cont[1]++;
        }
    }
}