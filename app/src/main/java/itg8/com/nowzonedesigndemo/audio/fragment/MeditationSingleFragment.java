package itg8.com.nowzonedesigndemo.audio.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MeditationSingleFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.lbl_meditation)
    CustomFontTextView lblMeditation;
    @BindView(R.id.lbl_meditation_count)
    CustomFontTextView lblMeditationCount;
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
    private String fromPlay="fromPlay";

    public MeditationSingleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meditation_single, container, false);
        unbinder = ButterKnife.bind(this, view);
        setInit();
        return view;
    }

    private void setInit() {
        imgPlay.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        imgComplete.setOnClickListener(this);
        imgPause.setClickable(true);
        imgPlay.setClickable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        Animation myAnim = null;
        switch (v.getId()) {

            case R.id.img_pause:
                imgPause.setVisibility(View.GONE);
                //checkAnimation(fromPlay);

                myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_down);
                myAnim.setDuration(2000);

                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        imgPlay.startAnimation(animation);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imgPlay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.img_play:
                imgPlay.setVisibility(View.GONE);
              //  checkAnimation(fromPlay);
              myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_up);
               // imgPause.startAnimation(myAnim);
                myAnim.setDuration(2000);


                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        imgPause.startAnimation(animation);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imgPause.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            case R.id.img_reply:
                break;
            case R.id.img_complete:
                break;

        }
    }

}
