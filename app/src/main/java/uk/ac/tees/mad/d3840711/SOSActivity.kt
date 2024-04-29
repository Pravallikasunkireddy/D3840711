package uk.ac.tees.mad.d3840711

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.d3840711.ui.theme.SheSafeTheme
import android.telephony.SmsManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberImagePainter

class SOSActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val SMS_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SheSafeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }

        if (checkSMSPermission()) {
             // Example number and message
        } else {
            requestSMSPermission()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    requestLocation()
                } else {
                    Toast.makeText(this@SOSActivity,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(applicationContext, "SMS Sent!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Failed to send SMS: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkSMSPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSMSPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 // Example number and message
            } else {
                Toast.makeText(this, "SMS permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()

        val items = listOf(
            NavigationClass.Home,
            NavigationClass.Account
        )

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    backgroundColor = Color.DarkGray, // Set background color to dark gray
                    contentColor = Color.White // Set text color to white
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    items.forEach {
                        BottomNavigationItem(
                            selected = currentRoute == it.route,
                            label = {
                                Text(
                                    text = it.label,
                                    color = if (currentRoute == it.route) Color.White else Color.LightGray
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = it.icons,
                                    contentDescription = null,
                                    tint = if (currentRoute == it.route) Color.White else Color.LightGray
                                )
                            },
                            onClick = {
                                if (currentRoute != it.route) {
                                    navController.graph?.startDestinationRoute?.let {
                                        navController.popBackStack(it, true)
                                    }
                                    navController.navigate(it.route) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) {
            NavigationController(navController = navController)
        }
    }

    @Composable
    fun NavigationController(navController: NavHostController) {
        NavHost(navController = navController, startDestination = NavigationClass.Home.route) {

            composable(NavigationClass.Home.route) {
                SOSScreen(this@SOSActivity,{})
            }


            composable(NavigationClass.Account.route) {
                UserProfileScreen(this@SOSActivity)
            }

        }
    }


    @Composable
    fun UserProfileScreen(context: Context) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var loading by remember { mutableStateOf(true)}


        val scrollState = rememberScrollState()
        val localContext = LocalContext.current

        var sheWomen by remember { mutableStateOf(SheModel(
            "super women",
            "sumper.women@example.com",
            "1234567890",
            "No message" ,
            "https://firebasestorage.googleapis.com/v0/b/shesafe-1789e.appspot.com/o/profile%20(2).jpg?alt=media&token=d4324620-120e-4430-a416-23022a2e0049",
            0.0,
            0.0
        )) }
        LaunchedEffect(uid) {
            if(uid != null) {
                FirebaseFirestore.getInstance().collection("superwomen").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val sheData = document.toObject(SheModel::class.java)
                            if (sheData != null) {
                                sheWomen = sheData
                                loading = false
                            }
                        }
                    }
            }
        }

        if(loading){
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
                    color = Color.Blue, // Change the color as needed
                    modifier = Modifier.size(50.dp) // Change the size as needed
                )
            }
        }
        else{

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
                        .verticalScroll(scrollState)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = rememberImagePainter(sheWomen.image_url),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Red, CircleShape),
                        contentScale = ContentScale.Crop

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Name:",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = sheWomen.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Email:",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = sheWomen.email,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Emergency Phone:",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = sheWomen.emergencyPhone,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Emergency Message:",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = sheWomen.emergencyMessage,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val intent = Intent(context, ManageProfile::class.java)
                            localContext.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Profile")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(context, LoginScreenActivity::class.java)
                            localContext.startActivity(intent)
                            (context as? Activity)?.finish()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }

    @Composable
    fun SOSScreen(context: Context,onSOSClicked: () -> Unit) {

        var sheWomen by remember { mutableStateOf(SheModel(
            "John Doe",
            "john.doe@example.com",
            "1234567890",
            "No message" ,
            "https://firebasestorage.googleapis.com/v0/b/shesafe-1789e.appspot.com/o/profile%20(2).jpg?alt=media&token=d4324620-120e-4430-a416-23022a2e0049",
            0.0,
            0.0
        )) }

        // Background gradient for aesthetic enhancement
        val backgroundGradient = Brush.radialGradient(
            colors = listOf(Color(0xFFB388FF), Color(0xFFB388FF)) // A gradient of soothing reds
        )

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var loading by remember { mutableStateOf(true)}

        LaunchedEffect(uid) {
            if(uid != null) {
                FirebaseFirestore.getInstance().collection("superwomen").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val sheData = document.toObject(SheModel::class.java)
                            if (sheData != null) {
                                sheWomen = sheData
                                loading = false
                            }
                        }
                    }
            }
        }

        if(loading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    color = Color.Blue, // Change the color as needed
                    modifier = Modifier.size(50.dp) // Change the size as needed
                )
            }

        }else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { sendSMS(sheWomen.emergencyPhone,sheWomen.emergencyMessage+"\n Location :  Latitiude - "+sheWomen.lat.toString()+" \n Longitude - "+sheWomen.long.toString())},
                    modifier = Modifier
                        .size(150.dp) // Adjust size as necessary
                        .padding(16.dp)
                        .background(color = Color.Red)
                ) {
                    Text(
                        "SOS",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }

    }



    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if(uid != null){
                        FirebaseFirestore.getInstance().collection("superwomen").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val userData = document.toObject(SheModel::class.java)
                                    if (userData != null) {
                                        // Update the user data
                                        val user = SheModel(
                                            userData.name,
                                            userData.email,
                                            userData.emergencyPhone,
                                            userData.emergencyMessage,
                                            userData.image_url,
                                            latitude,
                                            longitude
                                        )
                                        FirebaseFirestore.getInstance().collection("superwomen").document(uid).set(user)
                                    }
                                } else {

                                }
                            }


                    }

                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@SOSActivity,"Unable to retrive location",Toast.LENGTH_SHORT).show()
                // Handle failure
            }
    }

}
