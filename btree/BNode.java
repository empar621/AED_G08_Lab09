package btree;

import java.util.ArrayList;

public class BNode<E> {
    private static int idCounter = 1; 
    protected int idNode;             
    protected ArrayList<E> keys;     
    protected ArrayList<BNode<E>> childs; 
    protected int count;              

    public BNode(int n) {
        this.idNode = idCounter++; 
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n);
        this.count = 0;
        for (int i = 0; i < n; i++) { 
            this.keys.add(null);
            this.childs.add(null);
        }
    }

    // Comprueba si el nodo actual está lleno
    public boolean nodeFull(int maxKeys) {
        return this.count >= maxKeys;
    }

    // Comprueba si el nodo actual está vacío o por debajo del mínimo reglamentario
    public boolean nodeEmpty(int minKeys) {
        return this.count < minKeys;
    }
    

    // Busca una clave en el nodo actual
    @SuppressWarnings("unchecked")
    public boolean searchNode(E cl, int pos[]) {
        int i = 0;
        while (i < this.count && this.keys.get(i) != null) {
            Comparable<E> currentKey = (Comparable<E>) this.keys.get(i);
            int cmp = currentKey.compareTo(cl);
            if (cmp == 0) {
                pos[0] = i; // encontrado en posición i
                return true;
            } else if (cmp > 0) {
                break; 
            }
            i++;
        }
        pos[0] = i; 
        return false;
    }

    // Retorna la representación del identificador junto a sus claves
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node ").append(idNode).append(": [");
        for (int i = 0; i < this.count; i++) {
            sb.append(keys.get(i));
            if (i < this.count - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}