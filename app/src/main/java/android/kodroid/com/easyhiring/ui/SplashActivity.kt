package android.kodroid.com.easyhiring.ui

import android.content.Intent
import android.content.SharedPreferences
import android.kodroid.com.easyhiring.HomeActivity
import android.kodroid.com.easyhiring.R
import android.kodroid.com.easyhiring.data.CandidateData
import android.kodroid.com.easyhiring.data.CandidateStatus
import android.kodroid.com.easyhiring.data.Constants
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    private val PREF_NAME = "Data_Pref"
    private val KEY = "id_data_added"
    private var mDatabase: DatabaseReference? = null
    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.splash_layout)
        sharedPref = getSharedPreferences(PREF_NAME, 0)

        doSplash()

    }

    private fun doSplash() {
        //4second splash time
        Handler().postDelayed({
            val ifFirstTime = sharedPref!!.getBoolean(KEY, false)
            if (!ifFirstTime) {
                writeNewCandidate()
            } else {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }
        }, 4000)

    }

    private fun getCandidateList(): ArrayList<CandidateData> {
        val items = ArrayList<CandidateData>()
        //adding some dummy data to the list
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Jon Snow", "I am an Android Developer with 4 years of experience I am an Android Developer with 4 years of experience I am an Android Developer with 4 years of experience", "https://cdn3.vectorstock.com/i/1000x1000/44/37/cute-face-little-girl-ballerina-cartoon-character-vector-18274437.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Arya Stark", "I am an Android Developer with 9 years of experience", "https://cdn2.vectorstock.com/i/1000x1000/19/91/cute-cartoon-girl-laugh-face-expression-vector-15161991.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Joffrey Baratheon", "I am an Android Developer with 1 years of experience", "https://t3.ftcdn.net/jpg/01/38/22/38/500_F_138223845_1sz6vri7VLHeqFRjjP4JOvxdglvcLz9C.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "KOMAL BHALGE", "I am an Android Developer with 3 years of experience", "http://www.melisendevector.com/images/Image/girl_head_rock_style_preview_1364119809.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Brandon Stark", "I am an Android Developer with 4 years of experience", "http://www.melisendevector.com/images/Image/Cartoon-Black-Boy---Wavy-Hairstyle.jpg"))
        items.add(CandidateData("0", CandidateStatus.SHORTLISTED.ordinal, "Tyrion Lannister", "I am an Android Developer with 6 years of experience", "https://cdn1.vectorstock.com/i/1000x1000/40/25/cartoon-character-face-boy-children-vector-14814025.jpg"))
        return items
    }

    private fun writeNewCandidate() {
        mDatabase = FirebaseDatabase.getInstance().getReference()
        //Now add all the remaining candidates
        getCandidateList().forEach {
            val key = mDatabase!!.child(Constants.FIREBASE_ITEM).push().key
            it.id = key
            mDatabase!!.child(Constants.FIREBASE_ITEM).child(key).setValue(it)
        }
        val editor = sharedPref!!.edit()
        editor.putBoolean(KEY, true)
        editor.apply()
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        finish()

    }

}