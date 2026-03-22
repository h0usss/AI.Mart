package com.h0uss.aimart.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.data.model.AlertData
import com.h0uss.aimart.data.model.OrderData
import com.h0uss.aimart.data.model.PortfolioItemData
import com.h0uss.aimart.ui.assets.Alert
import com.h0uss.aimart.ui.assets.ShowPortfolio
import com.h0uss.aimart.ui.assets.SuccessNewOrder
import com.h0uss.aimart.ui.assets.chat.TaskBar
import com.h0uss.aimart.ui.state.authorize.SignIn
import com.h0uss.aimart.ui.state.authorize.SignUp
import com.h0uss.aimart.ui.state.chat.ChatUser
import com.h0uss.aimart.ui.state.chat.Chats
import com.h0uss.aimart.ui.state.create.NewOrder
import com.h0uss.aimart.ui.state.create.NewProduct
import com.h0uss.aimart.ui.state.info.OrderInfo
import com.h0uss.aimart.ui.state.info.ProductInfo
import com.h0uss.aimart.ui.state.main.Home
import com.h0uss.aimart.ui.state.main.MyProducts
import com.h0uss.aimart.ui.state.main.Orders
import com.h0uss.aimart.ui.state.profile.SellerProfileForSelf
import com.h0uss.aimart.ui.state.profile.SellerProfileForSelfEdit
import com.h0uss.aimart.ui.state.profile.SellerProfileForUser
import com.h0uss.aimart.ui.state.profile.TopUpWallet
import com.h0uss.aimart.ui.state.profile.UserProfileForOther
import com.h0uss.aimart.ui.state.profile.UserProfileForSelf
import com.h0uss.aimart.ui.state.search.Search
import com.h0uss.aimart.ui.state.search.SearchResult
import com.h0uss.aimart.ui.theme.Black20Transparent
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.viewModel.search.SearchViewModel

@SuppressLint("WrongStartDestinationType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    var isBottomNavBarShow by remember { mutableStateOf(false) }
    var isSplashSender by remember { mutableStateOf(true) }
    var isSeller by rememberSaveable { mutableStateOf<Boolean?>(null) }

    var alertData by remember { mutableStateOf<AlertData?>(null) }
    var portfolioData by remember { mutableStateOf<PortfolioItemData?>(null) }
    var sellerIdForOrder by remember { mutableLongStateOf(-1L) }
    var productIdForOrder by remember { mutableLongStateOf(-1L) }
    var isSuccessNewOrder by remember { mutableStateOf(false) }
    var isTopUpWallet by remember { mutableStateOf(false) }
    var taskBarInfo by remember { mutableStateOf<OrderData?>(null) }

    LaunchedEffect(authUserIdLong) {
        isSeller = if (authUserIdLong != 0L)
            userRepository.getUserIsSeller(authUserIdLong)
        else
            null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Splash
                ) {
                    composable<Splash> {
                        isBottomNavBarShow = false
                        isSplashSender = true
                        Splash(
                            navToHome = {
                                navController.navigate(Main)
                            },
                            navToNext = {
                                navController.navigate(Authorise)
                            }
                        )
                    }

                    navigation<Authorise>(
                        startDestination = CreateOrLogin
                    ) {
                        composable<CreateOrLogin> {
                            isBottomNavBarShow = false
                            CreateOrLogin(
                                isSplashSender = isSplashSender,
                                navToLogin = {
                                    navController.navigate(Login)
                                },
                                navToRegister = {
                                    navController.navigate(Register)
                                },
                            )
                            isSplashSender = false
                        }
                        composable<Login> {
                            isBottomNavBarShow = false

                            SignIn(
                                navToHome = {
                                    navController.navigate(Main)
                                },
                                navToRegister = {
                                    navController.navigate(Register)
                                },
                            )
                        }

                        composable<Register> {
                            isBottomNavBarShow = false

                            SignUp(
                                navToHome = {
                                    navController.navigate(Main)
                                },
                                navToLogin = {
                                    navController.navigate(Login)
                                },
                            )
                        }
                    }

                    navigation<Main>(
                        startDestination = Home
                    ) {
                        composable<Home> {
                            isBottomNavBarShow = true

                            Home(
                                navToSeller = { sellerId ->
                                    navController.navigate(Seller(sellerId))
                                },
                                navToProduct = { productId ->
                                    navController.navigate(ProductInfo(productId))
                                },
                                navToSearch = {
                                    navController.navigate(SearchTextField)
                                }
                            )
                        }
                        composable<ProductInfo> {
                            isBottomNavBarShow = true

                            ProductInfo(
                                productId = it.toRoute<ProductInfo>().productId,
                                navToUser = { userId ->
                                    navController.navigate(Seller(userId))
                                },
                                onBuy = { sellerId, productId ->
                                    sellerIdForOrder = sellerId
                                    productIdForOrder = productId
                                },
                                navToBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        navigation<Chat>(
                            startDestination = ChatList
                        ) {
                            composable<ChatList> {
                                isBottomNavBarShow = true
                                Chats(
                                    navToChat = { chatId ->
                                        navController.navigate(ChatWithUser(chatId))
                                    }
                                )
                            }

                            composable<ChatWithUser> {
                                isBottomNavBarShow = false
                                ChatUser(
                                    chatId = it.toRoute<ChatWithUser>().id,
                                    navToChatList = {
                                        navController.navigate(ChatList)
                                    },
                                    navToUser = { userId ->
                                        navController.navigate(User(userId))
                                    },
                                    navToSeller = { userId ->
                                        navController.navigate(Seller(userId))
                                    },
                                    onTaskBarClick = { orderInfo ->
                                        taskBarInfo = orderInfo
                                    },
                                    navToProduct = { productId ->
                                        navController.navigate(ProductInfo(productId))
                                    },
                                )
                            }
                        }
                        navigation<Search>(
                            startDestination = SearchTextField
                        ) {
                            composable<SearchTextField> {
                                isBottomNavBarShow = true
                                val backStackEntry =
                                    remember(it) { navController.getBackStackEntry(Search) }
                                val searchViewModel: SearchViewModel = viewModel(backStackEntry)

                                Search(
                                    viewModel = searchViewModel,
                                    navToSeller = { sellerId ->
                                        navController.navigate(Seller(sellerId))
                                    },
                                    navToProduct = { productId ->
                                        navController.navigate(ProductInfo(productId))
                                    },
                                    navToSearchResult = {
                                        navController.navigate(SearchResult)
                                    },
                                    navToSearch = {
                                        navController.navigate(SearchTextField)
                                    },
                                    navToHome = {
                                        navController.navigate(Home)
                                    },
                                )
                            }

                            composable<SearchResult> {
                                isBottomNavBarShow = true
                                val backStackEntry =
                                    remember(it) { navController.getBackStackEntry(Search) }
                                val searchViewModel: SearchViewModel = viewModel(backStackEntry)

                                SearchResult(
                                    viewModel = searchViewModel,
                                    navToSeller = { sellerId ->
                                        navController.navigate(Seller(sellerId))
                                    },
                                    navToProduct = { productId ->
                                        navController.navigate(ProductInfo(productId))
                                    },
                                    navToSearchResult = {
                                        navController.navigate(SearchResult)
                                    },
                                    navToSearch = {
                                        navController.navigate(SearchTextField)
                                    },
                                    navToHome = {
                                        navController.navigate(Home)
                                    },
                                )
                            }
                        }

                        navigation<Products>(
                            startDestination = MyProducts
                        ) {
                            composable<MyProducts> {
                                isBottomNavBarShow = true

                                MyProducts(
                                    navToProduct = { productId ->
                                        navController.navigate(ProductInfo(productId))
                                    },
                                    navToNewProduct = {
                                        navController.navigate(NewProduct)
                                    }
                                )
                            }

                            composable<NewProduct> {
                                isBottomNavBarShow = false

                                NewProduct (
                                    onExit = {
                                        navController.navigate(MyProducts)
                                    }
                                )
                            }
                        }


                        navigation<Orders>(
                            startDestination = OrderList
                        ) {
                            composable<OrderList> {
                                isBottomNavBarShow = true

                                Orders(
                                    navToOrder = { orderId ->
                                        navController.navigate(Order(orderId))
                                    }
                                )
                            }
                            composable<Order> {
                                isBottomNavBarShow = true

                                OrderInfo(
                                    orderId = it.toRoute<Order>().orderId,
                                    navToBack = {
                                        navController.popBackStack()
                                    },
                                    navToChat = { chatId ->
                                        navController.navigate(ChatWithUser(chatId))
                                    }
                                )
                            }
                        }
                        composable<Seller> {
                            isBottomNavBarShow = true

                            val sellerId = it.toRoute<Seller>().id

                            if (authUserIdLong == sellerId)
                                SellerProfileForSelf(
                                    navToEdit = {
                                        navController.navigate(ProfileEdit)
                                    },
                                    changeAlert = { data ->
                                        alertData = data
                                    },
                                    showPortfolio = { data ->
                                        portfolioData = data
                                    },
                                    topUpClick = {
                                        isTopUpWallet = true
                                    }
                                )
                            else
                                SellerProfileForUser(
                                    userId = sellerId,
                                    navToBack = {
                                        navController.popBackStack()
                                    },
                                    navToChat = { chatId ->
                                        navController.navigate(ChatWithUser(chatId))
                                    },
                                    showPortfolio = { data ->
                                        portfolioData = data
                                    },
                                )
                        }

                        composable<User> {
                            isBottomNavBarShow = true

                            val userId = it.toRoute<User>().id

                            UserProfileForOther(
                                userId = userId,
                                navToBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable<Profile> {
                            isBottomNavBarShow = true

                            when (isSeller) {
                                true -> SellerProfileForSelf(
                                    navToEdit = {
                                        navController.navigate(ProfileEdit)
                                    },
                                    changeAlert = { data ->
                                        alertData = data
                                    },
                                    showPortfolio = { data ->
                                        portfolioData = data
                                    },
                                    topUpClick = {
                                        isTopUpWallet = true
                                    }
                                )

                                false -> UserProfileForSelf(
                                    navToEditProfile = {
                                        // navController.navigate(ProfileEdit)
                                    },
                                    topUpClick = {
                                        isTopUpWallet = true
                                    }
                                )

                                null -> {

                                }
                            }
                        }

                        composable<ProfileEdit> {
                            isBottomNavBarShow = true

                            if (true)
                                SellerProfileForSelfEdit(
                                    navToProfile = {
                                        navController.navigate(Profile)
                                    },
                                    navToCreateOrLogin = {
                                        navController.navigate(Authorise)
                                    },
                                    changeAlert = { data ->
                                        alertData = data
                                    },
                                    changePortfolio = { data ->
                                        portfolioData = data
                                    },
                                )
                            //                else
                            //                    UserProfileForSelfEdit()
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isBottomNavBarShow
            ) {
                BottomNavBar(
                    navController = navController
                )
            }
        }


        AnimatedVisibility(
            visible = alertData != null
                    || portfolioData != null
                    || (sellerIdForOrder != -1L && productIdForOrder != -1L)
                    || isSuccessNewOrder,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black20Transparent)
                    .clickable {
                        alertData = null
                        portfolioData = null
                        sellerIdForOrder = -1L
                        productIdForOrder = -1L
                        isSuccessNewOrder = false
                        isTopUpWallet = false
                    }
                    .padding(horizontal = 21.dp),
                contentAlignment = Alignment.Center
            ) {

                alertData?.let { data ->
                    Alert(
                        modifier = Modifier.clickable(enabled = false) {},
                        data = data
                    )
                }

                portfolioData?.let { data ->
                    ShowPortfolio(
                        modifier = Modifier.clickable(enabled = false) {},
                        data = data,
                        onExit = {
                            portfolioData = null
                        }
                    )
                }

                if (sellerIdForOrder != -1L && productIdForOrder != -1L) {
                    NewOrder(
                        modifier = Modifier.clickable(enabled = false) {},
                        sellerId = sellerIdForOrder,
                        productId = productIdForOrder,
                        onExit = {
                            sellerIdForOrder = -1L
                            productIdForOrder = -1L
                        },
                        onSuccess = {
                            sellerIdForOrder = -1L
                            productIdForOrder = -1L
                            isSuccessNewOrder = true
                        }
                    )
                }

                if (isSuccessNewOrder) {
                    SuccessNewOrder(
                        modifier = Modifier.clickable(enabled = false) {},
                        onExit = {
                            isSuccessNewOrder = false
                        }
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isTopUpWallet,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black20Transparent)
                    .clickable {
                        isTopUpWallet = false
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                if (isTopUpWallet) {
                    TopUpWallet(
                        modifier = Modifier.clickable(enabled = false) {},
                        onExit = {
                            isTopUpWallet = false
                        }
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = taskBarInfo != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black20Transparent)
                    .clickable {
                        taskBarInfo = null
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                taskBarInfo?.let { order ->
                    TaskBar(
                        modifier = Modifier.clickable(enabled = false) {},
                        onExit = {
                            taskBarInfo = null
                        },
                        orderData = order
                    )
                }
            }
        }
    }
}
