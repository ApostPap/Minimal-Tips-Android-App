package com.apostpapad.dailytips;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class PreferencesConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);

    }


    public void writeUploadCheckboxStatus(boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_upload_checkbox), checked);
        editor.apply();
    }


    public boolean readUploadCheckboxStatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_upload_checkbox), true);
    }

    public void writePremiumStatus(boolean premium) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_become_premium), premium);
        editor.apply();
    }


    public boolean readPremiumStatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_become_premium), true);
    }

    // Write if ads are paused
    public void writeAdPause(boolean paused) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_ad_pause), paused);
        editor.apply();
    }

    // Read ad pause status
    public boolean readAdPause() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_ad_pause), false);
    }


    public void writeNotifyDailyStatus(boolean active) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_notify_daily), active);
        editor.apply();
    }

    // Read notify daily status
    public boolean readNotifyDailyStatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_notify_daily), true);
    }


    //Saves the starting Date
    public void writeStartDay(Date startDate) {

            /* Set hour to 0 so when the difference updates when a new day comes
             and not when the time of the starting date is more than 24 hours

             */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        Date trimDate = calendar.getTime();


        long millis = trimDate.getTime();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(context.getString(R.string.pref_day), millis);
        editor.apply();
    }

    //Returns the starting Date
    public Date readStartDay() {
        return new Date(sharedPreferences.getLong(context.getString(R.string.pref_day), 0));
    }

    //Logs everything from sharedPreferences
    public void logAllPref() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    /**
     * Save a string of indexes from tips array that the user down voted (ex. for index 1 and 7 it saves "1,7")
     *
     * @param downVotedTips int array of indexes for tips array
     */
    public void writeDownVotedTips(int[] downVotedTips) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder tipsIndexesString = new StringBuilder();
        int i;
        for (i = 0; i < downVotedTips.length - 1; i++) {
            if (downVotedTips[0] != -1) {
                tipsIndexesString.append((downVotedTips[i])).append(",");
            }
        }
        tipsIndexesString.append((downVotedTips[i]));
        editor.putString(context.getString(R.string.pref_down_voted_array), tipsIndexesString.toString());
        editor.apply();
    }

    /**
     * Reads from sharedPref a string of down voted Tips, it splits the string ands parses it to return the int array of tip indexes.
     *
     * @return int array of tip indexes
     */
    public int[] readDownVotedTips() {
        String tipsIndexesString = sharedPreferences.getString(context.getString(R.string.pref_down_voted_array), "-1");
        //Toast.makeText(context, "Down: " + tipsIndexesString, Toast.LENGTH_SHORT).show();
        assert tipsIndexesString != null;
        String[] splitTipsIndexes = tipsIndexesString.split(",");
        int[] downVotedTipsIndexes = new int[splitTipsIndexes.length];
        for (int i = 0; i < downVotedTipsIndexes.length; i++) {
            downVotedTipsIndexes[i] = Integer.parseInt(splitTipsIndexes[i]);
        }
        return downVotedTipsIndexes;

    }


    public void writeUserAddedTipToArray(String newAddedTip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String[] previousTipsArray = readUserAddedTipsArray();


        String tempString = "";


        if (previousTipsArray.length == 1 && previousTipsArray[0].equals("-1")) {
            tempString += newAddedTip;
        } else {


            for (int i = 0; i < previousTipsArray.length; i++) {
                tempString += previousTipsArray[i] + "///";
            }
            tempString += newAddedTip;

        }
        Log.d("pref ", "writeUserAddedTipToArray: " + tempString + "  .len : " + (previousTipsArray.length + 1));
        editor.putString(context.getString(R.string.pref_user_added_tips_array), tempString);
        editor.apply();
    }

    public String[] readUserAddedTipsArray() {
        String addedTipsString = sharedPreferences.getString(context.getString(R.string.pref_user_added_tips_array), "-1");


        //Toast.makeText(context, "Down: " + tipsIndexesString, Toast.LENGTH_SHORT).show();
        assert addedTipsString != null;


        return addedTipsString.split("///");


    }


    /**
     * Save a string of indexes from tips array that the user has favorite (ex. for index 1 and 7 it saves "1,7")
     *
     * @param favoriteTips int array of indexes for tips array
     */
    public void writeFavoriteTips(int[] favoriteTips) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder tipsIndexesString = new StringBuilder();
        int i;

        for (i = 0; i < favoriteTips.length - 1; i++) {

            tipsIndexesString.append((favoriteTips[i])).append(",");

        }
        tipsIndexesString.append((favoriteTips[i]));
        editor.putString(context.getString(R.string.pref_favorite_array), tipsIndexesString.toString());
        editor.apply();
    }


    public void writeAddFavoriteTip(int tipIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder tipsIndexesString = new StringBuilder();

        int[] favoriteTips = readFavoriteTips();

        if (favoriteTips.length == 1 && favoriteTips[0] == -1) {
            tipsIndexesString.append(tipIndex).append(",");
        } else {


            for (int favoriteTip : favoriteTips) {

                tipsIndexesString.append((favoriteTip)).append(",");

            }
            tipsIndexesString.append(tipIndex);
        }
        editor.putString(context.getString(R.string.pref_favorite_array), tipsIndexesString.toString());
        editor.apply();
    }


    public void writeRemoveFavoriteTip(int tipIndex) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder tipsIndexesString = new StringBuilder();

        int[] favoriteTips = readFavoriteTips();
        if (favoriteTips.length == 1) {
            if (favoriteTips[0] == tipIndex) {
                editor.putString(context.getString(R.string.pref_favorite_array), "-1");
                editor.apply();
            }
        } else {


            for (int favoriteTip : favoriteTips) {

                if (favoriteTip != tipIndex) {
                    tipsIndexesString.append((favoriteTip)).append(",");
                }

            }
            editor.putString(context.getString(R.string.pref_favorite_array), tipsIndexesString.toString());
            editor.apply();
        }




    }

    /**
     * Reads from sharedPref a string of favorite Tips, it splits the string ands parses it to return the int array of tip indexes.
     *
     * @return int array of tip indexes
     */
    public int[] readFavoriteTips() {
        String tipsIndexesString = sharedPreferences.getString(context.getString(R.string.pref_favorite_array), "-1");

        Log.d("pref", "readFavoriteTips: "+tipsIndexesString);
        assert tipsIndexesString != null;
        // Toast.makeText(context, "Fav: " + tipsIndexesString, Toast.LENGTH_SHORT).show();
        String[] splitTipsIndexes = tipsIndexesString.split(",");
        int[] favoriteTipsIndexes = new int[splitTipsIndexes.length];
        for (int i = 0; i < favoriteTipsIndexes.length; i++) {
            favoriteTipsIndexes[i] = Integer.parseInt(splitTipsIndexes[i]);
        }

        return favoriteTipsIndexes;

    }

    //Keep recently shown tips, if more than 10 remove first
    public void writeRecentlyShownTips(int[] recentTips) {

        if (recentTips.length > 10) {
            int[] recentTipsTemp = new int[10];
            System.arraycopy(recentTips, 1, recentTipsTemp, 0, 10);

            recentTips = recentTipsTemp;
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder tipsIndexesString = new StringBuilder();
        int i;
        for (i = 0; i < recentTips.length - 1; i++) {
            if (recentTips[0] != -1) {
                tipsIndexesString.append((recentTips[i])).append(",");
            }
        }
        tipsIndexesString.append((recentTips[i]));
        editor.putString(context.getString(R.string.pref_recently_shown_array), tipsIndexesString.toString());
        editor.apply();
    }

    public int[] readRecentlyShownTips() {
        String tipsIndexesString = sharedPreferences.getString(context.getString(R.string.pref_recently_shown_array), "-1");
        Toast.makeText(context, "Recent: " + tipsIndexesString, Toast.LENGTH_SHORT).show();
        assert tipsIndexesString != null;
        String[] splitTipsIndexes = tipsIndexesString.split(",");
        int[] recentlyShownTipsIndexes = new int[splitTipsIndexes.length];
        for (int i = 0; i < recentlyShownTipsIndexes.length; i++) {
            recentlyShownTipsIndexes[i] = Integer.parseInt(splitTipsIndexes[i]);
        }
        return recentlyShownTipsIndexes;

    }

    public void writeLastDayChanged(Date today) {
        long millis = today.getTime();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(context.getString(R.string.pref_last_day_changed), millis);
        editor.apply();
    }


    //Returns the last change Date
    public Date readLastDayChanged() {
        return new Date(sharedPreferences.getLong(context.getString(R.string.pref_last_day_changed), 0));
    }


    public void writeCurrentTheme(String themeName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_current_theme), themeName);
        editor.apply();
    }

    public String readCurrentTheme() {
        return sharedPreferences.getString(context.getString(R.string.pref_current_theme), context.getString(R.string.theme_minimal_white));
    }


}
