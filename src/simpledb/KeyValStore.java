package simpledb;

public interface KeyValStore {

    void set(String key, String value);

    String get(String key);

    void unset(String key);

    int numequalto(String value);

}
