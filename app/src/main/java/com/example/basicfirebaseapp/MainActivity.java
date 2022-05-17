package com.example.basicfirebaseapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.basicfirebaseapp.model.Pelicula;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Pelicula> listPel = new ArrayList<Pelicula>();
    ArrayAdapter<Pelicula> arrayAdapterPelicula;

    EditText nombre,año,pais,genero;
    ListView listV_peliculas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = findViewById(R.id.editTextNombre);
        año = findViewById(R.id.editTextAño);
        pais = findViewById(R.id.editTextPais);
        genero = findViewById(R.id.editTextGenero);

        listV_peliculas = findViewById(R.id.lvDatosPeliculas);
        inicializarFirebase();
        listarDatos();
    }

    private void listarDatos() {
        databaseReference.child("Pelicula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPel.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Pelicula pel = objSnapshot.getValue(Pelicula.class);
                    listPel.add(pel);

                    arrayAdapterPelicula = new ArrayAdapter<Pelicula>(MainActivity.this, android.R.layout.simple_list_item_1, listPel);
                    listV_peliculas.setAdapter(arrayAdapterPelicula);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nom = nombre.getText().toString();
        String yr = año.getText().toString();
        String ps = pais.getText().toString();
        String gnr = genero.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if(nom.equals("") || yr.equals("") || ps.equals("") || gnr.equals("")){
                    validacion();
                }else{
                    Pelicula pel = new Pelicula();
                    pel.setId(UUID.randomUUID().toString());
                    pel.setNombre(nom);
                    pel.setAño(yr);
                    pel.setGenero(gnr);
                    pel.setPais(ps);
                    databaseReference.child("Pelicula").child(pel.getId()).setValue(pel);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCajas();
                }
                break;
            }
            default: break;
        }
        return true;
    }

    private void limpiarCajas() {
        nombre.setText("");
        año.setText("");
        genero.setText("");
        pais.setText("");
    }

    private void validacion() {
        String nom = nombre.getText().toString();
        String yr = año.getText().toString();
        String ps = pais.getText().toString();
        String gnr = genero.getText().toString();

        if(nom.equals("")){
            nombre.setError("Required");
        }else if (yr.equals("")){
            año.setError("Required");
        }else if(ps.equals("")){
            pais.setError("Required");
        }else if(gnr.equals("")){
            genero.setError("Required");
        }
    }
}
