package itg8.com.nowzonedesigndemo.breath_history.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomDialogFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.txt_dialogue)
    CustomFontTextView txtDialogue;
    @BindView(R.id.fab_bad_posture)
    FloatingActionButton fabBadPosture;
    @BindView(R.id.fab_good)
    FloatingActionButton fabGood;
    @BindView(R.id.progress_view)
    ProgressBar progressView;
    @BindView(R.id.txt_note)
    CustomFontTextView txtNote;
    Unbinder unbinder;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    @BindView(R.id.fab_Ok)
    FloatingActionButton fabOk;
    @BindView(R.id.ll_bad_position)
    LinearLayout llBadPosition;
    @BindView(R.id.ll_ok)
    LinearLayout llOk;
    @BindView(R.id.ll_good_position)
    LinearLayout llGoodPosition;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private ObjectAnimator positionAnimation;


    public CustomDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomDialogFragment newInstance(String param1, String param2) {
        CustomDialogFragment fragment = new CustomDialogFragment();
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
        View view = inflater.inflate(R.layout.fragment_custom_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        fabBadPosture.setOnClickListener(this);
        fabGood.setOnClickListener(this);
        fabOk.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_bad_posture:
                // getDialog().dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_top, R.anim.bounce_down).remove(this).commit();
                break;
            case R.id.fab_good:
                saveCalibrate();
                break;
            case R.id.fab_Ok:
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_top, R.anim.bounce_down).remove(this).commit();
                break;

        }

    }

    private void saveCalibrate() {
        Prefs.putBoolean(CommonMethod.CALIBRATE, true);
        int width = llParent.getWidth() / 2;
        fabBadPosture.setVisibility(View.GONE);
        positionAnimation = ObjectAnimator.ofFloat(fabGood, View.X, width);
        positionAnimation.start();
        progressView.setVisibility(View.VISIBLE);
        progressView.setIndeterminate(true);
        progressView.setProgress(100);
        progressView.setVisibility(View.GONE);
        showHideView(llOk, llGoodPosition);
        showHideView(llOk, llBadPosition);


    }
   private void showHideView(View show,View hide){
       show.setVisibility(View.VISIBLE);
       hide.setVisibility(View.GONE);

    }
}
