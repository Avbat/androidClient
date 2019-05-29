package io.cell.androidclient.utils.typeAdapters;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Адаптер для десериализаии {@link TreeMap} по средствам {@link Gson}
 */
public class TreeMapTypeAdapterFactory implements TypeAdapterFactory {


    public static final TreeMapTypeAdapterFactory INSTANCE = new TreeMapTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!TreeMap.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {
            @Override
            public void write(com.google.gson.stream.JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            @SuppressWarnings("unchecked")
            public T read(com.google.gson.stream.JsonReader in) throws IOException {
                return (T) new TreeMap<>((Map) delegate.read(in));
            }
        };
    }

    public static <K, V> InstanceCreator<Map<K, V>> newCreator() {
        return new InstanceCreator<Map<K, V>>() {
            @Override
            public Map<K, V> createInstance(Type type) {
                return new TreeMap<K, V>();
            }
        };
    }
}
