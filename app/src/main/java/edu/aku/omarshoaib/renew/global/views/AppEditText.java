package edu.aku.omarshoaib.renew.global.views;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;

public class AppEditText extends LinearLayout {
    private final LayoutInflater mInflater;
    private LinearLayout viewLayout;
    private TextView etTitleTV;

    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        View view = mInflater.inflate(R.layout.view_edittext, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppEditText, 0, 0);

        try {
            Drawable etLayoutBackground = typedArray.getDrawable(R.styleable.AppEditText_etLayoutBackground);
            float etLayoutMarginTop = typedArray.getDimension(R.styleable.AppEditText_etLayoutMarginTop, context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp));
            String etTitleText = typedArray.getString(R.styleable.AppEditText_etTitleText);
            int defaultETId = typedArray.getResourceId(R.styleable.AppEditText_etId, 0);
            float etMarginTop = typedArray.getDimension(R.styleable.AppEditText_etMarginTop, context.getResources().getDimension(com.intuit.sdp.R.dimen._8sdp));
            Drawable etDrawableEnd = typedArray.getDrawable(R.styleable.AppEditText_etDrawableEnd);
            Drawable etDrawableStart = typedArray.getDrawable(R.styleable.AppEditText_etDrawableStart);
            String etHint = typedArray.getString(R.styleable.AppEditText_etHint);
            int etInputType = typedArray.getInt(R.styleable.AppEditText_android_inputType, InputType.TYPE_CLASS_TEXT);
            int etImeOption = typedArray.getInt(R.styleable.AppEditText_android_imeOptions, EditorInfo.IME_ACTION_DONE);
            boolean etFocusable = typedArray.getBoolean(R.styleable.AppEditText_etFocusable, true);
            int etMaxLength = typedArray.getInt(R.styleable.AppEditText_etMaxLength, 100);
            int etMaxLines = typedArray.getInt(R.styleable.AppEditText_etMaxLines, 1);
            float etMinHeight = typedArray.getDimension(R.styleable.AppEditText_etMinHeight, 0f);
            int etGravity = typedArray.getInt(R.styleable.AppEditText_android_gravity, Gravity.CENTER_VERTICAL);
            boolean etEnabled = typedArray.getBoolean(R.styleable.AppEditText_etEnabled, true);
            boolean etCursorVisible = typedArray.getBoolean(R.styleable.AppEditText_etCursorVisible, true);
            boolean etTitleVisible = typedArray.getBoolean(R.styleable.AppEditText_etTitleVisible, true);

            viewLayout = view.findViewById(R.id.etLayout);
            MarginLayoutParams defaultVLLP = (MarginLayoutParams) viewLayout.getLayoutParams();
            defaultVLLP.topMargin = (int) etLayoutMarginTop;
            if (etLayoutBackground != null) viewLayout.setBackground(etLayoutBackground);

            etTitleTV = view.findViewById(R.id.etTitleTV);
            etTitleTV.setText(etTitleText);
            etTitleTV.setVisibility(etTitleVisible ? VISIBLE : GONE);

            EditText defaultET = view.findViewById(R.id.defaultET);
            defaultET.setId(defaultETId);
            defaultET.setHint(etHint);
            defaultET.setInputType(etInputType);
            defaultET.setImeOptions(etImeOption);
            defaultET.setFocusable(etFocusable);
            defaultET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etMaxLength)});
            defaultET.setMaxLines(etMaxLines);
            defaultET.setGravity(etGravity);
            defaultET.setEnabled(etEnabled);
            defaultET.setCursorVisible(etCursorVisible);

            if (etMinHeight > 0) defaultET.setMinHeight((int) etMinHeight);

            if (etDrawableEnd != null) {
                AppConstants.setDrawable((Activity) context, defaultET, Objects.requireNonNull(etDrawableEnd),
                        R.color.icon_color, AppConstants.POSITION_RIGHT);
            }

            if(etDrawableStart!=null){
                AppConstants.setDrawable((Activity) context, defaultET, Objects.requireNonNull(etDrawableEnd),
                        R.color.icon_color, AppConstants.POSITION_LEFT);
            }

            MarginLayoutParams defaultETLP = (MarginLayoutParams) defaultET.getLayoutParams();
            defaultETLP.topMargin = (int) etMarginTop;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    public void setEtLayoutBackground(Drawable etLayoutBackground) {
        viewLayout.setBackground(etLayoutBackground);
    }

    public void setTitle(String title) {
        etTitleTV.setText(title);
    }

    public void setEtDrawable(EditText edittext, Drawable etDrawableEnd) {
        edittext.setCompoundDrawablesWithIntrinsicBounds(null, null, etDrawableEnd, null);
    }
}
