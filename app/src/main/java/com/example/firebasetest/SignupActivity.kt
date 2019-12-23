package com.example.firebasetest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity: AppCompatActivity()  {

    private lateinit var username: EditText

    private lateinit var password: EditText

    private lateinit var confirmPassword: EditText

    private lateinit var signup: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tells Android which XML layout file to use for this Activity
        // The "R" is short for "Resources" (e.g. accessing a layout resource in this case)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()

        // The "id" used here is what we had set in XML in the "id" field
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        signup = findViewById(R.id.signUp)
        confirmPassword = findViewById(R.id.confirm_password)

        signup.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val inputtedPasswordConfirm: String = confirm_password.text.toString().trim()

            if (inputtedPassword != inputtedPasswordConfirm){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth
                    .createUserWithEmailAndPassword(inputtedUsername,inputtedPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            Toast.makeText(this, "Created user: ${user!!.email}", Toast.LENGTH_LONG).show()

                            // Go to the next Activity ...
                            val intent = Intent(this, IconTableActivity::class.java)
                            startActivity(intent)

                        } else {
                            val exception = task.exception
                            Toast.makeText(this, "Failed: $exception", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}