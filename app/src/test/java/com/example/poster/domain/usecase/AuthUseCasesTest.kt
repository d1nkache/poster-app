package com.example.poster.domain.usecase

import com.example.poster.domain.model.AuthSession
import com.example.poster.domain.model.StartDestination
import com.example.poster.domain.repository.AuthRepository
import com.example.poster.domain.usecase.app.GetStartDestinationUseCase
import com.example.poster.domain.usecase.auth.SignInUseCase
import com.example.poster.domain.usecase.auth.SignUpUseCase
import com.example.poster.domain.usecase.auth.VerifyOtpUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

class AuthUseCasesTest {

    @Test
    fun signInReturnsErrorWhenEmailIsBlank() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignInUseCase(repository).invoke("", "password")

        assertFalse(result.isSuccess)
        assertEquals("Email is empty", result.exceptionOrNull()?.message)
        assertEquals(0, repository.signInCalls)
    }

    @Test
    fun signInReturnsErrorWhenEmailIsInvalid() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignInUseCase(repository).invoke("user.example.com", "password")

        assertFalse(result.isSuccess)
        assertEquals("Invalid email", result.exceptionOrNull()?.message)
        assertEquals(0, repository.signInCalls)
    }

    @Test
    fun signInReturnsErrorWhenPasswordIsBlank() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignInUseCase(repository).invoke("user@example.com", " ")

        assertFalse(result.isSuccess)
        assertEquals("Password is empty", result.exceptionOrNull()?.message)
        assertEquals(0, repository.signInCalls)
    }

    @Test
    fun signInDelegatesValidCredentialsToRepository() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignInUseCase(repository).invoke("user@example.com", "password")

        assertTrue(result.isSuccess)
        assertEquals(1, repository.signInCalls)
        assertEquals("user@example.com", repository.lastEmail)
        assertEquals("password", repository.lastPassword)
    }

    @Test
    fun signUpReturnsErrorWhenNameIsBlank() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignUpUseCase(repository).invoke(" ", "user@example.com", "password")

        assertFalse(result.isSuccess)
        assertEquals("Name is empty", result.exceptionOrNull()?.message)
        assertEquals(0, repository.signUpCalls)
    }

    @Test
    fun signUpReturnsErrorWhenEmailIsInvalid() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignUpUseCase(repository).invoke("User", "user.example.com", "password")

        assertFalse(result.isSuccess)
        assertEquals("Invalid email", result.exceptionOrNull()?.message)
        assertEquals(0, repository.signUpCalls)
    }

    @Test
    fun signUpDelegatesValidDataToRepository() = runSuspend {
        val repository = FakeAuthRepository()
        val result = SignUpUseCase(repository).invoke("User", "user@example.com", "password")

        assertTrue(result.isSuccess)
        assertEquals(1, repository.signUpCalls)
        assertEquals("User", repository.lastName)
        assertEquals("user@example.com", repository.lastEmail)
        assertEquals("password", repository.lastPassword)
    }

    @Test
    fun verifyOtpReturnsErrorWhenCodeIsNotSixDigits() = runSuspend {
        val repository = FakeAuthRepository()
        val result = VerifyOtpUseCase(repository).invoke("user@example.com", "12a456")

        assertFalse(result.isSuccess)
        assertEquals("Invalid OTP code", result.exceptionOrNull()?.message)
        assertEquals(0, repository.verifyOtpCalls)
    }

    @Test
    fun verifyOtpDelegatesValidCodeToRepository() = runSuspend {
        val repository = FakeAuthRepository()
        val result = VerifyOtpUseCase(repository).invoke("user@example.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(1, repository.verifyOtpCalls)
        assertEquals("user@example.com", repository.lastEmail)
        assertEquals("123456", repository.lastOtpCode)
    }

    @Test
    fun getStartDestinationReturnsChatsWhenUserIsAuthorized() = runSuspend {
        val repository = FakeAuthRepository(isAuthorized = true)
        val destination = GetStartDestinationUseCase(repository).invoke()

        assertEquals(StartDestination.CHATS, destination)
    }

    private fun runSuspend(block: suspend () -> Unit) {
        var completed = false
        var failure: Throwable? = null

        block.startCoroutine(
            object : Continuation<Unit> {
                override val context = EmptyCoroutineContext

                override fun resumeWith(result: Result<Unit>) {
                    completed = true
                    failure = result.exceptionOrNull()
                }
            }
        )

        check(completed) { "Test coroutine suspended unexpectedly" }
        failure?.let { throw it }
    }

    private class FakeAuthRepository(
        private val isAuthorized: Boolean = false,
    ) : AuthRepository {
        var signInCalls = 0
            private set
        var signUpCalls = 0
            private set
        var verifyOtpCalls = 0
            private set
        var lastName: String? = null
            private set
        var lastEmail: String? = null
            private set
        var lastPassword: String? = null
            private set
        var lastOtpCode: String? = null
            private set

        override suspend fun signIn(email: String, password: String): Result<Unit> {
            signInCalls += 1
            lastEmail = email
            lastPassword = password
            return Result.success(Unit)
        }

        override suspend fun signUp(name: String, email: String, password: String): Result<Unit> {
            signUpCalls += 1
            lastName = name
            lastEmail = email
            lastPassword = password
            return Result.success(Unit)
        }

        override suspend fun verifyOtp(email: String, code: String): Result<Unit> {
            verifyOtpCalls += 1
            lastEmail = email
            lastOtpCode = code
            return Result.success(Unit)
        }

        override suspend fun resendOtp(email: String): Result<Unit> = Result.success(Unit)

        override suspend fun logout(): Result<Unit> = Result.success(Unit)

        override suspend fun isAuthorized(): Boolean = isAuthorized

        override suspend fun refreshToken(): Result<Unit> = Result.success(Unit)

        override suspend fun getActiveSession(): AuthSession? = null
    }
}
