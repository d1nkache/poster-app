package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.auth.AuthResponseDto
import com.example.poster.data.remote.dto.auth.SignInRequestDto
import com.example.poster.data.remote.dto.auth.SignUpRequestDto
import com.example.poster.data.remote.dto.auth.UserDto
import com.example.poster.data.remote.dto.auth.VerifyOtpRequestDto

interface AuthApi {
    suspend fun signUp(request: SignUpRequestDto): AuthResponseDto
    suspend fun signIn(request: SignInRequestDto): AuthResponseDto
    suspend fun verifyOtp(request: VerifyOtpRequestDto): AuthResponseDto
    suspend fun resendOtp(email: String)
    suspend fun refresh(): AuthResponseDto
    suspend fun logout()
    suspend fun me(): UserDto
}
