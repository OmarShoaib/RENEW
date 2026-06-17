package edu.aku.omarshoaib.renew.activity.sections.child;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.validatorcrawler.aliazaz.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import edu.aku.omarshoaib.renew.activity.ImageViewerAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF05Binding;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.ImageUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form5;

public class SectionF05 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF05.this;

    ActivitySectionF05Binding bi;
    private AppDatabase appDatabase;
    private static final int VIEW_IMAGE_REQ_CODE = 1001;
    private static final int IMAGE_WEIGHT = 2;
    private static final int IMAGE_HEIGHT = 3;
    private int currentImageType = 0;
    private Form5.SF5 sF5;
    private List<RadioGroup> f0515RadioGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f05);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f5t0), getString(R.string.f5t1), false);
        appDatabase = AppDatabase.getDBInstance();

        MainApp.form5 = appDatabase.form5Dao().getDataByScrId(MainApp.user.getDistId(), MainApp.form4.getScrId());
        if (MainApp.form5 == null) Form5.initMeta();
        sF5 = Form5.SF5.getData();
        sF5 = sF5 == null ? new Form5.SF5() : sF5;
        bi.setForm(sF5);
        initUI();
    }

    private void initUI() {
        setChangeListeners();
        sF5.setF501(MainApp.user.getFullName() + " - " + MainApp.user.getUserId());
        sF5.setF502(MainApp.form4.getSF4().getF402());
        bi.f502.addTextChangedListener(new AppTextWatcher(bi.f502.getId(), dateTextWatcher));
        bi.f506.addTextChangedListener(new AppTextWatcher(bi.f506.getId(), dateTextWatcher));
        bi.f521a.addTextChangedListener(new AppTextWatcher(bi.f521a.getId(), dateTextWatcher));
//        bi.f512.addTextChangedListener(new AppTextWatcher(bi.f512.getId(),
//                (viewId, text) -> eligible()));
//        bi.fo515.setOnCheckedChangeListener((rG, i) -> rG.post(this::eligible));
        bi.f503.setOnCheckedChangeListener(f503Listener);
    }

    RadioGroup.OnCheckedChangeListener f503Listener = (RadioGroup radioGroup, int i) -> radioGroup.post(() -> {
        if (i == bi.f50301.getId()) {
            sF5.setF504(MainApp.form4.getSF4().getF405());
            sF5.setF511(MainApp.form4.getSF4().getF404());
            sF5.setF512(MainApp.form4.getSF4().getF410());
            sF5.setF512a(MainApp.form4.getSF4().getF410a());
            sF5.setFo515(MainApp.form4.getSF4().getF409());
            sF5.setF515a(MainApp.form4.getSF4().getF409a());
            float muac = Float.parseFloat(MainApp.form4.getSF4().getF410());
//            if(!sF5.getF515a().isEmpty() || muac < 12.5f) {
            String f515b = sF5.getFo515().equals("1") || muac < 11.5f ? "2" : "1";
            sF5.setF515b(f515b);
//            } else bi.fldGrpCVf515b.setVisibility(View.GONE);
            return;
        }
        sF5 = new Form5.SF5();
        sF5.setF501(MainApp.user.getFullName() + " - " + MainApp.user.getUserId());
        sF5.setF502(MainApp.form4.getSF4().getF402());
        sF5.setF503("2");
        bi.setForm(sF5);
    });

    AppTextWatcher.IAppTextWatcher dateTextWatcher = (viewId, text) -> {
        if (viewId == bi.f502.getId()) {
            sF5.setF506(_EMPTY_);
            if (text.isEmpty()) {
                String today = DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT);
                String maxDate1 = DateUtils.addSubMonths(today, -6);
                String minDate1 = DateUtils.addSubMonths(today, -59);
                bi.f506.setMinDate(minDate1);
                bi.f506.setMaxDate(maxDate1);
                return;
            }
            String maxDate2 = DateUtils.addSubMonths(text, -6);
            String minDate2 = DateUtils.addSubMonths(text, -59);
            bi.f506.setMinDate(minDate2);
            bi.f506.setMaxDate(maxDate2);
        } else if (viewId == bi.f506.getId()) {
            if (text.isEmpty()) {
                sF5.setF507dd("");
                sF5.setF507mm("");
                return;
            }
            String[] dob = text.split("-");
            List<String> age = DateUtils.calculateAge(sF5.getF502(), dob[0], dob[1], dob[2]);
            sF5.setF507dd(age.get(2));
            sF5.setF507mm(String.valueOf(DateUtils.getAgeInMonths(age.get(0), age.get(1))));
        }
    };

    private boolean formValidation() {
        if (!Validator.emptyCheckingContainer(activity, bi.GrpName)) return false;

        if(!sF5.getF513().isEmpty() && sF5.getWeightImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "WEIGHT"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if(!sF5.getFo514().isEmpty() && sF5.getHeightImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "HEIGHT"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if (sF5.getF521().equals("1"))
            if (Integer.parseInt(sF5.getF521a()) > Integer.parseInt(sF5.getF52101x())) {
                Validator.emptyCustomTextBox(activity, bi.f521a, "Given dose is greater from required dose");
                return false;
            }

        if (sF5.getF521().equals("2"))
            if (Integer.parseInt(sF5.getF521a()) > Integer.parseInt(sF5.getF52102x())) {
                Validator.emptyCustomTextBox(activity, bi.f521a, "Given dose is greater from required dose");
                return false;
            }

        return true;
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form5.saveMainData(MainApp.form4.getScrId());
        MainApp.form5.setIStatus("1");
        MainApp.form5.setEndingDate(DateUtils.getCurrentDateTime());
        Form5.SF5.saveData(sF5);
        appDatabase.form4Dao().updateIStatus(MainApp.form4.getId(), "1", "",
                true, DateUtils.getCurrentDateTime());
        AppConstants.gotoActivity(activity, MainActivity.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }

    private void getAllRadioGroups(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof RadioGroup)
                f0515RadioGroups.add((RadioGroup) child);

            if (child instanceof ViewGroup) {
                getAllRadioGroups((ViewGroup) child); // Recursive call
            }
        }
    }

    private void setChangeListeners() {
        getAllRadioGroups(bi.fldGrpCVf517);
        bi.f516.setOnCheckedChangeListener(listener);
        for (RadioGroup rg : f0515RadioGroups) rg.setOnCheckedChangeListener(listener);
    }

    private boolean areAnyJ517One() {
        return Stream.of(
                sF5.getF517a(), sF5.getF517b(), sF5.getF517c(),
                sF5.getF517d(), sF5.getF517e(), sF5.getF517f(),
                sF5.getF517g()
        ).anyMatch("1"::equals);
    }

    RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> group.post(() -> {
        bi.f517Info.setVisibility(areAnyJ517One() ? View.VISIBLE : View.GONE);
        if (areAnyJ517One() || sF5.getF516().equals("2")) sF5.setF521("3");
        else sF5.setF521(sF5.getF515b());
    });

    /*private boolean proceed() {
        boolean lowMuac = !sF5.getF512().isEmpty() && Float.parseFloat(sF5.getF512()) < 12.5f;
        boolean edema = sF5.getFo515().equals("1");
        return lowMuac || edema;
    }*/

    /*private void eligible() {
        if (proceed()) {
            bi.eligible.setVisibility(View.VISIBLE);
        } else {
            bi.eligible.setVisibility(View.GONE);
            sF5.clearUnEligible();
        }
    }*/

    /*private boolean isAnyF517One() {
        return Stream.of(
                sF5.getF517a(), sF5.getF517b(), sF5.getF517c(),
                sF5.getF517d(), sF5.getF517e(), sF5.getF517f(),
                sF5.getF517g()
        ).anyMatch("1"::equals);
    }*/

    /*RadioGroup.OnCheckedChangeListener listener =
            ((group, checkedId) -> group.post(() -> {
                if (isAnyF517One()) sF5.setF521("3");
                else sF5.setF521(sF5.getF515b());
            }));*/

    /*private void setRadioGroupListeners(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof RadioGroup) {
                ((RadioGroup) child).setOnCheckedChangeListener(listener);
            } else if (child instanceof ViewGroup) {
                setRadioGroupListeners((ViewGroup) child);
            }
        }
    }*/

    /**
     * TAKE PHOTO
     */
    public void takePhoto(View view) {
        if (view.getId() == bi.wPhotoBtn.getId()) currentImageType = IMAGE_WEIGHT;
        else if (view.getId() == bi.hPhotoBtn.getId()) currentImageType = IMAGE_HEIGHT;
        ImagePicker.with(activity).maxResultSize(512, 512).saveDir(AppConstants.GALLERY_DIR).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIEW_IMAGE_REQ_CODE) {
            if (resultCode == RESULT_OK) return;
            switch (currentImageType) {
                case IMAGE_WEIGHT:
                    sF5.setWeightImage("");
                    break;
                case IMAGE_HEIGHT:
                    sF5.setHeightImage("");
                    break;
            }
            return;
        }

        if (resultCode == RESULT_OK) {
            String imageName = null;
            switch (currentImageType) {
                case IMAGE_WEIGHT:
                    imageName = ImageUtils.generateImageName(TAG, "Weight");
                    sF5.setWeightImage(imageName);
                    break;
                case IMAGE_HEIGHT:
                    imageName = ImageUtils.generateImageName(TAG, "Height");
                    sF5.setHeightImage(imageName);
                    break;
            }

            if (imageName != null) {
                Uri uri = Objects.requireNonNull(data).getData();
                File file = new File(Objects.requireNonNull(uri).getPath());
                ImageUtils.renameTo(activity, file.getName(), imageName);
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            AppConstants.showSimpleSnackBar(activity, ImagePicker.getError(data), AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        } else {
            AppConstants.showSimpleSnackBar(activity, getString(R.string.image_not_taken), AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        }
    }

    public void viewPhoto(View view) {
        String[] imageNames = getStrings(view);
        ArrayList<File> matchingImages = ImageUtils.getImageFilesByNames(activity, imageNames);
        if (matchingImages == null || matchingImages.isEmpty()) {
            String image;
            if (view.getId() == bi.viewImageTVw.getId()) image = "WEIGHT";
            else image = "HEIGHT";

            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), image), AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return;
        }
        Intent intent = new Intent(activity, ImageViewerAC.class);
        intent.putExtra("image_files", matchingImages);
        activity.startActivityForResult(intent, VIEW_IMAGE_REQ_CODE);
    }

    private String[] getStrings(View view) {
        String imageName;
        if (view.getId() == bi.viewImageTVw.getId()) {
            currentImageType = IMAGE_WEIGHT;
            imageName = sF5.getWeightImage();
        } else {
            currentImageType = IMAGE_HEIGHT;
            imageName = sF5.getHeightImage();
        }
        return new String[]{imageName};
    }
}