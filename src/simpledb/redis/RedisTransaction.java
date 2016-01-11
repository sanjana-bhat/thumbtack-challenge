package simpledb.redis;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import simpledb.KeyValStore;
import simpledb.Transaction;

/**
 * Transaction operations related to Redis
 */
public class RedisTransaction implements Transaction, Iterable<KeyValStore> {

    //An LIFO store to save previous redis states during a new transaction
    private Deque<KeyValStore> savedStates;

    public RedisTransaction() {
        savedStates = new ArrayDeque<>();
    }

    @Override
    public void begin(final KeyValStore store) {
        savedStates.add(store);
    }

    @Override
    public KeyValStore commit(KeyValStore store) {
        if (savedStates.size() == 0) {
            return null;
        }
        savedStates.add(store);
        RedisStore firstState = (RedisStore) savedStates.pollFirst();
        while (!savedStates.isEmpty()) {
            RedisStore state = (RedisStore) savedStates.pollFirst();
            firstState.merge(state);
        }
        //Clear out all the saved state
        savedStates = new ArrayDeque<>();
        return firstState;
    }

    @Override
    public KeyValStore rollback() {
        if (savedStates.size() == 0) {
            return null;
        }
        return savedStates.pollLast();
    }

    @Override
    public Iterator iterator() {
        return savedStates.descendingIterator();

    }

}
