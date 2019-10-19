package com.apostpapad.dailytips;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.Date;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.CLIPBOARD_SERVICE;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private PreferencesConfig preferencesConfig;
    private Date startDate, todayDate;
    private String[] tips;
    private int[] favoriteIndexes;
    private int shownTipIndex, dayDiff;
    private boolean favoriteBool;

    private TextView dayTextView, tipTextVIew;

    private int heartOnInt, heartOffInt;
    private ImageButton heartButton, shareBtn, copyToClipBoardBtn;
    private String showingTip;

    // private AdView adView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        initLayout(view);
        init();


        return view;
    }


    private void init() {

        preferencesConfig = new PreferencesConfig(getContext());

        String currentTheme = preferencesConfig.readCurrentTheme();
        Log.d(TAG, "init: current theme: " + currentTheme);
        if (currentTheme.equals(getString(R.string.theme_minimal_white))) {
            heartOffInt = R.drawable.ic_heart_off_white_icon;
            heartOnInt = R.drawable.ic_heart_on_white_icon;
        } else if (currentTheme.equals(getString(R.string.theme_dark_night))) {
            heartOffInt = R.drawable.ic_heart_off_black;
            heartOnInt = R.drawable.ic_heart_on_black;
        }


        favoriteBool = false;


        favoriteIndexes = preferencesConfig.readFavoriteTips();


        startDate = preferencesConfig.readStartDay();
        todayDate = new Date(System.currentTimeMillis());


        if (!startDate.after(new Date(0))) {
            Log.d(TAG, "New startDate Added.");
            preferencesConfig.writeStartDay(todayDate);
            startDate = preferencesConfig.readStartDay();
        }
        dayDiff = getDifferenceDays(startDate, todayDate);


        // TIPS
        tips = getResources().getStringArray(R.array.tips);
        Log.d(TAG, "init: tips: " + tips.length);


        shownTipIndex = (dayDiff % tips.length) - 1;
        dayTextView.setText("Day " + dayDiff);
        showTip();



        // Remove comment and add an adView to display ads
        /*
        if (!preferencesConfig.readAdPause()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Log.d(TAG, "onAdLoaded: adLoaded");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    Log.d(TAG, "onAdFailedToLoad: ");
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    Log.d(TAG, "onAdOpened: ");
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                    Log.d(TAG, "onAdClicked: ");
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                    Log.d(TAG, "onAdLeftApplication: user left the app");
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                    Log.d(TAG, "onAdClosed: return after tap");
                }
            });
        }*/


    }


    /**
     * Initiate Layout
     *
     * @param view Fragment View
     */
    private void initLayout(View view) {

        // Day TextView
        dayTextView = view.findViewById(R.id.dayTV);
        // Tip TextView
        tipTextVIew = view.findViewById(R.id.tipTextView);


        // Copy Tip to ClipBoard Button
        copyToClipBoardBtn = view.findViewById(R.id.copyButton);
        copyToClipBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Copied to clipboard.", Toast.LENGTH_SHORT).show();

                ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", showingTip);
                clipboard.setPrimaryClip(clip);
            }
        });

        // Heart/Favorite Button
        heartButton = view.findViewById(R.id.heartButton);
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteBool) {
                    heartButton.setImageResource(heartOffInt);
                    removeFromFavorites();
                    favoriteBool = false;

                } else {
                    heartButton.setImageResource(heartOnInt);
                    addToFavorites();

                }
            }
        });

        // Share Button
        shareBtn = view.findViewById(R.id.shareButton);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Day " + dayDiff + " : " + showingTip + "\nShared via the " + getString(R.string.app_name) + " app.";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tip of the Day");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using.."));

            }
        });

        // AdView
        //  adView = view.findViewById(R.id.adView);


    }


    private void showTip() {
        Random random = new Random(System.currentTimeMillis());
        int chance = random.nextInt(1000);
        String[] userAddedTips = preferencesConfig.readUserAddedTipsArray();
        if (userAddedTips.length == 1 && userAddedTips[0].equals("-1")) {
            Log.d(TAG, "showTip: empty user added tips array, show from xml.");
            showingTip = tips[shownTipIndex];

        } else {
            if (chance < 100) // 10% chance of reading user added tips
            {
                Log.d(TAG, "showTip: show tip from user added tip array");
                showingTip = userAddedTips[random.nextInt(userAddedTips.length)];


            } else {
                Log.d(TAG, "showTip: show tip from xml");
                showingTip = tips[shownTipIndex];
            }
        }
        if (isFavorite()) {
            heartButton.setImageResource(heartOnInt);
            favoriteBool = true;

        } else {
            favoriteBool = false;

            heartButton.setImageResource(heartOffInt);
        }

        tipTextVIew.setText(showingTip);


    }


    /**
     * Calculates the difference between starting date and today
     * @param d1 Starting Date
     * @param d2 Today Date
     * @return day difference
     */
    private int getDifferenceDays(Date d1, Date d2) {
        int daysdiff = 0;

        return daysdiff;
    }


    /**
     * Calls method from shared preferences to add tip to favorites
     */
    private void addToFavorites() {


        Toast.makeText(getContext(), "Added to favorites.", Toast.LENGTH_SHORT).show();

    }


    /**
     * Calls method from shared preferences to remove tip to favorites
     */
    private void removeFromFavorites() {

        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

    }

    /**
     * Check if tip is in favorites
     */
    private boolean isFavorite() {

        return false;
    }

}
