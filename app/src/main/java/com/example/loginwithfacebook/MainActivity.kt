package com.example.loginwithfacebook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


class MainActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    var id = ""
    var firstName = ""
    var middleName = ""
    var lastName = ""
    var name = ""
    var picture = ""
    var email = ""
    var accessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@MainActivity, callbackManager, listOf("public_profile", "email"));
        }

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    getUserProfile(result.accessToken, result.accessToken.userId)
                }

                override fun onCancel() {
                    // App code
                    Toast.makeText(this@MainActivity, "onCancel", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Toast.makeText(this@MainActivity, "onError", Toast.LENGTH_LONG).show()
                }

            })

//        if (AccessToken.getCurrentAccessToken() != null) {
//            startActivity(Intent(this, SecondActivity::class.java))
//        }
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, middle_name, last_name, name, picture, email")

        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject ?: return@Callback

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }
                accessToken = token.toString()


                // Facebook Id
                id = if (jsonObject.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    facebookId.toString()
                } else {
                    "Not exists"
                }


                // Facebook First Name
                firstName = if (jsonObject.has("first_name")) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    facebookFirstName
                } else {
                    "Not exists"
                }


                // Facebook Middle Name
                middleName = if (jsonObject.has("middle_name")) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    facebookMiddleName
                } else {
                    "Not exists"
                }


                // Facebook Last Name
                lastName = if (jsonObject.has("last_name")) {
                    val facebookLastName = jsonObject.getString("last_name")
                    facebookLastName
                } else {
                    "Not exists"
                }


                // Facebook Name
                name = if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    facebookName
                } else {
                    "Not exists"
                }


                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                            picture = facebookProfilePicURL
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                    picture = "Not exists"
                }

                // Facebook Email
                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    email = facebookEmail
                } else {
                    email = "Not exists"
                }

                openDetailsActivity()
            }).executeAsync()
    }

    private fun openDetailsActivity() {
        val myIntent = Intent(this, SecondActivity::class.java)
        myIntent.putExtra("facebook_id", id)
        myIntent.putExtra("facebook_first_name", firstName)
        myIntent.putExtra("facebook_middle_name", middleName)
        myIntent.putExtra("facebook_last_name", lastName)
        myIntent.putExtra("facebook_name", name)
        myIntent.putExtra("facebook_picture", picture)
        myIntent.putExtra("facebook_email", email)
        myIntent.putExtra("facebook_access_token", accessToken)
        startActivity(myIntent)
        finish()
    }
}