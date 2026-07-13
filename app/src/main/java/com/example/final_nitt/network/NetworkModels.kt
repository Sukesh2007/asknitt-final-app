package com.example.final_nitt.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Register(
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String,
    @SerialName("rollno")
    val rollno: String,
    @SerialName("department")
    val department: String
)

@Serializable
data class RegisterReceive(
    @SerialName("name")
    val name: String,
    @SerialName("rollno")
    val rollno: String,
    @SerialName("department")
    val department: String
)

@Serializable
data class Token(
    @SerialName("encode_token")
    val token: String,
    @SerialName("type")
    val type: String
)

@Serializable
data class ErrorResponse(
    @SerialName("detail")
    val detail: String
)

@Serializable
data class Follow(
    @SerialName("followers")
    val follower: Int,
    @SerialName("following")
    val following: Int
)

@Serializable
data class QuestionPost(
    @SerialName("question")
    val question: String,
    @SerialName("tags")
    val tags: List<String>
)

typealias OtherUserQuestions = List<OtherUserQuestionsItem>

@Serializable
data class OtherUserQuestionsItem(
    @SerialName("department")
    val department: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("questions")
    val questions: List<QuestionFormat>,
    @SerialName("rollno")
    val rollno: String
)

@Serializable
data class Vote(
    @SerialName("answer_id")
    val id: Int,
    @SerialName("vote_dir")
    val dir: Int
)

@Serializable
data class UserVote(
    @SerialName("answer_id")
    val answerId: Int,

    @SerialName("vote_dir")
    val voteDir: Int
)

@Serializable
data class PostAns(
    @SerialName("question_id")
    val id: Int,
    @SerialName("answer")
    val answer: String
)

@Serializable
data class DiscoverUser(

    val id: Int,

    val name: String,

    val rollno: String,

    val department: String,

    @SerialName("follow_status")
    val followStatus: String
)