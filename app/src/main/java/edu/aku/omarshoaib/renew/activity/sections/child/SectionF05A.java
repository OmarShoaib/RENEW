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

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.ImageViewerAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF05aBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.ImageUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form5;
import edu.aku.omarshoaib.renew.model.Form5A;

public class SectionF05A extends BaseActivity {
    
    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF05A.this;

    ActivitySectionF05aBinding bi;
    private AppDatabase appDatabase;
    private static final int VIEW_IMAGE_REQ_CODE = 1001;
    private static final int IMAGE_MUAC = 1;
    private static final int IMAGE_WEIGHT = 2;
    private static final int IMAGE_HEIGHT = 3;
    private int currentImageType = 0;
    private Form5A.SF5A sF5a;
    private List<RadioGroup> f0515RadioGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f05a);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f5at0), getString(R.string.f5t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF5a = Form5A.SF5A.getData();
        sF5a = sF5a == null ? new Form5A.SF5A() : sF5a;
        bi.setForm(sF5a);
        initUI();
    }

    private void initUI() {
        setChangeListeners();
        sF5a.setF501(MainApp.user.getFullName() + " - " + MainApp.user.getUserId());
        bi.f502.setMinDate(MainApp.vFormF05a.getEnrollmentDate());
        bi.f502.addTextChangedListener(new AppTextWatcher(bi.f502.getId(), dateTextWatcher));
        bi.f506.addTextChangedListener(new AppTextWatcher(bi.f506.getId(), dateTextWatcher));
        bi.f521a.addTextChangedListener(new AppTextWatcher(bi.f521a.getId(), dateTextWatcher));
        sF5a.setF504(MainApp.vFormF05a.getChildName());
        sF5a.setF505(MainApp.vFormF05a.getParticipantId());
        sF5a.setF506(MainApp.vFormF05a.getDob());
        sF5a.setF508(MainApp.vFormF05a.getGender());
        sF5a.setF509(MainApp.vFormF05a.getFatherName());
        sF5a.setF510(MainApp.vFormF05a.getContactNo());
        sF5a.setF511(MainApp.vFormF05a.getVillage());
        bi.fo515.setOnCheckedChangeListener((rG, i) -> rG.post(this::setType));
        bi.f512.addTextChangedListener(new AppTextWatcher(bi.f512.getId(), (viewId, text) -> setType()));

//        bi.f503.setOnCheckedChangeListener(f503Listener);
    }

    private void setType() {
        bi.f517Info.setVisibility(areAnyJ517One() ||
                sF5a.getFo515().equals("1") ? View.VISIBLE : View.GONE);
        if(sF5a.getF512().length() < 4) return;
        float muac = Float.parseFloat(sF5a.getF512());
        String f515b = sF5a.getFo515().equals("1") || muac < 11.5f ? "2" : "1";
        sF5a.setF515b(f515b);
    }

    AppTextWatcher.IAppTextWatcher dateTextWatcher = (viewId, text) -> {
        if (viewId == bi.f502.getId()) {
            sF5a.setF506(_EMPTY_);
            if (text.isEmpty()) {
                String today = DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT);
                String maxDate1 = DateUtils.addSubMonths(today, -3);
                String minDate1 = DateUtils.addSubMonths(today, -72);
                bi.f506.setMinDate(minDate1);
                bi.f506.setMaxDate(maxDate1);
                return;
            }
            String maxDate2 = DateUtils.addSubMonths(text, -3);
            String minDate2 = DateUtils.addSubMonths(text, -72);
            bi.f506.setMinDate(minDate2);
            bi.f506.setMaxDate(maxDate2);
        } else if (viewId == bi.f506.getId()) {
            if (text.isEmpty()) {
                sF5a.setF507dd("");
                sF5a.setF507mm("");
                return;
            }
            String[] dob = text.split("-");
            List<String> age = DateUtils.calculateAge(sF5a.getF502(), dob[0], dob[1], dob[2]);
            sF5a.setF507dd(age.get(2));
            sF5a.setF507mm(String.
                    valueOf(DateUtils.
                            getAgeInMonths(age.get(0), age.get(1))
                    )
            );
        } else if (viewId == bi.f521a.getId()) {
            if (text.isEmpty()) {
                bi.fldGrpCVf522.setVisibility(View.GONE);
                sF5a.setF522("");
                return;
            }
            int count = Integer.parseInt(sF5a.getF521a());
            if ((sF5a.getF521().equals("1") && count >= 30) || (sF5a.getF521().equals("2") && count >= Integer.parseInt(sF5a.getF52102x()))) {
                bi.fldGrpCVf522.setVisibility(View.GONE);
                sF5a.setF522("");
                return;
            }
            bi.fldGrpCVf522.setVisibility(View.VISIBLE);
        }
    };

    private boolean formValidation() {
        if (!Validator.emptyCheckingContainer(activity, bi.GrpName)) return false;

        if(!sF5a.getF512().isEmpty() && sF5a.getMuacImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "MUAC"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if(!sF5a.getF513().isEmpty() && sF5a.getWeightImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "WEIGHT"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if(!sF5a.getFo514().isEmpty() && sF5a.getHeightImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "HEIGHT"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if (sF5a.getF521().equals("1"))
            if (Integer.parseInt(sF5a.getF521a()) > Integer.parseInt(sF5a.getF52101x())) {
                Validator.emptyCustomTextBox(activity, bi.f521a, "Given dose is greater from required dose");
                return false;
            }

        if (sF5a.getF521().equals("2"))
            if (Integer.parseInt(sF5a.getF521a()) > Integer.parseInt(sF5a.getF52102x())) {
                Validator.emptyCustomTextBox(activity, bi.f521a, "Given dose is greater from required dose");
                return false;
            }

        return true;
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form5A.saveMainData(MainApp.vFormF05a.getParticipantId());
        MainApp.form5a.setIStatus("1");
        MainApp.form5a.setEndingDate(DateUtils.getCurrentDateTime());
        Form5A.SF5A.saveData(sF5a);
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
        for (RadioGroup rg : f0515RadioGroups) rg.setOnCheckedChangeListener(listener);
//        bi.fo515.setOnCheckedChangeListener(listener);
    }

    private boolean areAnyJ517One() {
        return Stream.of(
                sF5a.getF517a(), sF5a.getF517b(), sF5a.getF517c(),
                sF5a.getF517d(), sF5a.getF517e(), sF5a.getF517f(),
                sF5a.getF517g()
        ).anyMatch("1"::equals);
    }

    RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> group.post(() -> {
        bi.f517Info.setVisibility(areAnyJ517One() ||
                sF5a.getFo515().equals("1") ? View.VISIBLE : View.GONE);
        if (areAnyJ517One()) sF5a.setF521("3");
        else sF5a.setF521(sF5a.getF515b());
    });

    /*private boolean proceed() {
        boolean lowMuac = !sF5a.getF512().isEmpty() && Float.parseFloat(sF5a.getF512()) < 12.5f;
        boolean edema = sF5a.getFo515().equals("1");
        return lowMuac || edema;
    }*/

    /*private void eligible() {
        if (proceed()) {
            bi.eligible.setVisibility(View.VISIBLE);
        } else {
            bi.eligible.setVisibility(View.GONE);
            sF5a.clearUnEligible();
        }
    }*/

    /*private boolean isAnyF517One() {
        return Stream.of(
                sF5a.getF517a(), sF5a.getF517b(), sF5a.getF517c(),
                sF5a.getF517d(), sF5a.getF517e(), sF5a.getF517f(),
                sF5a.getF517g()
        ).anyMatch("1"::equals);
    }*/

    /*RadioGroup.OnCheckedChangeListener listener =
            ((group, checkedId) -> group.post(() -> {
                if (isAnyF517One()) sF5a.setF521("3");
                else sF5a.setF521(sF5a.getF515b());
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
        if (view.getId() == bi.mPhotoBtn.getId()) currentImageType = IMAGE_MUAC;
        else if (view.getId() == bi.wPhotoBtn.getId()) currentImageType = IMAGE_WEIGHT;
        else if (view.getId() == bi.hPhotoBtn.getId()) currentImageType = IMAGE_HEIGHT;
        ImagePicker.with(activity).maxResultSize(512, 512).saveDir(AppConstants.GALLERY_DIR).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result from image viewer
        if (requestCode == VIEW_IMAGE_REQ_CODE) {

            if (resultCode == RESULT_OK) return;

            switch (currentImageType) {
                case IMAGE_MUAC:
                    sF5a.setMuacImage("");
                    break;

                case IMAGE_WEIGHT:
                    sF5a.setWeightImage("");
                    break;

                case IMAGE_HEIGHT:
                    sF5a.setHeightImage("");
                    break;
            }
            return;
        }

        if (resultCode == RESULT_OK) {
            String imageName = null;
            switch (currentImageType) {

                case IMAGE_MUAC:
                    imageName = ImageUtils.generateImageName(TAG, "Muac");
                    sF5a.setMuacImage(imageName);
                    break;

                case IMAGE_WEIGHT:
                    imageName = ImageUtils.generateImageName(TAG, "Weight");
                    sF5a.setWeightImage(imageName);
                    break;

                case IMAGE_HEIGHT:
                    imageName = ImageUtils.generateImageName(TAG, "Height");
                    sF5a.setHeightImage(imageName);
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
            if (view.getId() == bi.viewImageTVm.getId()) image = "MUAC";
            else if (view.getId() == bi.viewImageTVw.getId()) image = "WEIGHT";
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
        if (view.getId() == bi.viewImageTVm.getId()) {
            currentImageType = IMAGE_MUAC;
            imageName = sF5a.getMuacImage();
        } else if (view.getId() == bi.viewImageTVw.getId()) {
            currentImageType = IMAGE_WEIGHT;
            imageName = sF5a.getWeightImage();
        } else {
            currentImageType = IMAGE_HEIGHT;
            imageName = sF5a.getHeightImage();
        }
        return new String[]{imageName};
    }
}