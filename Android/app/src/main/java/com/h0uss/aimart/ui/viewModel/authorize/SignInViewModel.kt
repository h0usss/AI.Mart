package com.h0uss.aimart.ui.viewModel.authorize

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.saveUserId
import com.h0uss.aimart.Graph.userRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class SignInViewModel : ViewModel(){

    var state = MutableStateFlow(SignInState())
        private set

    var navigationEvents = Channel<SignInNavigationEvent>()
        private set


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: SignInEvent) {
        when(event){
            is SignInEvent.EmailChanged -> {
                state.update{
                    it.copy(emailValue = event.value, authError = null)
                }
            }
            is SignInEvent.PasswordChanged -> {
                state.update{
                    it.copy(passwordValue = event.value, authError = null)
                }
            }
            is SignInEvent.SignInClicked -> {
                val currentState = state.value
                val identifier = currentState.emailValue
                val password = currentState.passwordValue

                if (identifier.isBlank() || password.isBlank()) {
                    state.update { it.copy(authError = "Поля не должны быть пустыми") }
                    return
                }

                viewModelScope.launch {

                    val user = userRepository.getUserByEmailOrNick(identifier)

                    if (user == null || !BCrypt.checkpw(password, user.passwordHash)) {
                        state.update { it.copy(authError = "Неверный логин или пароль") }
                        return@launch
                    }

                    saveUserId(user.id)
                    navigationEvents.send(SignInNavigationEvent.Success)
                }
            }
            is SignInEvent.RegisterClicked -> {
                viewModelScope.launch {
                    navigationEvents.send(SignInNavigationEvent.NavigateToRegister)
                }
            }
            is SignInEvent.GoogleSignInClicked -> {}
            is SignInEvent.AgreementClicked -> {}
            is SignInEvent.PrivacyPolicyClicked -> {}
        }
    }
}

data class SignInState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val authError: String? = null,
)

sealed class SignInEvent {
    data class EmailChanged(val value: String) : SignInEvent()
    data class PasswordChanged(val value: String) : SignInEvent()
    object SignInClicked : SignInEvent()
    object GoogleSignInClicked : SignInEvent()
    object RegisterClicked : SignInEvent()
    object AgreementClicked : SignInEvent()
    object PrivacyPolicyClicked : SignInEvent()
}

sealed class SignInNavigationEvent {
    object Success : SignInNavigationEvent()
    object NavigateToRegister : SignInNavigationEvent()
}
