package edu.sjsu.stocksearch;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class StockActivity extends AppCompatActivity {
    private static final String TAG = "StockActivity";
    private TAdapter mTAdapter;
    private ViewPager mViewPager;
    private Toolbar mActionBarToolbar;
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Log.d(TAG, "onCreate: Starting");
        symbol = getIntent().getStringExtra("symbol");
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(symbol);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTAdapter = new TAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections pager
        mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        TAdapter adapter = new TAdapter(getSupportFragmentManager());
        adapter.addFragment(new Frag1(),"CURRENT");
        adapter.addFragment(new Frag2(),"HISTORICAL");
        viewPager.setAdapter(adapter);    }
    public String getSymbol(){
        return symbol;
    }
}
