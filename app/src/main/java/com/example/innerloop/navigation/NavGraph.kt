package com.example.innerloop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.innerloop.screens.AddPost
import com.example.innerloop.screens.BottomNav
import com.example.innerloop.screens.Home
import com.example.innerloop.screens.Login
import com.example.innerloop.screens.Notifications
import com.example.innerloop.screens.OtherUserProfile
import com.example.innerloop.screens.Profile
import com.example.innerloop.screens.Register
import com.example.innerloop.screens.Search
import com.example.innerloop.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes){
        composable(Routes.Splash.routes){
            Splash(navController)
        }

        composable(Routes.Home.routes){
            Home(navController)
        }

        composable(Routes.Notifications.routes){
            Notifications()
        }

        composable(Routes.Search.routes){
            Search(navController)
        }

        composable(Routes.AddPost.routes){
            AddPost(navController)
        }

        composable(Routes.Profile.routes){
            Profile(navController)
        }

        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }

        composable(Routes.Login.routes){
            Login(navController)
        }

        composable(Routes.Register.routes){
            Register(navController)
        }

        composable(Routes.OtherUserProfile.routes){
            val data = it.arguments?.getString("data")
            OtherUserProfile(navController, data!!)
        }

    }
}


