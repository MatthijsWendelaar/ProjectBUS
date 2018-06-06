package name.wendelaar.projectbus.util;

import java.util.LinkedHashMap;

public class ChainedLinkedHashMap<K,V> extends LinkedHashMap<K,V> {

    public ChainedLinkedHashMap<K,V> add(K key, V value) {
        put(key, value);
        return this;
    }
}
