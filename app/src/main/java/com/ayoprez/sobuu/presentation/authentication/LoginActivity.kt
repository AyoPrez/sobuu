package com.ayoprez.sobuu.presentation.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ayoprez.sobuu.presentation.NavGraphs
import com.ayoprez.sobuu.ui.theme.SobuuAuthTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SobuuAuthTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
                //TODO add here an if, so it goes to the main
                //TODO activity if there is a valid session token?
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SobuuAuthTheme {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}