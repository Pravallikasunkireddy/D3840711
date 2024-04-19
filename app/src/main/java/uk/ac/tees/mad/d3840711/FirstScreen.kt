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
//                            val intent = Intent(requireContext(), AuthenticationScreenActivity::class.java)
//                            startActivity(intent)
//                            activity?.finish()
//                        }

                    }
                }
            }.start()
        }
    }


    @Composable
    fun WelcomeScreen() {
        // Replace Color.Magenta with the color that matches your logo's background
        val backgroundColor = Color.Magenta

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.safe), // Replace with your logo resource ID
                contentDescription = "Logo",
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "She Safe", // Replace with your splash screen text
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Replace with the color that complements your logo
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }




}