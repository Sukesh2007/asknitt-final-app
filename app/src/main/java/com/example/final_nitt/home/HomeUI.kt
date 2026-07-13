package com.example.final_nitt.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.final_nitt.Logs
import com.example.final_nitt.OnBoardDestination
import com.example.final_nitt.R
import com.example.final_nitt.SignIn
import com.example.final_nitt.onBoarding.OnBoardUiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Home(navController: NavHostController) {

    val viewmodel: HomeViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewmodel.shard.collectLatest {
            when(it){
                HomeNavigate.ToLoginScreen -> {
                    navController.navigate(Logs.route)
                }
                HomeNavigate.ToSignInScreen -> {
                    navController.navigate(SignIn.route)
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1565C0), // Dark blue (top)
            Color(0xFF42A5F5), // Light blue (middle)
            Color.White        // White (bottom)
        )
    )).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(painter = painterResource(R.drawable.home_image),
            contentDescription = "onBoard_image")
        Text(
            buildAnnotatedString{
                withStyle (
                    style = SpanStyle(
                        color = Color.Blue,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                    )
                ){
                    append("Ask")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xEEB5DCFB),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append(" Nitt")
                }
            },
            modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Image(
            painter = painterResource(R.drawable.nitt),
            contentDescription = "Nitt logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(
            text = "Platform to Clarify your Doubts",
            color = Color(0xFFB5DCFB),
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 10.dp, bottom = 25.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Button(onClick = {
            viewmodel.onEvent(HomeNavigate.ToLoginScreen)
        },
            colors = ButtonDefaults.buttonColors(Color.Blue, contentColor = Color.White),
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth())
        {
            Text(text = "Login", fontSize = 22.sp)
        }
        Button(onClick = {
            viewmodel.onEvent(HomeNavigate.ToSignInScreen)
        },
            colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color.Blue),
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp).fillMaxWidth()){
            Text(text = "SignUp", fontSize = 22.sp)
        }
        Text(
            text = "Login to ask questions, share Answer and join the discussion",
            color = Color.DarkGray,
            fontSize = 12.sp
        )
    }
}