package com.ayoprez.sobuu.shared.features.authentication.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionTokenApi(@field:Json(name = "result") val sessionToken: String)

@JsonClass(generateAdapter = true)
data class AuthSessionTokenApi(@field:Json(name = "sessionToken") val sessionToken: String)