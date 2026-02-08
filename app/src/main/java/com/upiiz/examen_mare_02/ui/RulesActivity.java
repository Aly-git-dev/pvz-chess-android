package com.upiiz.examen_mare_02.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_mare_02.R;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tv = findViewById(R.id.tvRules);
        tv.setText(Html.fromHtml(RulesHTML.ALL, Html.FROM_HTML_MODE_LEGACY));
    }
}
