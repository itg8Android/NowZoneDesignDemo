package itg8.com.nowzonedesigndemo.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

import itg8.com.nowzonedesigndemo.R;

/**
 * This class extends the View class and is designed draw the compass on the
 * View.
 *
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class CompassView extends View {

    private static final AtomicBoolean drawing = new AtomicBoolean(false);
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static int parentWidth = 0;
    private static int parentHeight = 0;
    private static Matrix matrix = null;
    private static Bitmap bitmap = null;
    private int bearing;
    private boolean created;
    private int calibrated=360;

    public CompassView(Context context) {
        super(context);

        initialize();
    }

    public CompassView(Context context, AttributeSet attr) {
        super(context, attr);

        initialize();
    }

    private void initialize() {
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass_new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();

        if (!drawing.compareAndSet(false, true)) return;


        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        if (bitmap.getWidth() > canvasWidth || bitmap.getHeight() > canvasHeight) {
            // Resize the bitmap to the size of the canvas
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth * .3), (int) (bitmapHeight * .3), true);
        }

        int bitmapX = bitmap.getWidth() / 2;
        int bitmapY = bitmap.getHeight() / 2;

        int parentX = parentWidth / 2;
        int parentY = parentHeight / 2;

        int centerX = parentX - bitmapX;
        int centerY = parentY - bitmapY;

        int rotation = (int) (calibrated - bearing);

        matrix.reset();
        // Rotate the bitmap around it's center point so it's always pointing
        // north
        matrix.setRotate(rotation, bitmapX, bitmapY);
        // Move the bitmap to the center of the canvas
        matrix.postTranslate(centerX, centerY);

        canvas.drawBitmap(bitmap, matrix, paint);

        drawing.set(false);
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
        invalidate();
    }

    public void setCalibrated(int calibrated) {
        this.calibrated = calibrated;
        invalidate();
    }
}