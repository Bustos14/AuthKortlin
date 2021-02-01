package com.example.authenticationkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_init.*
import java.security.Provider

enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Setup
        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provider= bundle?.getString("provider")
        //Guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
        setup(email.toString(),provider.toString())
    }
    private fun setup(email: String, provider: String){
        title = "Perfil"
        emailTextView.text = email
        if(provider.equals("BASIC")){
            proveedorTextView.text = "Registrado con Email y contraseña"
        }else{
            proveedorTextView.text = "Registrado con " + provider
        }
        db.collection("users").document(email).get().addOnSuccessListener {
            etTelefono.setText(it.get("telefono") as? String)
            etDireccion.setText(it.get("direccion") as? String)
        }
        logOutButton.setOnClickListener{
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            if(provider == ProviderType.FACEBOOK.name){
                LoginManager.getInstance().logOut()
            }
            FirebaseAuth.getInstance().signOut()
            back()
        }
        saveButton.setOnClickListener {
            db.collection("users").document(email).set(
                    hashMapOf("provider" to provider,
                            "telefono" to etTelefono.text.toString(),
                            "direccion" to etDireccion.text.toString())
            )
            Toast.makeText(this, "Datos del perfil actualizados.",Toast.LENGTH_SHORT).show()
        }
    }
    private fun back() {
        val homeIntent = Intent(this, AuthActivity::class.java).apply {

        }
        startActivity(homeIntent)
    }

}
