package de.ellpeck.game.util;

import java.util.HashMap;
import java.util.Map;

public class RegistryIndexed<T>{

    private final Map<Integer, T> registry = new HashMap<>();

    public T get(int id){
        if(this.registry.containsKey(id)){
            return this.registry.get(id);
        }
        else{
            return null;
        }
    }

    public void register(int id, T value){
        if(value == null){
            throw new UnsupportedOperationException("Tried to register a null value with id "+id+"! This is not allowed!");
        }
        else{
            T stored = this.get(id);
            if(stored != null){
                throw new UnsupportedOperationException("Tried to register "+value+" with id "+id+" which is already present containing "+stored+"!");
            }
            else{
                this.registry.put(id, value);
            }
        }
    }
}
