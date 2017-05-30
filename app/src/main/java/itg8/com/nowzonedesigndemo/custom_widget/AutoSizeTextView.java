package itg8.com.nowzonedesigndemo.custom_widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import itg8.com.nowzonedesigndemo.R;

/**
 * Created by Android itg 8 on 5/20/2017.
 */

public class AutoSizeTextView  extends TextView {

    final String ZERO_WIDTH_SPACE = "\u200B";

    // Minimum size of the text in pixels
    private static final float DEFAULT_MIN_TEXT_SIZE = 8.0f; //sp




    private static final int NO_LINE_LIMIT = -1;
    private final RectF _availableSpaceRect = new RectF();

    private float mMaxTextSize;
    private float mMinTextSize = -1;
    private int mMaxLines;
    private boolean mIsInitialized = false;

    private TextPaint mPaint;
    private FontSizeUtils mFontSizeUtils;

    public AutoSizeTextView(final Context context) {
        this(context,null,0);
    }

    public AutoSizeTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoSizeTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        Init(context, attrs, defStyle);
    }

    private void Init(final Context c, final AttributeSet attrs, final int defStyle){

        //check min text size availability
        if(attrs != null){
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.AutoSizeTextView, defStyle, 0);
            try {
                mMinTextSize = a.getDimension(R.styleable.AutoSizeTextView_minTxtSize, -1);
            } finally {
                a.recycle();
            }
        }

        if(mMinTextSize == -1){
            mMinTextSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    DEFAULT_MIN_TEXT_SIZE, // using the minimal recommended font size
                    getResources().getDisplayMetrics()
            );
        }


        //due to problem of big font size, http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


 mMaxTextSize = getTextSize();

        if(mMaxLines == 0) {
            // no value was assigned during construction
            mMaxLines = NO_LINE_LIMIT;
        }

        mIsInitialized = true;


        mFontSizeUtils = new FontSizeUtils();
        mFontSizeUtils.setSizeTester(
                new FontSizeUtils.SizeTester(mMinTextSize, mMaxTextSize));

        mPaint = new TextPaint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        mPaint.setSubpixelText(true);
    }

    @Override
    public void setTypeface(final Typeface tf) {

        if(mPaint == null) {
            mPaint = new TextPaint(getPaint());
        }

        mPaint.setTypeface(tf);
        adjustTextSize();
        super.setTypeface(tf);
    }

    @Override
    public void setTextSize(final float size) {
        mMaxTextSize = size;
        adjustTextSize();
    }

    @Override
    public void setMaxLines(final int maxlines) {
        super.setMaxLines(maxlines);
        mMaxLines = maxlines;
        reAdjust();
    }

    @Override
    public int getMaxLines() {
        return mMaxLines;
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        mMaxLines = 1;
        reAdjust();
    }

    @Override
    public void setSingleLine(final boolean singleLine) {
        super.setSingleLine(singleLine);
        mMaxLines = singleLine ? 1 : NO_LINE_LIMIT;
        reAdjust();
    }

    @Override
    public void setLines(final int lines) {
        super.setLines(lines);
        mMaxLines = lines;
        reAdjust();
    }

    @Override
    public void setTextSize(final int unit,final float size) {
        final Context c = getContext();

        Resources r = c == null ?
                Resources.getSystem() :
                c.getResources();

        mMaxTextSize = TypedValue.applyDimension(unit,size,r.getDisplayMetrics());
        if(mFontSizeUtils.getSizeTester() != null){
            mFontSizeUtils.getSizeTester().setMaxTextSize(mMaxTextSize);
        }

        adjustTextSize();
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param minTextSize
     */
    public void setMinTextSize(final float minTextSize) {
        mMinTextSize = minTextSize;

        if(mFontSizeUtils.getSizeTester() != null){
            mFontSizeUtils.getSizeTester().setMinTextSize(minTextSize);
        }

        reAdjust();
    }

    private void reAdjust() {
        adjustTextSize();
    }

    private void adjustTextSize() {
        if(!mIsInitialized) {
            return;
        }

        final int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
        final int _widthLimit = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
        if(_widthLimit <= 0) {
            return;
        }

        _availableSpaceRect.right = _widthLimit;
        _availableSpaceRect.bottom = heightLimit;
        superSetTextSize();
    }

    private void superSetTextSize() {

        float size = efficientTextSizeSearch(_availableSpaceRect);

        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        String newText = getText().toString().trim();
        if(size - 1 <= mMinTextSize){
            newText = StringUtils.ellipsizeText(newText, _availableSpaceRect.width(), mPaint);
        }

        //workaround http://code.google.com/p/android/issues/detail?id=17343#c9
        //http://stackoverflow.com/a/13377239/1461625
        String fixString = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            fixString = ZERO_WIDTH_SPACE;
        }

        super.setText(newText + fixString);
    }

    private float efficientTextSizeSearch(final RectF availableSpace) {

        String text = getText().toString().trim();

        boolean isSingleLine = StringUtils.isSingleLine(text);

        float newSize = mFontSizeUtils.getFontSize(text, isSingleLine, availableSpace, mPaint);
        System.out.println(newSize);

        return newSize;
    }

    @Override
    protected void onSizeChanged(final int width,final int height,final int oldwidth,final int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if(width!=oldwidth||height!=oldheight)
            reAdjust();
    }

    /**
     * Resize text after measuring
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    Paint.FontMetricsInt fontMetricsInt;

    @Override
    protected void onDraw(Canvas canvas) {
        if (fontMetricsInt == null){
            fontMetricsInt = new Paint.FontMetricsInt();
            getPaint().getFontMetricsInt(fontMetricsInt);
        }
        canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        super.onDraw(canvas);
    }
}