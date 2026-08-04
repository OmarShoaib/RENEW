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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import edu.aku.omarshoaib.renew.activity.ImageViewerAC;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF06Binding;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.ImageUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form6;

public class SectionF06 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF06.this;

    ActivitySectionF06Binding bi;
    private AppDatabase appDatabase;
    private static final int VIEW_IMAGE_REQ_CODE = 1001;
    private static final int IMAGE_MUAC = 1;
    private static final int IMAGE_WEIGHT = 2;
    private static final int IMAGE_HEIGHT = 3;
    private int currentImageType = 0;
    private Form6.SF6 sF6;
    private List<RadioGroup> f610RadioGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f06);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f6t0), getString(R.string.f6t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF6 = Form6.SF6.getData();
        sF6 = sF6 == null ? new Form6.SF6() : sF6;
        bi.setForm(sF6);
        initUI();
    }

    private void initUI() {
        setChangeListeners();
        bi.lastVisitSachets.setText(String.format("Last Visit Sachets: %s", MainApp.vFormF06.getNoOfSachets()));
        bi.f602.setMinDate(MainApp.vFormF06.getEnrollmentDate());
        bi.f604b.setMinDate(MainApp.vFormF06.getEnrollmentDate());
        bi.f602.addTextChangedListener(new AppTextWatcher(bi.f602.getId(), iAppTextWatcher));
        bi.f611.setMaxvalue(Float.parseFloat(MainApp.vFormF06.getNoOfSachets()));
        bi.f61296x.setMaxvalue(AppConstants.parseFloat(MainApp.vFormF06.getNoOfSachets()));
        bi.f61396x.setMaxvalue(AppConstants.parseFloat(MainApp.vFormF06.getNoOfSachets()));
        bi.f611.addTextChangedListener(new AppTextWatcher(bi.f611.getId(), iAppTextWatcher));
        bi.f61296x.addTextChangedListener(new AppTextWatcher(bi.f61296x.getId(), iAppTextWatcher));
        bi.f61396x.addTextChangedListener(new AppTextWatcher(bi.f61396x.getId(), iAppTextWatcher));
        bi.f605.addTextChangedListener(new AppTextWatcher(bi.f605.getId(), iAppTextWatcher));
        sF6.setF601(MainApp.form6.getUsername());
        sF6.setF603(MainApp.vFormF06.getVisitNumber());
        MainApp.form6.setFollowupNo(MainApp.vFormF06.getVisitNumber());
        sF6.setF604(MainApp.vFormF06.getParticipantId());
        bi.f61905x.setMinDate(MainApp.vFormF06.getEnrollmentDate());
        setLastVisitStatus();
        bi.fldGrpCVf609.setVisibility(MainApp.vFormF06.getType().equals("2") ?
                View.VISIBLE : View.GONE);
    }

    private void setLastVisitStatus() {
        String lastStatus = MainApp.vFormF06.getLastVisitF604a();
        if (lastStatus == null || lastStatus.isEmpty()) {
            bi.fldGrpCVf604az.setVisibility(View.GONE);
        } else {
            bi.fldGrpCVf604az.setVisibility(View.VISIBLE);
            bi.f604az.setText(lastStatus.equals("1") ? getString(R.string.f604a01) :
                    lastStatus.equals("2") ? getString(R.string.f604a02) :
                            lastStatus.equals("3") ? getString(R.string.f604a03) :
                                    lastStatus.equals("4") ? getString(R.string.f604a04) :
                                            lastStatus.equals("5") ? getString(R.string.f604a05) :
                                                    lastStatus.equals("6") ? getString(R.string.f604a06) :
                                                            getString(R.string.f604a96));
        }
    }

    AppTextWatcher.IAppTextWatcher iAppTextWatcher = (viewId, text) -> {
        if (viewId == bi.f602.getId()) {
            if (text.isEmpty())
                bi.f604b.setMaxDate(DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT));
            else bi.f604b.setMaxDate(text);
        } else if (viewId == bi.f611.getId() || viewId == bi.f61296x.getId() ||
                viewId == bi.f61396x.getId()) {
            int f611 = AppConstants.parseInt(sF6.getF611()),
                    f612 = AppConstants.parseInt(sF6.getF61296x()),
                    f613 = AppConstants.parseInt(sF6.getF61396x());
            int previousSachets = AppConstants.parseInt(MainApp.vFormF06.getNoOfSachets());

            if (f611 + f612 + f613 < previousSachets) bi.fldGrpCVf613a.setVisibility(View.VISIBLE);
            else {
                bi.fldGrpCVf613a.setVisibility(View.GONE);
                sF6.setF613a("");
            }
        } else if (viewId == bi.f605.getId()) {
//            if (text.isEmpty()) return;
//            Float muac = Float.parseFloat(text);
//            if (muac >= 11.5f && muac < 12.5f) {
//                bi.fldGrpCVf609.setVisibility(View.GONE);
//                bi.f609.clearCheck();
//                sF6.setF609(_EMPTY_);
//            } else bi.fldGrpCVf609.setVisibility(View.VISIBLE);
            enableFollowupAnswers();
        }
    };

    private boolean formValidation() {
        if (!Validator.emptyCheckingContainer(activity, bi.GrpName)) return false;

        if (sF6.getF604a().equals("1")) {
            int f611 = AppConstants.parseInt(sF6.getF611()),
                    f612 = AppConstants.parseInt(sF6.getF61296x()),
                    f613 = AppConstants.parseInt(sF6.getF61396x());
            int previousSachets = Integer.parseInt(MainApp.vFormF06.getNoOfSachets());
            if (f611 + f612 + f613 > previousSachets) {
                Validator.emptyCustomTextBox(activity, bi.f611, "Incorrect count");
                return false;
            }
        }

        if (!sF6.getF605().isEmpty() && sF6.getMuacImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "MUAC"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if (!sF6.getF606().isEmpty() && sF6.getWeightImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "WEIGHT"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        if (!sF6.getF607().isEmpty() && sF6.getHeightImage().isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, String.format(Locale.ENGLISH, getString(R.string.no_image_found), "HEIGHT"),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return false;
        }

        return true;
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form6.saveMainData(MainApp.vFormF06.getParticipantId(), MainApp.vFormF06.getVisitNumber());
        MainApp.form6.setIStatus("1");
        MainApp.form6.setEndingDate(DateUtils.getCurrentDateTime());
        Form6.SF6.saveData(sF6);
        AppConstants.gotoActivity(activity, SectionF06ListAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, SectionF06ListAC.class);
    }

    private void getAllRadioGroups(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof RadioGroup) f610RadioGroups.add((RadioGroup) child);
            else if (child instanceof ViewGroup)
                getAllRadioGroups((ViewGroup) child); // Recursive call
        }
    }

    private void setChangeListeners() {
        getAllRadioGroups(bi.fldGrpCVf610);
        for (RadioGroup rg : f610RadioGroups) rg.setOnCheckedChangeListener(listener);
        bi.f604a.setOnCheckedChangeListener(listener);
        bi.f608a.setOnCheckedChangeListener(listener);
        bi.f609.setOnCheckedChangeListener(listener);
    }

    private boolean areAnyF619One() {
        return Stream.of(
                sF6.getF610a(), sF6.getF610b(), sF6.getF610c(),
                sF6.getF610d(), sF6.getF610e(), sF6.getF610f(),
                sF6.getF610g()
        ).anyMatch("1"::equals);
    }

    RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> {
        group.post(() -> {
            if (group == bi.f604a) {
                String val = sF6.getF604a();

                if ("1".equals(val)) {
                    bi.fldGrpCVf619.setVisibility(View.VISIBLE);
                } else if (!"2".equals(val) && isDefaulted()) {
                    bi.fldGrpCVf619.setVisibility(View.VISIBLE);
                    enableFollowupAnswers();
                } else {
                    bi.fldGrpCVf619.setVisibility(View.GONE);
                    bi.f619.clearCheck();
                }
            } else {
                bi.f610Info.setVisibility(areAnyF619One() ? View.VISIBLE : View.GONE);
                enableFollowupAnswers();
            }
        });
    };

    private void enableFollowupAnswers() {
        String f619TempValue = sF6.getF619();
        String f61901TempValue = sF6.getF61901();
        String f61905TempValue = sF6.getF61905();
        sF6.setF619("");
        bi.f619.clearCheck();
        AppConstants.disableViews(activity, bi.f619, Arrays.asList("f61901x", "f61905x"));
        if (referredCondition()) {
            AppConstants.enableViews(activity, bi.f61906);
            sF6.setF619("6");
        } else if (recoveredCondition()) {
            AppConstants.enableViews(activity, bi.f61902);
            sF6.setF619("2");
        } else if (nonResponder()) {
            AppConstants.enableViews(activity, bi.f61904);
            sF6.setF619("4");
        } else if (supplementContinuedRefused()) {
            AppConstants.enableViews(activity, Arrays.asList(bi.f61901, bi.f61907));
            sF6.setF619(f619TempValue.matches("[17]") ? f619TempValue : _EMPTY_);
            sF6.setF61901(f619TempValue.equals("1") ? f61901TempValue : _EMPTY_);
            sF6.setF61905(f619TempValue.equals("5") ? f61905TempValue : _EMPTY_);
        } else if (isDefaulted()) {
            AppConstants.enableViews(activity, bi.f61903);
            sF6.setF619("3");
        } else {
            AppConstants.enableViews(activity, bi.f619);
        }

    }

    private boolean isDefaulted() {
        return !(MainApp.vFormF06.getVisitNumber().equals("1") ||
                (
                        MainApp.vFormF06.getLastVisitF604a() != null &&
                                MainApp.vFormF06.getLastVisitF604a().equals("1")
                ) || sF6.getF604a().equals("1"));
    }

    private boolean referredCondition() {
        return areAnyF619One() || sF6.getF608a().equals("3") || sF6.getF609().equals("2");
    }

    private boolean recoveredCondition() {
        try {
            float hb = Float.parseFloat(sF6.getF605());

            return (MainApp.vFormF06.getType().equals("1")
                    && hb > 12.4f
                    && MainApp.vFormF06.getVisitNumber().equals("2"))
                    || (MainApp.vFormF06.getType().equals("2")
                    && hb > 11.4f
                    && MainApp.vFormF06.getVisitNumber().equals("4"));

        } catch (Exception e) {
            return false;
        }
    }

    private boolean nonResponder() {
        try {
            float hb = Float.parseFloat(sF6.getF605());

            return (MainApp.vFormF06.getType().equals("1")
                    && hb < 12.5f
                    && MainApp.vFormF06.getVisitNumber().equals("2"))
                    || (MainApp.vFormF06.getType().equals("2")
                    && hb < 11.5f
                    && MainApp.vFormF06.getVisitNumber().equals("4"));

        } catch (Exception e) {
            return false;
        }
    }

    private boolean supplementContinuedRefused() {
        return (MainApp.vFormF06.getType().equals("1")
                && MainApp.vFormF06.getVisitNumber().equals("1")) ||
                (MainApp.vFormF06.getType().equals("2")
                        && MainApp.vFormF06.getVisitNumber().matches("[123]"));
    }

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
                    sF6.setMuacImage("");
                    break;

                case IMAGE_WEIGHT:
                    sF6.setWeightImage("");
                    break;

                case IMAGE_HEIGHT:
                    sF6.setHeightImage("");
                    break;
            }
            return;
        }

        if (resultCode == RESULT_OK) {
            String imageName = null;
            switch (currentImageType) {

                case IMAGE_MUAC:
                    imageName = ImageUtils.generateImageName(TAG, "Muac");
                    sF6.setMuacImage(imageName);
                    break;

                case IMAGE_WEIGHT:
                    imageName = ImageUtils.generateImageName(TAG, "Weight");
                    sF6.setWeightImage(imageName);
                    break;

                case IMAGE_HEIGHT:
                    imageName = ImageUtils.generateImageName(TAG, "Height");
                    sF6.setHeightImage(imageName);
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
            imageName = sF6.getMuacImage();
        } else if (view.getId() == bi.viewImageTVw.getId()) {
            currentImageType = IMAGE_WEIGHT;
            imageName = sF6.getWeightImage();
        } else {
            currentImageType = IMAGE_HEIGHT;
            imageName = sF6.getHeightImage();
        }
        return new String[]{imageName};
    }
}