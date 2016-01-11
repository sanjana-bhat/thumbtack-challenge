package simpledb;

/**
 * This interface is implemented by any key-value store. Ex: Redis, Memcached etc
 */
public interface KeyValStore {

    void set(String key, String value);

    String get(String key);

    void unset(String key);

    Integer numequalto(String value);

}
