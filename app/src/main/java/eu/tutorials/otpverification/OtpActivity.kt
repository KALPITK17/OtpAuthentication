package eu.tutorials.otpverification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import eu.tutorials.otpverification.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOtpBinding
    private lateinit var auth : FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var phoneNumber:String=""
    private var authId :String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        authId = intent.getStringExtra("otpcr")!!
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = task.result?.user
                    sendHome() // When our task Complete
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


    fun sendHome(){
        val loginIntent =  Intent(this@OtpActivity,MainActivity::class.java)
        startActivity(loginIntent) //  It will shift from login activity to main activity
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (currentUser!=null){
            sendHome()    // If the the user is active it will send to main activity or home
            finish()   // After going to main activity  this function will finish
        }
    }

    fun verify_otp(view: View) {

        val otp:String = binding.etOpt.text.toString()
        if (otp.isEmpty()){
            val credential =  PhoneAuthProvider.getCredential(authId,otp)  // authId is credential
            signInWithPhoneAuthCredential(credential)
        }
    }
}