package com.example.authenticationkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_init.*

class InitActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        registro()
    }

    private fun registro(){
        successButton.setOnClickListener {
            if(regEmail.text.isNotEmpty() && regPass.text.isNotEmpty() ){
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                               regEmail.text.toString(), regPass.text.toString()
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                db.collection("users").document(regEmail.text.toString()).set(
                                        hashMapOf("provider" to ProviderType.BASIC,
                                        "telefono" to etTelefonoReg.text.toString(),
                                        "direccion" to etDireccionReg.text.toString())
                                )
                                showHome(it.result?.user?.email ?: " ", ProviderType.BASIC)
                            } else {
                                showAlert()
                            }
                        }
            }else{
                showAlert()
            }
        }
        cancelButton.setOnClickListener {
            back()
        }
    }
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autentificar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
    private fun back() {
        val homeIntent = Intent(this, AuthActivity::class.java).apply {
        }
        startActivity(homeIntent)
    }
}