package edu.aku.omarshoaib.renew.global.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import edu.aku.omarshoaib.renew.R;

public class AppRadioGroup extends RadioGroup {

    private int columnCount = 2; // Default column count

    public AppRadioGroup(Context context) {
        super(context);
        init(null);
    }

    public AppRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    // Initialize and optionally read custom attributes
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AppRadioGroup);
            columnCount = a.getInt(R.styleable.AppRadioGroup_columnCount, columnCount);
            a.recycle();
        }
    }

    /**
     * Optionally allow setting the column count programmatically.
     */
    public void setColumnCount(int count) {
        if (count > 0) {
            columnCount = count;
            requestLayout();
        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We'll divide the total available width evenly between columns.
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int count = getChildCount();
        int rowCount = (count + columnCount - 1) / columnCount;  // Ceiling division

        int childWidth = totalWidth / columnCount;
        int totalHeight = 0;

        int childIndex = 0;
        // Measure each row and calculate the required height.
        for (int row = 0; row < rowCount; row++) {
            int rowMaxHeight = 0;
            for (int col = 0; col < columnCount && childIndex < count; col++) {
                View child = getChildAt(childIndex++);
                if (child.getVisibility() != GONE) {
                    int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    measureChild(child, childWidthSpec, heightMeasureSpec);
                    rowMaxHeight = Math.max(rowMaxHeight, child.getMeasuredHeight());
                }
            }
            totalHeight += rowMaxHeight;
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int totalWidth = r - l;
        int childWidth = totalWidth / columnCount;
        int count = getChildCount();
        int rowCount = (count + columnCount - 1) / columnCount;

        boolean isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;

        int childIndex = 0;
        int top = 0;

        for (int row = 0; row < rowCount; row++) {
            int rowMaxHeight = 0;
            int left = 0;

            for (int col = 0; col < columnCount && childIndex < count; col++) {
                View child = getChildAt(childIndex++);
                if (child.getVisibility() != GONE) {
                    int childHeight = child.getMeasuredHeight();

                    // Adjust layout direction
                    int childLeft, childRight;
                    if (isRtl) {
                        childLeft = totalWidth - ((col + 1) * childWidth);
                        childRight = childLeft + childWidth;
                    } else {
                        childLeft = left;
                        childRight = left + childWidth;
                    }

                    child.layout(childLeft, top, childRight, top + childHeight);

                    left += childWidth;
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
            }
            top += rowMaxHeight;
        }
    }
}
