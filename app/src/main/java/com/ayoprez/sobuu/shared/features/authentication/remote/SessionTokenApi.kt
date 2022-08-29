package com.ayoprez.sobuu.shared.features.authentication.remote

import com.squareup.moshi.Json

data class SessionTokenApi(@field:Json(name = "result") val sessionToken: String)