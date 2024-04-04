package com.ua.historicalsitesapp.nav

import android.content.Context
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ua.historicalsitesapp.ui.screens.HomeScreen
import com.ua.historicalsitesapp.ui.screens.UserProfilePage
import com.ua.historicalsitesapp.ui.screens.FeedPage
import androidx.activity.viewModels
import com.ua.historicalsitesapp.ui.screens.CheckScreen
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    navController: NavHostController,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBarItem(
        label = { Text(text = screen.label) },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.route + " icon",
            )
        },
        selected = screen.route == backStackEntry.value?.destination?.route,
        onClick = { navController.navigate(screen.route) },
    )
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val screens =
        listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Map,
            BottomBarScreen.Profile,
        )
    NavigationBar {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                navController = navController,
            )
        }
    }
}

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Map.route,
    ) {
        composable(route = BottomBarScreen.Map.route) { HomeScreen(modifier) }
        composable(route = BottomBarScreen.Profile.route) { UserProfilePage(modifier) }
        composable(route = BottomBarScreen.Home.route) { CheckScreen(modifier) }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {

    data object Home :
        BottomBarScreen(
            route = "Home",
            label = "Home",
            icon = Icons.Rounded.Home, // Replace 'YourFeedIcon' with the actual icon
        )
    data object Map :
        BottomBarScreen(
            route = "Map",
            label = "Map",
            icon = Icons.Filled.Place,
        )

    data object Profile :
        BottomBarScreen(
            route = "Profile",
            label = "Profile",
            icon = Icons.Rounded.AccountBox,
        )
}