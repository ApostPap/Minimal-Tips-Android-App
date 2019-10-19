package com.apostpapad.dailytips;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;


import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";



    private PreferencesConfig preferencesConfig;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Button drawerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesConfig = new PreferencesConfig(this);



        if(preferencesConfig.readCurrentTheme().equals(getString(R.string.theme_minimal_white)))
        {
            setTheme(R.style.AppTheme);
        }
        else if(preferencesConfig.readCurrentTheme().equals(getString(R.string.theme_dark_night)))
        {
            setTheme(R.style.DarkAppTheme);
        }

       // setTheme(R.style.DarkAppTheme);
       // Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_main);



     //   createNotificationChannel();

        if (true) {
            Log.d(TAG, "onCreate: Adds are not pause.");
              //MobileAds.initialize(this,getString(R.string.adAppIDTest));
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
        }
        toolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.INVISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        drawerBtn = findViewById(R.id.drawerButton);
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  hideSoftKeyboard(MainActivity.this);
                closeKeyboard();
                openDrawer();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.drawer_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
          //     enableNotification();
            //setNotif();
            navigationView.setCheckedItem(R.id.item_home);
            //  navigationView.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String itemName = (String) menuItem.getTitle();
        Log.d(TAG, "onNavigationItemSelected: Clicked " + itemName);

        switch (menuItem.getItemId()) {
            case R.id.item_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
                drawerBtn.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.INVISIBLE);
                break;
            case R.id.item_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FavoritesFragment()).commit();
                drawerBtn.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.INVISIBLE);

                break;
            case R.id.item_add_tip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new AddTipFragment()).commit();
                drawerBtn.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.INVISIBLE);

                break;
            case R.id.item_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsFragment()).commit();
                drawerBtn.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                break;

        }

        closeDrawer();


        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public  void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
            drawerBtn.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
        }

        //  startActivity(new Intent(this, TermsConditionsActivity.class));

        return super.onOptionsItemSelected(item);
    }

/*
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel_name";
            String description = "channel_desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setNotif() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notifIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }


    private void enableNotification() {
        Log.d(TAG, "enableNotification: ");
        Date date = new Date(System.currentTimeMillis());
        Calendar nowDate = Calendar.getInstance();
        nowDate.setTime(date);
        nowDate.get(Calendar.HOUR_OF_DAY);
        if(nowDate.get(Calendar.HOUR_OF_DAY)>8 && nowDate.get(Calendar.MINUTE)>1) {

            Log.d(TAG, "enableNotification: Time has passed for today");
        }
        else {

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 40);
            calendar.set(Calendar.SECOND, 30);

            Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
            intent.setAction("MY_NOTIFICATION_MESSAGE");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
*/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else if (navigationView.getCheckedItem() != navigationView.getMenu().getItem(0)) {
            //If fragment not home, onBack go to home fragment
            navigationView.setCheckedItem(R.id.item_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
            drawerBtn.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.INVISIBLE);

        } else {


            super.onBackPressed();
        }
    }


}
