package com.h0uss.aimart.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
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
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    var isBottomNavBarShow by remember{ mutableStateOf(false) }
    var isSeller by rememberSaveable { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        isSeller = userRepository.getUserIsSeller(authUserIdLong)
    }
    Column{
        Box(
            modifier = Modifier
                .background(White)
                .systemBarsPadding()
                .fillMaxWidth()
                .weight(1f)
        ) {
            NavHost(
                navController = navController,
                startDestination = CheckLogin
            ) {
                composable<CheckLogin> {
                    CheckLogin(
                        navToHome = {
                            navController.navigate(Main)
                        },
                        navToLogin = {
                            navController.navigate(CreateOrLogin)
                        },
                    )
                }

                navigation<CreateOrLogin>(
                    startDestination = Login
                ) {
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
                                navController.navigate(SearchTextField(""))
                            }
                        )
                    }
                    navigation<Search>(
                        startDestination = SearchTextField() // ★★★ ИСПРАВЛЕНО
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
                                    navController.navigate(SearchResult(searchViewModel.state.value.searchValue))
                                },
                                navToSearch = {
                                    navController.navigate(SearchTextField(searchViewModel.state.value.searchValue))
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
                                    navController.navigate(SearchResult(searchViewModel.state.value.searchValue))
                                },
                                navToSearch = {
                                    navController.navigate(SearchTextField(searchViewModel.state.value.searchValue))
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

                        if (isSeller == true)
                            SellerProfileForSelf(
                                navToPortfolioItem = {
                                    //                            navController.navigate()
                                },
                                navToEdit = {
                                    navController.navigate(ProfileEdit)
                                }
                            )
                        if (isSeller == false)
                            UserProfileForSelf(
                                navToEditProfile = {
                                    //                            navController.navigate(ProfileEdit)
                                }
                            )
                    }

                    composable<ProfileEdit> {
                        isBottomNavBarShow = true

                        if (true)
                            SellerProfileForSelfEdit(
                                navToProfile = {
                                    navController.navigate(Profile)
                                },
                                navToLogin = {
                                    navController.navigate(CreateOrLogin)
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
