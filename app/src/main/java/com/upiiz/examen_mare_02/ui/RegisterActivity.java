package com.upiiz.examen_mare_02.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.AppDatabase;
import com.upiiz.examen_mare_02.data.entities.User;

import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = AppDatabase.get(this);

        EditText etName = findViewById(R.id.etUserR);
        EditText etPass = findViewById(R.id.etPassR);
        Button btnOk = findViewById(R.id.btnOk);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnOk.setOnClickListener(v -> {
            String u = etName.getText().toString().trim();
            String p = etPass.getText().toString().trim();
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show(); return;
            }
            Executors.newSingleThreadExecutor().execute(() -> {
                long id = db.userDao().insert(new User(u, p));
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registrado id=" + id, Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
