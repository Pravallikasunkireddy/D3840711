package uk.ac.tees.mad.d3840711

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationClass(val route: String, val label: String, val icons: ImageVector) {

    object Home : NavigationClass("home", "Home", Icons.Default.Home)

    object Account: NavigationClass("account","Account", Icons.Default.AccountCircle)

}