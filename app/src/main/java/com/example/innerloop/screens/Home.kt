package com.example.innerloop.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.innerloop.R
import com.example.innerloop.itemView.PostItem
import com.example.innerloop.viewModel.homeViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController) {
    val lazyListState = rememberLazyListState()
    Scaffold(modifier=Modifier.padding(0.dp),content = {
        Box(modifier = Modifier.fillMaxSize()){
            TopBar(lazyListState = lazyListState)
            PostsContent(lazyListState = lazyListState)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(lazyListState: LazyListState){
    TopAppBar(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 250))
            .height(height = if (lazyListState.isScrolled) 0.dp else 56.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        title = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.innerloop_ic),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(56.dp)
                        .padding(top = 4.dp)
                )
            }
        }
    )
}

@Composable
fun PostsContent(lazyListState: LazyListState){
    val homeViewModel:homeViewModel = viewModel()
    val postNUsers by homeViewModel.postsNUser.observeAsState(null)
    val padding by animateDpAsState(
        targetValue = if(lazyListState.isScrolled) 0.dp else 56.dp,
        animationSpec = tween(250), label = ""
    )
    LazyColumn(
        modifier = Modifier.padding(top=padding),
        state = lazyListState
    ) {
        items(postNUsers ?: emptyList()) { pairs ->
            PostItem(
                postModel = pairs.first,
                userModel = pairs.second,
                FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeView() {
//    Home()
//    TopBar()
}

val LazyListState.isScrolled:Boolean
    get() = firstVisibleItemIndex>0 || firstVisibleItemScrollOffset > 0