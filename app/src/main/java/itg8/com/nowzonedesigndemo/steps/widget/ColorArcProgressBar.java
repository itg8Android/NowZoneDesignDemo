package itg8.com.nowzonedesigndemo.steps.widget;

/**
 * Created by Android itg 8 on 5/23/2017.
 */


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import itg8.com.nowzonedesigndemo.R;

public class ColorArcProgressBar extends View{

    private int mWidth;
    private int mHeight;
    private int diameter = 40;
    private float centerX;
    private float centerY;

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    // textSize = 60 changed to 30
    // hint Size 15 to 20
    private float textSize = dipToPx(30);
    private float hintSize = dipToPx(30);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;
    // long degree 13 -- 10
    private float longdegree = dipToPx(10);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);

    private String hintColor = "#ffffff";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private String bgArcColor = "#ffffff";
    private String titleString;
    private String hintString;

    private boolean isShowCurrentSpeed = true;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;

    // sweepAngle / maxValues
    private float k;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};

        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, true);
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, true);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit,false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) ( longdegree + progressWidth + diameter +  DEGREE_PROGRESS_DISTANCE);
//        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
//        int height= (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height= (int) (longdegree + progressWidth + diameter +  DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {

        diameter = 3 * getScreenWidth() /7 ;
     //   diameter = 3* getScreenWidth()/5;

        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);


        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;


        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));


        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);


        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);


        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.WHITE);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.RIGHT);


        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.setDrawFilter(mDrawFilter);

        if (isNeedDial) {

            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        rotateMatrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);

        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX+14, centerY  * textSize / 3, hintPaint);
        }
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX+7, centerY + 7 * textSize / 3, curSpeedPaint);
        }

        invalidate();

    }


    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle/maxValues;
    }

    /**

     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**

     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**

     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**

     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**

     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**

     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**

     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**

     * @param title
     */
    public void setTitle(String title){
        this.titleString = title;
    }

    /**

     * @param isNeedTitle
     */
    public void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     * @param isNeedUnit
     */
    public void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     * @param isNeedDial
     */
    public void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle= (float) animation.getAnimatedValue();
                curValues = currentAngle/k;
            }
        });
        progressAnimator.start();
    }

    /**
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }}
