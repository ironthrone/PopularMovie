package com.example.popularmovies.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.util.NetworkUtil;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener{

    private static final String DETAIL_FRAG_TAG = "detail";
    private final String TAG = this.getClass().getSimpleName();

    private NetworkChangeReceiver mReceiver;
    private MainFragment mMainFrag;
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainFrag = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.main_frag);

        if(findViewById(R.id.detail_container) != null){
            mTwoPane = true;
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.detail_container,)
        }
        registerNetworkChangeReceiver();
    }

    /**
     * register receiver for network changing
     */
    private void registerNetworkChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mReceiver = new NetworkChangeReceiver();
        registerReceiver(mReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Movie movie) {
        if(mTwoPane){
            DetailFragment detailFragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putSerializable("data",movie);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,
                    detailFragment,DETAIL_FRAG_TAG).commit();
        }else {
            Intent intent = new Intent(this,DetailActivity.class);
            intent.putExtra("data",movie);
            startActivity(intent);
        }
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {
        public NetworkChangeReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatus(context);
            if(status != NetworkUtil.TYPE_NO_CONNECT){
                mMainFrag.loadData();
            }

        }
    }
}
