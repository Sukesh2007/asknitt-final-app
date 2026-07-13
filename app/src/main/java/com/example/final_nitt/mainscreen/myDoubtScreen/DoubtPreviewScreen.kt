package com.example.final_nitt.mainscreen.myDoubtScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardQuestion(num: Int = 1, time: String = "2:02", question: String = "How are you", tags: String = "inform next", answers: Int = 10, isSolved : Boolean = false, taken: (String) -> Unit, press: () -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val status = if (isSolved) "Solved" else "Not Solved"
    Card(colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(color = Color.LightGray, width = 3.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(size = 16.dp),
        modifier = Modifier.padding(10.dp).clickable{
            press()
        }) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)){

                Row(
                    modifier = Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question $num",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = time,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }


                Text(
                    text = question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )


                Text(
                    text = tags,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )


                Text(
                    text = "$answers Answers",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {

                        OutlinedTextField(
                            value = status,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Status")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text("Solved")
                                },
                                onClick = {
                                    expanded = false
                                    taken("Solved")
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Unsolved")
                                },
                                onClick = {
                                    expanded = false
                                    taken("UnSolved")
                                }
                            )
                        }
                    }
                }

        }
    }
}

