package edu.aku.omarshoaib.renew.global.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import edu.aku.omarshoaib.renew.R;

public class AppRadioGroup extends RadioGroup {

    private int columnCount = 2;

    public AppRadioGroup(Context context) {
        super(context);
        init(null);
    }

    public AppRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AppRadioGroup);
            columnCount = a.getInt(R.styleable.AppRadioGroup_columnCount, columnCount);
            a.recycle();
        }
    }

    public void setColumnCount(int count) {
        if (count > 0) {
            columnCount = count;
            requestLayout();
        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    public static class LayoutParams extends RadioGroup.LayoutParams {

        public int span = 1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.AppRadioGroup_Layout);
            span = a.getInt(R.styleable.AppRadioGroup_Layout_layout_span, 1);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(RadioGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof RadioGroup.LayoutParams) {
            return new LayoutParams((RadioGroup.LayoutParams) p);
        } else {
            return new LayoutParams(p);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int singleColWidth = totalWidth / columnCount;

        int totalHeight = 0;
        int rowMaxHeight = 0;
        int usedSpan = 0;

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int span = Math.min(lp.span, columnCount);

            if (usedSpan + span > columnCount) {
                totalHeight += rowMaxHeight;
                rowMaxHeight = 0;
                usedSpan = 0;
            }

            int childWidth = singleColWidth * span;
            int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);

            measureChild(child, childWidthSpec, heightMeasureSpec);

            rowMaxHeight = Math.max(rowMaxHeight, child.getMeasuredHeight());
            usedSpan += span;

            if (usedSpan == columnCount) {
                totalHeight += rowMaxHeight;
                rowMaxHeight = 0;
                usedSpan = 0;
            }
        }

        if (usedSpan > 0) {
            totalHeight += rowMaxHeight;
        }

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int totalWidth = r - l;
        int singleColWidth = totalWidth / columnCount;

        boolean isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;

        int x = 0;
        int y = 0;
        int rowMaxHeight = 0;
        int usedSpan = 0;

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int span = Math.min(lp.span, columnCount);

            if (usedSpan + span > columnCount) {
                y += rowMaxHeight;
                x = 0;
                rowMaxHeight = 0;
                usedSpan = 0;
            }

            int childWidth = singleColWidth * span;
            int childHeight = child.getMeasuredHeight();

            int left, right;

            if (isRtl) {
                right = totalWidth - x;
                left = right - childWidth;
            } else {
                left = x;
                right = left + childWidth;
            }

            child.layout(left, y, right, y + childHeight);

            x += childWidth;
            usedSpan += span;
            rowMaxHeight = Math.max(rowMaxHeight, childHeight);

            if (usedSpan == columnCount) {
                y += rowMaxHeight;
                x = 0;
                rowMaxHeight = 0;
                usedSpan = 0;
            }
        }
    }
}