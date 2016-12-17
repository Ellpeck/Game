package de.ellpeck.game.util;

import java.util.HashMap;
import java.util.Map;

public class NameRegistry<T>{

    private final Map<String, T> registry = new HashMap<>();

    public T get(String key){
        if(this.registry.containsKey(key)){
            return this.registry.get(key);
        }
        else{
            return null;
        }
    }

    public void register(String key, T value){
        if(value == null){
            throw new UnsupportedOperationException("Tried to register a null value with key "+key+"! This is not allowed!");
        }
        else{
            T stored = this.get(key);
            if(stored != null){
                throw new UnsupportedOperationException("Tried to register "+value+" with key "+key+" which is already present, storing "+stored+"!");
            }
            else{
                this.registry.put(key, value);
            }
        }
    }
}
