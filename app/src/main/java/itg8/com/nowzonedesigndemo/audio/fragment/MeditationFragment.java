package itg8.com.nowzonedesigndemo.audio.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.widget.maditation.SpinKitView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeditationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditationFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.lbl_meditation)
    CustomFontTextView lblMeditation;
    @BindView(R.id.lbl_meditation_count)
    CustomFontTextView lblMeditationCount;
//    @BindView(R.id.SpinView)
//    SpinKitView SpinView;
    @BindView(R.id.img_play)
    ImageView imgPlay;
    @BindView(R.id.img_pause)
    ImageView imgPause;
    @BindView(R.id.img_complete)
    ImageView imgComplete;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.txt_time)
    CustomFontTextView txtTime;
    @BindView(R.id.img_time)
    ImageView imgTime;
    @BindView(R.id.img_)
    ImageView img;
    @BindView(R.id.img_reply)
    ImageView imgReply;
    @BindView(R.id.img_like)
    ImageView imgLike;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MeditationFragment meditationFragment;
    private Animation animFadeOut;
    private Animation animFadeIn;


    public MeditationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditationFragment newInstance(String param1, String param2) {
        MeditationFragment fragment = new MeditationFragment();
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
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);
        unbinder = ButterKnife.bind(this, view);
        setInit();


//        ViewCompat.setTransitionName(view.findViewById(R.id.ivLogo2), "2");
        return view;
    }

    private void setInit() {
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        imgPlay.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        imgComplete.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
         switch (v.getId())
         {
             case R.id.img_pause:
                 imgPlay.setVisibility(View.VISIBLE);
                 imgPause.setVisibility(View.GONE);
                 final Animation myAnims = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_down);
                 imgPlay.startAnimation(myAnims);
                 break;
             case R.id.img_play:
                 imgPlay.setVisibility(View.GONE);
                 imgPause.setVisibility(View.VISIBLE);
                 final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_up);
                 imgPause.startAnimation(myAnim);
                 break;
             case R.id.img_reply:
                 break;
             case R.id.img_complete:
                 break;
         }

    }
}
