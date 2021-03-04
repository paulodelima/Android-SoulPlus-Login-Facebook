package digital.soulplus.soulplusloginfacebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager

    var id = ""
    var firstName = ""
    var middleName = ""
    var lastName = ""
    var name = ""
    var pictureUrl = ""
    var email = ""
    var accessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btFbLogin = findViewById<Button>(R.id.btFbLogin)
        val btDetails = findViewById<Button>(R.id.btDetails)
        val tvName = findViewById<TextView>(R.id.tvName)


        callbackManager = CallbackManager.Factory.create()

        if (isLoggedIn()) {
            Log.d("LoggedIn? :", "YES")
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))

            tvName.text = getString(R.string.hello, name)
            btFbLogin.text = getString(R.string.logout)
            btDetails.visibility = View.VISIBLE

        } else {
            Log.d("LoggedIn? :", "NO")
            tvName.text = ""
            btFbLogin.text = getString(R.string.log_in_with_facebook)
            btDetails.visibility = View.GONE
        }

        btFbLogin.setOnClickListener {
            if (isLoggedIn()) {
                logOutUser()
                tvName.text = ""
                btFbLogin.text = getString(R.string.log_in_with_facebook)
                btDetails.visibility = View.GONE

            } else {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("public_profile", "email"))
                tvName.text = getString(R.string.hello, name)
                btFbLogin.text = getString(R.string.logout)
                btDetails.visibility = View.VISIBLE

            }

            tvName.text = getString(R.string.hello, name)
        }

        btDetails.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("FACEBOOK_ID", id)
            intent.putExtra("FACEBOOK_FIRST_NAME", firstName)
            intent.putExtra("FACEBOOK_MIDDLE_NAME", middleName)
            intent.putExtra("FACEBOOK_LAST_NAME", lastName)
            intent.putExtra("FACEBOOK_NAME", name)
            intent.putExtra("FACEBOOK_PICTURE_URL", pictureUrl)
            intent.putExtra("FACEBOOK_EMAIL", email)
            intent.putExtra("FACEBOOK_TOKEN", accessToken)
            startActivity(intent)
        }

        // Callback registration
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("TAG", "Success Login")
                    getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, middle_name, last_name, name, picture, email")

        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }
                accessToken = token.toString()

                id = facebookResult(jsonObject, "id", getString(R.string.facebook_id))
                firstName = facebookResult(jsonObject, "first_name", getString(R.string.facebook_first_name))
                middleName = facebookResult(jsonObject, "middle_name", getString(R.string.facebook_middle_name))
                lastName = facebookResult(jsonObject, "last_name", getString(R.string.facebook_last_name))
                name = facebookResult(jsonObject, "name", getString(R.string.facebook_name))
                email = facebookResult(jsonObject, "email", getString(R.string.facebook_email))

                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i(getString(R.string.facebook_picture_url), facebookProfilePicURL)
                            pictureUrl = facebookProfilePicURL
                        }
                    }
                } else {
                    Log.i(getString(R.string.facebook_picture_url), getString(R.string.not_exist))
                    pictureUrl = getString(R.string.not_exist)
                }

            }).executeAsync()
    }

    private fun facebookResult(jsonObject: JSONObject, fieldName: String, strLogReturn: String): String {
        if (jsonObject.has(fieldName)) {
            val field = jsonObject.getString(fieldName)
            Log.i(strLogReturn, field.toString())
            return field.toString()
        } else {
            Log.i(strLogReturn, getString(R.string.not_exist))
            return getString(R.string.not_exist)
        }
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun logOutUser() {
        LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}

