package com.example.testbasadata.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testbasadata.firstchat.LoginActivity
import com.example.testbasadata.R
import com.example.testbasadata.firstchat.TestClassData
import com.example.testbasadata.dialogs.DialogExit
import com.google.firebase.database.*

class Test : AppCompatActivity() {

    private lateinit var listView: ListView
    private var adapter: ArrayAdapter<String>? = null
    private var listData: MutableList<String>? = null
    private lateinit var mDataBase: DatabaseReference
    private lateinit var btn: View
    private lateinit var editT: EditText
    private lateinit var progressBarTwo: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        mDataBase = FirebaseDatabase.getInstance().reference
        btn = findViewById<ImageView>(R.id.imageViewF)
        initAdd()
        dataFromDb
        progressBarTwo = findViewById(R.id.progressBar2)
        editT = findViewById(R.id.ed_name_fire)

        btn.setOnClickListener {
            onClickButton()
        }
    }

    private fun onClickButton() {
        val editT = findViewById<EditText>(R.id.ed_name_fire)
        val name = editT.text.toString()

        if (name != "") {
            val newUser = TestClassData(name)
            mDataBase.push().setValue(newUser)
            editT.text = null
        } else {
            Toast.makeText(this, "Введіть повідомлення", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initAdd() {
        listView = findViewById(R.id.id_list_view_two)
        listData = ArrayList()
        adapter = ArrayAdapter(
            this, R.layout.list_custom_items,
            listData as ArrayList<String>
        )
        listView.adapter = adapter
        mDataBase = FirebaseDatabase.getInstance().reference
    }

    private val dataFromDb: Unit
        get() {
            val vListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    progressBarTwo.visibility = View.GONE
                    if (listData!!.size > 0) listData!!.clear()
                    for (ds in dataSnapshot.children) {
                        val user: TestClassData = ds.getValue(TestClassData::class.java)!!
                        user.name?.let { listData!!.add(it) }
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            mDataBase.addValueEventListener(vListener)
        }

    fun onCLick(item: MenuItem) {
        val myDialogFragment = DialogExit()
        val manager = supportFragmentManager
        myDialogFragment.show(manager, "myDialog")
    }

    fun exitActivity() {
        finish()
    }

    fun closeDataBase() {
        val dataBase = LoginActivity()
        dataBase.onExitBase()
    }
}