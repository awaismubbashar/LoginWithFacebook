package com.example.loginwithfacebook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loginwithfacebook.databinding.ActivitySecondBinding
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import java.net.URL


class SecondActivity : AppCompatActivity() {

    lateinit var secondBinding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        secondBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(secondBinding.root)

        val facebookId = intent.getStringExtra("facebook_id")
        val facebookFirstName = intent.getStringExtra("facebook_first_name")
        val facebookMiddleName = intent.getStringExtra("facebook_middle_name")
        val facebookLastName = intent.getStringExtra("facebook_last_name")
        val facebookName = intent.getStringExtra("facebook_name")
        val facebookPicture = intent.getStringExtra("facebook_picture")
        val facebookEmail = intent.getStringExtra("facebook_email")
        val facebookAccessToken = intent.getStringExtra("facebook_access_token")

        secondBinding.btnLogout.setOnClickListener {
            logout()
        }


        secondBinding.facebookIdTextview.text = facebookId
        secondBinding.facebookFirstNameTextview.text = facebookFirstName
        secondBinding.facebookMiddleNameTextview.text = facebookMiddleName
        secondBinding.facebookLastNameTextview.text = facebookLastName
        secondBinding.facebookNameTextview.text = facebookName
        secondBinding.facebookEmailTextview.text = facebookEmail
        secondBinding.facebookAccessTokenTextview.text = facebookAccessToken


//        val a = URL(facebookPicture).openStream()
//        val img = BitmapFactory.decodeStream(a)
//        secondBinding.facebookProfile.setImageBitmap(img)

//        secondBinding.facebookProfile.setImageBitmap(BitmapFactory.decodeFile(img.toString()));

        Glide.with(this).load(facebookPicture).into(secondBinding.facebookProfile);
    }

    private fun logout() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            GraphRequest.Callback {
                LoginManager.getInstance().logOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }).executeAsync()
    }
}