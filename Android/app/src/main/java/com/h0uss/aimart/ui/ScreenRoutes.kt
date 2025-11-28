package com.h0uss.aimart.ui

import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreensRoutes

@Serializable
object Splash: ScreensRoutes

@Serializable
object Authorise: ScreensRoutes
@Serializable
object CreateOrLogin: ScreensRoutes
@Serializable
object Login: ScreensRoutes
@Serializable
object Register: ScreensRoutes

@Serializable
object Main: ScreensRoutes
@Serializable
object Home: ScreensRoutes
@Serializable
object Chat: ScreensRoutes
@Serializable
object MyProducts: ScreensRoutes
@Serializable
object Orders: ScreensRoutes
@Serializable
data class Seller(
    val id: Long
): ScreensRoutes
@Serializable
data class User(
    val id: Long
): ScreensRoutes
@Serializable
object Profile: ScreensRoutes
@Serializable
object ProfileEdit: ScreensRoutes

@Serializable
object Search: ScreensRoutes
@Serializable
object SearchTextField: ScreensRoutes
@Serializable
object SearchResult: ScreensRoutes