package edu.aku.omarshoaib.renew.synced_recs;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import edu.aku.omarshoaib.renew.global.MainApp;

/* This is currently used for Synced Recs.
 * The case is we need to use class variable instead of
 * SerializedName when converting from Object to json from GSON */

/* The local db/model has some variable names different from the server variables
 * that is why we need this custom serializer and deserializer */

public class GSONTypedAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {
    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        for (Field field : src.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(src);

                if (value != null) {
                    String fieldName = getFieldName(field, true, false);
                    // TABLE_NAME, SECTION_NAME or in future if any field that needs
                    // to be excluded for serialization i.e. column not exists in
                    // localdb. Add here
                    if (fieldName.equals("TABLE_NAME") || fieldName.equals("SECTION_NAME")
                            || fieldName.equals("SYNCED_RECS_ITEMS"))
                        continue;
                    // For Synced Recs we explicitly need to set isFormCompleteOnce true
                    // to show skipToEnd button
                    if (fieldName.equals("isFormCompleteOnce")) {
                        jsonObject.add(fieldName, context.serialize(true));
                        continue;
                    }
                    // We explicitly insert empty value on id field while converting it
                    // to json because of unique constraints type type of id id long
                    // so it was by default 0.
                    if (fieldName.equals("id") || fieldName.equals("_id")) {
                        jsonObject.add(fieldName, context.serialize(0));
                        continue;
                    }
                    if (field.getType().isMemberClass()) {
                        // If the field is an inner class, serialize it recursively
                        // Create inner object as json
                       /* JSONObject innerObject = new JSONObject();
                        innerObject.put(fieldName, "\"" + MainApp.gson.toJson(value) + "\"");*/

                        JsonElement innerElement = context.serialize(context.serialize(MainApp.gson.toJson(value)));
                        jsonObject.add(fieldName, innerElement);
                    } else {
                        jsonObject.add(fieldName, context.serialize(value));
                        // Invoke/Call setter method to set SyncedRecs object data
                        // for displaying in SyncedRecsList. The SyncedRecs object setters
                        // are set in the object(src) respective fields setters that we need
                        // to show on display list
//                        invokeSetter(src, field, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<?> clazz = (Class<?>) typeOfT;
        // This boolean is used to check whether this deserialization i.e. json to object
        // is from the server json or its from the local data i.e. is converted to json.
        boolean isLocalDeserialization = false;
        try {
            if (jsonObject.has("syncDate"))
                // This is local db's json deserialization
                isLocalDeserialization = true;

            Object instance = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                // Check both field name and SerializedName annotation
                String fieldName = getFieldName(field, false, isLocalDeserialization);
                // Check if field name is syncDate or sync_date
                // then store col_dt in syncDate column for checking synced
                // conditions in the app
                if (fieldName.equals("syncDate") || fieldName.equals("sync_date")) {
                    JsonElement element/* = jsonObject.getAsJsonObject("col_dt").get("date")*/;
                    if (jsonObject.has("col_dt"))
                        // This extra json object is because col_dt is a Json Object not a String
                        element = jsonObject.getAsJsonObject("col_dt").get("date");
                    else element = jsonObject.get(field.getName());
                    field.set(instance, context.deserialize(element, field.getType()));
                    continue;
                }
                if (jsonObject.has(fieldName) && !field.getType().isMemberClass()) {
                    // id is auto generated field in local db so we exclude
                    // it from being deserialized i.e. json to object
                    if (fieldName.equals("id") || fieldName.equals("_id")) continue;
                    // Otherwise, deserialize normally
                    JsonElement element = jsonObject.get(fieldName);
                    field.set(instance, context.deserialize(element, field.getType()));
                } else if (field.getType().isMemberClass()) {
                    // If the field is an inner class, deserialize it recursively
//                    JsonElement innerElement = jsonObject.get(fieldName);
//                    Object innerInstance = context.deserialize(innerElement, field.getType());
                    Object innerInstance/* = MainApp.gson.fromJson(jsonObject.toString(), field.getType())*/;
                    if (jsonObject.has(fieldName)) {
                        // If inner exists i.e. for the local db record
                       /* JsonElement innerJson = jsonObject.get(fieldName);
                        Class<?> innerClazz = field.getType();*/
                        String innerJson = jsonObject.get(fieldName).toString()
                                .replaceAll("^\"|\"$", "")
                                .replaceAll("\\\\", "");
                        innerInstance = MainApp.gson.fromJson(innerJson, field.getType());
                    } else
                        // If inner exists i.e. for the server db record
                        innerInstance = MainApp.gson.fromJson(jsonObject.toString(), field.getType());
                    field.set(instance, innerInstance);
                }
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JsonParseException("Error creating object", e);
        }
    }

    // For Serialization
    private String getFieldName(Field field, boolean isSerialized, boolean isLocalDeserialization) {
        if (isSerialized || isLocalDeserialization)
            return field.getName();
        else {
            // For Deserialization
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            return (serializedName != null) ? serializedName.value() : field.getName();
        }
    }

    /*// This is used to call setter of the fields when serializing
    private void invokeSetter(Object object, Field field, Object value) {
        try {
            String fieldName = field.getName();
            // This check is only for boolean setters
            // i.e. boolean variable like isError, its setter is setError() NOT setIsError()
            // so we remove 'is' substring (if exists) from the field name
            if (field.getType() == boolean.class && fieldName.substring(0, 2).equalsIgnoreCase("is"))
                fieldName = fieldName.substring(2);
            String setterMethodName = "set" + AppConstants.capitalizeFirstChar(fieldName);
            Method setterMethod = object.getClass().getDeclaredMethod(setterMethodName, field.getType());
            setterMethod.invoke(object, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }*/
}
