package itg8.com.nowzonedesigndemo.sleep;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.sleep.adapter.ViewPagerSleepAdapter;

public class SleepActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.txt_day)
    TextView txtDay;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.img_graph)
    ImageView imgGraph;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViewPager();


    }

    private void setUpViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new ViewPagerSleepAdapter(this,fm));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
