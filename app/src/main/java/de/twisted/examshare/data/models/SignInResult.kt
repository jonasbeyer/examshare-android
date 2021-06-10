package de.twisted.examshare.data.models

data class SignInResult(
        val token: String,
        val user: User
)