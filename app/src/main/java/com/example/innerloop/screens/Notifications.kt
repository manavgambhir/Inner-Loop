package com.example.innerloop.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.innerloop.R
import com.example.innerloop.navigation.Routes

@Composable
fun Notifications(navController:NavHostController) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        val (crossImg, text, text2) = createRefs()

        Image(painter = painterResource(id = R.drawable.close_ic), contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossImg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(33.dp)
                .clickable {
                    navController.navigate(Routes.BottomNav.routes) {
                        popUpTo(Routes.Notifications.routes) {
                            inclusive = true
                        }
                    }
                }
        )

        Text(
            text = "Notifications",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(text) {
                top.linkTo(crossImg.top)
                start.linkTo(crossImg.end, margin = 12.dp)
                bottom.linkTo(crossImg.bottom)
            })

        Text(
            text = "You are all up to date for now",
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(text2) {
            top.linkTo(text.bottom, margin = 18.dp)
            start.linkTo(parent.start, margin = 7.dp)
        })
    }
}