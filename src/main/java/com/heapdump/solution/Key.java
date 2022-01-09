package com.heapdump.solution;
public class Key {
    public static String key;
    
    public Key(String key) {
        Key.key = key;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    public boolean equals(String str) {
        return key.equals(str);
    }
}