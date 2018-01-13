package com.ce.ems.base.core;

import java.util.HashMap;
import java.util.Map;

@Todo("Investigate how vertc threadpool affects this.")
public class RequestThreadAttributes {
	
	private static final RequestThreadAttributes instance = new RequestThreadAttributes();
	
    private static ThreadLocal<Map<String, Object>> threadAttrs = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static Object get(String key) {
        return threadAttrs.get().get(key);
    }

    public static RequestThreadAttributes set(String key, Object value) {
        threadAttrs.get().put(key, value);
        return instance;
    }
    
    public static RequestThreadAttributes remove(String key) {
        threadAttrs.get().remove(key);
        return instance;
    }
}
