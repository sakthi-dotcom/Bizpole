package com.example.bizpole

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bizpole.databinding.ActivityLoginBinding
import com.example.bizpole.utility.UserManager
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth :FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)


        val savedEmail = sharedPreferences.getString("email", null)
        savedEmail?.let {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        val progressBarColor = ContextCompat.getColor(this, R.color.green)
        binding.loginProgress.indeterminateTintList = ColorStateList.valueOf(progressBarColor)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editUsernameInput.text.toString().trim()
            val password = binding.editPasswordInput.text.toString().trim()
            binding.loginProgress.visibility = View.VISIBLE


            if (email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful){
                            saveUserEmail(email)
                            val userId = getUserIdByEmail(email)
                            Log.d("saved user Id",userId.toString())
                            if (userId != null) {
                                UserManager.saveCurrentUser(this, userId)
                            }
                            binding.loginProgress.visibility = View.INVISIBLE
                            Toast.makeText(this,"Logged Successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this,HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            binding.loginProgress.visibility = View.INVISIBLE
                            val exception = task.exception
                            if (exception is FirebaseAuthException) {
                                val errorCode = exception.errorCode
                                when (errorCode) {
                                    "ERROR_INVALID_CREDENTIAL" -> {
                                        Toast.makeText(this, "Invalid Credentials or user does not exist", Toast.LENGTH_LONG).show()
                                    }
                                    else -> {
                                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else if (exception is FirebaseNetworkException) {
                                Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            }else{
                binding.loginProgress.visibility = View.INVISIBLE
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }

        binding.buttonSignUp.setOnClickListener {
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveUserEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }
    private fun getUserIdByEmail(email: String): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
    }
}