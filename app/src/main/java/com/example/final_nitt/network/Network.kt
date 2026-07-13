package com.example.final_nitt.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

object Network {
    private val client: HttpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(HttpTimeout) {
            socketTimeoutMillis = 3000
            requestTimeoutMillis = 3000
            connectTimeoutMillis = 3000
        }

        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTP
                host = "10.0.2.2"
                port = 8000
            }
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
    }

    suspend fun register(reg: Register): RegisterResult{
        return try {
            val response = client.post {
                url {
                    path("/register")
                }
                contentType(ContentType.Application.Json)
                setBody(reg)
            }
            RegisterResult.Success(response.body<RegisterReceive>())
        }catch(e: ClientRequestException){
            val error = e.response.body<ErrorResponse>()
            RegisterResult.Error(error.detail)
        }catch(e: Exception){
            println(e.message)
            RegisterResult.Error(e.message)
        }
    }

    suspend fun login(log: Register): LoginResult{
        return try {
            val response = client.post {
                url {
                    path("/login")
                }
                contentType(ContentType.Application.Json)
                setBody(log)
            }
            LoginResult.Success(response.body<Token>())
        } catch (e: ClientRequestException) {
            val detail = e.response.body<ErrorResponse>()
            LoginResult.Error("$detail")
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun dashboardAccess(token: String?): RegisterResult{
        return try{
            val response1 = client.get("/dashboard"){
                header(
                    HttpHeaders.Authorization, "Bearer $token"
                )
            }
            RegisterResult.Success(response1.body<RegisterReceive>())
        }catch(e: ClientRequestException){
            val error = e.response.body<ErrorResponse>()
            RegisterResult.Error(error.detail)
        }catch(e: Exception){
            println(e.message)
            RegisterResult.Error(e.message)
        }
    }

    suspend fun followCount(token: String): FollowResult{
        return try{
            val response2 = client.get("user/follow/count"){
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<Follow>()
            FollowResult.Success(response2)
        }catch(e: Exception){
            println(e.message)
            FollowResult.Error(e.message)
        }
    }

    suspend fun postQuestion(que: QuestionPost, token: String): QuestionResult{
        return try{
            val response = client.post("/post/question"){
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(que)
            }.body<QuestionFormat>()
            QuestionResult.Success(response)
        }catch (e: Exception){
            println(e.message)
            QuestionResult.Error(e.message)
        }
    }

    suspend fun getMyQuestion(token: String): AllMyQuestionResult{
        return try{
            val response = client.get("/get/question"){
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<List<QuestionFormat>>()
            AllMyQuestionResult.Success(response)
        }catch (e: Exception){
            println(e.message)
            AllMyQuestionResult.Error(e.message)
        }
    }

    suspend fun solveQuestion(token: String, qid: Int): String{
        return try{
            val response = client.patch("/solve/question"){
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("q_id", qid)
            }.body<Map<String, String>>()
            response["detail"] ?: ""
        }catch(e: Exception){
            println(e.message)
            e.message.toString()
        }
    }

    suspend fun getAnswer(qid: Int): GetAnswerResult{
        return try{
            val response = client.get("/question/answers/$qid").body<List<AnswersForQidItem>>()
            GetAnswerResult.Success(response)
        }catch(e: Exception){
            println("network Issue1: ${e.message}")
            GetAnswerResult.Error(e.message.toString())
        }
    }

    suspend fun getVotes(aid: Int): String?{
        return try{
            val response = client.get("/get/votes"){
                parameter("id", aid)
            }.body<Map<String , Int>>()
            return response["votes"].toString()
        }catch(e: Exception){
            println("network Issue2: ${e.message}")
            return e.message.toString()
        }
    }

    suspend fun getOtherUserQuestion(token: String = ""): OtherUserQuestionResult{
        return try{
            val response = client.get("/user/questions/other"){
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<OtherUserQuestions>()
            OtherUserQuestionResult.Success(response)
        }catch(e: Exception){
            println("Error123: ${e.message}")
            OtherUserQuestionResult.Error(e.message.toString())
        }
    }

    suspend fun postVotes(cast: Vote, token : String): String{
        return try{
            val response = client.post("/vote/answer"){
                contentType(ContentType.Application.Json)
                setBody(cast)
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<Map<String, String>>()
            return response["message"].toString()
        }catch(e: Exception){
            println("Error404: ${e.message}")
            return e.message.toString()
        }
    }

    suspend fun getUserVotes(token: String): UserVoteResult{
        return try{
            val response = client.get("/votes/me"){
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<List<UserVote>>()
            return UserVoteResult.Success(response)
        }catch (e:Exception){
            println("Error1234: ${e.message}")
            return UserVoteResult.Error(e.message.toString())
        }
    }
    suspend fun postAnswer(token: String, qid: Int, answer: String): PostAnswerResult{
        val data = PostAns(id = qid, answer = answer)
        return try{
            val response = client.post("/post/answer"){
                contentType(ContentType.Application.Json)
                setBody(data)
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<AnswersForQidItem>()
            return PostAnswerResult.Success(response)
        }catch (e:Exception){
            println("Error1234: ${e.message}")
            return PostAnswerResult.Error(e.message.toString())
        }
    }

    suspend fun getFollowing(
        token: String
    ): FollowingResult {

        return try {

            val response = client.get("/user/following") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.isSuccess()) {

                val body = response.body<List<Owner>>()

                FollowingResult.Success(body)

            } else {

                FollowingResult.Error(
                    response.bodyAsText()
                )

            }

        } catch (e: Exception) {

            println("Error123: ${e.message}")

            FollowingResult.Error(
                e.message ?: "Unknown Error"
            )

        }
    }

    suspend fun unfollow(
        token: String,
        userId: Int
    ): UnfollowResult {
        return try {
            val response = client.delete("/unfollow") {
                header("Authorization", "Bearer $token")
                parameter("id", userId)
            }
            if (response.status.isSuccess()) {
                val body = response.body<JsonObject>()
                UnfollowResult.Success(
                    body["detail"]?.jsonPrimitive?.content ?: "Success"
                )
            } else {
                UnfollowResult.Error(
                    response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Error123: ${e.message}")
            UnfollowResult.Error(
                e.message ?: "Unknown Error"
            )
        }
    }

    suspend fun getFollowers(
        token: String
    ): FollowersResult {
        return try {
            val response = client.get("/user/followers") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            if (response.status.isSuccess()) {
                val body = response.body<List<Owner>>()
                FollowersResult.Success(body)
            } else {
                FollowersResult.Error(
                    response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Error123: ${e.message}")
            FollowersResult.Error(
                e.message ?: "Unknown Error"
            )
        }
    }

    suspend fun requestFollow(
        token: String,
        userId: Int
    ): Map<String, String> {
        return try {
            val response = client.get("/follow/request") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("id", userId)
            }
            if (response.status.isSuccess()) {
                response.body<Map<String, String>>()
            } else {
                mapOf(
                    "detail" to response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Error123: ${e.message}")
            mapOf(
                "detail" to (e.message ?: "Unknown Error")
            )
        }
    }

    suspend fun acceptFollowRequest(
        token: String,
        userId: Int
    ): Map<String, String> {
        return try {
            val response = client.patch("/follow/accept") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("id", userId)
            }
            if (response.status.isSuccess()) {
                response.body<Map<String, String>>()
            } else {
                mapOf(
                    "detail" to response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Error123: ${e.message}")
            mapOf(
                "detail" to (e.message ?: "Unknown Error")
            )
        }
    }

    suspend fun rejectFollowRequest(
        token: String,
        userId: Int
    ): Map<String, String> {
        return try {
            val response = client.delete("/follow/reject") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("id", userId)
            }
            if (response.status.isSuccess()) {
                response.body<Map<String, String>>()
            } else {
                mapOf(
                    "detail" to response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Error123: ${e.message}")
            mapOf(
                "detail" to (e.message ?: "Unknown Error")
            )
        }
    }

    suspend fun getDiscoverUsers(
        token: String
    ): GetDiscoverUsersResult {

        return try {

            val response = client.get("/user/discover") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.isSuccess()) {

                val body = response.body<List<DiscoverUser>>()

                GetDiscoverUsersResult.Success(body)

            } else {

                GetDiscoverUsersResult.Error(
                    response.bodyAsText()
                )

            }

        } catch (e: Exception) {

            println("Error123: ${e.message}")

            GetDiscoverUsersResult.Error(
                e.message ?: "Unknown Error"
            )

        }
    }

    suspend fun getFollowRequests(
        token: String
    ): GetFollowRequestsResult {

        return try {

            val response = client.get("/user/requests") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.isSuccess()) {

                val body = response.body<List<Owner>>()

                GetFollowRequestsResult.Success(body)

            } else {

                GetFollowRequestsResult.Error(
                    response.bodyAsText()
                )

            }

        } catch (e: Exception) {

            println("Error123: ${e.message}")

            GetFollowRequestsResult.Error(
                e.message ?: "Unknown Error"
            )

        }
    }
}

// discover follow status 1. pending requested , 2. none have a follow button