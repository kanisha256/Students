package com.example.students.prof

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.students.MainActivity
import com.example.students.R
import com.example.students.ball.*
import com.example.students.list.Univers
import com.example.students.map.MapYa
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Profile()
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Profile() {
        var name by remember { mutableStateOf("") }
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.email
        db
            .collection("users")
            .document(currentUser.toString())
            .get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    name = document.getString("username").toString()
                }
            }
        intent = Intent(this, MainActivity::class.java)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFF2C3E50))
            ) {
                // Картинка профиля
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp, top = 64.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { /* Handle image click event */ }
                    )
                    IconButton(onClick = {
                        val firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.signOut()
                        startActivity(intent)
                        finish()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Logout")
                    }
                }
                // Имя и статус профиля
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Абитуриент",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "Локация: Казань",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Описание профиля
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 220.dp, start = 16.dp, end = 16.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                var tabIndex = rememberSaveable { mutableStateOf(0) }
                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()
                val tabTitles = listOf<String>("Физ","Хим","Соц","Общ Б","Баллы")
                val white = Color(0xFFA3A3A3)
                val ash7a = Color(0xFF414141)
                val pearl = Color(0xFF666666)


                Column {
                    TabRow(selectedTabIndex = tabIndex.value,
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .background(color = Color.Transparent),
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier
                                    .pagerTabIndicatorOffset(
                                        pagerState,
                                        tabPositions
                                    )
                                    .height(0.dp)
                                    .size(0.dp)
                            )
                        }) {
                        tabTitles.forEachIndexed { index, title ->
                            val tabColor = remember {
                                Animatable(white)
                            }

                            val textColor = remember {
                                Animatable(ash7a)
                            }

                            LaunchedEffect(key1 = pagerState.currentPage == index) {
                                tabColor.animateTo(if (pagerState.currentPage == index) pearl else white)
                                textColor.animateTo(if (pagerState.currentPage == index) white else ash7a)
                            }

                            Tab(
                                selected = pagerState.currentPage == index,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(
                                        color = tabColor.value
                                    ),
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }) {
                                Text(
                                    tabTitles[index],
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    style = TextStyle(
                                        color = textColor.value,
                                        )
                                )
                            }

                        }
                    }
                    HorizontalPager(
                        count = tabTitles.size,
                        state = pagerState,
                    ) { tabIndex ->
                        if (tabIndex == 0){
                            Fizmat1()
                        }
                        if (tabIndex == 1){
                            Himbio1()
                        }
                        if (tabIndex == 2){
                            Socgym1()
                        }
                        if (tabIndex == 3){
                            Obsie1()
                        }
                        if (tabIndex == 4){
                            DisplayUserData()
                        }
                    }
                }
            }
        }
    }



    @Composable
    fun Fizmat1() {
        val balfiz = remember { mutableStateOf("0") }
        val balinf = remember { mutableStateOf("0") }

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser!!.email
        fun onButtonClick() {
            db.collection(email.toString()).document("1")
                .set(Fizmat(balinf = balinf.value, balfiz = balfiz.value))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = balfiz.value,
                onValueChange = { balfiz.value = it },
                label = { Text("Баллы по физике") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balinf.value,
                onValueChange = { balinf.value = it },
                label = { Text("Баллы по информатике") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Отправить")
            }
        }
    }

    @Composable
    fun Obsie1() {
        val balrus = remember { mutableStateOf("0") }
        val balprofmat = remember { mutableStateOf("0") }
        val balmat = remember { mutableStateOf("0") }

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser!!.email
        fun onButtonClick() {
            db.collection(email.toString()).document("4")
                .set(Obsie(balrus = balrus.value, balprofmat = balprofmat.value,balmat = balmat.value))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = balrus.value,
                onValueChange = { balrus.value = it },
                label = { Text("Баллы по русскому") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balmat.value,
                onValueChange = { balmat.value = it },
                label = { Text("Баллы по математике") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balprofmat.value,
                onValueChange = { balprofmat.value = it },
                label = { Text("Баллы по профильной математике") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Отправить")
            }
        }
    }

    @Composable
    fun Himbio1() {
        val balbio = remember { mutableStateOf("0") }
        val balhim = remember { mutableStateOf("0") }

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser!!.email
        fun onButtonClick() {
            db.collection(email.toString()).document("2")
                .set(Himbio(balbio = balbio.value, balhim = balhim.value))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            OutlinedTextField(
                value = balbio.value,
                onValueChange = { balbio.value = it },
                label = { Text("Баллы по биологии") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balhim.value,
                onValueChange = { balhim.value = it },
                label = { Text("Баллы по химии") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Отправить")
            }
        }
    }

    @Composable
    fun Socgym1() {
        val balobs = remember { mutableStateOf("0") }
        val balist = remember { mutableStateOf("0") }
        val balin = remember { mutableStateOf("0") }

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser!!.email
        fun onButtonClick() {
            db.collection(email.toString()).document("3")
                .set(Socgym(balobs = balobs.value, balist = balist.value, balin = balin.value))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = balobs.value,
                onValueChange = { balobs.value = it },
                label = { Text("Баллы по обществознанию") },
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = balin.value,
                onValueChange = { balin.value = it },
                label = { Text("Баллы по иностранному языку") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balist.value,
                onValueChange = { balist.value = it },
                label = { Text("Баллы по истории") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Отправить")
            }
        }
    }

    @Composable
    fun DisplayUserData() {
        val viewModel: FirestoreViewModel = viewModel()
        val balinf by viewModel.balinf.collectAsState(initial = "")
        val balfiz by viewModel.balfiz.collectAsState(initial = "")
        val balrus by viewModel.balrus.collectAsState(initial = "")
        val balprofmat by viewModel.balprofmat.collectAsState(initial = "")
        val balmat by viewModel.balmat.collectAsState(initial = "")
        val balhim by viewModel.balhim.collectAsState(initial = "")
        val balbio by viewModel.balbio.collectAsState(initial = "")
        val balobs by viewModel.balobs.collectAsState(initial = "")
        val balist by viewModel.balist.collectAsState(initial = "")
        val balin by viewModel.balin.collectAsState(initial = "")

        LaunchedEffect(Unit) {
            viewModel.fetchData1()
        }
        LaunchedEffect(Unit) {
            viewModel.fetchData2()
        }
        LaunchedEffect(Unit) {
            viewModel.fetchData3()
        }
        LaunchedEffect(Unit) {
            viewModel.fetchData4()
        }

        LazyColumn(Modifier.padding(top = 5.dp)) {
            item {
                Row(Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        Text("Информат: $balinf", fontSize = 24.sp)
                        Text("Физика: $balfiz", fontSize = 24.sp)
                        Text("Русский: $balrus", fontSize = 24.sp)
                        Text("Проф.Мат: $balprofmat", fontSize = 24.sp)
                        Text("Матем: $balmat", fontSize = 24.sp)
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Химия: $balhim", fontSize = 24.sp)
                        Text("Биология: $balbio", fontSize = 24.sp)
                        Text("Общество: $balobs", fontSize = 24.sp)
                        Text("История: $balist", fontSize = 24.sp)
                        Text("Ин. язык: $balin", fontSize = 24.sp)
                    }
                }
            }
        }

        Button(
            onClick = {
                val intent = Intent(this@HomeActivity, Univers::class.java)
                startActivity(intent)
                finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 250.dp)
        ) {
            Text(text = "К Университетам")
        }

    }


}