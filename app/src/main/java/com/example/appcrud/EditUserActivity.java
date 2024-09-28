package com.example.appcrud;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcrud.db.SQLiteHelper;

public class EditUserActivity extends AppCompatActivity {

    EditText editNombre, editTelefono, editEmail;
    Button btnActualizar;
    SQLiteHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Inicializar la base de datos
        dbHelper = new SQLiteHelper(this);

        // Inicializar vistas
        editNombre = findViewById(R.id.editNombre);
        editTelefono = findViewById(R.id.editTelefono);
        editEmail = findViewById(R.id.editEmail);
        btnActualizar = findViewById(R.id.btnActualizar);

        // Obtener el ID del usuario de la intención
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);  // Asegúrate de usar la clave "userId"
        String nombre = intent.getStringExtra("nombre");
        String telefono = intent.getStringExtra("telefono");
        String email = intent.getStringExtra("email");

        // Rellenar campos con datos existentes
        editNombre.setText(nombre);
        editTelefono.setText(telefono);
        editEmail.setText(email);

        // Acción para el botón Actualizar
        btnActualizar.setOnClickListener(v -> {
            String nombreActualizado = editNombre.getText().toString();
            String telefonoActualizado = editTelefono.getText().toString();
            String emailActualizado = editEmail.getText().toString();

            if (!nombreActualizado.isEmpty() && !telefonoActualizado.isEmpty() && !emailActualizado.isEmpty()) {
                if (userId != -1) {  // Asegúrate de que userId es válido
                    boolean isUpdated = dbHelper.updateUser(userId, nombreActualizado, telefonoActualizado, emailActualizado);
                    if (isUpdated) {
                        Toast.makeText(EditUserActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();

                        // Volver a la pantalla principal
                        finish(); // Finalizar actividad para regresar a la anterior automáticamente
                    } else {
                        Toast.makeText(EditUserActivity.this, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditUserActivity.this, "ID de usuario inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditUserActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}