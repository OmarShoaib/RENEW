package edu.aku.omarshoaib.renew.activity.sections.child;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.validatorcrawler.aliazaz.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import edu.aku.omarshoaib.renew.activity.ImageViewerAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF04Binding;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.ImageUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form4;
import edu.aku.omarshoaib.renew.model.HCF;

public class SectionF04 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF04.this;

    ActivitySectionF04Binding bi;
    private AppDatabase appDatabase;
    private static final int VIEW_IMAGE_REQ_CODE = 1;
    private Form4.SF4 sF4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f04);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f4t0), getString(R.string.f4t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF4 = Form4.SF4.getData();
        sF4 = sF4 == null ? new Form4.SF4() : sF4;
        bi.setForm(sF4);
        initUI();
    }

    private void initUI() {
        sF4.setF401(MainApp.form4.getUsername());
        HCF hcf = appDatabase.hcfDao().getHcfbyCode(sF4.getF403().trim());
        bi.f403.setText(hcf == null ? "" : hcf.getHfName());
        bi.f409.setOnCheckedChangeListener((group, checkedId) -> group.post(this::viewF411));
        bi.f410.addTextChangedListener(new AppTextWatcher(bi.f410.getId(), (viewId, text) -> viewF411()));
    }

    private void viewF411() {
        if (verifyCrieteria()) bi.fldGrpCVf411.setVisibility(View.VISIBLE);
        else {
            bi.fldGrpCVf411.setVisibility(View.GONE);
            sF4.setF411(_EMPTY_);
        }
    }

    private boolean verifyCrieteria() {
        boolean lowMuac = !sF4.getF410().isEmpty() && Float.parseFloat(sF4.getF410()) < 12.5f;
        boolean edema = sF4.getF409().equals("1"); //&& !sF4.getF409a().equals("3");
        return !sF4.getF409a().equals("3") && (lowMuac || edema);
    }

    private boolean formValidation() {
        if(!Validator.emptyCheckingContainer(activity, bi.GrpName)) return false;

        if(!sF4.getF410().isEmpty() && sF4.getMuacImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "MUAC"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        return true;
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form4.saveMainData(MainApp.form4.getScrId());
        if(MainApp.form4.getIStatus().equals(""))
            MainApp.form4.setIStatus(sF4.getF411().equals("1") ? "" : "1");
        MainApp.form4.setEndingDate(DateUtils.getCurrentDateTime());
        Form4.SF4.saveData(sF4);
        AppConstants.gotoActivity(activity, sF4.getF411().equals("1") && !sF4.getF409a().equals("3")?
                SectionF05.class : MainActivity.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }



    /**
     * TAKE PHOTO
     */
    public void takePhoto(View view) {
        ImagePicker.with(activity).maxResultSize(512, 512).saveDir(AppConstants.GALLERY_DIR).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIEW_IMAGE_REQ_CODE) {
            if (resultCode == RESULT_OK) return;
            sF4.setMuacImage("");
        } else {
            if (resultCode == RESULT_OK) {
                String imageName;
                imageName = ImageUtils.generateImageName(TAG, "Muac");
                sF4.setMuacImage(imageName);


                Uri uri = Objects.requireNonNull(data).getData();
                File file = new File(Objects.requireNonNull(uri).getPath());
                ImageUtils.renameTo(activity, file.getName(), imageName);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                AppConstants.showSimpleSnackBar(activity, ImagePicker.getError(data), AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            } else {
                AppConstants.showSimpleSnackBar(activity, getString(R.string.image_not_taken), AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            }
        }
    }

    public void viewPhoto(View view) {
        String[] imageNames = getStrings(view);

        // Get matching image files
        ArrayList<File> matchingImages = ImageUtils.getImageFilesByNames(activity, imageNames);
        if (matchingImages == null || matchingImages.isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "MUAC"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return;
        }

        // Create an intent to start the ImageViewerActivity
        Intent intent = new Intent(activity, ImageViewerAC.class);
        intent.putExtra("image_files", matchingImages);

        activity.startActivityForResult(intent, VIEW_IMAGE_REQ_CODE);
    }

    private String[] getStrings(View view) {
        String[] imageNames;
        imageNames = new String[]{sF4.getMuacImage()};
        return imageNames;
    }
}