package eu.tutorials.otpverification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import eu.tutorials.otpverification.databinding.ActivityLoginBinding
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var phoneNumber:String=""  // Creating new variable for number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        currentUser=auth.currentUser
    }


     fun sendHome(){
         val loginIntent =  Intent(this@LoginActivity,MainActivity::class.java)
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

    fun generate_OTP(view: View) {
         phoneNumber = binding.etPh.text.toString()
        if (phoneNumber!=null){ // When phone number is not null
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
              val otpIntent = Intent(this@LoginActivity,OtpActivity::class.java) // We will call otp activity from login activity
            otpIntent.putExtra("otpcr",verificationId) // otp cr extra creditional share karenga
            startActivity(otpIntent)
            finish()


          //  storedVerificationId = verificationId
          //  resendToken = token
        }
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
}