package itg8.com.nowzonedesigndemo.steps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayWeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayWeekFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.img_right)
    ImageView imgRight;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SampleAdapter adapter;


    public DayWeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayWeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayWeekFragment newInstance(String param1, String param2) {
        DayWeekFragment fragment = new DayWeekFragment();
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
        View view = inflater.inflate(R.layout.fragment_day_week, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new SampleAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        imgLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_left) {
            changeViewpagerBackward();
        } else if (view.getId() == R.id.img_right) {
            changeViewpagerForward();
        }
    }

    private void changeViewpagerForward() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1 < adapter.getCount() ? viewpager.getCurrentItem() + 1 : 0);
    }

    private void changeViewpagerBackward() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() - 1 > 0 ? viewpager.getCurrentItem() - 1 : adapter.getCount()-1);
    }


    public class SampleAdapter extends FragmentPagerAdapter {
        public SampleAdapter(FragmentManager mgr) {
            super(mgr);
        }

        @Override
        public int getCount() {
            return (10);
        }

        @Override
        public Fragment getItem(int position) {
            return (WeekFragment.newInstance());
        }

        @Override
        public String getPageTitle(int position) {
            return "";
        }
    }
}
