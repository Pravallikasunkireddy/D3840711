package uk.ac.tees.mad.d3840711

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import uk.ac.tees.mad.d3840711.ui.theme.SheSafeTheme
import java.io.ByteArrayOutputStream

class ManageProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SheSafeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EditProfileScreen()
                }
            }
        }
    }
    @Composable
    fun EditProfileScreen() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        var loading by remember { mutableStateOf(true) }
        var sheWomen by remember { mutableStateOf(SheModel()) } // Empty default model

        // Remember the form inputs
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var emergencyPhone by remember { mutableStateOf("") }
        var emergencyMessage by remember { mutableStateOf("") }

        LaunchedEffect(uid) {
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("superwomen").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val sheData = document.toObject(SheModel::class.java)
                            if (sheData != null) {
                                sheWomen = sheData
                                name = sheData.name
                                email = sheData.email
                                emergencyPhone = sheData.emergencyPhone
                                emergencyMessage = sheData.emergencyMessage
                                loading = false
                            }
                        } else {
                            loading = false
                        }
                    }
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFB388FF), Color(0xFFB388FF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    color = Color.Blue,
                    modifier = Modifier.size(50.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFB388FF), Color(0xFFB388FF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberImagePainter(sheWomen.image_url),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Red, CircleShape)
                            .clickable{
                                triggerCamera()
                            },
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = emergencyPhone,
                        onValueChange = { emergencyPhone = it },
                        label = { Text("Emergency Phone", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = emergencyMessage,
                        onValueChange = { emergencyMessage = it },
                        label = { Text("Emergency Message", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            val updatedUser = SheModel(
                                name,
                                email,
                                emergencyPhone,
                                emergencyMessage,
                                sheWomen.image_url,
                                sheWomen.lat,
                                sheWomen.long
                            )
                            if (uid != null) {
                                FirebaseFirestore.getInstance().collection("superwomen").document(uid)
                                    .set(updatedUser)
                                    .addOnSuccessListener {
                                        val intent = Intent(this@ManageProfile, SOSActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle failure
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Profile")
                    }
                }
            }
        }
    }



    private val REQUEST_IMAGE_CAPTURE = 1

    private fun triggerCamera() {
        if (checkPermissionsCamera()) {
            if (isCameraPermissionEnabled()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                }}
        }
        else{
            Log.e("man log" , "requesting for camera permission")
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                triggerCamera()
            } else {
                // Permission denied, handle accordingly (show a message, etc.)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkPermissionsCamera(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isCameraPermissionEnabled(): Boolean {
        val permission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 123





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val storage = FirebaseStorage.getInstance()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //binding.image.setImageBitmap(imageBitmap)
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child("profile_photo").child(fileName)
            // Convert the Bitmap to a byte array
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // Upload the image to Firebase Storage
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid()
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("superwomen").document(uid).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val sheData = document.toObject(SheModel::class.java)
                                        if (sheData != null) {
                                            // Update the user data
                                            val user = SheModel(
                                                sheData.name,
                                                sheData.email,
                                                sheData.emergencyPhone,
                                                sheData.emergencyMessage,
                                                downloadUrl,
                                                sheData.lat,
                                                sheData.long
                                            )
                                            FirebaseFirestore.getInstance().collection("superwomen").document(uid).set(user)
                                        }
                                    } else {
                                        // The document does not exist
                                    }
                                }

                        }
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                    } } else {
                    val exception = task.exception
                }
            }
        }
    }


}
