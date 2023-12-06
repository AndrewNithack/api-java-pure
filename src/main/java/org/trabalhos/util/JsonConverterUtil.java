package org.trabalhos.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConverterUtil {
    private JsonConverterUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T convertFromJson(String json, Class<T> clazz) throws ReflectiveOperationException {
        T obj = clazz.getDeclaredConstructor().newInstance();
        json = json.trim();
        String jsonContent = json.substring(1, json.length() - 1); // Remove the curly braces
        jsonContent = jsonContent.replaceAll("\"", ""); // Remove quotes
        String[] keyValuePairs = jsonContent.split(",");

        Map<String, String> jsonElements = new HashMap<>();
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            jsonElements.put(entry[0].trim(), entry[1].trim());
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = jsonElements.get(fieldName);

            if (value != null) {
                Class<?> fieldType = field.getType();

                if (fieldType.equals(String.class)) {
                    field.set(obj, value);
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    field.set(obj, Double.parseDouble(value));
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    field.set(obj, Integer.parseInt(value));
                } else {
                    throw new IllegalArgumentException("Tipo não mapeado para converção");
                }
            }
        }
        return obj;
    }

    public static String convertArrayToJson(List objects) throws ReflectiveOperationException {
        String[] jsonObjects = new String[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            jsonObjects[i] = convertToJson(objects.get(i));
        }
        return "[" + String.join(",", jsonObjects) + "]";
    }

    public static String convertToJson(Object object) throws ReflectiveOperationException {
        Map<String, String> jsonElements = new HashMap<>();
        Method[] methods = object.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (isGetter(method)) {
                String propertyName = extractPropertyNameFromGetter(method.getName());
                Object value = method.invoke(object);
                if (value != null) {
                    jsonElements.put(propertyName, value.toString());
                }
            }
        }
        return toJsonString(jsonElements);
    }

    private static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) return false;
        if (method.getParameterTypes().length != 0) return false;
        if (void.class.equals(method.getReturnType())) return false;
        return true;
    }

    private static String extractPropertyNameFromGetter(String getterName) {
        return Character.toLowerCase(getterName.charAt(3)) + getterName.substring(4);
    }

    private static String toJsonString(Map<String, String> jsonMap) {
        StringBuilder elementsString = new StringBuilder();
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            if (!elementsString.isEmpty()) {
                elementsString.append(",");
            }
            elementsString.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
        }
        return "{" + elementsString + "}";
    }
}
