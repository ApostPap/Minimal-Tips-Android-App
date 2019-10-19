package com.apostpapad.dailytips;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        //  Toolbar toolbar = view.findViewById(R.id.toolBar);
        // android.widget.Toolbar toolbar2 = view.findViewById(R.id.toolBar);
        //  Toolbar toolbar1 = view.findViewById(R.id.settingToolbar);

        //    Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).getSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsPrefFragment()).commit();


        return view;
    }

    public static class SettingsPrefFragment extends PreferenceFragmentCompat {


        private int clickCounter = 0;
        private PreferencesConfig preferencesConfig;
        private boolean ad_pauseBool;
        private ListPreference themesPref;

        private String previousTheme;


        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);


            initPref();
        }

        private void initPref() {

            preferencesConfig = new PreferencesConfig(getContext());
            ad_pauseBool = preferencesConfig.readAdPause();

/*
            //Become Premium Button
            androidx.preference.Preference becomePremiumPref = findPreference(getString(R.string.key_become_premium));
            becomePremiumPref.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    if (preferencesConfig.readPremiumStatus()) {
                        Toast.makeText(getContext(), "Already Premium", Toast.LENGTH_SHORT).show();
                    } else {



                        preferencesConfig.writePremiumStatus(true);
                        Toast.makeText(getContext(), "Become Premium", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

*/
            themesPref = (ListPreference) findPreference(getString(R.string.key_themes));
            previousTheme = themesPref.getValue();
            themesPref.setEnabled(preferencesConfig.readPremiumStatus());
            themesPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {

                    String themeValue = o.toString();


                    if (!previousTheme.equals(themeValue) && themeValue.equals(getString(R.string.theme_minimal_white))) {
                        Toast.makeText(getContext(), "Switch to Minimal White.", Toast.LENGTH_SHORT).show();
                        preferencesConfig.writeCurrentTheme(getString(R.string.theme_minimal_white));
                        //getActivity().setTheme(R.style.AppTheme);
                        restartApp();
                    } else if (!previousTheme.equals(themeValue) && themeValue.equals(getString(R.string.theme_dark_night))) {
                        // getActivity().setTheme(R.style.DarkAppTheme);
                        Toast.makeText(getContext(), "Switch to Dark Night.", Toast.LENGTH_SHORT).show();
                        preferencesConfig.writeCurrentTheme(getString(R.string.theme_dark_night));

                        restartApp();
                    }
                    return true;

                }

            });


            //Clear All Favorites Button
            androidx.preference.Preference clearFavoritesPref = findPreference(getString(R.string.key_clear_favorites));

            clearFavoritesPref.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    final int[] favArr = preferencesConfig.readFavoriteTips();
                    if (favArr.length == 1 && favArr[0] == -1) {
                        Toast.makeText(getContext(), "You have no favorite tips.", Toast.LENGTH_SHORT).show();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setMessage("All your favorite tips will be deleted!")
                                .setTitle("Are you sure?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        int[] tempArr = new int[1];
                                        tempArr[0] = -1;
                                        preferencesConfig.writeFavoriteTips(tempArr);
                                        if (favArr.length > 1) {
                                            Toast.makeText(getContext(), "Cleared " + favArr.length + " tips.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Cleared " + favArr.length + " tip.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", null);


                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    return false;
                }
            });


            //Version Button
            androidx.preference.Preference versionPref = findPreference(getString(R.string.key_version));
            versionPref.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    clickCounter++;
                    //   Toast.makeText(getContext(), ""+clickCounter, Toast.LENGTH_SHORT).show();
                    if (!ad_pauseBool) {
                        if (clickCounter > 14 && clickCounter < 20) {
                            Toast.makeText(getContext(), "Answer 42.", Toast.LENGTH_SHORT).show();
                        } else if (clickCounter > 20) {
                            Toast.makeText(getContext(), "...", Toast.LENGTH_SHORT).show();
                            preferencesConfig.writeAdPause(true);
                            ad_pauseBool = true;
                        }
                    } else {
                        Toast.makeText(getContext(), ".", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });


            //Rate Us Button
            androidx.preference.Preference ratePref = findPreference(getString(R.string.key_rate_app));
            ratePref.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.apostpapad.dailytips")));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.apostpapad.dailytips")));
                    }
                    return false;
                }
            });

            //Privacy Policy Button
            androidx.preference.Preference privacyPol = findPreference(getString(R.string.key_privacy_policy));
            privacyPol.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    Intent intent = new Intent(getContext(), PolicyConditionsActivity.class);
                    intent.putExtra("type", getString(R.string.key_privacy_policy));
                    startActivity(intent);
                    return false;
                }
            });

            //Term and Conditions Policy Button
            androidx.preference.Preference termsCond = findPreference(getString(R.string.key_terms_conditions));
            termsCond.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(androidx.preference.Preference preference) {
                    Intent intent = new Intent(getContext(), PolicyConditionsActivity.class);
                    intent.putExtra("type", getString(R.string.key_terms_conditions));
                    startActivity(intent);
                    return false;
                }
            });
/*
            final CheckBoxPreference notifyCheckBoxPreference = (CheckBoxPreference) findPreference(getString(R.string.key_notify_daily));
                notifyCheckBoxPreference.setChecked(preferencesConfig.readNotifyDailyStatus());
                notifyCheckBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (notifyCheckBoxPreference.isChecked()) {
                        enableNotification();
                        preferencesConfig.writeNotifyDailyStatus(true);
                        Toast.makeText(getContext(), "chec", Toast.LENGTH_SHORT).show();
                    } else {
                        preferencesConfig.writeNotifyDailyStatus(false);
                        Toast.makeText(getContext(), "ttt", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

*/

            /*

            // Send Feedback Button
            Preference feedbackPref = findPreference(getString(R.string.key_send_feedback));
            feedbackPref.setOnPreferenceClickListener(new  android.support.v7.preference.Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                    //   sendFeedback();
                    startActivity(new Intent(context, SendFeedbackActivity.class));
                    return false;
                }
            });*/

        }
/*
        private void enableNotification()
        {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY,2);
            calendar.set(Calendar.MINUTE,10);
            calendar.set(Calendar.SECOND,40);

            Intent intent = new Intent(getContext(), Notification_receiver.class);
            intent.setAction("MY_NOTIFICATION_MESSAGE");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }*/


        private void restartApp() {
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            if (getActivity() != null)
                getActivity().finish();
        }


    }


}
