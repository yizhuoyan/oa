package com.neusoft.oa.core.dto;

import javafx.scene.shape.VLineTo;

import javax.naming.OperationNotSupportedException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2017/10/23.
 */
public class VOMap implements Map<String, Object> {

    private class KeyValue implements Map.Entry<String,Object>{
        private final String key;
        private final  Object value;

        public KeyValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            return null;
        }
    }
    private List<KeyValue> data;

    public VOMap(int initialCapacity) {
        this.data = new ArrayList<>(initialCapacity);
    }

    public VOMap(Map<? extends String, ?> m) {
        this(m.size());
        this.putAll(m);
    }

    public static VOMap of(int size) {
        return new VOMap(size);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object get(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VOMap put(String key, Object value) {
        if (key != null && (key = key.trim()).length() != 0 && value != null) {
            this.data.add(new KeyValue(key, value));
        }
        return this;
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set keySet() {
        return this.data.stream().map(this::get).collect(Collectors.toSet());
    }

    @Override
    public Collection values() {
        throw new UnsupportedOperationException();
    }
    private class KeyValueIterator implements  Iterator<Entry<String, Object>>{
        private final List<KeyValue> list;
        private int  size;

        public KeyValueIterator(List<KeyValue> list) {
            this.list = list;
            this.size=list.size()-1;
        }
        @Override
        public boolean hasNext() {
            return size>=0;
        }
        @Override
        public Entry<String, Object> next() {
            return list.get(size--);
        }
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return  new AbstractSet<Entry<String,Object>>(){
            @Override
            public Iterator<Entry<String, Object>> iterator() {
                return new KeyValueIterator(data);
            }
            @Override
            public int size() {
                return VOMap.this.size();
            }
        };
    }
    @SuppressWarnings("unchecked")
    public <T> T getAs(String key) {
        return (T) this.get(key);
    }
    @SuppressWarnings("unchecked")
    public <T> T getAs(String key, T defaultValue) {
        return (T) this.getOrDefault(key, defaultValue);
    }
}
