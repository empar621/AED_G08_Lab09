package btree;

public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) { 
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(E cl) throws ItemDuplicated { 
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if (up) {
            pnew = new BNode<E>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    private E push(BNode<E> current, E cl) throws ItemDuplicated {
        int pos[] = new int[1];
        E mediana;
        if (current == null) {
            up = true;
            nDes = null;
            return cl;
        } else {
            boolean fl;
            fl = current.searchNode(cl, pos);
            if (fl) {
                System.out.println("Item duplicado\n");
                up = false;
                throw new ItemDuplicated("Error: El ítem '" + cl + "' ya se encuentra registrado en el árbol.");
            }
            mediana = push(current.childs.get(pos[0]), cl);
            if (up) {
                if (current.nodeFull(this.orden - 1)) {
                    mediana = dividedNode(current, mediana, pos[0]);
                } else {
                    up = false;
                    putNode(current, mediana, nDes, pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
        int i;
        for (i = current.count - 1; i >= k; i--) { 
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count++;
    }

    private E dividedNode(BNode<E> current, E cl, int k) { 
        BNode<E> rd = nDes;
        int i, posMdna;
        posMdna = (k <= this.orden / 2) ? this.orden / 2 : this.orden / 2 + 1;
        nDes = new BNode<E>(this.orden);
        for (i = posMdna; i < this.orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }
        nDes.count = (this.orden - 1) - posMdna;
        current.count = posMdna;
        if (k <= this.orden / 2) {
            putNode(current, cl, rd, k);
        } else {
            putNode(nDes, cl, rd, k - posMdna);
        }
        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));
        current.count--;
        return median;
    }
    @Override
    public String toString() { 
        String s = "";
        if (isEmpty()) {
            s += "BTree is empty...";
        } else {
            // Cabeceras formateadas alineadas en columnas homogéneas
            s += String.format("%-10s %-20s %-10s %-15s\n", "Id.Nodo", "Claves Nodo", "Id.Padre", "Id.Hijos");
            s += writeTree(this.root);
        }
        return s;
    }

    private String writeTree(BNode<E> current) {
        return writeTreeHelper(current, "--");
    }

    private String writeTreeHelper(BNode<E> current, String parentId) {
        if (current == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        // 1. Formatear secuencialmente las claves encapsuladas: (k1, k2, ...)
        StringBuilder keysStr = new StringBuilder("(");
        for (int i = 0; i < current.count; i++) {
            keysStr.append(current.keys.get(i));
            if (i < current.count - 1) {
                keysStr.append(", ");
            }
        }
        keysStr.append(")");

        // 2. Mapear de forma limpia los identificadores correspondientes a los hijos inmediatos
        StringBuilder childsStr = new StringBuilder();
        if (current.childs.get(0) != null) {
            childsStr.append("[");
            for (int i = 0; i <= current.count; i++) {
                if (current.childs.get(i) != null) {
                    childsStr.append(current.childs.get(i).idNode);
                    if (i < current.count && current.childs.get(i + 1) != null) {
                        childsStr.append(", ");
                    }
                }
            }
            childsStr.append("]");
        } else {
            childsStr.append("--"); // Es nodo hoja
        }

        // Agregar la información del registro del nodo al flujo de impresión
        sb.append(String.format("%-10d %-20s %-10s %-15s\n", 
                current.idNode, keysStr.toString(), parentId, childsStr.toString()));

        // 3. Propagación recursiva sistemática en pre-orden sobre los descendientes directos
        for (int i = 0; i <= current.count; i++) {
            if (current.childs.get(i) != null) {
                sb.append(writeTreeHelper(current.childs.get(i), "[" + current.idNode + "]"));
            }
        }

        return sb.toString();
    }
    
  ///  EJERCICIOS  ///
  ///
    // Ejercicio 01
    public boolean search(E cl) {
        return searchHelper(this.root, cl);
    }

    private boolean searchHelper(BNode<E> current, E cl) {
        if (current == null) {
            return false; // Condición de parada: Puntero nulo (Elemento no encontrado)
        }
        
        int[] pos = new int[1];
        boolean found = current.searchNode(cl, pos);
        
        if (found) {
            // Mensaje formal requerido con idNode y posición interna
            System.out.println(cl + " se encuentra en el nodo " + current.idNode + " en la posición " + pos[0]);
            return true;
        }
        
        // Si no se encontró en este nodo, desciende al hijo indexado en pos[0]
        return searchHelper(current.childs.get(pos[0]), cl);
    }
    
    // Ejercicio 02 //    
    public void searchRange(E min, E max) {
        if (min == null || max == null || min.compareTo(max) > 0) {
            System.out.println("Error: Rango inválido proporcionado (min > max o valores nulos).");
            return;
        }
        System.out.print("Claves en el rango [" + min + ", " + max + "]: ");
        searchRangeHelper(this.root, min, max);
        System.out.println(); // Salto de línea decorativo final
    }

    private void searchRangeHelper(BNode<E> current, E min, E max) {
        if (current == null) {
            return;
        }
        
        for (int i = 0; i <= current.count; i++) {
            boolean visitChild = true;

            if (i < current.count && current.keys.get(i) != null) {
                if (min.compareTo(current.keys.get(i)) > 0) {
                    visitChild = false; 
                }
            }

            if (i > 0 && current.keys.get(i - 1) != null) {
                if (max.compareTo(current.keys.get(i - 1)) < 0) {
                    visitChild = false;
                }
            }

            if (visitChild && current.childs.get(i) != null) {
                searchRangeHelper(current.childs.get(i), min, max);
            }

            if (i < current.count && current.keys.get(i) != null) {
                E key = current.keys.get(i);
                if (key.compareTo(min) >= 0 && key.compareTo(max) <= 0) {
                    System.out.print(key + " ");
                }
            }
        }
    }
    
    // Ejercicio 03 //
    public void remove(E cl) {
        if (isEmpty()) {
            System.out.println("El árbol se encuentra vacío. No se puede eliminar.");
            return;
        }

        boolean removed = removeHelper(this.root, cl);

        if (removed) {
            System.out.println("La clave '" + cl + "' ha sido eliminada con éxito.");

            if (this.root.count == 0) {
                if (this.root.childs.get(0) == null) {
                    this.root = null; 
                } else {
                    this.root = this.root.childs.get(0); 
                }
            }
        } else {
            System.out.println("La clave '" + cl + "' no se encuentra en el árbol.");
        }
    }

    private boolean removeHelper(BNode<E> current, E cl) {
        int[] pos = new int[1];
        boolean found = current.searchNode(cl, pos);
        int idx = pos[0];

        int minKeys = (this.orden + 1) / 2 - 1;

        if (found) {
            if (current.childs.get(0) == null) { // Caso 1
                for (int i = idx; i < current.count - 1; i++) {
                    current.keys.set(i, current.keys.get(i + 1));
                }
                current.keys.set(current.count - 1, null);
                current.count--;
                return true;
            } else { // Caso 2
                E succ = getSuccessor(current, idx);
                current.keys.set(idx, succ); 
                removeHelper(current.childs.get(idx + 1), succ); 

                if (current.childs.get(idx + 1).count < minKeys) {
                    fixUnderflow(current, idx + 1);
                }
                return true;
            }
        } else {
            if (current.childs.get(0) == null) {
                return false; 
            }

            boolean res = removeHelper(current.childs.get(idx), cl);

            if (res && current.childs.get(idx).count < minKeys) {
                fixUnderflow(current, idx);
            }
            return res;
        }
    }

    private E getSuccessor(BNode<E> current, int idx) {
        BNode<E> node = current.childs.get(idx + 1);
        while (node.childs.get(0) != null) {
            node = node.childs.get(0);
        }
        return node.keys.get(0);
    }

    private void fixUnderflow(BNode<E> parent, int idx) {
        int minKeys = (this.orden + 1) / 2 - 1;

        // Opción A: préstamo al hermano izquierdo (Redistribución)
        if (idx > 0 && parent.childs.get(idx - 1).count > minKeys) {
            borrowFromLeft(parent, idx);
        }
        // Opción B: préstamo al hermano derecho (Redistribución)
        else if (idx < parent.count && parent.childs.get(idx + 1).count > minKeys) {
            borrowFromRight(parent, idx);
        }
        // Opción C: No hay hermanos con exceso de claves, Fusionar
        else {
            if (idx > 0) {
                mergeNodes(parent, idx - 1); 
            } else {
                mergeNodes(parent, idx);
            }
        }
    }

    private void borrowFromLeft(BNode<E> parent, int idx) {
        BNode<E> child = parent.childs.get(idx);
        BNode<E> leftSibling = parent.childs.get(idx - 1);

        for (int i = child.count - 1; i >= 0; i--) {
            child.keys.set(i + 1, child.keys.get(i));
            child.childs.set(i + 2, child.childs.get(i + 1));
        }
        child.childs.set(1, child.childs.get(0));

        child.keys.set(0, parent.keys.get(idx - 1));
        child.childs.set(0, leftSibling.childs.get(leftSibling.count));

        parent.keys.set(idx - 1, leftSibling.keys.get(leftSibling.count - 1));

        leftSibling.keys.set(leftSibling.count - 1, null);
        leftSibling.childs.set(leftSibling.count, null);

        child.count++;
        leftSibling.count--;
    }

    private void borrowFromRight(BNode<E> parent, int idx) {
        BNode<E> child = parent.childs.get(idx);
        BNode<E> rightSibling = parent.childs.get(idx + 1);

        child.keys.set(child.count, parent.keys.get(idx));
        child.childs.set(child.count + 1, rightSibling.childs.get(0));

        parent.keys.set(idx, rightSibling.keys.get(0));

        for (int i = 0; i < rightSibling.count - 1; i++) {
            rightSibling.keys.set(i, rightSibling.keys.get(i + 1));
            rightSibling.childs.set(i, rightSibling.childs.get(i + 1));
        }
        rightSibling.childs.set(rightSibling.count - 1, rightSibling.childs.get(rightSibling.count));

        rightSibling.keys.set(rightSibling.count - 1, null);
        rightSibling.childs.set(rightSibling.count, null);

        child.count++;
        rightSibling.count--;
    }

    private void mergeNodes(BNode<E> parent, int idx) {
        BNode<E> leftNode = parent.childs.get(idx);
        BNode<E> rightNode = parent.childs.get(idx + 1);

        leftNode.keys.set(leftNode.count, parent.keys.get(idx));
        leftNode.count++;

        for (int i = 0; i < rightNode.count; i++) {
            leftNode.keys.set(leftNode.count, rightNode.keys.get(i));
            leftNode.childs.set(leftNode.count, rightNode.childs.get(i));
            leftNode.count++;
        }
        leftNode.childs.set(leftNode.count, rightNode.childs.get(rightNode.count));

        for (int i = idx; i < parent.count - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }
        parent.keys.set(parent.count - 1, null);
        parent.childs.set(parent.count, null);
        parent.count--;
    }
    
    // EJERCICIO 04
    // 1. Buscar mostrando el camino recorrido (idNode por el que pasa)
    public E searchWithPath(E cl) {
        return searchWithPathHelper(this.root, cl);
    }

    private E searchWithPathHelper(BNode<E> current, E cl) {
        if (current == null) {
            System.out.println(" -> [Nodo null: No encontrado]");
            return null;
        }
        System.out.print(" -> [Nodo ID: " + current.idNode + "]");
        int[] pos = new int[1];
        boolean found = current.searchNode(cl, pos);
        if (found) {
            System.out.println(" -> ¡Elemento Encontrado!");
            return current.keys.get(pos[0]);
        }
        return searchWithPathHelper(current.childs.get(pos[0]), cl);
    }

    // 2. Obtener la altura exacta del árbol
    public int getHeight() {
        return getHeightHelper(this.root);
    }

    private int getHeightHelper(BNode<E> current) {
        if (current == null) return 0;
        return 1 + getHeightHelper(current.childs.get(0));
    }

    // 3. Obtener el número total de claves registradas (size)
    public int size() {
        return sizeHelper(this.root);
    }

    private int sizeHelper(BNode<E> current) {
        if (current == null) return 0;
        int total = current.count;
        for (int i = 0; i <= current.count; i++) {
            total += sizeHelper(current.childs.get(i));
        }
        return total;
    }

    // 4. Recorrido In-Order para mostrar los elementos en orden ascendente
    public void printInOrder() {
        printInOrderHelper(this.root);
    }

    private void printInOrderHelper(BNode<E> current) {
        if (current == null) return;
        for (int i = 0; i < current.count; i++) {
            printInOrderHelper(current.childs.get(i));
            System.out.println("   " + current.keys.get(i));
        }
        printInOrderHelper(current.childs.get(current.count));
    }
}