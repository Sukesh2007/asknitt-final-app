package com.example.final_nitt.mainscreen.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.final_nitt.Home
import com.example.final_nitt.Preference
import com.example.final_nitt.R
import com.example.final_nitt.mainscreen.MainScreenState
import com.example.final_nitt.mainscreen.dashboard.follow.FollowersScreen
import com.example.final_nitt.mainscreen.dashboard.follow.FollowingScreen

@Composable
fun DashBoardMainScreen(
    navController: NavController,
    padding: PaddingValues,
    state: MainScreenState
){
    val navController2 = rememberNavController()
    var route by remember { mutableStateOf(Dashboard.route) }
    NavHost(navController = navController2, startDestination = route){
        composable(route = Dashboard.route){
            DashBoard(navController2, padding, state, navController)
        }
        composable(route = Follower.route){
            FollowersScreen(padding)
        }
        composable(route = Following.route){
            FollowingScreen(padding)
        }
    }
}

@Composable
fun DashBoard(navController: NavController, padding: PaddingValues, state: MainScreenState, navController2: NavController){
    val context = LocalContext.current
    val sharedPreference = Preference(context)

    Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).background(
        brush = Brush.verticalGradient(listOf( Color(0xFF0158BF),
            Color(0xFF3A8AE4), Color(0xFF9ECCF9)))
    ).padding(top = 60.dp, start = 12.dp,end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                ){
                    append("Ask")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(" NITT")
                }
            }
        )
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile Image",
            modifier = Modifier.padding(top = 10.dp).size(100.dp).clip(RoundedCornerShape(50))
        )
        Text(
            text = state.username,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = "Roll No: ${state.rollNo}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF004DB5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                CardBox(12, "Asked")
                CardBox(5, "Answered")
                CardBox(120, "Reputation")
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.White)
        ){
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 6.dp, bottom = 6.dp).fillMaxWidth(),
                text = "Information",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Blue
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp).fillMaxWidth(),
                thickness = 1.5.dp,
                color = DividerDefaults.color
            )
            Record(Icons.Filled.Email, "Email", "${state.rollNo}@nitt.edu")
            Record(Icons.Filled.School, "College", "Nit Trichy")
            Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                Icon(
                    painter = painterResource(R.drawable.branch_name),
                    contentDescription = "",
                    tint = Color.Blue
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Department",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = state.department, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Light
                )

            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp).fillMaxWidth(),
                thickness = 1.5.dp,
                color = DividerDefaults.color
            )
            Record(Icons.Filled.CalendarMonth, "Joined", "Aug 2024")
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround){
            ProfileActionButton(
                text = "Followers",
                icon = Icons.Default.Group,
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
            ){
                navController.navigate(Follower.route)
            }

            ProfileActionButton(
                text = "Following",
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp).clickable{
                    navController.navigate(Following.route)
                }
            ){
                navController.navigate(Following.route)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        ProfileActionButton(
            text = "Notification Settings",
            icon = Icons.Default.Notifications,
            modifier = Modifier
        ){
            println("Wait")
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Logout (Danger Button)
        Button(
            onClick = {
                sharedPreference.deleteToken()
                navController2.navigate(Home.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


interface DashboardMainScreen{
    val route : String
}

object Dashboard: DashboardMainScreen{
    override val route = "dashboard"
}

object Follower: DashboardMainScreen{
    override val route = "follower"
}

object Following: DashboardMainScreen{
    override val route = "following"
}

@Composable
fun Record(icons : ImageVector, label: String, detail : String){
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp)){
        Icon(
            imageVector = icons,
            contentDescription = label,
            tint = Color.Blue,
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = detail, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Light
        )

    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp).fillMaxWidth(),
        thickness = 1.5.dp,
        color = DividerDefaults.color
    )
}

@Composable
fun CardBox(value: Int, detail: String){
    Card(
        modifier = Modifier
            .width(90.dp)
            .height(70.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF035AC4),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = detail,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileActionButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF2F6FF),
            contentColor = Color(0xFF0D47A1)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Medium
        )
    }
}

