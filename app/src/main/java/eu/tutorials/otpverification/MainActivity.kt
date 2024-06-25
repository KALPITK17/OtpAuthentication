package eu.tutorials.otpverification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import eu.tutorials.otpverification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth :FirebaseAuth
    private var currentUser:FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }
    fun logout(view: View){
       auth.signOut() // when a person logout it will go to login page
       login()
    }
    fun login(){
        val loginIntent = Intent(this@MainActivity,LoginActivity::class.java)
        startActivity(loginIntent) // It will help to shift from main activity to login activity
        finish() // And this login function will execute when there will be no current user
    }


    override fun onStart() {
        super.onStart()
        if (currentUser==null){
            login()  // when there is no currentUser it will go to login
        }else{
            var userInfo = currentUser!!.phoneNumber  // Information will be displayed when login
            binding.txSts.text = "Welcome $userInfo"
        }
    }
  // change 1
}