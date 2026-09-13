package com.example.final_nitt.mainscreen.answer

import android.text.format.Formatter.formatFileSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_nitt.network.Attachment

@Composable
fun QuestionHeader(
    questionNo: Int,
    createdAt: String,
    question: String,
    tags: String,
    answerCount: Int,
    isSolved: Boolean,
    attachment: List<Attachment>,
    onAttachmentClick: (Attachment) -> Unit
) {

    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
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
                    "Question #$questionNo",
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
                question,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            Text(
                tags,
                color = Color(0xFF1565C0),
                fontWeight = FontWeight.Medium
            )

            if (attachment.isNotEmpty()) {

                Spacer(Modifier.height(14.dp))

                attachment.forEach { file ->

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAttachmentClick(file)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE0E0E0)
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFE3F2FD)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "PDF",
                                    color = Color(0xFF1565C0),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = file.fileName,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(Modifier.height(3.dp))

                                Text(
                                    text = formatFileSize(context, file.fileSize.toLong()),
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                "View",
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Surface(
                    color =
                        if (isSolved)
                            Color(0xFFE8F5E9)
                        else
                            Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        if (isSolved) "Solved" else "Unsolved",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    )
                }

                Text(
                    "$answerCount Answers",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//@Composable
//fun QuestionHeader(
//    questionNo: Int,
//    createdAt: String,
//    question: String,
//    tags: String,
//    answerCount: Int,
//    isSolved: Boolean,
//    attachment: List<Attachment>
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(12.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        border = BorderStroke(1.dp, Color.LightGray)
//    ) {
//
//        Column(
//            Modifier.padding(16.dp)
//        ) {
//
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                Text(
//                    "Question #$questionNo",
//                    fontWeight = FontWeight.Bold
//                )
//
//                Text(
//                    createdAt,
//                    color = Color.Gray,
//                    fontSize = 12.sp
//                )
//            }
//
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                question,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(Modifier.height(10.dp))
//
//            Text(
//                tags,
//                color = Color(0xFF1565C0),
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(Modifier.height(14.dp))
//
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                Surface(
//                    color =
//                        if (isSolved)
//                            Color(0xFFE8F5E9)
//                        else
//                            Color(0xFFFFF3E0),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//
//                    Text(
//                        if (isSolved) "Solved" else "Unsolved",
//                        modifier = Modifier.padding(
//                            horizontal = 12.dp,
//                            vertical = 6.dp
//                        )
//                    )
//                }
//
//                Text(
//                    "$answerCount Answers",
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//        }
//
//    }
//}