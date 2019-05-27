package android.kodroid.com.easyhiring.adapters

import android.content.Context
import android.content.Intent
import android.kodroid.com.easyhiring.R
import android.kodroid.com.easyhiring.R.id.*
import android.kodroid.com.easyhiring.data.CandidateData
import android.kodroid.com.easyhiring.data.CandidateStatus
import android.kodroid.com.easyhiring.data.Constants
import android.kodroid.com.easyhiring.ui.CandidateDetail
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

public  class MyBaseAdapter(context: Context, candidateList: MutableList<CandidateData>): RecyclerView.Adapter<MyBaseAdapter.ViewHolder>() {
    private var itemList = candidateList
     var mContext=context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBaseAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(v,mContext)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MyBaseAdapter.ViewHolder, position: Int) {
        holder.bindItems(itemList.get(position))
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(view: View, context: Context): RecyclerView.ViewHolder(view){
       lateinit var candidate:CandidateData
        var context=context

        fun bindItems(data : CandidateData){
            this.candidate=data
            val _name: TextView = itemView.findViewById(R.id.tv_candidate_name)
            val _description: TextView = itemView.findViewById(R.id.tx_description)
            val _imageView: ImageView = itemView.findViewById(R.id.img_userprofile)
            val status_image: ImageView = itemView.findViewById(R.id.img_result)
           _name.text = data.name
            _description.text = data.description

            //Display Candidate picture
            Glide.with(context)
                    .load(data.profile_picture)
                    .apply(RequestOptions().placeholder(R.mipmap.ic_launcher).centerCrop().fitCenter().error(R.mipmap.ic_launcher))
                    .into(_imageView)

            //Display Candidate status
            when(this.candidate.status){
                CandidateStatus.SHORTLISTED.ordinal -> status_image.setImageResource(R.drawable.ic_hired)
                CandidateStatus.ON_HOLD.ordinal ->  status_image.setImageResource(R.drawable.ic_on_hold)
                CandidateStatus.REJECTED.ordinal ->  status_image.setImageResource(R.drawable.ic_rejected)
                CandidateStatus.NONE.ordinal ->{
                    status_image.visibility=View.INVISIBLE
                }
            }
            //set the onclick listener for the singlt list item
            itemView.setOnClickListener({
                Log.e("ItemClicked", data.name)
                val showPhotoIntent = Intent(context, CandidateDetail::class.java)

                showPhotoIntent.putExtra(Constants.CANDIDATE_NAME, candidate.name)
                showPhotoIntent.putExtra(Constants.CANDIDATE_DESC, candidate.description)
                showPhotoIntent.putExtra(Constants.CANDIDATE_IMAGE, candidate.profile_picture)
                showPhotoIntent.putExtra(Constants.CANDIDATE_STATUS, candidate.status)
                showPhotoIntent.putExtra(Constants.CANDIDATE_ID, candidate.id)
                context.startActivity(showPhotoIntent)
            })
        }

    }

}