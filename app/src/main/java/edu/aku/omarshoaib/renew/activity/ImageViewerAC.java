package edu.aku.omarshoaib.renew.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.databinding.ActivityImageViewerBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;

public class ImageViewerAC extends AppCompatActivity {

    ActivityImageViewerBinding bi;
    private int currentIndex = 0;
    public static int IMAGE_VIEWER_REQUEST_CODE = 1001;

    // List of image files and their names
    private ArrayList<File> filePaths;
    private List<String> imageNamesList = new ArrayList<>();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(ImageViewerAC.this, R.layout.activity_image_viewer);

        // Retrieve the list of file paths and names from the intent
        filePaths = (ArrayList<File>) getIntent().getSerializableExtra("image_files");
        setImageNames();

        // Hide delete button if imageNamesArr is empty
        if (imageNamesList.isEmpty())
            bi.btnDelete.setVisibility(View.GONE);

        if (filePaths == null || filePaths.isEmpty()) {
            // Handle the case where no image files are found
            AppConstants.showSimpleSnackBar(ImageViewerAC.this, this.getString(R.string.no_image_found),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return;
        }

        if (filePaths.size() > 1) bi.btnNext.setVisibility(View.VISIBLE);

        // Set initial image
        displayCurrentImage();
    }

    public void showPreviousImage(View view) {
        bi.btnNext.setVisibility(View.VISIBLE);
        currentIndex--;
        displayCurrentImage();
        if (currentIndex == 0) {
            bi.btnPrevious.setVisibility(View.INVISIBLE);
        }
    }

    public void showNextImage(View view) {
        bi.btnPrevious.setVisibility(View.VISIBLE);
        currentIndex++;
        displayCurrentImage();
        if (currentIndex == filePaths.size() - 1) {
            bi.btnNext.setVisibility(View.INVISIBLE);
        }
    }

    private void setImageNames(){
        for (File file : filePaths) {
            imageNamesList.add(file.getName()); // Get only the file name
        }
    }

    private void displayCurrentImage() {
        bi.imageView.setImageURI(Uri.fromFile(filePaths.get(currentIndex)));
    }

    public void deleteCurrentImage(View view) {
        if (filePaths.isEmpty()) return;

        File currentFile = filePaths.get(currentIndex);
        if (currentFile.exists()) {
            if (currentFile.delete()) {
                // Remove the file from the list and image names
                filePaths.remove(currentIndex);
                imageNamesList.remove(currentIndex);

                // Notify the user
                AppConstants.showSimpleSnackBar(ImageViewerAC.this, "Image deleted successfully.", AppConstants.MSG_DURATION, AppConstants.TYPE_SUCCESS);

                // Update the UI
                if (filePaths.isEmpty()) {
                    // Here Canceled means Delete
                    setResult(RESULT_CANCELED, new Intent().putExtra("delete", true));
                    finish();
                } else {
                    if (currentIndex >= filePaths.size()) {
                        currentIndex = filePaths.size() - 1;
                    }
                    displayCurrentImage();
                    updateNavigationButtons();
                }
            } else {
                AppConstants.showSimpleSnackBar(ImageViewerAC.this, "Failed to delete image.", AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            }
        } else {
            AppConstants.showSimpleSnackBar(ImageViewerAC.this, "Image file does not exist.", AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        }
    }

    public void finishActivity(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity(null);
    }

    private void updateNavigationButtons() {
        if (currentIndex == 0) {
            bi.btnPrevious.setVisibility(View.INVISIBLE);
        } else {
            bi.btnPrevious.setVisibility(View.VISIBLE);
        }
        if (currentIndex == filePaths.size() - 1) {
            bi.btnNext.setVisibility(View.INVISIBLE);
        } else {
            bi.btnNext.setVisibility(View.VISIBLE);
        }
    }
}
