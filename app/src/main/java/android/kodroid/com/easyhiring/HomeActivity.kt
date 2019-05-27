package android.kodroid.com.easyhiring

import android.kodroid.com.easyhiring.adapters.MyBaseAdapter
import android.kodroid.com.easyhiring.data.CandidateData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class HomeActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var toDoItemList: MutableList<CandidateData>? = null
    lateinit var adapter: MyBaseAdapter
    private lateinit var _recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

         _recyclerView = findViewById(R.id.recyclerview)
        _recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

//init()
    }

    override fun onResume() {
        super.onResume()
        Log.e("KTag", "OnResume()")
        init()
    }
    private fun init(){
        mDatabase = FirebaseDatabase.getInstance().reference
        toDoItemList = mutableListOf<CandidateData>()
        adapter = MyBaseAdapter(this, toDoItemList!!)
        _recyclerView!!.setAdapter(adapter)
        this.mDatabase?.orderByKey()!!.addListenerForSingleValueEvent(itemListener)
    }
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
                todoItem.id= map.get("id") as String?
                todoItem.status  = (map.get("status") as Long).toInt()
                todoItem.name = map.get("name") as String?
                todoItem.description = map.get("description") as String?
                todoItem.profile_picture= map.get("profile_picture") as String?
                toDoItemList!!.add(todoItem);
            }
        }
        //alert adapter that has changed
        adapter.notifyDataSetChanged()
    }
}
