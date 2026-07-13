package com.example.final_nitt.mainscreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.final_nitt.mainscreen.ExploreUsers.ExploreUsersScreen
import com.example.final_nitt.Preference
import com.example.final_nitt.mainscreen.dashboard.DashBoardMainScreen
import com.example.final_nitt.mainscreen.otherDoubt.OtherAnswerQuestion
import com.example.final_nitt.network.FollowResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.RegisterResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController){
    val context = LocalContext.current
    val preference = remember {
        Preference(context.applicationContext)
    }
    val viewModel: MainScreenViewModel= viewModel(
        factory = MainScreenViewModelFactory(preference)
    )
    val state = viewModel.state.collectAsState()
    val destinations = remember {
        listOf<Destinations>(
            DashBoard,
            Doubt,
            ExploreUser,
            Clarify
        )
    }
    val scrollState = rememberScrollState()
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val mainScreenNavigation = rememberNavController()
    var route by rememberSaveable { mutableStateOf(DashBoard.route) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState),
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                destinations.forEachIndexed {index, item->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = {
                            selected = index
                            route = destinations[selected].route
                            mainScreenNavigation.navigate(item.route){
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                            )
                        },
                        label = {
                            Text(text = item.title, color = Color(0xFF845DB5),
                                fontWeight = FontWeight.Medium)
                        }
                    )
                }
            }

        }
    ){padding->
        NavHost(navController = mainScreenNavigation, startDestination = route){
            composable(route = DashBoard.route){
                DashBoardMainScreen(navController,padding, state.value)
            }
            composable(route = Doubt.route){
                AnswerDoubtScreen(padding)
            }
            composable(route = Clarify.route){
                OtherAnswerQuestion(padding)
            }
            composable(route = ExploreUser.route){
                ExploreUsersScreen(Modifier, padding)
            }
        }
    }
}


class MainScreenViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            return MainScreenViewModel(preference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class MainScreenViewModel(val preference: Preference) : ViewModel(){
    private val _state = MutableStateFlow<MainScreenState>(MainScreenState())
    val state = _state.asStateFlow()
    init{
        _state.update {
            it.copy(token = preference.getToken() ?: "")
        }
        load()
    }
    fun load(){
        viewModelScope.launch(Dispatchers.IO){
            val res1 = Network.dashboardAccess(_state.value.token)
            val res2 = Network.followCount(_state.value.token)
            println("Hello")
            if(res1 is RegisterResult.Success && res2 is FollowResult.Success){
                loadUser(res1, res2)
                println(res1.user.name)
            }
        }
    }
    fun loadUser(data : RegisterResult.Success, data2: FollowResult.Success){
        _state.update {
            it.copy(username = data.user.name, rollNo = data.user.rollno, department = data.user.department, followers = data2.success.follower, following = data2.success.following)
        }
    }
}