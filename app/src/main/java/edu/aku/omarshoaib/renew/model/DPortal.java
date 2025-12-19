package edu.aku.omarshoaib.renew.model;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SharedPrefs;

/**
 * DICTIONARY PORTAL DATA
 * DYNAMIC LABELS AND RANGES
 */

public class DPortal {

    private static DPortal instance;

    public static DPortal getInstance() {
        if (instance == null) {
            instance = new DPortal();
        }
        return instance;
    }

    public static void setInstance(DPortal instance) {
        DPortal.instance = instance;
    }

    // =============================
    // For getting strings labels
    // =============================

    public static class LabelData {
        private String langCode;
        private Map<String, String> labels; // Using Map<String, String> for the inner "labels" object

        // Getters
        public String getLangCode() {
            return langCode;
        }

        public Map<String, String> getLabels() {
            return labels;
        }

        // You can optionally add setters if you need to build these objects programmatically
        public void setLangCode(String langCode) {
            this.langCode = langCode;
        }

        public void setLabels(Map<String, String> labels) {
            this.labels = labels;
        }

        @NonNull
        @Override
        public String toString() {
            return "LanguageData{" +
                    "langCode='" + langCode + '\'' +
                    ", labels=" + labels +
                    '}';
        }
    }

    // =============================
    // For getting field ranges
    // =============================

    private Map<String, RangeData> rangesMap;

    // Get rangesMap
    public Map<String, RangeData> getRangesMap() {
        return rangesMap;
    }

    // Set ranges in hashmap like <label, rangesData>
    public void setRanges(List<RangeData> rangesList) {
        if (rangesMap == null) rangesMap = new HashMap<>();
        else rangesMap.clear();

        if (rangesList != null) {
            for (RangeData range : rangesList) {
                rangesMap.put(range.getLabel(), range);
            }
        }
    }

    // Get Specific label Range
    public RangeData getRange(String label) {
        return rangesMap.get(label);
    }

    // Load rangesMap from SharedPrefs
    // This will be useful when the app restarts
    public Map<String, RangeData> loadFromSharedPrefs() {
        if (rangesMap == null) rangesMap = new HashMap<>();
        else rangesMap.clear();
        String json = SharedPrefs.read(SharedPrefs.FIELD_RANGES, null);
        if (json != null) {
            Type type = new TypeToken<Map<String, RangeData>>() {}.getType();
            Map<String, RangeData> savedMap = MainApp.gson.fromJson(json, type);
            if (savedMap != null) {
                rangesMap.clear();
                rangesMap.putAll(savedMap);
            }
        }
        return rangesMap;
    }

    public static class RangeData {
        private String label;
        private String minValue;
        private String maxValue;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getMinValue() {
            return minValue;
        }

        public void setMinValue(String minValue) {
            this.minValue = minValue;
        }

        public String getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(String maxValue) {
            this.maxValue = maxValue;
        }

        @NonNull
        @Override
        public String toString() {
            return "RangesData{" +
                    "label='" + label + '\'' +
                    ", minValue=" + minValue +
                    ", maxValue=" + maxValue +
                    '}';
        }
    }
}
