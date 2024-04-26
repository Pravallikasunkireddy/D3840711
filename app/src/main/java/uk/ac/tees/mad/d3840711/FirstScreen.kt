package uk.ac.tees.mad.d3840711

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment

class welcomeScreen: Fragment(R.layout.first_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            WelcomeScreen()
            object : Thread() {
                override fun run() {
                    try {
                        sleep(1000)
                    } catch (e: Exception) {
                    } finally {

//                        if (isUserLoggedIn()) {
//                            val intent = Intent(requireContext(), HomeActivity2::class.java)
//                            startActivity(intent)
//                            activity?.finish()
//                        } else {
                            val intent = Intent(requireContext(), CreateAccountScreen::class.java)
                            startActivity(intent)
                            activity?.finish()
//                        }

                    }
                }
            }.start()
        }
    }

    @Composable
    fun WelcomeScreen() {
        // Create a radial gradient
        val gradient = Brush.radialGradient(
            colors = listOf(
                Color(0xFF6200EE), // Deep purple
                Color(0xFFB388FF)  // Lighter purple shade
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.safe), // Ensure this is the correct ID
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(180.dp) // Adjusted size for better visibility
                        .padding(bottom = 20.dp), // Added padding to separate from text
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "She Safe", // App name displayed as part of the splash screen
                    fontSize = 28.sp, // Slightly larger for emphasis
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }


}