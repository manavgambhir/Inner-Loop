package com.example.innerloop.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.innerloop.itemView.PostItem
import com.example.innerloop.models.UserModel
import com.example.innerloop.navigation.Routes
import com.example.innerloop.utils.SharedPref
import com.example.innerloop.viewModel.UserPostsViewModel
import com.example.innerloop.viewModel.authViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUserProfile(navController: NavHostController, uid:String) {
    val authViewModel: authViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userPostsViewModel: UserPostsViewModel = viewModel()
    val posts by userPostsViewModel.posts.observeAsState(null)
    val user by userPostsViewModel.user.observeAsState(null)
    val context = LocalContext.current


    userPostsViewModel.fetchPosts(uid)
    userPostsViewModel.fetchUser(uid)

    LaunchedEffect(firebaseUser) {
        if(firebaseUser==null){
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn() {
        item {
            ConstraintLayout(modifier = Modifier.padding(16.dp)) {
                val (name,userName,bio,pfp,logoutBtn,followers,following) = createRefs()

                Text(text = user!!.name, style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ), modifier = Modifier.constrainAs(name){
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start)
                })

                Text(text = user!!.userName, style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                ), modifier = Modifier.constrainAs(userName){
                    top.linkTo(name.bottom, margin = 2.dp)
                    start.linkTo(parent.start)
                })

                Text(text = user!!.bio, modifier = Modifier.fillMaxWidth()
                    .constrainAs(bio) {
                        top.linkTo(userName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                    .padding(end = 90.dp))

                Image(painter = rememberAsyncImagePainter(model = user!!.downloadedUrl), contentDescription = "pfp",
                    modifier = Modifier
                        .constrainAs(pfp) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top, margin = 10.dp)
                        }
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(text = "10 Followers", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp) ,modifier = Modifier.constrainAs(followers){
                    top.linkTo(bio.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })

                Text(text = "8 Following", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp), modifier = Modifier.constrainAs(following){
                    top.linkTo(bio.bottom, margin = 12.dp)
                    start.linkTo(followers.end, margin = 10.dp)
                })

                ElevatedButton(onClick = {
                    //TODO
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier.constrainAs(logoutBtn){
                        top.linkTo(followers.bottom, margin = 15.dp)
                        start.linkTo(parent.start)
                    }
                ) {
                    Text(text = "Follow", fontSize = 16.sp)
                }

            }
        }

        if(posts!=null && user!=null) {
            items(posts ?: emptyList()) { post ->
                PostItem(
                    postModel = post,
                    userModel = user!!,
                    navController = navController,
                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                )
            }
        }
    }
}