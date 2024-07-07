package com.example.innerloop.screens

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.innerloop.BuildConfig
import com.example.innerloop.R
import com.example.innerloop.navigation.Routes
import com.example.innerloop.utils.SharedPref
import com.example.innerloop.viewModel.newPostViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPost(navController:NavHostController) {
    val context = LocalContext.current

    var newPostText by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val bottomSheetState = rememberModalBottomSheetState()

    val coroutineScope = rememberCoroutineScope()

    val permissionToRequest = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    }
    else{
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted:Boolean->
        if(isGranted){

        }
        else{

        }
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri=uri
    }

    val postViewModel:newPostViewModel = viewModel()
    val isPosted by postViewModel.isPosted.observeAsState(false)

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    LaunchedEffect(isPosted) {
        if(isPosted){
            newPostText = ""
            imageUri = null
            Toast.makeText(context, "Your post added successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.BottomNav.routes){
                //Removing AddPost from backstack
                popUpTo(Routes.AddPost.routes){
                    inclusive = true
                }
            }
        }
    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
          val (crossImg,text,pfp,username,editText, attachGallery,
              attachCamera,replyText, postBtn,imageBox,aiBtn,generateText) = createRefs()

        Image(painter = painterResource(id = R.drawable.close_ic), contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossImg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(32.dp)
                .clickable {
                    navController.navigate(Routes.BottomNav.routes) {
                        popUpTo(Routes.AddPost.routes) {
                            inclusive = true
                        }
                    }
                }
        )

        Text(text = "New Post", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.constrainAs(text){
            top.linkTo(crossImg.top)
            start.linkTo(crossImg.end, margin = 12.dp)
            bottom.linkTo(crossImg.bottom)
        })

        Image(painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)), contentDescription = "pfp",
            modifier = Modifier
                .constrainAs(pfp) {
                    top.linkTo(crossImg.bottom, margin = 30.dp)
                }
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(text = SharedPref.getUserName(context)!!, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.constrainAs(username){
            start.linkTo(pfp.end, margin = 20.dp)
            top.linkTo(text.bottom, margin = 30.dp)
        })

        CustomTextField(hint = "Whats New?", value = newPostText, onValueChange = { newPostText = it},
            modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(username.bottom)
                    start.linkTo(pfp.end)
                    end.linkTo(parent.end)
                }
                .padding(start = 45.dp, end = 22.dp, top = 8.dp)
                .fillMaxWidth()
        )

        if(imageUri==null){
            Image(painter = painterResource(id = R.drawable.gallery_ic), contentDescription = "attachGallery",
                modifier = Modifier
                    .constrainAs(attachGallery) {
                        start.linkTo(pfp.end, margin = 20.dp)
                        top.linkTo(editText.bottom, margin = 20.dp)
                    }
                    .size(27.dp)
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    }
            )

            Image(painter = painterResource(id = R.drawable.camera_ic), contentDescription = "attachCam",
                modifier = Modifier
                    .constrainAs(attachCamera) {
                        top.linkTo(attachGallery.top)
                        bottom.linkTo(attachGallery.bottom)
                        start.linkTo(attachGallery.end, margin = 7.dp)
                    }
                    .size(27.dp)
                    .clickable {
                        //TODO: Click Functionality
                    }
            )
        }
        else{
            Box(modifier = Modifier
                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(12.dp))
                .padding(10.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                }
                .height(240.dp)
                .fillMaxWidth()
            ){
                Image(painter = rememberAsyncImagePainter(model = imageUri), contentDescription = "attachCam",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Fit
                )
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "remove",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        }
                )
            }
        }

//        Text(text = "Anyone can reply", fontSize = 15.sp ,fontWeight = FontWeight.Medium, modifier = Modifier
//            .constrainAs(replyText) {
//                start.linkTo(parent.start)
//                bottom.linkTo(parent.bottom)
//            }
//            .padding(start = 7.dp, bottom = 11.dp))

        OutlinedButton(onClick = {
            showBottomSheet = true
        },
            shape= CircleShape ,
            colors = ButtonDefaults.buttonColors(Color(0xFFE5F7FF)),
            border = BorderStroke(3.dp, Color(0xFF00668B)),
            contentPadding = PaddingValues(start = 14.dp,end = 14.dp, top=11.dp, bottom = 9.dp),
            modifier = Modifier
                .size(60.dp)
                .constrainAs(aiBtn) {
                    start.linkTo(postBtn.start)
                    end.linkTo(postBtn.end, margin = 5.dp)
                    bottom.linkTo(generateText.top)
                }
        ) {
            Image(painter = painterResource(id = R.drawable.ai_ic), contentDescription = "aiIcon")
        }

        Text(text = "Generate\nwith AI", fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = TextStyle(lineHeight = 16.sp),
            modifier = Modifier
                .padding(top = 4.dp)
                .constrainAs(generateText) {
                    bottom.linkTo(postBtn.top)
                    start.linkTo(aiBtn.start)
                    end.linkTo(aiBtn.end)
                }
        )

        ElevatedButton(onClick = {
            if(imageUri==null){
                postViewModel.saveData(newPostText,FirebaseAuth.getInstance().currentUser!!.uid,"")
            }
            else{
                postViewModel.saveImageNData(newPostText,FirebaseAuth.getInstance().currentUser!!.uid, imageUri!!)
            }
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .constrainAs(postBtn) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(top = 28.dp)
        ) {
            Text(text = "Post", fontSize = 16.sp, modifier = Modifier)
        }

        if(showBottomSheet){
            BottomSheet(imageUri,bottomSheetState,generativeModel,coroutineScope,context,onDismiss = { showBottomSheet = false },onContentGenerated = { generatedText -> newPostText = generatedText })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(imageUri: Uri?, bottomSheetState:SheetState,generativeModel:GenerativeModel,coroutineScope: CoroutineScope,context: Context, onDismiss: () -> Unit,onContentGenerated: (String) -> Unit) {
    var generatedText by remember {
        mutableStateOf("")
    }
    var isLoading by remember { mutableStateOf(false) }
    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Image(painter = painterResource(id = R.drawable.ai_ic), contentDescription = "Ai_Ic", modifier = Modifier
                    .size(50.dp)
                    .padding(start = 9.dp, bottom = 10.dp))

                Text(text = "Generate content with AI", fontWeight = FontWeight.Medium, fontSize = 20.sp, modifier = Modifier.padding(bottom = 10.dp))

                CustomGenerateTextField(generatedText,onValueChange = { generatedText = it})

                if(imageUri != null){
                    Text(text = "OR")
                    TextButton(onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            val bitmap = getBitmapFromUri(context,imageUri)
                            val response = generativeModel.generateContent(
                                content {
                                    image(bitmap!!)
                                    text("Generate content for a social media post from the image")
                                }
                            )
                            onContentGenerated(response.text!!)
                            isLoading = false
                            bottomSheetState.hide()
                            onDismiss()
                        }
                    }) {
                        Text(text = "Generate post content for post image", color = Color.DarkGray, fontSize = 16.sp)
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 30.dp, end = 18.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            val response = generativeModel.generateContent("Write a social media post about $generatedText")
                            onContentGenerated(response.text!!)
                            isLoading = false
                            bottomSheetState.hide()
                            onDismiss()
                        }
                    }) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(text = "Generate Content")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CustomTextField(hint:String, value:String, onValueChange: (String)-> Unit, modifier: Modifier){
    Box(modifier = modifier){
        if(value.isEmpty()){
            Text(text = hint, color = Color.Gray)
        }
//        style = TextStyle(
//            lineHeight = 24.sp,
//            fontSize = 16.sp
//        )
        BasicTextField(value = value, onValueChange = onValueChange, textStyle = TextStyle.Default.copy(color = Color.Black, lineHeight = 21.sp, fontSize = 16.sp),
            modifier= Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomGenerateTextField(text:String,onValueChange: (String)-> Unit){
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Tell me what you want to post about?",
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = Color.DarkGray
        )

        TextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            trailingIcon = { Icon(Icons.Filled.Create, "", tint = Color.Black) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true)
@Composable
fun NewPostView(){
//    AddPost()
//    BottomSheet()
}