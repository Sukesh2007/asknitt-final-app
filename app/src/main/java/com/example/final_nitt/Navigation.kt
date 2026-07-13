package com.example.final_nitt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.final_nitt.mainscreen.MainScreen
import com.example.final_nitt.home.Home
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.RegisterResult
import com.example.final_nitt.onBoarding.Logined
import com.example.final_nitt.onBoarding.SignIn

@Composable
fun MyNavigation() {

    val context = LocalContext.current
    val token = Preference(context).getToken()

    var route by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {

        val type = Network.dashboardAccess(token)

        route = when(type){
            is RegisterResult.Error -> Home.route
            is RegisterResult.Success -> "main"
        }
    }

    if(route == null){

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }

    }else{

        val navController = rememberNavController()
        //route = Home.route
        NavHost(
            navController = navController,
            startDestination = route!!
        ){

            composable(Home.route){
                Home(navController)
            }

            composable(SignIn.route){
                SignIn(navController)
            }

            composable(Logs.route){
                Logined(navController)
            }

            composable("main"){
                MainScreen(navController)
            }

        }

    }

}