package btree;

public class ItemDuplicated extends Exception {

    public ItemDuplicated() {
        super();
    }

    public ItemDuplicated(String message) {
        super(message);
    }
}