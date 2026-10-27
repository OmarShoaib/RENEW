package edu.aku.omarshoaib.renew.global.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import edu.aku.omarshoaib.renew.R;
//  ***Advantages***
//1)  best and biggest adv: will not have to type cast objects into Strings
//    its best to use this with models instead of strings
//2)  optional values and builder pattern no over load hell with possible 720 overloads
//3)  handles resetting selection on edit
//4)  handles show different value and save different value no need to display values as
//    (userId - UserName)
//5)  No need create User Obj with name Please Select
//6)  able to set Special values with both save and select options
public class SpinnerBuilder<T> {
    /**
     *
     * @param data List<T> can be String or user objects
     * @param setter pass the setter for setting Value
     * @param getter pass the getter for getting Value in EditMode
     * @param setValueAs perform operation on object in case value has to be save differently e.g
     *                   FamilyMember::getLineNo (saves line no in setter) OR
     *                   value -> value.split("-")[0].trim() (saves first part in setter)
     * @param displayValueAs perform operation on object in case value has to be displayed differently
     * @param hintText change selection number 0 text
     * @param notApplicableText Add a not applicable text in the end which saves value "97"
     *
     */

    /**
     * ***********Use Case**********
     * new SpinnerBuilder<>(activity, bi.spinnerId, List<T>, value -> sA2.setA213(value))
     *                 .setValueAs(fm -> String.valueOf(fm.getLineNo()))
     *                 .getter(() -> sA2.getA213())
     *                 .displayValueAs(FamilyMember::getName)
     *                 .hintText("Select an option")
     *                 .otherText("Others Specify")
     *                 .notApplicableText("Not Applicable")
     *                 .build();
     */
    // Mandatory fields
    private final Context context;
    private final Spinner spinner;
    private final List<T> data;
    private final Consumer<String> setter;

    // Optional fields
    private Function<T, String> setValueAs;
    private Supplier<String> getter;
    private Function<T, String> displayValueAs;
    private String hintText = "Please select";
    private String notApplicableText;
    private final String notApplicableValue = "97";
    private String otherText;
    private final String otherValue = "96";

    // Constructor with mandatory fields
    public SpinnerBuilder(Context context, Spinner spinner, List<T> data, Consumer<String> setter) {
        this.context = context;
        this.spinner = spinner;
        this.data = data;
        this.setter = setter;
    }

    public SpinnerBuilder<T> setValueAs(Function<T, String> setValueAs) {
        this.setValueAs = setValueAs;
        return this;
    }

    public SpinnerBuilder<T> getter(Supplier<String> getter) {
        this.getter = getter;
        return this;
    }

    public SpinnerBuilder<T> displayValueAs(Function<T, String> displayValueAs) {
        this.displayValueAs = displayValueAs;
        return this;
    }

    public SpinnerBuilder<T> hintText(String hintText) {
        this.hintText = hintText;
        return this;
    }

    public SpinnerBuilder<T> notApplicableText(String specialText) {
        this.notApplicableText = specialText;
        return this;
    }

    public SpinnerBuilder<T> otherText(String otherText) {
        this.otherText = otherText;
        return this;
    }

    public void build() {
        List<Object> list = new ArrayList<>();
        list.add(hintText);
        list.addAll(data);

        if (otherText != null) list.add(otherText);

        if (notApplicableText != null) list.add(notApplicableText);


        ArrayAdapter<Object> adapter = getArrayAdapter(list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setter.accept("");
                    return;
                }
                if (notApplicableText != null &&
                        position == list.size() - 1 &&
                        list.get(position) instanceof String) {
                    setter.accept(notApplicableValue);
                    return;
                }
                if (otherText != null &&
                        position == list.size() - 2 &&
                        list.get(position) instanceof String) {
                    setter.accept(otherValue);
                    return;
                }
                T selected = (T) list.get(position);
                if (setValueAs != null)
                    setter.accept(setValueAs.apply(selected));
                else
                    setter.accept(selected.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (getter != null) {
            String savedValue = getter.get();
            if (savedValue == null || savedValue.isEmpty()) {
                spinner.setSelection(0);
                return;
            }
            if (savedValue.equals(notApplicableValue)) {
                spinner.setSelection(list.size() - 1);
                return;
            }
            if (savedValue.equals(otherValue)) {
                spinner.setSelection(list.size() - 2);
                return;
            }
            for (int i = 1; i < list.size(); i++) {
                if (notApplicableText != null && i == list.size() - 1) continue;
                if (otherText != null && i == list.size() - 2) continue;
                Object obj = list.get(i);
                String compareValue = setValueAs != null ? setValueAs.apply((T) obj) : obj.toString();
                if (savedValue.equals(compareValue)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    @NonNull
    private ArrayAdapter<Object> getArrayAdapter(List<Object> list) {
        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                context,
                R.layout.item_spinner,
                list
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setText(hintText);
                } else if (notApplicableText != null &&
                        position == list.size() - 1 &&
                        list.get(position) instanceof String) {
                    tv.setText(notApplicableText);
                } else if (otherText != null &&
                        position == list.size() - 2 &&
                        list.get(position) instanceof String) {
                    tv.setText(otherText);
                } else {
                    T item = (T) list.get(position);
                    tv.setText(displayValueAs != null ? displayValueAs.apply(item) : item.toString());
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                return getView(position, convertView, parent);
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
