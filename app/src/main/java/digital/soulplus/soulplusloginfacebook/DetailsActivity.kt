package digital.soulplus.soulplusloginfacebook

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val fbId = intent.getStringExtra("FACEBOOK_ID")
        val fbFirstName = intent.getStringExtra("FACEBOOK_FIRST_NAME")
        val fbMiddleName = intent.getStringExtra("FACEBOOK_MIDDLE_NAME")
        val fbLastName = intent.getStringExtra("FACEBOOK_LAST_NAME")
        val fbName = intent.getStringExtra("FACEBOOK_NAME")
        val fbPictureUrl = intent.getStringExtra("FACEBOOK_PICTURE_URL")
        val fbEmail = intent.getStringExtra("FACEBOOK_EMAIL")
        val fbToken = intent.getStringExtra("FACEBOOK_TOKEN")


        val tvFbId = findViewById<TextView>(R.id.tvFbId)
        val tvFbFirstName = findViewById<TextView>(R.id.tvFbFirstName)
        val tvFbMiddleName = findViewById<TextView>(R.id.tvFbMiddleName)
        val tvFbLastName = findViewById<TextView>(R.id.tvFbLastName)
        val tvFbName = findViewById<TextView>(R.id.tvFbName)
        val tvFbPictureUrl = findViewById<TextView>(R.id.tvFbPictureUrl)
        val tvFbEmail = findViewById<TextView>(R.id.tvFbEmail)
        val tvFbToken = findViewById<TextView>(R.id.tvFbToken)

        tvFbId.text = fbId
        tvFbFirstName.text = fbFirstName
        tvFbMiddleName.text = fbMiddleName
        tvFbLastName.text = fbLastName
        tvFbName.text = fbName
        tvFbPictureUrl.text = fbPictureUrl
        tvFbEmail.text = fbEmail
        tvFbToken.text = fbToken

    }
}
