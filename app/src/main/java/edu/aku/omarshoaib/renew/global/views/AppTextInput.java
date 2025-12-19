package edu.aku.omarshoaib.renew.global.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import edu.aku.omarshoaib.renew.R;

public class AppTextInput extends TextInputLayout {

    private TextInputLayout defaultTI;
    private EditText defaultET;

    public AppTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_textinput, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppTextInput, 0, 0);

        try {
            int defaultTiId = typedArray.getResourceId(R.styleable.AppTextInput_tiId, 0);
            String tiHint = typedArray.getString(R.styleable.AppTextInput_tiHint);
            Drawable tiDrawableStart = typedArray.getDrawable(R.styleable.AppTextInput_tiStartIconDrawable);
            int tiInputType = typedArray.getInt(R.styleable.AppTextInput_android_inputType, InputType.TYPE_CLASS_TEXT);
            int tiImeOption = typedArray.getInt(R.styleable.AppTextInput_android_imeOptions, EditorInfo.IME_ACTION_DONE);
            int tiMaxLength = typedArray.getInt(R.styleable.AppTextInput_tiMaxLength, 100);
            int tiMaxLines = typedArray.getInt(R.styleable.AppTextInput_tiMaxLines, 1);
            float tiMinHeight = typedArray.getDimension(R.styleable.AppTextInput_tiMinHeight, 0f);
            int tiGravity = typedArray.getInt(R.styleable.AppTextInput_android_gravity, Gravity.CENTER_VERTICAL);
            boolean tiEnabled = typedArray.getBoolean(R.styleable.AppTextInput_tiEnabled, true);
            boolean tiFocusable = typedArray.getBoolean(R.styleable.AppTextInput_tiFocusable, true);

            defaultTI = (TextInputLayout) view.findViewById(R.id.titleTIL);
            defaultTI.setHint(tiHint);
            if (tiDrawableStart != null)
                defaultTI.setStartIconDrawable(tiDrawableStart);

            defaultET = view.findViewById(R.id.defaultET);
            defaultET.setId(defaultTiId);
            defaultET.setInputType(tiInputType);
            defaultET.setImeOptions(tiImeOption);
            defaultET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(tiMaxLength)});
            defaultET.setMaxLines(tiMaxLines);
            defaultET.setGravity(tiGravity);
            defaultET.setEnabled(tiEnabled);
            defaultET.setFocusable(tiFocusable);

            if (tiMinHeight > 0) defaultET.setMinHeight((int) tiMinHeight);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    public TextInputLayout getDefaultTI() {
        return defaultTI;
    }

    public EditText getDefaultET() {
        return defaultET;
    }
}
