package com.example.innerloop.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import coil.compose.AsyncImagePainter
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

    var currentUserId = ""
    if(FirebaseAuth.getInstance().currentUser != null){
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    val followerList by userPostsViewModel.followerList.observeAsState(null)
    val followingList by userPostsViewModel.followingList.observeAsState(null)


    userPostsViewModel.fetchPosts(uid)
    userPostsViewModel.fetchUser(uid)

    userPostsViewModel.getFollowers(uid)
    userPostsViewModel.getFollowing(uid)

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
                val (name,userName,bio,pfp,followBtn,followers,following,unfollowBtn) = createRefs()

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

                Text(text = user!!.bio, modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bio) {
                        top.linkTo(userName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                    .padding(end = 90.dp))

                Box(modifier = Modifier
                    .constrainAs(pfp) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 10.dp)
                    }.size(100.dp).clip(CircleShape)){
                    val painter = rememberAsyncImagePainter(model = user!!.downloadedUrl)
                    if(painter.state is AsyncImagePainter.State.Loading){
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),
                            strokeWidth = 3.dp)
                    }

                    Image(painter = painter, contentDescription = "pfp",
                        Modifier.size(100.dp),
                        contentScale = ContentScale.Crop)
                }

                Text(text = "${followerList?.size} Followers", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp) ,modifier = Modifier.constrainAs(followers){
                    top.linkTo(bio.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                })

                Text(text = "${followingList?.size} Following", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp), modifier = Modifier.constrainAs(following){
                    top.linkTo(bio.bottom, margin = 12.dp)
                    start.linkTo(followers.end, margin = 10.dp)
                })

                ElevatedButton(onClick = {
                    if(FirebaseAuth.getInstance().currentUser!=null){
                        userPostsViewModel.followUsers(uid,currentUserId)
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier.constrainAs(followBtn){
                        top.linkTo(followers.bottom, margin = 15.dp)
                        start.linkTo(parent.start)
                    }
                ) {
                    Text(text = if(followerList!=null && followerList!!.isNotEmpty() && followerList?.contains(currentUserId) == true){
                        "Following"
                    } else {
                        "Follow"
                    }, fontSize = 16.sp)
                }

                if(followerList?.contains(currentUserId) == true){
                    ElevatedButton(onClick = {
                        if(FirebaseAuth.getInstance().currentUser!=null){
                            userPostsViewModel.unfollowUser(uid,currentUserId)
                        }
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        border = BorderStroke(2.dp, Color.Black),
                        modifier = Modifier.constrainAs(unfollowBtn){
                        top.linkTo(followers.bottom, margin = 15.dp)
                        start.linkTo(followBtn.end, margin = 13.dp)
                    }) {
                        Text(text = "Unfollow", fontSize = 16.sp)
                    }
                }

            }
        }

        if(posts!=null && user!=null) {
            items(posts!!.asReversed()) { post ->
                PostItem(
                    postModel = post,
                    userModel = user!!,
//                    navController = navController,
                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                )
            }
        }
    }
}