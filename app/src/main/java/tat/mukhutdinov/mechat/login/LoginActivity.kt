package tat.mukhutdinov.mechat.login

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import tat.mukhutdinov.mechat.R
import tat.mukhutdinov.mechat.main.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            AUTH_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val builder = AlertDialog.Builder(this)

                builder
                    .setMessage(R.string.auth_required)
                    .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int -> finish() }
                    .create()
                    .show()
            }
        }
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 33
    }
}