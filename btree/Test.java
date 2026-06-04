package btree;

public class Test {
    public static void main(String[] args) {
        try {
            // Arbol orden 4
            BTree<Integer> arbol = new BTree<>(4);
            
            System.out.println("=== 1. INSERTANDO CLAVES ===");
            int[] claves = {50, 20, 70, 10, 30, 60, 80, 25, 27, 26, 65, 75, 85, 5};
            for (int k : claves) {
                arbol.insert(k);
            }
            System.out.println("Árbol construido con éxito.\n");
            System.out.println(arbol.toString());

            System.out.println("=== 2. PRUEBAS DE BÚSQUEDA (EJERCICIO 01) ===");
            // Caso A
            System.out.print("[Prueba Raíz]: ");
            arbol.search(50);
            
            // Caso B
            System.out.print("[Prueba Extremo Inicial Leaf]: ");
            arbol.search(5);
            
            // Caso C
            System.out.print("[Prueba Extremo Final Leaf]: ");
            arbol.search(85);
            
            // Caso D: Dato no encontrado
            System.out.print("[Prueba No Encontrado]: ");
            boolean res = arbol.search(99);
            System.out.println("Resultado booleano: " + res + "\n");

            System.out.println("=== 3. PRUEBAS DE RANGO (EJERCICIO 02) ===");
            System.out.println("[Prueba Rango Existente (20 a 40)]:");
            arbol.searchRange(20, 40);

            System.out.println("\n[Prueba Rango Inexistente (11 a 19)]:");
            arbol.searchRange(11, 19);

            System.out.println("\n[Prueba Rango Inválido (60 a 10)]:");
            arbol.searchRange(60, 10);
            
            System.out.println("\n=== 4. PRUEBA EXCEPCIÓN===");
            arbol.insert(26);

        } catch (ItemDuplicated e) {
            System.out.println("\nExcepción Capturada Correctamente: " + e.getMessage());
        }
    }
}