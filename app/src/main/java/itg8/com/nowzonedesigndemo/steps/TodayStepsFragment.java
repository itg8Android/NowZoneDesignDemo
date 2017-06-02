package itg8.com.nowzonedesigndemo.steps;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayStepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayStepsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = TodayStepsFragment.class.getSimpleName();


    Unbinder unbinder;

    @BindView(R.id.rl_releative)
    RelativeLayout rlReleative;
    @BindView(R.id.customProgressRectangle)
    ProgressBar customProgressRectangle;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean mShowUnit = true;


    public TodayStepsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodayStepsFragment newInstance(String param1, String param2) {
        TodayStepsFragment fragment = new TodayStepsFragment();
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
        View view = inflater.inflate(R.layout.fragment_today_steps, container, false);

        unbinder = ButterKnife.bind(this, view);
        setProgressbar();
        customProgressRectangle.setProgress(50);
        return view;
    }

    private void setProgressbar() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.custom_progressbar);
        customProgressRectangle.setProgress(25);
        customProgressRectangle.setSecondaryProgress(50);
        customProgressRectangle.setMax(100);
        customProgressRectangle.setProgressDrawable(drawable);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
