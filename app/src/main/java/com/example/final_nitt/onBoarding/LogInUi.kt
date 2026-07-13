package com.example.final_nitt.onBoarding

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.final_nitt.CustomTextField
import com.example.final_nitt.OnBoardDestination
import com.example.final_nitt.PasswordTextField
import com.example.final_nitt.Preference
import com.example.final_nitt.SignIn
import kotlinx.coroutines.flow.collectLatest


@Composable
fun Logined(navController: NavHostController) {
    val context = LocalContext.current
    val preference = remember {
        Preference(context.applicationContext)
    }
    val viewModel: SignInViewModel = viewModel(
        factory = SignInViewModelFactory(preference)
    )
    LaunchedEffect(Unit) {
        viewModel.shared.collectLatest{
            when(it){
                OneTimeOnBoardEvent.NavigateToDashboardScreen -> {
                    navController.navigate("main")
                }
                is OneTimeOnBoardEvent.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    println("In Login Screen")
                }
            }
        }
    }
    val state = viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\nLogin Here",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = Modifier.padding(
                    top = 60.dp,
                    bottom = 20.dp
                )
            )
            Text(
                "Welcome back You have been missed!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Column(
                modifier = Modifier.fillMaxSize().offset(x = 0.dp, y = 60.dp)
            ) {
                CustomTextField( state.value.username, "User Name") {
                    viewModel.onEvent1(OnBoardUiEvent.SetUserName(it))
                }
                Spacer(modifier = Modifier.height(15.dp))
                PasswordTextField(state.value.password, { viewModel.onEvent1(OnBoardUiEvent.SetPassword(it))}, "Password")
                Spacer(modifier = Modifier.height(15.dp))
                CustomTextField(state.value.rollNo , "Roll No") {
                    viewModel.onEvent1(OnBoardUiEvent.SetRollNo(it))
                }
                Spacer(modifier = Modifier.height(35.dp))
                CustomTextField(state.value.department, "Department") {
                    viewModel.onEvent1(OnBoardUiEvent.SetDepartment(it))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        viewModel.onEvent1(OnBoardUiEvent.ClickLogIn)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Blue
                    ),
                    enabled = !state.value.isLoading,
                    modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth()
                ) {
                    if(!state.value.isLoading) Text(text = "SignUp", fontSize = 22.sp)
                    else  CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        navController.navigate(SignIn.route)
                    },
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black),
                    modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth()
                ) {
                    Text(
                        text = "Create new Account",
                        fontSize = 16.sp
                    )
                }

            }
        }
    }
}