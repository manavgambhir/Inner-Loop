package com.example.innerloop.itemView

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.innerloop.models.PostModel
import com.example.innerloop.models.UserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PostItem(postModel:PostModel,userModel: UserModel,navController: NavHostController,userId: String) {

    var isLiked by remember {
        mutableStateOf(false)
    }

    Column {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ){
            val (pfp,userName,date,time,postContent,image,likeImg) = createRefs()

            Box(modifier = Modifier.constrainAs(pfp){
                top.linkTo(parent.top, margin = 4.dp)
                start.linkTo(parent.start)
            }.size(45.dp).clip(CircleShape)){
                val painter = rememberAsyncImagePainter(model = userModel.downloadedUrl)
                if(painter.state is AsyncImagePainter.State.Loading){
                    CircularProgressIndicator(modifier = Modifier
                        .size(25.dp).align(Alignment.Center), strokeWidth = 2.dp)
                }

                Image(
                    painter = painter,
                    contentDescription = "pfp",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(text = userModel.userName, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.constrainAs(userName){
                start.linkTo(pfp.end, margin = 20.dp)
                top.linkTo(parent.top)
            })

            Text(text = getDate(postModel.timeStamp.toLong()), modifier = Modifier.constrainAs(date){
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            })

            Text(text = postModel.postContent, modifier = Modifier
                .constrainAs(postContent) {
                    top.linkTo(userName.bottom, margin = 5.dp)
                    start.linkTo(pfp.end)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(start = 42.dp, end = 20.dp))

            if(postModel.imageUrl!=""){
                Card(modifier = Modifier
                    .padding(7.dp)
                    .constrainAs(image) {
                        top.linkTo(postContent.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                    }
                    .height(220.dp)
                    .fillMaxWidth()
                ){
                    Box(modifier = Modifier.fillMaxSize()){
                        val painter = rememberAsyncImagePainter(model = postModel.imageUrl)
                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),
                                strokeWidth = 3.dp
                        )}

                        Image(painter = rememberAsyncImagePainter(model = postModel.imageUrl), contentDescription = "attachCam",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Icon(imageVector = if(!isLiked) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite, contentDescription = "like_Ic",
                modifier = Modifier
                    .constrainAs(likeImg) {
                        if (postModel.imageUrl == "") {
                            top.linkTo(postContent.bottom, margin = 15.dp)
                        } else {
                            top.linkTo(image.bottom, margin = 15.dp)
                        }
                        start.linkTo(parent.start, margin = 3.dp)
                    }
                    .size(27.dp)
                    .clickable {
                        isLiked = !isLiked
                    }, tint = if(isLiked) Color(0xFFE91E63) else Color.Black
                )

                Text(text = getTime(postModel.timeStamp.toLong()), modifier = Modifier.constrainAs(time){
                    if(postModel.imageUrl==""){
                        top.linkTo(postContent.bottom, margin = 18.dp) }
                    else{
                        top.linkTo(image.bottom, margin = 18.dp) }
                    end.linkTo(parent.end)
                })
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

fun getTime(timeStamp: Long): String {
    val simpleDateFormat = SimpleDateFormat("h:mma", Locale.getDefault())
    val time = Date(timeStamp)
    return simpleDateFormat.format(time)
}

fun getDate(timeStamp: Long): String {
    val simpleDateFormat = SimpleDateFormat("d MMMM yy", Locale.getDefault())
    val date = Date(timeStamp)
    return simpleDateFormat.format(date)
}

@Preview(showBackground = true)
@Composable
fun PostItemView(){
//    PostItem()
}