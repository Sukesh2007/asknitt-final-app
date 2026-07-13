package com.example.final_nitt.network


sealed class RegisterResult {
    data class Success(
        val user: RegisterReceive
    ) : RegisterResult()
    data class Error(
        val message: String?
    ) : RegisterResult()
}

sealed class LoginResult {
    data class Success(
        val user: Token
    ) : LoginResult()
    data class Error(
        val message: String?
    ) : LoginResult()
}

sealed class FollowResult {
    data class Success(
        val success: Follow
    ) : FollowResult()
    data class Error(
        val message: String?
    ): FollowResult()
}

sealed class QuestionResult{
    data class Success(
        val success: QuestionFormat
    ) : QuestionResult()
    data class Error(
        val message: String?
    ) : QuestionResult()
}

sealed class AllMyQuestionResult{
    data class Success(
        val success: List<QuestionFormat>
    ) : AllMyQuestionResult()
    data class Error(
        val message: String?
    ) : AllMyQuestionResult()
}

sealed class GetAnswerResult{
    data class Success(val message: List<AnswersForQidItem>) : GetAnswerResult()
    data class Error(val message: String) : GetAnswerResult()
}

sealed class OtherUserQuestionResult{
    data class Success(val message: OtherUserQuestions) : OtherUserQuestionResult()
    data class Error(val message: String) : OtherUserQuestionResult()
}

sealed class UserVoteResult {

    data class Success(
        val message: List<UserVote>
    ) : UserVoteResult()

    data class Error(
        val message: String
    ) : UserVoteResult()
}

sealed class PostAnswerResult{
    data class Success(val message: AnswersForQidItem) : PostAnswerResult()
    data class Error(val message: String) : PostAnswerResult()
}

sealed class FollowingResult {
    data class Success(
        val message: List<Owner>
    ) : FollowingResult()

    data class Error(
        val message: String
    ) : FollowingResult()
}

sealed class UnfollowResult {

    data class Success(
        val message: String
    ) : UnfollowResult()

    data class Error(
        val message: String
    ) : UnfollowResult()

}

sealed class FollowersResult {

    data class Success(
        val message: List<Owner>
    ) : FollowersResult()

    data class Error(
        val message: String
    ) : FollowersResult()

}

sealed interface GetFollowRequestsResult {

    data class Success(
        val users: List<Owner>
    ) : GetFollowRequestsResult

    data class Error(
        val error: String
    ) : GetFollowRequestsResult
}

sealed interface GetDiscoverUsersResult {

    data class Success(
        val users: List<DiscoverUser>
    ) : GetDiscoverUsersResult

    data class Error(
        val error: String
    ) : GetDiscoverUsersResult
}