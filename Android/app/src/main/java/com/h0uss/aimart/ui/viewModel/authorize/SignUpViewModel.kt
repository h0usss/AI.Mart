package com.h0uss.aimart.ui.viewModel.authorize

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.h0uss.aimart.Graph.saveUserId
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.emun.FormField
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

            is SignUpEvent.SignUpClicked -> {
                viewModelScope.launch {
                    val currentState = state.value

                    val nameError =
                        currentState.nameState.text.toString().validateName().takeIf { it.isNotEmpty() }
                    val emailError =
                        currentState.emailState.text.toString().validateMail().takeIf { it.isNotEmpty() }
                    val dateError =
                        currentState.dateState.text.toString().validateDate().takeIf { it.isNotEmpty() }
                    val passwordError =
                        currentState.passwordState.text.toString().validatePassword().takeIf { it.isNotEmpty() }

                    val hasError =
                        listOfNotNull(nameError, emailError, dateError, passwordError).any()
                    val isRegister =
                        userRepository.getUserByEmailOrNick(currentState.emailState.text.toString())

                    if (hasError) {
                        state.update {
                            it.copy(
                                nameError = nameError,
                                emailError = emailError,
                                dateError = dateError,
                                passwordError = passwordError
                            )
                        }
                    } else if (isRegister != null) {
                        state.update {
                            it.copy(
                                emailError = "Пользователь с такой почтой уже зарегистрирован",
                            )
                        }
                    } else {
                        val id = userRepository.createUser(
                            UserRegistrationData(
                                name = currentState.nameState.text.toString(),
                                email = currentState.emailState.text.toString(),
                                password = currentState.passwordState.text.toString(),
                                dateOfBirth = currentState.dateState.text.toString().toLocalDateTime()
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
            is SignUpEvent.DateSelected -> {
                state.update {
                    it.copy(
                        dateState = TextFieldState(event.date),
                        dateError = "",
                    )
                }
            }
            is SignUpEvent.ClearError -> {
                state.update { currentState ->
                    when (event.field) {
                        FormField.NAME -> currentState.copy(nameError = "")
                        FormField.EMAIL -> currentState.copy(emailError = "")
                        FormField.PASSWORD -> currentState.copy(passwordError = "")
                        FormField.DATE -> currentState.copy(dateError = "")
                        FormField.IMAGE -> currentState.copy(dateError = "")
                        FormField.PRICE -> currentState.copy(dateError = "")
                        FormField.DESCRIPTION -> currentState.copy(dateError = "")
                    }
                }
            }
            is SignUpEvent.GoogleSignUpClicked -> {}
            is SignUpEvent.AgreementClicked -> {}
            is SignUpEvent.PrivacyPolicyClicked -> {}
        }
    }
}

data class SignUpState(
    val nameState: TextFieldState = TextFieldState(""),
    val emailState: TextFieldState = TextFieldState(""),
    val passwordState: TextFieldState = TextFieldState(""),
    val dateState: TextFieldState = TextFieldState(""),

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val dateError: String? = null,
)

sealed class SignUpEvent {
    data class DateSelected(val date: String) : SignUpEvent()
    data class ClearError(val field: FormField) : SignUpEvent()
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
