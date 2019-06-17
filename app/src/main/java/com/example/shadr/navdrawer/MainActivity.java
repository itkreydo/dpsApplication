package com.example.shadr.navdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class MainActivity extends AppCompatActivity {
    String [] scope = new String[]{VKScope.FRIENDS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VKSdk.isLoggedIn()){
            Intent i = new Intent(this,NavigationDrawer.class);
            startActivity(i);
            Toast.makeText(this, "LoggedIn", Toast.LENGTH_SHORT).show();
        };
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


    }
    public void vkLogin(View v){
        VKSdk.login(this, scope);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                Toast.makeText(MainActivity.this, "GOOD", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(),NavigationDrawer.class);
                startActivity(i);
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)

                Toast.makeText(MainActivity.this, "Error auth", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
