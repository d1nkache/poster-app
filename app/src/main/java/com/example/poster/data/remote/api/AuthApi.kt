package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.auth.AuthResponseDto
import com.example.poster.data.remote.dto.auth.RefreshRequestDto
import com.example.poster.data.remote.dto.auth.RegisterResponseDto
import com.example.poster.data.remote.dto.auth.SignInRequestDto
import com.example.poster.data.remote.dto.auth.SignUpRequestDto
import com.example.poster.data.remote.dto.auth.VerifyOtpRequestDto

interface AuthApi {
    suspend fun signUp(request: SignUpRequestDto): RegisterResponseDto
    suspend fun signIn(request: SignInRequestDto): AuthResponseDto
    suspend fun verifyOtp(request: VerifyOtpRequestDto): AuthResponseDto
    suspend fun refresh(request: RefreshRequestDto): AuthResponseDto
    suspend fun logout()
}
