package com.h0uss.aimart.ui.theme

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun AnnotatedString.Builder.agreementAnnotatedString(
    isLogin: Boolean,
    onClickAgreement: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
) {
    val lightStyle = SpanStyle(
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        color = Black45
    )
    val heavyStyle = SpanStyle(
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        color = Black100
    )
    val innerText = if (isLogin) "\"Войти\"" else "\"Зарегистрироваться\""

    withStyle(
        style = lightStyle
    ) {
        append("Нажимая $innerText, вы соглашаетесь\nс ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "agreement",
                linkInteractionListener = LinkInteractionListener {
                    onClickAgreement()
                },
                styles = TextLinkStyles(
                    style = heavyStyle
                )
            )
        ) {
            append("Пользовательским соглашением")
        }

        append(" и ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "privacyPolicy",
                linkInteractionListener = LinkInteractionListener {
                    onClickPrivacyPolicy()
                },
                styles = TextLinkStyles(
                    style = heavyStyle
                )
            )
        ) {
            append("Политикой конфиденциальности")
        }
    }
}

fun AnnotatedString.Builder.noAccountAnnotatedString(
    onClickRegister: () -> Unit,
) {
    val heavyStyle = SpanStyle(
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        color = Black100
    )
    val blueStyle = SpanStyle(
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        color = Blue
    )

    withStyle(
        style = heavyStyle
    ) {
        append("Нет аккаунта? ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "register",
                linkInteractionListener = LinkInteractionListener {
                    onClickRegister()
                },
                styles = TextLinkStyles(
                    style = blueStyle
                )
            )
        ) {
            append("Зарегистрироваться")
        }
    }
}

fun AnnotatedString.Builder.haveAccountAnnotatedString(
    onClickLogin: () -> Unit,
) {
    val heavyStyle = SpanStyle(
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        color = Black100
    )
    val blueStyle = SpanStyle(
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        color = Blue
    )

    withStyle(
        style = heavyStyle
    ) {
        append("Уже есть аккаунт? ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "register",
                linkInteractionListener = LinkInteractionListener {
                    onClickLogin()
                },
                styles = TextLinkStyles(
                    style = blueStyle
                )
            )
        ) {
            append("Войти")
        }
    }
}