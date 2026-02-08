package com.upiiz.examen_mare_02.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.AppDatabase;
import com.upiiz.examen_mare_02.data.entities.User;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText etUser, etPass;
    private CheckBox cbRemember;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        db = AppDatabase.get(this);

        // 1) Revisar si ya hay sesión recordada
        SharedPreferences sp = getSharedPreferences("SESSION", MODE_PRIVATE);
        long savedId = sp.getLong("currentUserId", -1);
        boolean remember = sp.getBoolean("remember", false);
        if (remember && savedId != -1) {
            // Auto-login: ir directo al menú de jugadores
            startActivity(new Intent(this, UserListActivity.class));
            finish();
            return;
        }

        // 2) Referencias UI
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        cbRemember = findViewById(R.id.cbRemember);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String u = etUser.getText().toString().trim();
        String p = etPass.getText().toString().trim();
        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Completa usuario y clave", Toast.LENGTH_SHORT).show();
            return;
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().login(u, p);
            runOnUiThread(() -> {
                if (user == null) {
                    Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sp = getSharedPreferences("SESSION", MODE_PRIVATE);
                    sp.edit()
                            .putLong("currentUserId", user.id)
                            .putBoolean("remember", cbRemember.isChecked())
                            .apply();

                    startActivity(new Intent(this, UserListActivity.class));
                    finish();
                }
            });
        });
    }
}
