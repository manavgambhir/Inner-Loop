package com.example.innerloop.navigation

//Sealed classes are those classes that allow classes inside of Routes class to inherit from Routes
sealed class Routes(val routes:String) {
    object Home:Routes("home")
    object Notifications:Routes("notification")
    object Profile:Routes("profile")
    object Search:Routes("search")
    object Splash:Routes("splash")
    object AddPost:Routes("add_post")
    object BottomNav:Routes("bottomNav")
    object Login:Routes("login")
    object Register:Routes("register")
    object OtherUserProfile:Routes("other_user_profile/{data}")
}