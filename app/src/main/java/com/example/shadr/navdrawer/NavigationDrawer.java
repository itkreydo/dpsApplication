package com.example.shadr.navdrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadr.navdrawer.fragment.FragmentDialog;
import com.example.shadr.navdrawer.fragment.FragmentGallery;
import com.example.shadr.navdrawer.fragment.FragmentSettings;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//Состав команды
//Dima  pidr
//Igor pidr
    FragmentGallery fgallery;
    FragmentDialog fdialogs;
    FragmentSettings fsettings;

    FragmentTransaction ftrans;
    TextView username_view;
    ImageView avatar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println(Arrays.asList(fingerprints));

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

        View navHeader = navigationView.getHeaderView(0);
        avatar_view = (ImageView) navHeader.findViewById(R.id.user_avatar);
        username_view = (TextView) navHeader.findViewById(R.id.user_name);
        TextView exit_view = (TextView) navHeader.findViewById(R.id.nav_header_exit);
        exit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //VKRequest request = new VKRequest("account.getProfileInfo");
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_100"));
        request.setPreferredLang("ru");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                //Do complete stuff
                Log.d("1223" ,response.responseString);
                try {
                    String name = response.json.getJSONArray("response").getJSONObject(0).getString("first_name");
                    String lastname = response.json.getJSONArray("response").getJSONObject(0).getString("last_name");
                    String photoUrl = response.json.getJSONArray("response").getJSONObject(0).getString("photo_100");
                    getBitmapFromUrl getBitmapFromUrl = new getBitmapFromUrl(getApplicationContext(), photoUrl, avatar_view) {
                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            iv.setImageBitmap(bitmap);
                        }
                    };

                    getBitmapFromUrl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    username_view.setText(lastname+" "+name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onError(VKError error) {
                //Do error stuff
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                //I don't really believe in progress
            }
        });
        //TextView profession_view = (TextView) navHeader.findViewById(R.id.profession_title);


        fgallery = new FragmentGallery();
        fdialogs = new FragmentDialog();
        fsettings = new FragmentSettings();

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
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            VKSdk.logout();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
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
