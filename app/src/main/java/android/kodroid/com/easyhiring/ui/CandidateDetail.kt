package android.kodroid.com.easyhiring.ui

import android.kodroid.com.easyhiring.R
import android.kodroid.com.easyhiring.data.CandidateStatus
import android.kodroid.com.easyhiring.data.Constants
import android.kodroid.com.easyhiring.interfaces.CandidateRowListener
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.candidate_detail_layout.*

class CandidateDetail : AppCompatActivity(), CandidateRowListener {
    var mDatabase: DatabaseReference? = null
    private var rowListener: CandidateRowListener = this as CandidateRowListener
    override fun modifyCandidateState(candidateId: String, status: Int) {
        val itemReference = mDatabase!!.child(Constants.FIREBASE_ITEM).child(candidateId)
        itemReference.child("status").setValue(status);
    }

    private var candidateStatus: Int = 0
    private lateinit var candidateId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_detail_layout)

        mDatabase = FirebaseDatabase.getInstance().reference

        val bundle: Bundle? = intent.extras
        setData(bundle)
    }

    private fun setData(bundle: Bundle?) {
        //get the candidate unique id
        candidateId = bundle?.getString(Constants.CANDIDATE_ID)!!
        //store the candidate status
        candidateStatus = Integer.parseInt(bundle?.get(Constants.CANDIDATE_STATUS).toString())

        //set candidate image
        Glide.with(this)
                .load(bundle?.getString(Constants.CANDIDATE_IMAGE))
                .apply(RequestOptions().placeholder(R.mipmap.ic_launcher).centerCrop().fitCenter().error(R.mipmap.ic_launcher))
                .into(itemImage)

        //set name and description
        itemName.setText(bundle?.getString(Constants.CANDIDATE_NAME))
        itemDescription.setText(bundle?.getString(Constants.CANDIDATE_DESC))


        //Display Candidate status
        when (candidateStatus) {
            CandidateStatus.SHORTLISTED.ordinal -> selected.isChecked = true
            CandidateStatus.ON_HOLD.ordinal -> onhold.isChecked = true
            CandidateStatus.REJECTED.ordinal -> rejected.isChecked = true
            CandidateStatus.NONE.ordinal -> {
               /* selected.isChecked = false
                onhold.isChecked = false
                rejected.isChecked = false*/
                radiogroup_results.clearCheck()
            }
        }
    }

    fun onResultSelected(view: View) {
        val radioGroup: RadioGroup = findViewById(R.id.radiogroup_results)


        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@CandidateDetail)

        // Display a message on alert dialog
        builder.setMessage("Are you sure you want to change the status?")

        builder.setCancelable(false)

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button
            when (radioGroup.checkedRadioButtonId) {R.id.selected -> {
                Log.e("KTag", "rejected")
                //Store the updated value in Database
                rowListener.modifyCandidateState(candidateId!!, CandidateStatus.SHORTLISTED.ordinal)

            }
                R.id.onhold -> {
                    Log.e("KTag", "onhold")
                    //Store the updated value in Database
                    rowListener.modifyCandidateState(candidateId!!, CandidateStatus.ON_HOLD.ordinal)

                }
                R.id.rejected -> {
                    Log.e("KTag", "SHORTLISTED")
                    //Store the updated value in Database
                    rowListener.modifyCandidateState(candidateId!!, CandidateStatus.REJECTED.ordinal)

                }
            }
        }
        builder.setNegativeButton("No"){dialog,which ->
            //Display Candidate status
            when (candidateStatus) {
                CandidateStatus.SHORTLISTED.ordinal -> selected.isChecked = true
                CandidateStatus.ON_HOLD.ordinal -> onhold.isChecked = true
                CandidateStatus.REJECTED.ordinal -> rejected.isChecked = true
                CandidateStatus.NONE.ordinal -> {
                    /* selected.isChecked = false
                     onhold.isChecked = false
                     rejected.isChecked = false*/
                    radiogroup_results.clearCheck()
                }
            }
           dialog.dismiss()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }
}