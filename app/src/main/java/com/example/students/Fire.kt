package com.example.students

import android.os.Bundle
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.students.ball.Univer
import com.google.firebase.firestore.FirebaseFirestore

class Fire : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyForm()
        }
    }

    @Composable
    fun MyForm() {
        var balinf by remember { mutableStateOf("0") }
        var balfiz by remember { mutableStateOf("0") }
        var balhim by remember { mutableStateOf("0") }
        var balbio by remember { mutableStateOf("0") }
        var balrus by remember { mutableStateOf("0") }
        var balprofmat by remember { mutableStateOf("0") }
        var balmat by remember { mutableStateOf("0") }
        var balobs by remember { mutableStateOf("0") }
        var balist by remember { mutableStateOf("0") }
        var balin by remember { mutableStateOf("0") }
        var name by remember { mutableStateOf("KFU") }
        var nap by remember { mutableStateOf("Information security") }
        var coord1 by remember { mutableStateOf("55.792139") }
        var coord2 by remember { mutableStateOf("49.122135") }
        var forma by remember { mutableStateOf("free") }

        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            // TextField items
            item {
                TextField(
                    value = balinf,
                    onValueChange = { balinf = it },
                    label = { Text("balinf") }
                )
            }
            item {
                TextField(
                    value = balfiz,
                    onValueChange = { balfiz = it },
                    label = { Text("balfiz") }
                )
            }
            item {
                TextField(
                    value = balhim,
                    onValueChange = { balhim = it },
                    label = { Text("balhim") }
                )
            }

            item {
                TextField(
                    value = balbio,
                    onValueChange = { balbio = it },
                    label = { Text("balbio") }
                )
            }
            item {
                TextField(
                    value = balrus,
                    onValueChange = { balrus = it },
                    label = { Text("balrus") }
                )
            }
            item {
                TextField(
                    value = balprofmat,
                    onValueChange = { balprofmat = it },
                    label = { Text("balprofmat") }
                )
            }
            item {
                TextField(
                    value = balmat,
                    onValueChange = { balmat = it },
                    label = { Text("balmat") }
                )
            }
            item {
                TextField(
                    value = balobs,
                    onValueChange = { balobs = it },
                    label = { Text("balobs") }
                )
            }
            item {
                TextField(
                    value = balist,
                    onValueChange = { balist = it },
                    label = { Text("balist") }
                )
            }
            item {
                TextField(
                    value = balin,
                    onValueChange = { balin = it },
                    label = { Text("balin") }
                )
            }

            item {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("name") }
                )
            }
            item {
                TextField(
                    value = nap,
                    onValueChange = { nap = it },
                    label = { Text("nap") }
                )
            }
            item {
                TextField(
                    value = coord1,
                    onValueChange = { coord1 = it },
                    label = { Text("coord1") }
                )
            }
            item {
                TextField(
                    value = coord2,
                    onValueChange = { coord2 = it },
                    label = { Text("coord2") }
                )
            }
            item {
                TextField(
                    value = forma,
                    onValueChange = { forma = it },
                    label = { Text("forma") }
                )
            }

            item {
                Button(
                    onClick = {
                        val db = FirebaseFirestore.getInstance()
                        db.collection("univer").document()
                            .set(
                                Univer(
                                    balobs = balobs,
                                    balist = balist,
                                    balin = balin,
                                    balrus = balrus,
                                    balmat = balmat,
                                    balhim = balhim,
                                    balbio = balbio,
                                    balfiz = balfiz,
                                    balinf = balinf,
                                    balprofmat = balprofmat,
                                    name = name,
                                    nap = nap,
                                    coord1 = coord1,
                                    coord2 = coord2,
                                    forma = forma
                                )
                            )
                    },
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}