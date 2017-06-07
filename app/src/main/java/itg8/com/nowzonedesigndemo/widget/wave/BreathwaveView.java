package itg8.com.nowzonedesigndemo.widget.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BreathwaveView extends View {
//    private static final int BREATHVIEW_CROP = 200;
    private static final int BREATHVIEW_DURATION = 10000;
//    private static final int BREATHVIEW_MAX_INTERVAL = 200;
//    private static final double BREATHVIEW_MINMAX_MARGIN = 0.02d;

    private static final float BREATHVIEW_SHADOW_SHIFT = 1.5f;
    private static final float BREATHVIEW_STROKE_WIDTH = 2.75f;
//    private static final String TAG = BreathwaveView.class.getSimpleName();
    private float mDensity;
    private List<BreathSample> mSamples;
    private float[] mVert;
    private Paint mWavePaint;
    private List<BreathSample> mWaveSamples;
    private Paint mWaveShadowPaint;
    private BreathSample breathSample32;

    public BreathwaveView(Context context) {
        super(context);
        init();
    }

    public BreathwaveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        this.mDensity = getResources().getDisplayMetrics().density;
        this.mSamples = new ArrayList();
        this.mWaveSamples = new ArrayList();
        this.mVert = new float[1];
        this.mWavePaint = new Paint();
        this.mWavePaint.setAntiAlias(true);
        this.mWavePaint.setStyle(Style.STROKE);
        this.mWavePaint.setColor(Color.parseColor("#FFFFFF"));
        this.mWavePaint.setStrokeWidth(BREATHVIEW_STROKE_WIDTH * this.mDensity);
        this.mWavePaint.setStrokeJoin(Join.ROUND);
        this.mWavePaint.setStrokeCap(Cap.ROUND);
        this.mWaveShadowPaint = new Paint(this.mWavePaint);
        this.mWaveShadowPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mWaveShadowPaint.setAlpha(15);

        setLayerType(2, null);
    }


    private float getX(BreathSample breathSample, int width, long elapsedRealtime) {
        long j2 = elapsedRealtime - BREATHVIEW_DURATION;
        return (float) (((breathSample.getTimestamp() - j2) * ((long) width)) / (elapsedRealtime - j2));
    }

    private float getY(BreathSample breathSample, int height, double dConst1, double dConst2) {
        return (float) (((double) height) - (((breathSample.getValue() - dConst1) * ((double) height)) / (dConst2 - dConst1)));
//        return (float) (((double) height) * (((breathSample.getValue() - dConst1)) / (dConst2 - dConst1)));
    }

    private void findWaveSamples(long j) {
        long j2 = j - 200;
        long j3 = (j - BREATHVIEW_DURATION) + 200;
        BreathSample breathSample = null;
        BreathSample breathSample2 = null;
        this.mWaveSamples.clear();
        for (BreathSample breathSample3 : this.mSamples) {
            BreathSample breathSample32 = breathSample3;
            long timestamp = breathSample32.getTimestamp();
            if (timestamp >= j3) {
                if (timestamp > j2) {
                    breathSample2 = breathSample32;
                    break;
                } else {
                    this.mWaveSamples.add(breathSample32);
                    breathSample32 = breathSample;
                }
            }
            breathSample = breathSample32;
        }
        if (breathSample != null) {
            while (this.mSamples.get(0) != breathSample) {
                this.mSamples.remove(0);
            }
        }

        if (this.mWaveSamples.size() >= 2) {
            breathSample32 = (BreathSample) this.mWaveSamples.get(0);
            if (breathSample != null && breathSample.getTimestamp() + 200 >= breathSample32.getTimestamp()) {
                this.mWaveSamples.add(0, breathSample.interpolate(breathSample32, j3));
            }
            breathSample32 = (BreathSample) this.mWaveSamples.get(this.mWaveSamples.size() - 1);
            if (breathSample2 != null && breathSample32.getTimestamp() + 200 >= breathSample2.getTimestamp()) {
                this.mWaveSamples.add(this.mWaveSamples.size(), breathSample32.interpolate(breathSample2, j2));
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        findWaveSamples(elapsedRealtime);
        if (this.mWaveSamples.size() >= 2) {
            int size = (this.mWaveSamples.size() - 1) * 4;
            if (this.mVert.length < size * 2) {
                this.mVert = new float[((size * 2) + 64)];
            }
            long j = 0;
            int i = 0;
            int i2 = size;
            int i3 = size;
            for (BreathSample breathSample : this.mWaveSamples) {
                int i4;
                float x = getX(breathSample, canvas.getWidth(), elapsedRealtime);
                float y = getY(breathSample, canvas.getHeight(), 3000, 6000);
                int i5 = i + 1;
                this.mVert[i] = x;
                int i6 = i5 + 1;
                this.mVert[i5] = y;
                int i7 = i3 + 1;
                this.mVert[i3] = x;
                i3 = i7 + 1;
                this.mVert[i7] = (BREATHVIEW_SHADOW_SHIFT * this.mDensity) + y;
                if (j == 0 || 200 + j >= breathSample.getTimestamp()) {
                    i5 = i3;
                    i4 = i6;
                    i7 = i2;
                } else {
                    i5 = i3 - 4;
                    i4 = i6 - 4;
                    i7 = i2 - 4;
                }
                long timestamp = breathSample.getTimestamp();
                if (!(i4 == 2 || i4 == i7)) {
                    int i8 = i4 + 1;
                    this.mVert[i4] = x;
                    i4 = i8 + 1;
                    this.mVert[i8] = y;
                    i8 = i5 + 1;
                    this.mVert[i5] = x;
                    i5 = i8 + 1;
                    this.mVert[i8] = y + (BREATHVIEW_SHADOW_SHIFT * this.mDensity);
                }
                j = timestamp;
                i = i4;
                i2 = i7;
                i3 = i5;
            }
            canvas.drawLines(this.mVert, i2, i2, this.mWaveShadowPaint);
            canvas.drawLines(this.mVert, 0, i2, this.mWavePaint);
        }
        invalidate();
    }

    public void reset() {
        this.mSamples.clear();
    }

    public void addSample(long timeStamp, double value) {
//        Log.d(TAG,"value : "+value+" , "+" timestamp: "+ CommonMethod.getTimeFromTMP(timeStamp));
        this.mSamples.add(new BreathSample(timeStamp, value));
//        invalidate();
    }
}
