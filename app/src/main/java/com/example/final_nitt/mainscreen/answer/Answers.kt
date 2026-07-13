package com.example.final_nitt.mainscreen.answer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnswerCard(
    username: String,
    createdAt: String,
    answer: String,
    votes: Int,
    accepted: Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {

        Column(
            Modifier.padding(16.dp)
        ) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    username,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    createdAt,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                answer,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row {

                    Icon(
                        Icons.Default.ThumbUp,
                        null
                    )

                    Spacer(Modifier.width(4.dp))

                    Text("$votes")
                }

                if (accepted) {

                    Surface(
                        color = Color(0xFFC8E6C9),
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Row(
                            Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                Icons.Default.CheckCircle,
                                null,
                                tint = Color(0xFF2E7D32)
                            )

                            Spacer(Modifier.width(5.dp))

                            Text("Accepted")
                        }

                    }

                }

            }

        }

    }

}