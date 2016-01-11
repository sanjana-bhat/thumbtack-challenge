package simpledb;

/**
 * A common interface for transactional operations in key-value stores
 */
public interface Transaction {

    void begin(KeyValStore store);

    KeyValStore commit(KeyValStore store);

    KeyValStore rollback();

}
