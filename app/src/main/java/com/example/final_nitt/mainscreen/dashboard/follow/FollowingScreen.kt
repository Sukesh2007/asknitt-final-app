package com.example.final_nitt.mainscreen.dashboard.follow

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.final_nitt.Preference


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowingScreen(
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val preference = Preference(context.applicationContext)

    val viewModel: FollowingViewModel = viewModel(
        factory = FollowingViewModelFactory(preference)
    )
    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        "Following (${state.value.users.size})"
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
                itemsIndexed(state.value.users) { idx, user ->
                    FollowingUserCard(
                        name = user.name,
                        rollNo = user.rollno,
                        department = user.department,
                        isRemoving = user.id in state.value.removingUsers,
                        onUnfollow = {
                            viewModel.onEvent(FollowingEvent.Unfollow(user.id))
                        }
                    )
                }

        }
    }
}

@Composable
fun FollowingUserCard(
    name: String,
    rollNo: String,
    department: String,
    isRemoving: Boolean,
    onUnfollow: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = rollNo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = department,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

            }

            if (isRemoving) {

                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                OutlinedButton(
                    onClick = onUnfollow
                ) {
                    Icon(
                        Icons.Default.PersonRemove,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Unfollow")
                }

            }

        }

    }

}