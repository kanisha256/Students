package com.example.students.prof

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.ball.Fizmat
import com.example.students.ball.Himbio
import com.example.students.ball.Obsie
import com.example.students.ball.Socgym
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val email = auth.currentUser!!.email

    // LiveData для хранения данных
    val balinf = MutableStateFlow("")
    val balfiz = MutableStateFlow("")
    val balrus = MutableStateFlow("")
    val balprofmat = MutableStateFlow("")
    val balmat = MutableStateFlow("")
    val balhim = MutableStateFlow("")
    val balbio = MutableStateFlow("")
    val balobs = MutableStateFlow("")
    val balist = MutableStateFlow("")
    val balin = MutableStateFlow("")

    // Функции для получения данных из Firestore
    fun fetchData1() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val collection = db.collection(email.toString())
                val document = collection.document("1")
                val snapshot = document.get().await()
                val data = snapshot.toObject(Fizmat::class.java)
                // Обновление состояния LiveData с полученными данными
                data?.let {
                    balinf.value = it.balinf.toString()
                    balfiz.value = it.balfiz.toString()
                }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun fetchData2() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val collection = db.collection(email.toString())
                val document = collection.document("2")
                val snapshot = document.get().await()
                val data = snapshot.toObject(Himbio::class.java)
                // Обновление состояния LiveData с полученными данными
                data?.let {
                    balhim.value = it.balhim.toString()
                    balbio.value = it.balbio.toString()
                }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun fetchData3() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val collection = db.collection(email.toString())
                val document = collection.document("3")
                val snapshot = document.get().await()
                val data = snapshot.toObject(Socgym::class.java)
                // Обновление состояния LiveData с полученными данными
                data?.let {
                    balobs.value = it.balobs.toString()
                    balist.value = it.balist.toString()
                    balin.value = it.balin.toString()
                }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun fetchData4() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val collection = db.collection(email.toString())
                val document = collection.document("4")
                val snapshot = document.get().await()
                val data = snapshot.toObject(Obsie::class.java)
                // Обновление состояния LiveData с полученными данными
                data?.let {
                    balmat.value = it.balmat.toString()
                    balrus.value = it.balrus.toString()
                    balprofmat.value = it.balprofmat.toString()
                }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

}
