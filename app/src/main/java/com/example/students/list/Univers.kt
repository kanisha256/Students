package com.example.students.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.students.R
import com.example.students.map.MapYa
import com.example.students.prof.FirestoreViewModel
import com.example.students.prof.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class Univers : ComponentActivity() {
    private var recview: RecyclerView? = null
    private var datalist: ArrayList<model?>? = null
    private var db: FirebaseFirestore? = null
    private var adapter: adapter? = null

    private val auth = FirebaseAuth.getInstance()
    private val email = auth.currentUser!!.email
    private val firestoreViewModel: FirestoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view)
        var button = findViewById(R.id.button4) as Button
        button.setOnClickListener {
            startActivity(Intent(this@Univers, HomeActivity::class.java))
            finish()
        }
        initializeViews()
        fetchValueFromFirestore()
        setupItemClickListener()
    }

    private fun initializeViews() {
        recview = findViewById<View>(R.id.recview) as RecyclerView
        recview!!.layoutManager = LinearLayoutManager(this)
        datalist = ArrayList()
        adapter = adapter(context = this, datalist)
        recview!!.adapter = adapter
    }

    private fun continueFetchingData(
        balinf: String?,
        balfiz: String?,
        balbio: String?,
        balhim: String?,
        balin: String?,
        balist: String?,
        balobs: String?,
        balmat: String?,
        balrus: String?,
        balprofmat: String?
    ) {
        db!!.collection("univer")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val list = queryDocumentSnapshots.documents
                for (d in list) {
                    val obj = d.toObject(model::class.java)
                    if (obj != null && obj.balinf!! <= balinf!!
                        && obj.balfiz!! <= balfiz!!
                        && obj.balbio!! <= balbio!!
                        && obj.balhim!! <= balhim!!
                        && obj.balin!! <= balin!!
                        && obj.balist!! <= balist!!
                        && obj.balobs!! <= balobs!!
                        && obj.balmat!! <= balmat!!
                        && obj.balrus!! <= balrus!!
                        && obj.balprofmat!! <= balprofmat!!
                    ) {
                        datalist!!.add(obj)
                    }
                }
                adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
            }
    }

    private fun fetchValueFromFirestore() {
        db = FirebaseFirestore.getInstance()
        db!!.collection(email.toString()).document("1")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val balinf = documentSnapshot.getString("balinf")
                val balfiz = documentSnapshot.getString("balfiz")
                fetchValueFromFirestore2(balinf, balfiz)
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
            }
    }

    private fun fetchValueFromFirestore2(balinf: String?, balfiz: String?) {
        db!!.collection(email.toString()).document("2")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val balbio = documentSnapshot.getString("balbio")
                val balhim = documentSnapshot.getString("balhim")
                fetchValueFromFirestore3(balinf, balfiz, balbio, balhim)
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
            }
    }

    private fun fetchValueFromFirestore3(
        balinf: String?,
        balfiz: String?,
        balbio: String?,
        balhim: String?
    ) {
        db!!.collection(email.toString()).document("3")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val balin = documentSnapshot.getString("balin")
                val balist = documentSnapshot.getString("balist")
                val balobs = documentSnapshot.getString("balobs")
                fetchValueFromFirestore4(balinf, balfiz, balbio, balhim, balin, balist, balobs)
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
            }
    }

    private fun fetchValueFromFirestore4(
        balinf: String?,
        balfiz: String?,
        balbio: String?,
        balhim: String?,
        balin: String?,
        balist: String?,
        balobs: String?
    ) {
        db!!.collection(email.toString()).document("4")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val balmat = documentSnapshot.getString("balmat")
                val balprofmat = documentSnapshot.getString("balprofmat")
                val balrus = documentSnapshot.getString("balrus")
                continueFetchingData(
                    balinf,
                    balfiz,
                    balbio,
                    balhim,
                    balin,
                    balist,
                    balobs,
                    balmat,
                    balrus,
                    balprofmat
                )
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
            }
    }


    private fun setupItemClickListener() {
        adapter?.setOnItemClickListener(object : adapter.OnItemClickListener {
            override fun onItemClick(item: model?, coord1: String, coord2: String) {
                val intent = Intent(this@Univers, MapYa::class.java)
                intent.putExtra("coord1", coord1)
                intent.putExtra("coord2", coord2)
                startActivity(intent)
                finish()
            }
        })
    }
}
