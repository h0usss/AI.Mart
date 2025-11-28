package com.h0uss.aimart.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.h0uss.aimart.Graph.authUserIdLong
import com.h0uss.aimart.Graph.userRepository
import com.h0uss.aimart.ui.state.authorize.SignIn
import com.h0uss.aimart.ui.state.authorize.SignUp
import com.h0uss.aimart.ui.state.main.Home
import com.h0uss.aimart.ui.state.main.MyProducts
import com.h0uss.aimart.ui.state.main.Orders
import com.h0uss.aimart.ui.state.profile.SellerProfileForSelf
import com.h0uss.aimart.ui.state.profile.SellerProfileForSelfEdit
import com.h0uss.aimart.ui.state.profile.SellerProfileForUser
import com.h0uss.aimart.ui.state.profile.UserProfileForOther
import com.h0uss.aimart.ui.state.profile.UserProfileForSelf
import com.h0uss.aimart.ui.state.search.Search
import com.h0uss.aimart.ui.state.search.SearchResult
import com.h0uss.aimart.ui.theme.White
import com.h0uss.aimart.ui.viewModel.search.SearchViewModel

@SuppressLint("WrongStartDestinationType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    var isBottomNavBarShow by remember{ mutableStateOf(false) }
    var isSplashSender by remember{ mutableStateOf(true) }
    var isSeller by rememberSaveable { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(authUserIdLong) {
        isSeller = if (authUserIdLong != 0L)
            userRepository.getUserIsSeller(authUserIdLong)
        else
            null
    }

    Column{
        Box(
            modifier = modifier
                .background(White)
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
//                        navToLogin = {
//                            navController.navigate(Login)
//                        },
//                        navToRegister = {
//                            navController.navigate(Register)
//                        },
                    )
                }

                navigation<Authorise>(
                    startDestination = CreateOrLogin
                ) {
                    composable<CreateOrLogin>{
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
                            navToSeller = {sellerId ->
                                navController.navigate(Seller(sellerId))
                            },
                            navToProduct = {
                                //                        productId ->
                                //                        navController.navigate(Product(productId))
                            },
                            navToSearch = {
                                navController.navigate(SearchTextField)
                            }
                        )
                    }
                    navigation<Search>(
                        startDestination = SearchTextField
                    ){
                        composable<SearchTextField> {
                            isBottomNavBarShow = true
                            val backStackEntry = remember(it) { navController.getBackStackEntry(Search) }
                            val searchViewModel: SearchViewModel = viewModel(backStackEntry)

                            Search(
                                viewModel = searchViewModel,
                                navToSeller = {sellerId ->
                                    navController.navigate(Seller(sellerId))
                                },
                                navToProduct = {
//                                navController.navigate(Home)
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
                            val backStackEntry = remember(it) { navController.getBackStackEntry(Search) }
                            val searchViewModel: SearchViewModel = viewModel(backStackEntry)

                            SearchResult(
                                viewModel = searchViewModel,
                                navToSeller = { sellerId ->
                                    navController.navigate(Seller(sellerId))
                                },
                                navToProduct = {
//                                navController.navigate(Home)
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

                    composable<MyProducts> {
                        isBottomNavBarShow = true

                        MyProducts(
                            navToProduct = {
                                //                        navController.navigate()
                            },
                            navToNewProduct = {
                                //                        navController.navigate()
                            }
                        )
                    }

                    composable<Orders> {
                        isBottomNavBarShow = true

                        Orders(
                            navToProduct = {
                                //                        navController.navigate()
                            }
                        )
                    }

                    composable<Seller> {
                        isBottomNavBarShow = true

                        val sellerId = it.toRoute<Seller>().id

                        if (authUserIdLong == sellerId)
                            SellerProfileForSelf(
                                navToPortfolioItem = {
                                    //                            navController.navigate()
                                },
                                navToEdit = {
                                    navController.navigate(ProfileEdit)
                                }
                            )
                        else
                            SellerProfileForUser(
                                userId = sellerId,
                                navToBack = {
                                    navController.popBackStack()
                                },
                                navToChat = {
                                    //                            navController.navigate()
                                },
                                navToPortfolioItem = {
                                    //                            navController.navigate()
                                }
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
                                navToPortfolioItem = {
                                    // navController.navigate()
                                },
                                navToEdit = {
                                    navController.navigate(ProfileEdit)
                                }
                            )
                            false -> UserProfileForSelf(
                                navToEditProfile = {
                                    // navController.navigate(ProfileEdit)
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
                                navToPortfolioItem = {
                                    //                            navController.navigate()
                                }
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
}
