package simpledb;

public interface Transaction {

    void begin(KeyValStore store);

    KeyValStore commit(KeyValStore store);

    KeyValStore rollback();

}
