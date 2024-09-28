package com.example.appcrud;




import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcrud.db.SQLiteHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText editNombre, editTelefono, editEmail;
    Button btnGuardar;
    SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar la base de datos
        dbHelper = new SQLiteHelper(this);

        // Inicializar las vistas
        editNombre = findViewById(R.id.editNombre);
        editTelefono = findViewById(R.id.editTelefono);
        editEmail = findViewById(R.id.editEmail);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Acción para el botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString();
            String telefono = editTelefono.getText().toString();
            String email = editEmail.getText().toString();

            if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty()) {
                dbHelper.addUser(nombre, telefono, email);
                Toast.makeText(RegisterActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();

                // Volver a la pantalla principal
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
