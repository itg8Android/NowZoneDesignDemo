package itg8.com.nowzonedesigndemo.breath;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath.mvp.BreathHistoryMVP;
import itg8.com.nowzonedesigndemo.breath.mvp.BreathHistoryPresenterImp;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;

import static android.support.v7.recyclerview.R.attr.layoutManager;

public class BreathHistoryActivity extends AppCompatActivity implements BreathHistoryMVP.BreathHistoryView {


    BreathHistoryMVP.BreathHistoryPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_history);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        presenter=new BreathHistoryPresenterImp(this);
        presenter.initListOfState();

    }

    @Override
    public void onListAvailable(List<TblState> list) {
        BreathHistoryAdapter adapter = new BreathHistoryAdapter(list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(),
                layoutManager.getOrientation());
        recyclerview.addItemDecoration(dividerItemDecoration);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onErrorLoading(String error) {

    }

    @Override
    public void onConnectionFailed(String issue) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
