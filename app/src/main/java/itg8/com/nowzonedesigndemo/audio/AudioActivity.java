package itg8.com.nowzonedesigndemo.audio;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.audio.widget.media.AnimatorPath;
import itg8.com.nowzonedesigndemo.audio.widget.media.PathEvaluator;
import itg8.com.nowzonedesigndemo.audio.widget.media.PathPoint;

public class AudioActivity extends AppCompatActivity {


    public final static float SCALE_FACTOR = 8f;
    public final static int ANIMATION_DURATION = 300;
    public final static int MINIMUN_X_DISTANCE = 200;
    private static final float MINIMUN_Y_DISTANCE = 200;
    private static final String TAG = AudioActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_one)
    FloatingActionButton fabOne;
    @BindView(R.id.fab_two)
    FloatingActionButton fabTwo;
    @BindView(R.id.fab_three)
    FloatingActionButton fabThree;
    @BindView(R.id.fab_four)
    FloatingActionButton fabFour;
    @BindView(R.id.fab_five)
    FloatingActionButton fabFive;
    @BindView(R.id.fab_six)
    FloatingActionButton fabSix;
    @BindView(R.id.fab_seven)
    FloatingActionButton fabSeven;
    @BindView(R.id.fab_eight)
    FloatingActionButton fabEight;
    @BindView(R.id.fab_nine)
    FloatingActionButton fabNine;
    @BindView(R.id.fab_ten)
    FloatingActionButton fabTen;
    @BindView(R.id.media_controls_container)
    LinearLayout mediaControlsContainer;
    @BindView(R.id.fram_layout)
    FrameLayout framLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private int mFabSize;
    private boolean mRevealFlag;
    private AnimatorListenerAdapter mEndRevealListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            fabTen.setVisibility(View.INVISIBLE);
            framLayout.setBackgroundColor(getResources()
                    .getColor(R.color.colorOrange));

            for (int i = 0; i < framLayout.getChildCount(); i++) {
                View v = framLayout.getChildAt(i);
                ViewPropertyAnimator animator = v.animate()
                        .scaleX(1).scaleY(1)
                        .setDuration(ANIMATION_DURATION);

                animator.setStartDelay(i * 50);
                animator.start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mFabSize = getResources().getDimensionPixelSize(R.dimen.fabSize);
                bindViews();
            }


        });




    }

    private void bindViews() {
        final float startX = fabTen.getX();
        final float startY = fabTen.getY();
        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.curveTo(400, 450, -400, 450, 0, 100);
        //path.curveTo(400, 450, -400, 450, 0, 100);
//        path.curveTo(-100, 200, -200, 100, -300, 50);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG,"FAb Y:"+fabTen.getY());
                if (Math.abs(startY - fabTen.getY()) > MINIMUN_Y_DISTANCE) {
                    if (!mRevealFlag) {
                        framLayout.setVisibility(View.VISIBLE);

                        framLayout.setY(framLayout.getY() + mFabSize / 2);

                        fabTen.animate()
                                .scaleXBy(SCALE_FACTOR)
                                .scaleYBy(SCALE_FACTOR)
                                .setListener(mEndRevealListener)
                                .setDuration(ANIMATION_DURATION);

                        mRevealFlag = true;
                    }
                }
            }
        });
    }

    /**
     * We need this setter to translate between the information the animator
     * produces (a new "PathPoint" describing the current animated location)
     * and the information that the button requires (an xy location). The
     * setter will be called by the ObjectAnimator given the 'fabLoc'
     * property string.
     */
    public void setFabLoc(PathPoint newLoc) {
        fabTen.setTranslationX(newLoc.mX);

        if (mRevealFlag)
            fabTen.setTranslationY(newLoc.mY - (mFabSize / 2));
        else
            fabTen.setTranslationY(newLoc.mY);
    }


}
