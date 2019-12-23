package com.example.firebasetest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText

    private lateinit var password: EditText

    private lateinit var login: Button

    private lateinit var signup: Button

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var preferences: SharedPreferences

    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tells Android which XML layout file to use for this Activity
        // The "R" is short for "Resources" (e.g. accessing a layout resource in this case)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        preferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)

        // The "id" used here is what we had set in XML in the "id" field
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signup = findViewById(R.id.signUp)
        checkBox = findViewById((R.id.checkBox))

        // Kotlin shorthand for login.setEnabled(false)
        login.isEnabled = false

        checkBox.isChecked = preferences.getBoolean("SAVE_CREDS", false)
        if (checkBox.isChecked){
            username.setText(preferences.getString("SAVED_USERNAME", ""))
            password.setText(preferences.getString("SAVED_PASSWORD", ""))
            login.isEnabled = true
        }

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        checkBox.setOnClickListener {
                preferences.edit().putBoolean("SAVE_CREDS", checkBox.isChecked).apply()
        }

        // An OnClickListener is an interface with a single function, so you can use lambda-shorthand
        // The lambda is called when the user pressed the button
        // https://developer.android.com/reference/android/view/View.OnClickListener
        login.setOnClickListener {

            val inputtedUsername = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Logged in as user: ${user!!.email}", Toast.LENGTH_SHORT).show()

                        // Go to the next Activity ...
                        val intent = Intent(this, IconTableActivity::class.java)
                        startActivity(intent)

                    } else {
                        val exception = task.exception
                        Toast.makeText(this, "Failed: $exception", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        signup.setOnClickListener {
            // Go to the signup Activity ...
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


    }

    // A TextWatcher is an interface with three functions, so we cannot use lambda-shorthand
    // The functions are called accordingly as the user types in the EditText
    // https://developer.android.com/reference/android/text/TextWatcher
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(newString: CharSequence, start: Int, before: Int, count: Int) {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()

            if (inputtedUsername.isNotEmpty())
                preferences.edit().putString("SAVED_USERNAME", username.text.toString()).apply()
            if (inputtedPassword.isNotEmpty())
                preferences.edit().putString("SAVED_PASSWORD", password.text.toString()).apply()

            val enabled: Boolean = inputtedUsername.isNotEmpty() && inputtedPassword.isNotEmpty()

            // Kotlin shorthand for login.setEnabled(enabled)
            login.isEnabled = enabled
        }
    }
}