package edu.aku.omarshoaib.renew.global.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import edu.aku.omarshoaib.renew.R;

public class AppButton extends AppCompatButton {

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        AppCompatButton button = (AppCompatButton) View.inflate(context, R.layout.view_button, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppButton, 0, 0);

        try {
            int btnId = typedArray.getResourceId(R.styleable.AppButton_btnId, 0);
            Drawable btnBackground = typedArray.getDrawable(R.styleable.AppButton_btnBackground);
            String btnText = typedArray.getString(R.styleable.AppButton_btnText);
            int btnTextColor = typedArray.getColor(R.styleable.AppButton_btnTextColor, ContextCompat.getColor(context, R.color.background_color));
            boolean btnEnabled = typedArray.getBoolean(R.styleable.AppButton_btnEnabled, true);
            int btnPaddingStart = (int) typedArray.getDimension(R.styleable.AppButton_btnPaddingStart, context.getResources().getDimension(com.intuit.sdp.R.dimen._20sdp));
            int btnPaddingTop = (int) typedArray.getDimension(R.styleable.AppButton_btnPaddingTop, context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp));
            int btnPaddingEnd = (int) typedArray.getDimension(R.styleable.AppButton_btnPaddingEnd, context.getResources().getDimension(com.intuit.sdp.R.dimen._20sdp));
            int btnPaddingBottom = (int) typedArray.getDimension(R.styleable.AppButton_btnPaddingBottom, context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp));

            button.setId(btnId);
            button.setBackground(btnBackground);
            button.setText(btnText);
            button.setTextColor(btnTextColor);
            button.setEnabled(btnEnabled);
            button.setPadding(btnPaddingStart, btnPaddingTop, btnPaddingEnd, btnPaddingBottom);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            typedArray.recycle();
        }
    }
}
