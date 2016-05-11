package io.github.jianloong.calllogtocal;

import android.Manifest;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks{

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;

    private ViewPager pager;

    // For the action bar tabs
    private static final String ACTION_BAR_TITLES[] = new String[]{"Calls", "SMS"};

    // For the navigation drawer
    private static final String DRAWER_TITLES[] = new String[]{"Home"};

    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;

    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.navdrawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setHorizontalScrollBarEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }

        pager = (ViewPager) findViewById(R.id.viewpager);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        //pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), ACTION_BAR_TITLES));

        slidingTabLayout.setDistributeEvenly(true);

        // Check permissions here

        //slidingTabLayout.setViewPager(pager);

        setActionBar();

        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.MAGENTA;
            }
        });

        slidingTabLayout.setHorizontalScrollBarEnabled(false);

        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name);

        mDrawerLayout.addDrawerListener(drawerToggle);
        setmDrawerLayout();
    }

    /**
     * Set the navigation drawer layout
     */
    private void setmDrawerLayout() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                DRAWER_TITLES);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int colour = 0;
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                // GravityCompat to handle android M issues.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void setActionBar(){

        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), ACTION_BAR_TITLES));

        String[] perms = {Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG};

        if (EasyPermissions.hasPermissions(
                this, perms
        )) {
            slidingTabLayout.setViewPager(pager);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your SMS logs.",
                    REQUEST_PERMISSIONS,
                    perms
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

}
