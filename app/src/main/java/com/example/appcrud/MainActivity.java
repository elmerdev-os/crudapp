package com.example.appcrud;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcrud.db.SQLiteHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewUsuarios;
    SQLiteHelper dbHelper;
    ArrayList<String> listaUsuarios; // Lista original de usuarios
    ArrayList<String> listaUsuariosFiltrada; // Lista para los usuarios filtrados
    ArrayList<Integer> listaIds; // Lista para los IDs de los usuarios
    UserAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers(); // Cargar la lista de usuarios cada vez que la actividad es visible
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View btnNuevoRegistro = findViewById(R.id.btnNuevoRegistro);
        btnNuevoRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        listViewUsuarios = findViewById(R.id.listViewUsuarios);
        dbHelper = new SQLiteHelper(this);
        listaUsuarios = new ArrayList<>();
        listaUsuariosFiltrada = new ArrayList<>(); // Inicializar la lista filtrada
        listaIds = new ArrayList<>();

        loadUsers();
    }

    private void loadUsers() {
        listaUsuarios.clear();
        listaIds.clear();

        Cursor cursor = dbHelper.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String telefono = cursor.getString(2);
                String email = cursor.getString(3);

                listaUsuarios.add("Nombre: " + nombre + "\nTeléfono: " + telefono + "\nEmail: " + email);
                listaIds.add(id);
            } while (cursor.moveToNext());
        }

        // Inicializar la lista filtrada con todos los usuarios al principio
        listaUsuariosFiltrada.clear();
        listaUsuariosFiltrada.addAll(listaUsuarios);

        adapter = new UserAdapter();
        listViewUsuarios.setAdapter(adapter);
    }

    private class UserAdapter extends ArrayAdapter<String> {

        UserAdapter() {
            super(MainActivity.this, R.layout.list_item_usuario, listaUsuariosFiltrada);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_usuario, parent, false);
            }

            TextView textViewDatosUsuario = convertView.findViewById(R.id.textViewDatosUsuario);
            ImageButton btnEditar = convertView.findViewById(R.id.btnEditar);
            ImageButton btnEliminar = convertView.findViewById(R.id.btnEliminar);

            textViewDatosUsuario.setText(listaUsuariosFiltrada.get(position));

            // Acción para el botón Editar
            btnEditar.setOnClickListener(v -> {
                int userId = listaIds.get(position);
                String[] userData = listaUsuariosFiltrada.get(position).split("\n");
                String nombre = userData[0].split(": ")[1];
                String telefono = userData[1].split(": ")[1];
                String email = userData[2].split(": ")[1];

                Intent intent = new Intent(MainActivity.this, EditUserActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("nombre", nombre);
                intent.putExtra("telefono", telefono);
                intent.putExtra("email", email);
                startActivity(intent);
            });

            // Acción para el botón Eliminar
            btnEliminar.setOnClickListener(v -> {
                int userId = listaIds.get(position);
                dbHelper.deleteUser(userId);
                Toast.makeText(MainActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                loadUsers(); // Recargar la lista después de eliminar
            });

            return convertView;
        }
    }

    // Método para filtrar usuarios
    public void filterUsers(String query) {
        listaUsuariosFiltrada.clear();
        if (query.isEmpty()) {
            listaUsuariosFiltrada.addAll(listaUsuarios);
        } else {
            for (String user : listaUsuarios) {
                if (user.toLowerCase().contains(query.toLowerCase())) {
                    listaUsuariosFiltrada.add(user);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }
}
