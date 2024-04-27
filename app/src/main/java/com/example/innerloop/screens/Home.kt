package com.example.innerloop.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.innerloop.R
import com.example.innerloop.itemView.PostItem
import com.example.innerloop.viewModel.homeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {
    val homeViewModel:homeViewModel = viewModel()
    val postNUsers by homeViewModel.postsNUser.observeAsState(null)

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Box(modifier = Modifier.fillMaxSize().padding(end = 16.dp), contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(id = R.drawable.innerloop_ic),
                        contentDescription = "Logo",
                        modifier = Modifier.size(45.dp)
                    )
                }
            }, modifier = Modifier.shadow(elevation = 6.dp))
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(postNUsers ?: emptyList()) { pairs ->
                PostItem(
                    postModel = pairs.first,
                    userModel = pairs.second,
                    navController,
                    FirebaseAuth.getInstance().currentUser!!.uid
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeView() {
//    Home()
}