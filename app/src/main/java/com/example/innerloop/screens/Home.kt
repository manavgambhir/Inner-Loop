package com.example.innerloop.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.innerloop.itemView.PostItem
import com.example.innerloop.viewModel.homeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController) {
    val homeViewModel:homeViewModel = viewModel()
    val postNUsers by homeViewModel.postsNUser.observeAsState(null)

    val context = LocalContext.current

    LazyColumn {
        items(postNUsers?: emptyList()){pairs->
            PostItem(postModel = pairs.first, userModel=pairs.second, navController, FirebaseAuth.getInstance().currentUser!!.uid )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeView() {
//    Home()
}