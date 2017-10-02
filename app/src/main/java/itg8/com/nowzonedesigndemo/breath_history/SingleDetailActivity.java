package itg8.com.nowzonedesigndemo.breath_history;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.Helper;
import itg8.com.nowzonedesigndemo.widget.AnimationUtils;
import itg8.com.nowzonedesigndemo.widget.RevealAnimationSetting;

public class SingleDetailActivity extends AppCompatActivity implements View.OnClickListener, AnimationUtils.AnimationFinishedListener, AnimationUtils.Dismissable, OnMapReadyCallback {
    //,LocationListener
    private static final String TAG = SingleDetailActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lbl_state)
    TextView lblState;
    @BindView(R.id.lbl_time)
    TextView lblTime;
    @BindView(R.id.lbl_diff)
    TextView lblDiff;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab)
    ImageButton fabCommentAddBtn;
    @BindView(R.id.lbl_comments)
    CustomFontTextView lblComments;
    @BindView(R.id.rl_card)
    LinearLayout rlCard;
    @BindView(R.id.txt_comments)
    CustomFontTextView txtComments;
    //    @BindView(R.id.cardView)
//    CardView cardView;

    @BindView(R.id.lbl_location)
    CustomFontTextView lblLocation;
    @BindView(R.id.rl_card_location)
    LinearLayout rlCardLocation;
    @BindView(R.id.txt_address)
    CustomFontTextView txtAddress;
    @BindView(R.id.card_location)
    CardView cardLocation;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.img_chat_icon)
    ImageView imgChatIcon;
    //    @BindView(R.id.btn_add)
//    Button btnAdd;
//    @BindView(R.id.img_add_icon)
//    ImageView imgAddIcon;
//    @BindView(R.id.lbl_add_comments)
//    CustomFontTextView lblAddComments;
//    @BindView(R.id.rl_card_add)
//    LinearLayout rlCardAdd;
//    @BindView(R.id.txt_comment)
//    CustomFontTextView txtComment;
//    @BindView(R.id.card_add_comment)
//    CardView cardAddComment;
    int bgColor = 0;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.img_icon)
    ImageView imgIcon;

    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.input_comment)
    TextInputLayout inputComment;
    @BindView(R.id.cardview_comment_add)
    CardView cardCommnetAdd;
    @BindView(R.id.img_icon_location)
    ImageView imgIconLocation;
    @BindView(R.id.fab_check)
    ImageButton fabCommentOkBtn;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.cardview_comment)
    CardView cardviewCommnet;
    @BindView(R.id.rl_main_card)
    RelativeLayout rlMainCard;
    @BindView(R.id.view)
    View view;
    AnimationUtils.Dismissable listener;
    private TblState state;
    private String comment;
    private int cx;
    private int cy;
    private int mainWidth;
    private int mainHeight;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        init();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(21.117068, 79.0448063);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Nagpur"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        //mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);


    }


    private void init() {
        fabCommentAddBtn.setOnClickListener(this);
        fabCommentOkBtn.setOnClickListener(this);
        String titile = null;
        TextView diff = (TextView) findViewById(R.id.lbl_diff);
        TextView time = (TextView) findViewById(R.id.lbl_time);
        ImageView imageView = (ImageView) findViewById(R.id.img_icon);

//        imageView.setTransitionName(imageView.getTransitionName());
//        time.setTransitionName(time.getTransitionName());
//        diff.setTransitionName(diff.getTransitionName());

        diff.setTransitionName(getString(R.string.diff));
        imageView.setTransitionName(getString(R.string.img));
        time.setTransitionName(getString(R.string.time));


        if (getIntent().hasExtra(CommonMethod.STATE)) {
            state = getIntent().getParcelableExtra(CommonMethod.STATE);
            time.setText(Helper.getDateTimeFromMillies(state.getTimestampStart()) + "\n-\n" + Helper.getDateTimeFromMillies(state.getTimestampEnd()));
            // diff.setText(Helper.calculateMinuteDiffFromTimestamp(state.getTimestampEnd(), state.getTimestampStart()));
            String dif = (Helper.calculateMinuteDiffFromTimestamp(state.getTimestampStart(), state.getTimestampEnd()));
            lblDiff.setText(dif);


            Log.d(TAG, "Diff:" + diff);
            switch (state.getState()) {

                case "FOCUSED":
                    appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_attentive_half));
                    imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.focus_streak_icon));
                    titile = getString(R.string.focus_state);
                    bgColor = R.color.color_attentive_half;
                    // rlCard.setBackgroundColor(ContextCompat.getColor(this, R.color.color_attentive_half));
                    rlCardLocation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_attentive_half));


                    break;
                case "CALM":
                    appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_composed_half));
                    imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.calm_streak_icon));
                    titile = getString(R.string.compose_state);
                    bgColor = R.color.color_composed_half;
                    // rlCard.setBackgroundColor(ContextCompat.getColor(this, R.color.color_composed_half));
                    rlCardLocation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_composed_half));


                    break;
                case "STRESS":
                    appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_stress_half));
                    imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.stress_streak_icon));
                    bgColor = R.color.color_stress_half;
                    //  rlCard.setBackgroundColor(ContextCompat.getColor(this, R.color.color_stress_half));
                    rlCardLocation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_stress_half));

                    titile = getString(R.string.stress_state);
                    break;
                case "SEDENTARY":
                    appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_normal_half));
                    toolbarLayout.setContentScrimColor(R.color.color_normal_half);
                    imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.sedentory_icon));
                    titile = getString(R.string.silent_state);
                    bgColor = R.color.color_normal_half;
                    // rlCard.setBackgroundColor(ContextCompat.getColor(this, R.color.color_normal_half));
                    rlCardLocation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_normal_half));
                    break;
                case "ACTIVITY":
                    appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_steps_half));
                    toolbarLayout.setContentScrimColor(R.color.color_steps_half);
                    imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.activity_icon));
                    titile = getString(R.string.activity);
                    //  rlCard.setBackgroundColor(ContextCompat.getColor(this, R.color.color_steps_half));
                    rlCardLocation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_steps_half));
                    bgColor = R.color.color_steps_half;
                    break;

            }
        }

        if (titile != null) {

            lblState.setText(titile);
            toolbarLayout.setTitle(titile);
            toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));
            toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }
        toolbarLayout.setContentScrimColor(bgColor);
        toolbarLayout.setStatusBarScrimColor(bgColor);
        imgChatIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite));
        lblComments.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        imgIconLocation.setColorFilter(Color.WHITE);
        fabCommentAddBtn.setColorFilter(ContextCompat.getColor(this, bgColor));
        rlCard.setBackgroundColor(ContextCompat.getColor(this, bgColor));
//
//   if (comment == null) {
        //  showHideItem(cardCommnetAdd, cardviewCommnet);
//            // animateRevealColor(rlMainCard, bgColor);
//        } else {

        showHideItem(cardviewCommnet, cardCommnetAdd);
        txtComments.setTextColor(ContextCompat.getColor(this, bgColor));
        //}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                //OpenBottomSheetFragment();
                break;
            case R.id.fab:
                setFabAddCommnet();
                break;
            case R.id.fab_check:
                setFabSaveComment();
                break;
        }

    }

    private void setFabSaveComment() {

        animateRevealColor(cardviewCommnet, bgColor, false);
        showHideItem(fabCommentAddBtn, fabCommentOkBtn);
        fabCommentAddBtn.setImageResource(R.drawable.ic_add);
        fabCommentAddBtn.setColorFilter(ContextCompat.getColor(this, bgColor));
        showHideItem(cardviewCommnet, cardCommnetAdd);
        comment = edtComment.getText().toString().trim();
        txtComments.setText(comment);


    }

    private void setFabAddCommnet() {

        showHideItem(fabCommentOkBtn, fabCommentAddBtn);
        fabCommentOkBtn.setImageResource(R.drawable.ic_ok);
        fabCommentOkBtn.setColorFilter(ContextCompat.getColor(this, bgColor));
        showHideItem(cardCommnetAdd, cardviewCommnet);
        animateRevealColor(cardCommnetAdd, bgColor, true);


    }


    private void animateRevealColor(CardView viewRoot, int color, boolean isForward) {
        cx = (viewRoot.getRight());
        cy = (viewRoot.getTop());
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy, isForward);
    }

    private Animator animateRevealColorFromCoordinates(CardView viewRoot, int color, int x, int y, boolean isForward) {
        mainWidth = viewRoot.getWidth();
        mainHeight = viewRoot.getHeight();


        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
        float startRadius = isForward ? 0 : finalRadius;
        float endRadius = isForward ? finalRadius : 0;

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, startRadius, endRadius);

        viewRoot.setCardBackgroundColor(ContextCompat.getColor(this, color));


        anim.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isForward) {
                    viewRoot.setCardBackgroundColor(Color.WHITE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return anim;
    }


    private RevealAnimationSetting setRevealAnimationSetting() {


        return new RevealAnimationSetting() {
            @Override
            public int getCenterX() {
                return cx;
            }

            @Override
            public int getCenterY() {
                return cy;
            }

            @Override
            public int getWidth() {
                return mainWidth;
            }

            @Override
            public int getHeight() {
                return mainHeight;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onAnimationFinished() {


    }

    @Override
    public void dismiss(OnDismissedListener listener) {

        AnimationUtils.startCreateShareLinkCircularRevealExitAnimation(this, rlMainCard, bgColor, setRevealAnimationSetting(), new OnDismissedListener() {
            @Override
            public void onDismissed() {
                listener.onDismissed();
            }
        });


    }

    private void showHideItem(View show, View hide) {
        show.setVisibility(View.VISIBLE);
        hide.setVisibility(View.GONE);
    }


//    @Override
//    public void onLocationChanged(Location location) {
////        double latitude = location.getLatitude();
////        double longitude = location.getLongitude();
////        Log.d(TAG,"Latitude:" + latitude + ", Longitude:" + longitude);
////        LatLng latLng = new LatLng(latitude, longitude);
////        mMap.addMarker(new MarkerOptions().position(latLng));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
