package com.ayoprez.sobuu.presentation.authentication

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ayoprez.sobuu.ui.theme.DarkLava
import com.ayoprez.sobuu.ui.theme.WhiteBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

enum class TextType {
    TERMS_AND_CONDITIONS, PRIVACY_POLICY
}

@Composable
@Destination
fun LongTextScreen(
    nav: DestinationsNavigator?,
    textType: TextType,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WhiteBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(12.dp),
            onClick = {
                nav?.navigateUp()
            }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "",
                tint = DarkLava,
                modifier = Modifier.size(24.dp)
            )
        }

        val url: String = if(textType == TextType.PRIVACY_POLICY) {
            //TODO Complete this with the right link
            "https://www.geeksforgeeks.org/webview-in-android-using-jetpack-compose/"
        } else {
            //TODO Complete this with the right link
            "https://proandroiddev.com/how-to-display-html-using-android-compose-c59e24ec0c6f"
        }

        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        }, update = {
            it.loadUrl(url)
        })
    }
}

@Preview(showSystemUi = true, showBackground = true, group = "Done")
@Composable
fun ComposableLongTextPreview() {
    LongTextScreen(
        null,
        textType = TextType.PRIVACY_POLICY,
    )
}
