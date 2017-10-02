package itg8.com.nowzonedesigndemo.breath_history.fragment;


import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.BreathHistoryDetailsActivity;
import itg8.com.nowzonedesigndemo.breath_history.SleepDetailActivity;
import itg8.com.nowzonedesigndemo.breath_history.adapter.OnTodaysItemClickListener;
import itg8.com.nowzonedesigndemo.breath_history.model.TodayStaModel;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.widget.RevealAnimationSetting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridLayoutFragment extends Fragment implements OnTodaysItemClickListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.img_stress)
    ImageView imgStress;
    @BindView(R.id.name)
    CustomFontTextView name;
    @BindView(R.id.txt_breath_value)
    CustomFontTextView txtBreathValue;
    @BindView(R.id.lbl_breath_time)
    CustomFontTextView lblBreathTime;
    @BindView(R.id.card_stress)
    CardView cardStress;
    @BindView(R.id.img_composed)
    ImageView imgComposed;
    @BindView(R.id.card_compose)
    CardView cardCompose;
    @BindView(R.id.img_attentive)
    ImageView imgAttentive;
    @BindView(R.id.card_attentive)
    CardView cardAttentive;
    @BindView(R.id.ll_breath)
    LinearLayout llBreath;


    @BindView(R.id.img_posture)
    ImageView imgPosture;

    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    CommonMethod.OnFragmentSendToActivityListener activityListener;
    @BindView(R.id.img_sedentary)
    ImageView imgSedentary;
    @BindView(R.id.lbl_silent)
    CustomFontTextView lblSilent;
    @BindView(R.id.txt_sedentary_value)
    CustomFontTextView txtSedentaryValue;
    @BindView(R.id.lbl_sedentary_time)
    CustomFontTextView lblSedentaryTime;
    @BindView(R.id.card_sedentary)
    CardView cardSedentary;
    @BindView(R.id.img_activity)
    ImageView imgActivity;
    @BindView(R.id.card_sleep)
    CardView cardSleep;
    @BindView(R.id.card_posture)
    CardView cardPosture;
    @BindView(R.id.card_activity)
    CardView cardActivity;
    @BindView(R.id.img_sleep)
    ImageView imgSleep;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private List<TodayStaModel> itemList;
    private int[] color = new int[6];
    private FragmentManager fm;
    private int cx;
    private int cy;
    private int mainWidth;
    private int mainHeight;
    private BreathState type;
    private View sharedView;

    private List<TblState> listState = new ArrayList<>();


    public GridLayoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridLayoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridLayoutFragment newInstance(String param1, String param2) {
        GridLayoutFragment fragment = new GridLayoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        activityListener.onHideToggle();
        return view;

    }

    private void init() {
        cardAttentive.setOnClickListener(this);
        cardStress.setOnClickListener(this);
        //cardBreathNormal.setOnClickListener(this);
        cardCompose.setOnClickListener(this);
        cardSedentary.setOnClickListener(this);
        cardActivity.setOnClickListener(this);
        cardSleep.setOnClickListener(this);
        cardPosture.setOnClickListener(this);


        //cardPosture.setOnClickListener(this);
        //cardActive.setOnClickListener(this);

    }

    private void setRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(new StaggeredItemAdapter(mContext, itemList, this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        fm = getActivity().getSupportFragmentManager();
        this.activityListener = (CommonMethod.OnFragmentSendToActivityListener) context;

    }

    @Override
    public void onItemClicked(View view, int position) {
    }

    private void animateRevealColor(ViewGroup viewRoot, int color) {
        cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy);
    }

    @Override
    public void onClick(View v) {
        int color = 0;
        switch (v.getId()) {
            case R.id.card_stress:
                color = R.color.color_stress_half;
                type = BreathState.STRESS;
                sharedView = imgStress;
                setFragmentDetails(color, sharedView);


                break;
            case R.id.card_compose:
                color = R.color.color_composed_half;
                type = BreathState.CALM;
                sharedView = imgComposed;
                setFragmentDetails(color, sharedView);

                break;
            case R.id.card_attentive:
                color = R.color.color_attentive_half;
                type = BreathState.FOCUSED;
                sharedView = imgAttentive;
                setFragmentDetails(color, sharedView);

                break;

            case R.id.card_sedentary:
                color = R.color.color_normal_half;
                type = BreathState.SEDENTARY;
                sharedView = imgSedentary;
                setFragmentDetails(color, sharedView);

                break;
            case R.id.card_activity:
                color = R.color.color_steps_half;
                type = BreathState.ACTIVITY;
                sharedView = imgActivity;
                setFragmentDetails(color, sharedView);

                break;
            case R.id.card_sleep:
                sharedView = imgSleep;
                type = BreathState.SLEEP;
                color = R.color.color_sleep_half;
                setActivitySleep();
                break;

            case R.id.card_posture:
                sharedView = imgPosture;
                color = R.color.color_posture_half;
                 type = BreathState.POSTURE;
                setFragmentDetails(color, sharedView);

        }


        //animateRevealColor((ViewGroup) v,  R.color.colorWhite);
//        R.color.colorWhite

        // setFragmentDetails();
    }

    private void setActivitySleep() {
        ActivityOptionsCompat options;
//        List<TblState> filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();
//       activityListener.onBackFragmentSendListener(TodaysStacDetailsFragment.newInstance(color, setRevealAnimationSetting(), filterList));
        Intent intent = new Intent(mContext, SleepDetailActivity.class);

        options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) mContext, sharedView, getString(R.string.activity_image_trans));
        startActivity(intent, options.toBundle());

    }


    private void setFragmentDetails(int color, View sharedView) {
        ActivityOptionsCompat options;
//        List<TblState> filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();
//        activityListener.onBackFragmentSendListener(TodaysStacDetailsFragment.newInstance(color, setRevealAnimationSetting(), filterList));
        Intent intent = new Intent(mContext, BreathHistoryDetailsActivity.class);
        options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) mContext, sharedView, getString(R.string.activity_image_trans));
        //intent.putSerializableExtra(CommonMethod.COLOR , type);
        //  startActivity(intent, options.toBundle());
        activityListener.onChangeToolbarColor(intent, this.type, options.toBundle());

    }

    private Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, int color, int x, int y) {
        mainWidth = scrollView.getWidth();
        mainHeight = scrollView.getHeight();


//        float finalRadius = (float) Math.hypot(scrollView.getWidth(), scrollView.getHeight());
//        Animator anim = ViewAnimationUtils.createCircularReveal(scrollView, x, y, 0, finalRadius);
//        scrollView.setBackgroundColor(color);
//
//        anim.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//        anim.setInterpolator(new AccelerateDecelerateInterpolator());
//        anim.start();
//        anim.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        return anim;
        return null;
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


}
