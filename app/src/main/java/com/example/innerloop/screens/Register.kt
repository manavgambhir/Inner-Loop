package com.example.innerloop.screens


import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.innerloop.R
import com.example.innerloop.navigation.Routes
import com.example.innerloop.viewModel.authViewModel

@Composable
fun Register(navController: NavHostController) {
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    //Initializing the viewModel
    val authViewModel:authViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current

    val permissionToRequest = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
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

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
        imageUri=uri
    }

    LaunchedEffect(firebaseUser) {
        if(firebaseUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Column(Modifier.fillMaxSize()){
        Row {
            Text(text = "Register", fontSize = 44.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.offset(22.dp,25.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
                .offset(y = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Image(
                    painter = if(imageUri==null) painterResource(id = R.drawable.profile)
                    else rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "ProfilePic",
                    modifier = Modifier
                        .size(120.dp)
                        .offset(7.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            val isGranted = ContextCompat.checkSelfPermission(
                                context, permissionToRequest
                            ) == PackageManager.PERMISSION_GRANTED
                            if (isGranted) {
                                launcher.launch("image/*")
                            } else {
                                permissionLauncher.launch(permissionToRequest)
                            }
                        },
                    contentScale = ContentScale.Crop
                )
                Image(imageVector = Icons.Rounded.AddCircle , contentDescription = null, colorFilter = ColorFilter.tint(Color(0xFF0270e7)),
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .offset((-15).dp, -5.dp)
                        .size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = bio,
                onValueChange = { bio = it },
                label = { Text(text = "Bio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = pass,
                onValueChange = { pass = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            ElevatedButton(onClick = {
                if(name.isEmpty()||username.isEmpty()||bio.isEmpty()||email.isEmpty()||pass.isEmpty()){
                    Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                }
                else{
                    authViewModel.registerNewUser(email,pass,name,username,imageUri!!,bio,context)
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register", fontSize = 16.sp, modifier = Modifier)
            }

            TextButton(onClick = {
                navController.navigate(Routes.Login.routes){
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }) {
                Text(text = "Already registered? Login to account", color = Color.Black, fontSize = 17.sp)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RegisterView(){
//    Register()
}