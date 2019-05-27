package android.kodroid.com.easyhiring.ui

import android.content.Context
import android.kodroid.com.easyhiring.R
import android.kodroid.com.easyhiring.adapters.MyBaseAdapter
import android.kodroid.com.easyhiring.data.CandidateData
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.home_layout.*

class HomeActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var toDoItemList: MutableList<CandidateData>? = null
    private var mDataListener: ValueEventListener? = null
    lateinit var adapter: MyBaseAdapter
    private lateinit var _recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        _recyclerView = findViewById(R.id.recyclerview)
        _recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable()){
            init()
        }else{
            val snak = Snackbar.make(root_layout,"Please check the internet connection!", Snackbar.LENGTH_LONG)
            snak.show()
        }

    }

    override fun onStart() {
        super.onStart()
    }
    private fun init() {
        var itemListener: ValueEventListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                addDataToList(dataSnapshot)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }
        }
        mDataListener=itemListener
        mDatabase = FirebaseDatabase.getInstance().reference
        toDoItemList = mutableListOf<CandidateData>()
        adapter = MyBaseAdapter(this, toDoItemList!!)
        _recyclerView!!.setAdapter(adapter)
        this.mDatabase?.orderByKey()!!.addListenerForSingleValueEvent(mDataListener)
    }



    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                val todoItem = CandidateData.create()
                //get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>
                //key will return Firebase ID
                todoItem.id = map.get("id") as String?
                todoItem.status = (map.get("status") as Long).toInt()
                todoItem.name = map.get("name") as String?
                todoItem.description = map.get("description") as String?
                todoItem.profile_picture = map.get("profile_picture") as String?
                toDoItemList!!.add(todoItem);
            }
        }
        //alert adapter that has changed
        adapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()

        if (mDataListener != null) {
            mDatabase!!.removeEventListener(mDataListener)
        }
    }
    fun isNetworkAvailable():Boolean{
        val manager: Any? =getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (manager is ConnectivityManager){
            val networkInfo:NetworkInfo?=manager.activeNetworkInfo
            networkInfo?.isConnected?:false
        }else false
    }
}
