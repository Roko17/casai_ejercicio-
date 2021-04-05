package com.example.loginscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginEmail(v: View){
        val usuario = findViewById<EditText>(R.id.editTextUsuario).text.toString()
        val contrasena = findViewById<EditText>(R.id.editTextContrasena).text.toString()
        Log.e("Login","Usuario: $usuario  |  Contraseña: $contrasena")

        if (usuario.isNotEmpty() || contrasena.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contrasena).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("Login","Succes!")
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()

                }
                else{
                    //TODO MOSTRAR TOAST DE ERROR EN EL LOGIN
                    Log.e("Login","Error!")
                    Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}