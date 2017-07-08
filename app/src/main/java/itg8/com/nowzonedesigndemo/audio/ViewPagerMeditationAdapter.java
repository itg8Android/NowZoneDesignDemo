package itg8.com.nowzonedesigndemo.audio;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;


import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.audio.fragment.MeditationFragment;

/**
 * Created by Android itg 8 on 7/8/2017.
 */

public class ViewPagerMeditationAdapter extends FragmentPagerAdapter {


    private Context mContext;

    public ViewPagerMeditationAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Fragment getItem(int position) {
        // Create fragment object
        MeditationFragment fragment = new MeditationFragment ();

//        Bundle args = new Bundle();
//        args.putInt("page_position", position + 1);
//
//        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sb = new SpannableStringBuilder(" page "+(position+1) ); // space added before text for convenience
        Drawable drawable = mContext.getResources().getDrawable( R.drawable.ic_play_arrow_black_24dp );
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb ;
    }

}
