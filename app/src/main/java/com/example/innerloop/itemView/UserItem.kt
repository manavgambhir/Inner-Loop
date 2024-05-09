package com.example.innerloop.itemView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.innerloop.R
import com.example.innerloop.models.UserModel
import com.example.innerloop.navigation.Routes

@Composable
fun UserItem(userModel: UserModel, navController: NavHostController) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .clickable {
            val routes = Routes.OtherUserProfile.routes.replace("{data}", userModel.uid)
            navController.navigate(routes)
        }
    ) {
        val (pfp, userName, name) = createRefs()

        Box(modifier = Modifier.constrainAs(pfp){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }.size(60.dp).clip(CircleShape)){

            val painter = rememberAsyncImagePainter(model = userModel.downloadedUrl)

            if(painter.state is AsyncImagePainter.State.Loading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(25.dp),
                    strokeWidth = 2.5.dp)
            }

            Image(painter =painter,
                contentDescription = "pfp",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
        }


//        Image(painter =painter,
//            contentDescription = "pfp",
//            modifier = Modifier
//                .constrainAs(pfp) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                }
//                .size(60.dp)
//                .clip(CircleShape),
//            contentScale = ContentScale.Crop
//        )

        Text(
            text = userModel.userName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(userName) {
                start.linkTo(pfp.end, margin = 20.dp)
                top.linkTo(parent.top, margin = 5.dp)
            }
        )

        Text(
            text = userModel.name,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(pfp.end, margin = 20.dp)
                top.linkTo(userName.bottom, margin = 5.dp)
            },
            color = Color.Gray
        )

    }
}


@Preview(showBackground = true)
@Composable
fun SearchItemView() {
//    UserItem()
}