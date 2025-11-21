package com.h0uss.aimart.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.h0uss.aimart.R
import com.h0uss.aimart.data.model.BottomNavBarItem
import com.h0uss.aimart.ui.theme.White

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items: List<BottomNavBarItem> = listOf(
        BottomNavBarItem(
            image = painterResource(R.drawable.home),
            label = "Home",
            route = Home,
        ),
        BottomNavBarItem(
            image = painterResource(R.drawable.chat),
            label = "Chat",
            route = Chat,
        ),
        BottomNavBarItem(
            image = painterResource(R.drawable.products),
            label = "MyProducts",
            route = MyProducts,
        ),
        BottomNavBarItem(
            image = painterResource(R.drawable.orders),
            label = "Orders",
            route = Orders,
        ),
        BottomNavBarItem(
            image = painterResource(R.drawable.profile),
            label = "Profile",
            route = Profile,
        ),
    )

    NavigationBar (
        modifier = modifier
            .fillMaxWidth()
            .shadow(50.dp, shape = RectangleShape)
            .background(White)
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
        ,
        containerColor = White,
        contentColor = White,
        tonalElevation = 7.dp
    ){
        items.forEach { item ->
            val isSelected = currentRoute?.let { route ->
                route.startsWith(item.route.toString())
            } ?: false
            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIconColor = White,
                    selectedTextColor = White,
                    selectedIndicatorColor = White,
                    unselectedIconColor = White,
                    unselectedTextColor = White,
                    disabledIconColor = White,
                    disabledTextColor = White,
                ),
                icon = {
                    Image(
                        painter = item.image,
                        contentDescription = item.label
                    )
                },
                selected = isSelected,
                onClick = {
                    if (item.route != Chat)
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    BottomNavBar(
        navController = rememberNavController()
    )
}