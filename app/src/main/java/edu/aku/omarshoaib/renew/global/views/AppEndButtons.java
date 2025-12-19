package edu.aku.omarshoaib.renew.global.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;

public class AppEndButtons extends LinearLayout implements View.OnClickListener {
    private final Context context;
    private String btnPosClick, btnNegClick;

    public AppEndButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_end_buttons, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppEndButtons, 0, 0);

        try {
            Drawable btnPosDefaultBG = ContextCompat.getDrawable(context, R.drawable.bg_rounded_app_dark_filled);
            Drawable btnNegDefaultBG = ContextCompat.getDrawable(context, R.drawable.bg_rounded_red_filled);
            String btnPosDefaultClick = "btnContinue";
            String btnNegDefaultClick = "btnEnd";

            /*Positive Button*/
            Button posBtn = view.findViewById(R.id.posBtn);
            Drawable btnPosBG = typedArray.getDrawable(R.styleable.AppEndButtons_btnPosBackground);
            String btnPosText = typedArray.getString(R.styleable.AppEndButtons_btnPosText);
            int btnPosTextColor = typedArray.getColor(R.styleable.AppEndButtons_btnPosTextColor, -1);
            int btnPosVisible = typedArray.getInt(R.styleable.AppEndButtons_btnPosVisible, 0);
            boolean btnPosEnabled = typedArray.getBoolean(R.styleable.AppEndButtons_btnPosEnabled, true);
            btnPosClick = typedArray.getString(R.styleable.AppEndButtons_btnPosClick);
            btnPosClick = btnPosClick != null ? btnPosClick : btnPosDefaultClick;

            posBtn.setBackground(btnPosBG != null ? btnPosBG : btnPosDefaultBG);
            if (btnPosText != null)
                posBtn.setText(btnPosText);
            if (btnPosTextColor != -1)
                posBtn.setTextColor(btnPosTextColor);
            posBtn.setVisibility(btnPosVisible == -1 ? View.INVISIBLE : View.VISIBLE);
            posBtn.setEnabled(btnPosEnabled);
            posBtn.setOnClickListener(this);

            /*Negative Button*/
            Button negBtn = view.findViewById(R.id.negBtn);
            Drawable btnNegBG = typedArray.getDrawable(R.styleable.AppEndButtons_btnNegBackground);
            String btnNegText = typedArray.getString(R.styleable.AppEndButtons_btnNegText);
            int btnNegTextColor = typedArray.getColor(R.styleable.AppEndButtons_btnNegTextColor, -1);
            int btnNegVisible = typedArray.getInt(R.styleable.AppEndButtons_btnNegVisible, 0);
            boolean btnNegEnabled = typedArray.getBoolean(R.styleable.AppEndButtons_btnNegEnabled, true);
            btnNegClick = typedArray.getString(R.styleable.AppEndButtons_btnNegClick);
            btnNegClick = btnNegClick != null ? btnNegClick : btnNegDefaultClick;

            negBtn.setBackground(btnNegBG != null ? btnNegBG : btnNegDefaultBG);
            if (btnNegText != null)
                negBtn.setText(btnNegText);
            if (btnNegTextColor != -1)
                negBtn.setTextColor(btnNegTextColor);
            negBtn.setVisibility(btnNegVisible == -1 ? View.INVISIBLE : View.VISIBLE);
            negBtn.setEnabled(btnNegEnabled);
            negBtn.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.posBtn) {
            // Positive Button Click
            AppConstants.callMethod((Activity) context, view, btnPosClick);
        } else if (viewId == R.id.negBtn) {
            // Negative Button Click
            AppConstants.callMethod((Activity) context, view, btnNegClick);
        }
    }
}
