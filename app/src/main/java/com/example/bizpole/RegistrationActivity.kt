package com.example.bizpole

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bizpole.databinding.ActivityRegistrationBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val progressBarColor = ContextCompat.getColor(this, R.color.green)
        binding.signupProcess.indeterminateTintList = ColorStateList.valueOf(progressBarColor)

        binding.registerButton.setOnClickListener {
            binding.signupProcess.visibility = View.VISIBLE
            val email = binding.editUsernameInput.text.toString().trim()
            val password = binding.editPasswordInput.text.toString().trim()


            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.signupProcess.visibility = View.INVISIBLE
                            Toast.makeText(this, "SignedUp Successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val exception = task.exception
                            binding.signupProcess.visibility = View.INVISIBLE
                            if (task.exception?.message == "The email address is already in use by another account.") {
                                Toast.makeText(this, "Email address is already in use", Toast.LENGTH_LONG).show()
                            }else if (exception is FirebaseNetworkException){
                                Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_LONG).show()
                            }
                            else {
                                Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            } else {
                binding.signupProcess.visibility = View.INVISIBLE
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }

        }

        binding.textLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}