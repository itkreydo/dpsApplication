package com.example.shadr.navdrawer;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shadr.navdrawer.fragment.FragmentGallery;
import com.example.shadr.navdrawer.fragment.FragmentImport;
import com.example.shadr.navdrawer.fragment.FragmentSend;
import com.example.shadr.navdrawer.fragment.FragmentShare;
import com.example.shadr.navdrawer.fragment.FragmentSlideshow;
import com.example.shadr.navdrawer.fragment.FragmentTools;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentGallery fgallery;
    FragmentImport fimport;
    FragmentSend fsend;
    FragmentShare fshare;
    FragmentSlideshow fslideshow;
    FragmentTools ftools;

    FragmentTransaction ftrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final ConstraintLayout content = findViewById(R.id.container);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);

                // а также меняем размер
                content.setScaleX(1 - slideOffset);
                content.setScaleY(1 - slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fgallery = new FragmentGallery();
        fimport = new FragmentImport();
        fsend = new FragmentSend();
        fshare = new FragmentShare();
        fslideshow = new FragmentSlideshow();
        ftools = new FragmentTools();

        ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.container, fgallery);
        ftrans.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ftrans = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_camera) {
            ftrans.replace(R.id.container, fimport);
        } else if (id == R.id.nav_gallery) {
            ftrans.replace(R.id.container, fgallery);
        } else if (id == R.id.nav_slideshow) {
            ftrans.replace(R.id.container, fslideshow);
        } else if (id == R.id.nav_manage) {
            ftrans.replace(R.id.container, ftools);
        } else if (id == R.id.nav_share) {
            ftrans.replace(R.id.container, fshare);
        } else if (id == R.id.nav_send) {
            ftrans.replace(R.id.container, fsend);
        }
        ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}