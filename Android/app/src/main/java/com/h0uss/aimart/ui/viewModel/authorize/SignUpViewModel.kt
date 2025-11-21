package com.h0uss.aimart.ui.viewModel.authorize

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.saveUserId
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.UserRegistrationData
import com.h0uss.aimart.util.toLocalDateTime
import com.h0uss.aimart.util.validate.validateDate
import com.h0uss.aimart.util.validate.validateMail
import com.h0uss.aimart.util.validate.validateName
import com.h0uss.aimart.util.validate.validatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel(){

    var state = MutableStateFlow(SignUpState())
        private set

    var navigationEvents = Channel<SignUpNavigationEvent>()
        private set


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: SignUpEvent) {
        when(event){
            is SignUpEvent.NameChanged -> {
                state.update{
                    it.copy(nameValue = event.value, nameError = null)
                }
            }
            is SignUpEvent.EmailChanged -> {
                state.update{
                    it.copy(emailValue = event.value, emailError = null)
                }
            }
            is SignUpEvent.PasswordChanged -> {
                state.update{
                    it.copy(passwordValue = event.value, passwordError = null)
                }
            }
            is SignUpEvent.DateChanged -> {
                state.update{
                    it.copy(dateValue = event.value, dateError = null)
                }

            }

            is SignUpEvent.SignUpClicked -> {
                val currentState = state.value

                val nameError = currentState.nameValue.validateName().takeIf { it.isNotEmpty() }
                val emailError = currentState.emailValue.validateMail().takeIf { it.isNotEmpty() }
                val dateError = currentState.dateValue.validateDate().takeIf { it.isNotEmpty() }
                val passwordError = currentState.passwordValue.validatePassword().takeIf { it.isNotEmpty() }

                val hasError = listOfNotNull(nameError, emailError, dateError, passwordError).any()

                if (hasError) {
                    state.update {
                        it.copy(
                            nameError = nameError,
                            emailError = emailError,
                            dateError = dateError,
                            passwordError = passwordError
                        )
                    }
                } else {
                    viewModelScope.launch {
                        val id = userRepository.createUser(
                            UserRegistrationData(
                                name = currentState.nameValue,
                                email = currentState.emailValue,
                                password = currentState.passwordValue,
                                dateOfBirth = currentState.dateValue.toLocalDateTime()
                            )
                        )
                        saveUserId(id)
                        
                        navigationEvents.send(SignUpNavigationEvent.Success)
                    }
                }
            }
            is SignUpEvent.LoginClicked -> {
                viewModelScope.launch {
                    navigationEvents.send(SignUpNavigationEvent.NavigateToLogin)
                }
            }
            is SignUpEvent.GoogleSignUpClicked -> {}
            is SignUpEvent.AgreementClicked -> {}
            is SignUpEvent.PrivacyPolicyClicked -> {}
        }
    }
}

data class SignUpState(
    val nameValue: String = "",
    val emailValue: String = "",
    val passwordValue: String = "",
    val dateValue: String = "",

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val dateError: String? = null,
)

sealed class SignUpEvent {
    data class NameChanged(val value: String) : SignUpEvent()
    data class EmailChanged(val value: String) : SignUpEvent()
    data class PasswordChanged(val value: String) : SignUpEvent()
    data class DateChanged(val value: String) : SignUpEvent()
    object SignUpClicked : SignUpEvent()
    object GoogleSignUpClicked : SignUpEvent()
    object LoginClicked : SignUpEvent()
    object AgreementClicked : SignUpEvent()
    object PrivacyPolicyClicked : SignUpEvent()
}

sealed class SignUpNavigationEvent {
    object Success : SignUpNavigationEvent()
    object NavigateToLogin : SignUpNavigationEvent()
}
