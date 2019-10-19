package com.apostpapad.dailytips;


import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PolicyConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_condition);
        Toolbar toolbar = findViewById(R.id.privacyToolbar);
        setSupportActionBar(toolbar);
        String type = getIntent().getStringExtra("type");
        TextView textView = findViewById(R.id.textView3);
        if(type.equals(getString(R.string.key_privacy_policy)))
        {
            toolbar.setTitle(getString(R.string.title_privacy_policy));
            textView.setText(Html.fromHtml(getString(R.string.summary_privacy_policy)));
        }
        else if(type.equals(getString(R.string.key_terms_conditions)))
        {
            toolbar.setTitle(getString(R.string.title_terms_conditions));
            textView.setText(Html.fromHtml(getString(R.string.summary_terms_conditions)));
        }



        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
