package com.example

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.testbasadata.R
import com.example.testbasadata.TestClassData
import com.google.firebase.database.*


class ReadActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private var adapter: ArrayAdapter<String>? = null
    private var listData: MutableList<String>? = null
    private var mDataBase: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_base)
        init()
        dataFromDb
    }

    private fun init() {
        listView = findViewById(R.id.id_list_view)
        listData = ArrayList()
        adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
            listData as ArrayList<String>
        )
        listView.adapter = adapter
        mDataBase = FirebaseDatabase.getInstance().reference
    }

    private val dataFromDb: Unit
        get() {
            val vListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (listData!!.size > 0) listData!!.clear()
                    for (ds in dataSnapshot.children) {

                        val user: TestClassData = ds.getValue(TestClassData::class.java)!!
                        user.name?.let { listData!!.add(it) }
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            mDataBase!!.addValueEventListener(vListener)
        }
}