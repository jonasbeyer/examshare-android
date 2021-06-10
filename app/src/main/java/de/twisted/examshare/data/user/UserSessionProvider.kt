package de.twisted.examshare.data.user

interface UserSessionProvider {

    fun getSessionToken(): String?
}