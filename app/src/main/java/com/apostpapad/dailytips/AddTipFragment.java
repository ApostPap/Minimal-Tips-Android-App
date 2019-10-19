package com.apostpapad.dailytips;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class AddTipFragment extends Fragment {
    private static final String TAG = "AddTipFragment";

    private static ApiInterface apiInterface;

    private PreferencesConfig preferencesConfig;

    private EditText tipEditText;
    private Button addTipBtn;
    private CheckBox uploadCheckbox;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tip, container, false);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        preferencesConfig = new PreferencesConfig(getContext());
        initLayout(view);
        return view;
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void closeKeyboard()
    {
        Activity activity = getActivity();
        if(activity!=null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    private void initLayout(View view) {

        //Hide keyboard when click is detected outside the edit text
        ConstraintLayout constraintLayout = view.findViewById(R.id.add_tip_layout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    //hideSoftKeyboard(getActivity());
                    closeKeyboard();
                }
            }
        });


        uploadCheckbox = view.findViewById(R.id.uploadCheckBox);
        uploadCheckbox.setChecked(preferencesConfig.readUploadCheckboxStatus());
        uploadCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesConfig.writeUploadCheckboxStatus(true);
                } else {
                    preferencesConfig.writeUploadCheckboxStatus(false);
                }
            }
        });

        tipEditText = view.findViewById(R.id.tipEditText);

        addTipBtn = view.findViewById(R.id.addTipButton);
        addTipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipEditText.getText().toString().trim().length() > 0) {
                    Toast.makeText(getContext(), "Added Tip : " + tipEditText.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                    if (uploadCheckbox.isChecked()) {
                        if (isOnline(getContext())) {
                            uploadTipToServer(tipEditText.getText().toString().trim());
                        } else {
                            Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Device only Tip : " + tipEditText.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                    }
                    preferencesConfig.writeUserAddedTipToArray(tipEditText.getText().toString().trim());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();

                } else {
                    Toast.makeText(getContext(), "No text " + tipEditText.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                }
            }
        });
/*
        addToServerBtn = view.findViewById(R.id.addToServerButton);
        addToServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipEditText.getText().toString().trim().length()>0)
                {
                Toast.makeText(getContext(), "Server : "+tipEditText.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                if(isOnline(getContext())) {
                    uploadTipToServer(tipEditText.getText().toString().trim());
                }else {
                    Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
                }else{
                    Toast.makeText(getContext(), "No text " + tipEditText.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                }
            }
        });*/
    }

    private void uploadTipToServer(String tipString) {

        Call<Tip> uploadTip = apiInterface.uploadTip(tipString);
        uploadTip.enqueue(new Callback<Tip>() {
            @Override
            public void onResponse(Call<Tip> call, Response<Tip> response) {
                assert response.body() != null;
                String uploadResponse = response.body().getResponse();
               /* if(uploadResponse.equals("TipAdded"))
                {
                   // Toast.makeText(getContext(), "Uploaded!!", Toast.LENGTH_SHORT).show();
                }else {
                  //  Toast.makeText(getContext(), "Error Uploading. Please try again later.", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<Tip> call, Throwable t) {
                //Toast.makeText(getContext(), "Error Uploading Tip.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "uploadTip onFailure: " + t.getMessage());
            }
        });
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
