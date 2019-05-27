package android.kodroid.com.easyhiring.ui

import android.content.Intent
import android.content.SharedPreferences
import android.kodroid.com.easyhiring.R
import android.kodroid.com.easyhiring.data.CandidateData
import android.kodroid.com.easyhiring.data.CandidateStatus
import android.kodroid.com.easyhiring.data.Constants
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    private val PREF_NAME = "Data_Pref"
    private val KEY = "id_data_added"
    private var mDatabase: DatabaseReference? = null
    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        }, 3000)

    }

    private fun getCandidateList(): ArrayList<CandidateData> {
        val items = ArrayList<CandidateData>()
        //adding some dummy data to the list
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Jon Snow", getString(R.string.jon_intro), "https://pbs.twimg.com/profile_images/901947348699545601/hqRMHITj_400x400.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Arya Stark", getString(R.string.arya_intro), "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRavDJk9fj_Z_X3_cUUMfrZWafLMbsAm7KPGjum4ebKpks6CMyG"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Joffrey Baratheon", getString(R.string.joff_intro), "https://i.dailymail.co.uk/i/pix/2014/06/26/article-0-1ED7483100000578-525_470x423.jpg"))
        items.add(CandidateData("0", CandidateStatus.SHORTLISTED.ordinal, "KOMAL BHALGE", getString(R.string.komal_intro), "https://pbs.twimg.com/profile_images/899649475030900736/_MnbkXcQ_400x400.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Melisandre", getString(R.string.melisa_intro), "https://lovelace-media.imgix.net/uploads/915/0dbafd40-eef0-0133-2443-0e1b1c96d76b.jpg"))
        items.add(CandidateData("0", CandidateStatus.NONE.ordinal, "Tyrion Lannister", getString(R.string.tyri_intro), "https://pbs.twimg.com/profile_images/1338985026/Picture_1.png"))
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