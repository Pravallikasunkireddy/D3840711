package uk.ac.tees.mad.d3840711

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.d3840711.ui.theme.SheSafeTheme


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberImagePainter
class ArticleScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val image_url = intent.getStringExtra("image_url")
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        val content = intent.getStringExtra("content")
        setContent {
            SheSafeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (url != null) {
                        if (image_url != null) {
                            if (title != null) {
                                if (content != null) {
                                    LatestArticle(image_url,title,content,url)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LatestArticle(urlToImage:String,title:String,content:String,url:String){
        val context = LocalContext.current

        // Background gradient for aesthetic enhancement
        val backgroundGradient = Brush.radialGradient(
            colors = listOf(Color(0xFFB388FF), Color(0xFFB388FF))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFB388FF), Color(0xFFB388FF))
                    )
                ),
        ){

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(brush = backgroundGradient)
            ) {
                // Display image if available
                Image(
                    painter = rememberImagePainter(urlToImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display headline
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.White
                )

                // Display content
                Text(
                    text = content,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White.copy(alpha = 0.85f)  // slightly translucent for better readability
                )

                // Read more button
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))  // A deep purple button
                ) {
                    Text("Read More", color = Color.White)
                }
            }
        }

    }
}
