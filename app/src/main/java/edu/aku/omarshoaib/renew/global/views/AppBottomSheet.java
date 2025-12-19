package edu.aku.omarshoaib.renew.global.views;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import edu.aku.omarshoaib.renew.R;

public class AppBottomSheet {
    public static BottomSheetDialog getBottomSheetDialog(Activity activity, int theme, int layout, boolean isPeekHeight, IBottomSheetEventCallback iBottomSheetEventCallback) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, theme);

        if (isPeekHeight) {
            BottomSheetBehavior<FrameLayout> bottomSheetDialogBehavior = bottomSheetDialog.getBehavior();
            bottomSheetDialogBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels, true);
        }

        bottomSheetDialog.setContentView(layout);

        Button bsCloseBtn = bottomSheetDialog.findViewById(R.id.bsCloseBtn);
        assert bsCloseBtn != null;
        bsCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iBottomSheetEventCallback == null) bottomSheetDialog.dismiss();
                else iBottomSheetEventCallback.onDismiss(bottomSheetDialog);
            }
        });
        return bottomSheetDialog;
    }

    public interface IBottomSheetEventCallback {
        void onDisplay(BottomSheetDialog bottomSheetDialog);

        void onDismiss(BottomSheetDialog bottomSheetDialog);
    }
}
