package simpledb.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import simpledb.KeyValStore;

/**
 * For simplicity, this store can only be accessed by one client at a time and is not thread safe. If multiple clients
 * need to connect to the store, then Concurrent collection libraries need to be used like ConcurrentHashMap Complexity:
 * All operations take O(1). In JDK 8, HashMap has been tweaked for a worst case complexity of O(logn)
 */
public class RedisStore implements KeyValStore {

    private Map<String, String> store = new HashMap<>();
    private Map<String, Integer> valueCounter = new HashMap<>();
    //This set is needed to keep track of keys that have been unset inside a transaction and is used during merge
    private Set<String> unsetKeys = new HashSet<>();

    @Override
    public void set(final String key, final String value) {
        //Decrement the count for the current value
        if (store.containsKey(key)) {
            String oldVal = store.get(key);
            decreaseValueCountBy(oldVal, 1);
        }
        //If the key was unset prior to this operation, remove from the set
        if (isKeyMarkedUnset(key)) {
            unsetKeys.remove(key);
        }
        //Increment the count for the new value
        store.put(key, value);
        increaseValueCountBy(value, 1);
    }

    @Override
    public String get(final String key) {
        if (isKeyMarkedUnset(key)) {
            return null;
        }
        return store.get(key);
    }

    @Override
    public void unset(final String key) {
        unsetKeys.add(key);
        if (!store.containsKey(key)) {
            return;
        }
        //Decrement the count for the value and remove the key
        final String value = store.get(key);
        decreaseValueCountBy(value, 1);
        store.remove(key);
    }

    @Override
    public Integer numequalto(final String value) {
        Integer count = null;
        if (valueCounter.containsKey(value)) {
            count = valueCounter.get(value);
        }
        return count;
    }

    public void merge(RedisStore other) {
        this.store.putAll(other.store);
        for (String key : other.unsetKeys) {
            this.store.remove(key);
        }
        this.valueCounter.putAll(other.valueCounter);
    }

    public boolean isKeyMarkedUnset(final String key) {
        return unsetKeys.contains(key);
    }

    public void increaseValueCountBy(final String value, int count) {
        if (valueCounter.containsKey(value)) {
            int oldCount = valueCounter.get(value);
            valueCounter.put(value, count + oldCount);
        } else {
            valueCounter.put(value, count);
        }
    }

    public void decreaseValueCountBy(final String value, int count) {
        if (valueCounter.containsKey(value)) {
            int oldCount = valueCounter.get(value);
            valueCounter.put(value, oldCount - count);
        } else {
            valueCounter.put(value, 0);
        }
    }

}
