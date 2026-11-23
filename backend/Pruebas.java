package backend;

import java.util.Arrays;
import java.util.List;

public class Pruebas {

    private interface Operacion {
        void ejecutar() throws Exception;
    }

    public static String ejecutarSuite() {
        StringBuilder sb = new StringBuilder();
        int[] cont = new int[2];

        sb.append("===== PRUEBAS DE FUNCIONAMIENTO - ACTIVIDAD 3.3 =====").append('\n');

        GrafoPonderado g = new GrafoPonderado();

        registrar(
                sb,
                cont,
                "Crear grafo vacío",
                g.estaVacio()
                        && g.cantidadVertices() == 0
                        && g.cantidadAristas() == 0
        );

        agregarVertices(g, "1", "2", "3", "4", "5", "6", "7", "8");

        registrar(
                sb,
                cont,
                "Insertar 8 vértices con IDs diferentes",
                g.cantidadVertices() == 8
        );

        registrar(
                sb,
                cont,
                "Rechazar identificador duplicado",
                captura(() -> {
                    g.agregarVertice(
                            new Vertice("1", "Duplicado", "Terminal", "Norte", 10)
                    );
                })
        );

        g.agregarArista("1", "2", 4);
        g.agregarArista("1", "3", 3);
        g.agregarArista("1", "4", 2);
        g.agregarArista("2", "5", 1);
        g.agregarArista("3", "5", 2);
        g.agregarArista("4", "6", 1);
        g.agregarArista("5", "7", 2);
        g.agregarArista("6", "7", 3);
        g.agregarArista("7", "8", 1);

        double[][] matriz = g.getMatrizAdyacencia();

        registrar(
                sb,
                cont,
                "Conectar 8 nodos con aristas y generar matriz",
                g.cantidadAristas() == 9
                        && matriz.length == 8
                        && matriz[0][1] == 4.0
                        && matriz[6][7] == 1.0
        );

        registrar(
                sb,
                cont,
                "Buscar vértice existente e inexistente",
                g.buscarVertice("5") != null
                        && g.buscarVertice("99") == null
        );

        g.agregarVertice(
                new Vertice("9", "Cliente aislado", "Cliente", "Sur", 5)
        );

        g.eliminarVertice("9");

        registrar(
                sb,
                cont,
                "Eliminar vértice aislado",
                !g.existeVertice("9")
                        && g.cantidadVertices() == 8
        );

        GrafoPonderado cascada = new GrafoPonderado();

        agregarVertices(
                cascada,
                "1", "2", "3", "4", "5", "6", "7", "8"
        );

        cascada.agregarArista("1", "2", 1);
        cascada.agregarArista("1", "3", 1);
        cascada.agregarArista("2", "5", 1);
        cascada.agregarArista("3", "5", 1);
        cascada.agregarArista("4", "5", 1);
        cascada.agregarArista("5", "6", 1);
        cascada.agregarArista("5", "7", 1);
        cascada.agregarArista("6", "8", 1);
        cascada.agregarArista("7", "8", 1);

        cascada.eliminarVertice("5");

        registrar(
                sb,
                cont,
                "Eliminar vértice con aristas conectadas (cascada)",
                !cascada.existeVertice("5")
                        && cascada.cantidadAristas() == 4
        );

        List<String> bfs = g.bfs("1");
        List<String> dfs = g.dfs("1");

        registrar(
                sb,
                cont,
                "BFS desde el nodo 1 en grafo de 8 nodos",
                bfs.equals(
                        Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")
                )
        );

        registrar(
                sb,
                cont,
                "DFS desde el nodo 1 en grafo de 8 nodos",
                dfs.equals(
                        Arrays.asList("1", "2", "5", "7", "8", "3", "4", "6")
                )
        );

        GrafoPonderado fuerte = new GrafoPonderado();

        agregarVertices(
                fuerte,
                "1", "2", "3", "4", "5", "6", "7", "8"
        );

        fuerte.agregarArista("1", "2", 1);
        fuerte.agregarArista("2", "3", 1);
        fuerte.agregarArista("3", "4", 1);
        fuerte.agregarArista("4", "5", 1);
        fuerte.agregarArista("5", "6", 1);
        fuerte.agregarArista("6", "7", 1);
        fuerte.agregarArista("7", "8", 1);
        fuerte.agregarArista("8", "1", 1);

        registrar(
                sb,
                cont,
                "Conectividad: fuertemente conectado (8 nodos)",
                fuerte.evaluarConectividad().equals("Fuertemente conectado")
        );

        registrar(
                sb,
                cont,
                "Conectividad: débilmente conectado (8 nodos)",
                g.evaluarConectividad().equals("Débilmente conectado")
        );

        GrafoPonderado desconectado = new GrafoPonderado();

        agregarVertices(
                desconectado,
                "1", "2", "3", "4", "5", "6", "7", "8"
        );

        desconectado.agregarArista("1", "2", 1);
        desconectado.agregarArista("2", "3", 1);
        desconectado.agregarArista("3", "4", 1);
        desconectado.agregarArista("4", "5", 1);
        desconectado.agregarArista("5", "6", 1);
        desconectado.agregarArista("6", "7", 1);

        registrar(
                sb,
                cont,
                "Conectividad: desconectado (8 nodos, nodo 8 aislado)",
                desconectado.evaluarConectividad().equals("Desconectado")
        );

        GrafoPonderado destruir = new GrafoPonderado();

        agregarVertices(
                destruir,
                "1", "2", "3", "4", "5", "6", "7", "8"
        );

        destruir.agregarArista("1", "2", 1);
        destruir.agregarArista("2", "3", 1);
        destruir.agregarArista("3", "4", 1);
        destruir.agregarArista("4", "5", 1);
        destruir.agregarArista("5", "6", 1);
        destruir.agregarArista("6", "7", 1);
        destruir.agregarArista("7", "8", 1);
        destruir.agregarArista("8", "1", 1);

        destruir.destruirGrafo();

        registrar(
                sb,
                cont,
                "Destruir grafo y quedar vacío",
                destruir.estaVacio()
                        && destruir.cantidadVertices() == 0
                        && destruir.cantidadAristas() == 0
        );

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

                    gp.agregarVertice(
                            new Vertice("a", "A", "Terminal", "Norte", 1)
                    );

                    gp.agregarVertice(
                            new Vertice("b", "B", "Terminal", "Norte", 1)
                    );

                    gp.agregarArista("a", "b", Double.NEGATIVE_INFINITY);
                })
                && captura(() -> {
                    GrafoPonderado gp = new GrafoPonderado();

                    gp.agregarVertice(
                            new Vertice("a", "A", "Terminal", "Norte", 1)
                    );

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

        sb.append('\n');
        sb.append("Resumen: ")
                .append(cont[0])
                .append(" pruebas pasaron, ")
                .append(cont[1])
                .append(" fallaron.")
                .append('\n');

        return sb.toString();
    }

    private static void agregarVertices(GrafoPonderado g, String... ids) {
        for (String id : ids) {
            g.agregarVertice(
                    new Vertice(
                            id,
                            "Nodo " + id,
                            "Terminal",
                            "Zona " + id,
                            10.0
                    )
            );
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

    private static void registrar(
            StringBuilder sb,
            int[] cont,
            String nombre,
            boolean ok
    ) {
        sb.append(ok ? "[PASS] " : "[FAIL] ")
                .append(nombre)
                .append('\n');

        if (ok) {
            cont[0]++;
        } else {
            cont[1]++;
        }
    }
}
