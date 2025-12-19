package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import edu.aku.omarshoaib.renew.R;

public class AlertPopup {

    /**
     * ====================
     * SIMPLE ALERT DIALOG
     * ====================
     */

    // Simple Alert Dialog
    public static void alert(Activity activity, String title, String message, int type) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.view_alert, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        TextView titleTV = dialogView.findViewById(R.id.titleTV);
        titleTV.setText(title);

        setDialogViewByType(activity, titleTV, type);

        TextView dialogMessageTV = dialogView.findViewById(R.id.messageTV);
        dialogMessageTV.setText(message);

        Button positiveBtn = dialogView.findViewById(R.id.posBtn);
        positiveBtn.setVisibility(View.VISIBLE);

        Button negativeBtn = dialogView.findViewById(R.id.negBtn);
        negativeBtn.setVisibility(View.GONE);

        // To prevent error on show dialog after activity is finishing or destroyed
        if (activity.isFinishing() || activity.isDestroyed()) return;

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        positiveBtn.setOnClickListener(view -> {
            dialog.cancel();
        });

        dialog.show();
    }

    /**
     * ========================================
     * SIMPLE ALERT DIALOG WITH CUSTOM ACTIONS
     * ========================================
     */

    public static void alert(int popupId, Activity activity, String title, String message,
                             int type, String positiveBtnText, Object pasObj,
                             Callbacks.IAlertCallback iAlertCallback) {
        alert(popupId, activity, title, message, type, positiveBtnText,
                null, pasObj, iAlertCallback);
    }

    public static void alert(int popupId, Activity activity, String title, String message,
                             int type, String positiveBtnText,
                             Callbacks.IAlertCallback iAlertCallback) {
        alert(popupId, activity, title, message, type, positiveBtnText,
                null, null, iAlertCallback);
    }

    public static void alert(int popupId, Activity activity, String title, String message,
                             int type, String positiveBtnText, String negativeBtnText,
                             Callbacks.IAlertCallback iAlertCallback) {
        alert(popupId, activity, title, message, type, positiveBtnText,
                negativeBtnText, null, iAlertCallback);
    }

    public static void alert(int popupId, Activity activity, String title, String message,
                             int type, String positiveBtnText, String negativeBtnText,
                             Object passObj, Callbacks.IAlertCallback iAlertCallback) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.view_alert, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);

        TextView titleTV = dialogView.findViewById(R.id.titleTV);
        titleTV.setText(title);

        setDialogViewByType(activity, titleTV, type);

        TextView dialogMessageTV = dialogView.findViewById(R.id.messageTV);
        dialogMessageTV.setText(message);

        Button positiveBtn = dialogView.findViewById(R.id.posBtn);
        positiveBtn.setText(positiveBtnText);
        positiveBtn.setVisibility(View.VISIBLE);

        Button negativeBtn = dialogView.findViewById(R.id.negBtn);
        negativeBtn.setVisibility(View.GONE);

        // To prevent error on show dialog after activity is finishing or destroyed
        if (activity.isFinishing() || activity.isDestroyed()) return;

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        positiveBtn.setOnClickListener(view -> {
            dialog.cancel();
            iAlertCallback.onClick(popupId, true, passObj);
        });

        if (!AppConstants.isEmpty(negativeBtnText)) {
            negativeBtn.setText(negativeBtnText);
            negativeBtn.setVisibility(View.VISIBLE);
            negativeBtn.setOnClickListener(view -> {
                dialog.cancel();
                iAlertCallback.onClick(popupId, false, passObj);
            });
        }

        dialog.show();
    }

    // Set Alert dialog UI based on the type
    private static void setDialogViewByType(Activity activity, TextView titleTV, int type) {
        if (type == AppConstants.TYPE_SUCCESS)
            titleTV.setTextColor(ContextCompat.getColor(activity, R.color.success_color));
        else if (type == AppConstants.TYPE_ERROR)
            titleTV.setTextColor(ContextCompat.getColor(activity, R.color.error_color));
        else if (type == AppConstants.TYPE_WARNING)
            titleTV.setTextColor(ContextCompat.getColor(activity, R.color.warning_color));
        else
            titleTV.setTextColor(ContextCompat.getColor(activity, R.color.info_color));
    }

}
