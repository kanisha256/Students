package com.example.students

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.students.ball.Users
import com.example.students.prof.HomeActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(){
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                var tabIndex = rememberSaveable { mutableStateOf(0) }
                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()
                val tabTitles = listOf<String>("Логин", "Регистрация")
                val white = Color(0xFFA3A3A3)
                val ash7a = Color(0xFF414141)
                val pearl = Color(0xFF000000)


                Column {
                    TabRow(selectedTabIndex = tabIndex.value,
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .padding(top = 24.dp)
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
                                    .padding(8.dp)
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
                        if (tabIndex == 1){
                            Register()
                        } else {
                            Login()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    @Composable
    fun Register() {
        var email by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("ФИО") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Подтвердждение Пароль") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { registerUser(context, email = email,
                    name = name, password = password, confirmPassword = confirmPassword
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Регистрация")
            }
        }
    }

    private fun registerUser(context: Context, email: String,name: String, password: String, confirmPassword: String) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && name.isNotEmpty()) {
            if (password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                            db.collection("users").document(email)
                                .set(Users(email,name))
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(context, "Регистрация не успешна", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun Login() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Логин",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { loginUser(context, email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Вход")
            }
        }
    }

    private fun loginUser(context: Context, email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Вход успешен", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(context, "Вход не успешен", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

}