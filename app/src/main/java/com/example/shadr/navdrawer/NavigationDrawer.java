package com.example.shadr.navdrawer;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shadr.navdrawer.fragment.FragmentDialog;
import com.example.shadr.navdrawer.fragment.FragmentGallery;
import com.example.shadr.navdrawer.fragment.FragmentSettings;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//Состав команды
//Dima  pidr
//Igor pidr
    //hol xyu
    FragmentGallery fgallery;
    FragmentDialog fdialogs;
    FragmentSettings fsettings;

    FragmentTransaction ftrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().hide();

        final ConstraintLayout content = findViewById(R.id.container);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fgallery = new FragmentGallery();
        fdialogs = new FragmentDialog();
        fsettings = new FragmentSettings();

        ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.container, fgallery);
        ftrans.commit();


        //Для кнопки выйти красным
        //setTextColorForMenuItem(navigationView.getMenu().getItem(0), R.color.colorAccent);
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
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.replace(R.id.container, fgallery);
            ftrans.commit();
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
        if (id == R.id.nav_dialogs) {
            getSupportActionBar().setTitle("Диалоги");
            ftrans.replace(R.id.container, fdialogs);
        } else if (id == R.id.nav_map) {

            ftrans.replace(R.id.container, fgallery);
        } else if (id == R.id.nav_settings) {
            getSupportActionBar().setTitle("Настройки");
            ftrans.replace(R.id.container, fsettings);
        } else if (id == R.id.nav_exit) {

            //ftrans.replace(R.id.container, ftools);
        }
        ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Для кнопки Выйти
    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }
}
