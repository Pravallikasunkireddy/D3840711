package uk.ac.tees.mad.d3840711

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.d3840711.ui.theme.SheSafeTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SheSafeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignupScreen()
                }
            }
        }
    }

    @Composable
    fun SignupScreen() {
        val gradient = Brush.radialGradient(
            colors = listOf(Color(0xFFB388FF), Color(0xFFB388FF))
        )

        val scrollState = rememberScrollState()

        val emailState = remember { mutableStateOf(TextFieldValue()) }
        val nameState = remember { mutableStateOf(TextFieldValue()) }
        val passwordState = remember { mutableStateOf(TextFieldValue()) }
        val emergencyMessageState = remember { mutableStateOf(TextFieldValue()) }
        val emergencyNumberState = remember { mutableStateOf(TextFieldValue()) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(32.dp)
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.safe), // Ensure you have a suitable icon
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(20.dp),
                    contentScale = ContentScale.Fit
                )
                Text("Sign Up", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = { Text("Name", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = emergencyMessageState.value,
                    onValueChange = { emergencyMessageState.value = it },
                    label = { Text("Emergency Message", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = emergencyNumberState.value,
                    onValueChange = { emergencyNumberState.value = it },
                    label = { Text("Emergency Number", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if(emailState.value.text.isNotEmpty() && passwordState.value.text.isNotEmpty() && emergencyMessageState.value.text.isNotEmpty()  && emergencyNumberState.value.text.isNotEmpty() && emailState.value.text.isNotEmpty()){
                            FirebaseAuth
                                .getInstance()
                                .createUserWithEmailAndPassword(emailState.value.text, passwordState.value.text)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        CreateUserDetailsInFirebase(nameState.value.text,emailState.value.text, emergencyNumberState.value.text,emergencyMessageState.value.text)
                                        val intent = Intent(this@CreateAccountScreen, SOSActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Login failed, get the error message
                                        val errorMessage = task.exception?.message
                                        if (errorMessage != null) {
                                            Toast.makeText(this@CreateAccountScreen, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }.addOnFailureListener { exception ->
                                    Toast.makeText(this@CreateAccountScreen, exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }else{
                            Toast.makeText(this@CreateAccountScreen, "No feild Can be Empty", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = {
                    val intent = Intent(this@CreateAccountScreen, LoginScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }) {
                    Text("Already have an account? Log In", color = Color.White)
                }
            }
        }
    }

    private fun CreateUserDetailsInFirebase(name2:String,email:String,emergencyPhone:String,emergencyMessage:String){
        var user = FirebaseAuth.getInstance().getCurrentUser()
        if (user != null) {
            val uid = user.uid

            val userDoc = SheModel(
                name2,
                email,
                emergencyPhone,
                emergencyMessage,
                "https://firebasestorage.googleapis.com/v0/b/shesafe-1789e.appspot.com/o/profile%20(2).jpg?alt=media&token=d4324620-120e-4430-a416-23022a2e0049",
                0.0,
                0.0
            )
            FirebaseFirestore.getInstance().collection("superwomen").document(uid).set(userDoc).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {

                }else{
                    val errorMessage = task.exception?.message
                    if (errorMessage != null) {
                        Toast.makeText(this@CreateAccountScreen, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}

