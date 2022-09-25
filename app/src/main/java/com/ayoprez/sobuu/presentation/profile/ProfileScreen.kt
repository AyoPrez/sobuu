package com.ayoprez.sobuu.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoprez.sobuu.R
import com.ayoprez.sobuu.shared.models.Profile
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    val state = viewModel.state

    state.profileInfo?.let {
        ProfileCard(profile = it, modifier = modifier)
    }

    if(state.isLoading) {
        ProfileLoading()
    }

    state.error?.let {
        Text(text = it, color = Color.Red, textAlign = TextAlign.Center)
    }
}

@Composable
fun ProfileCard(
    profile: Profile,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.profile_name, "${profile.firstName} ${profile.lastName}"))
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Icon(Icons.Filled.Face, contentDescription = "Following")
                Spacer(Modifier.width(5.dp))
                Text(text = "${profile.following.size}")
            }
        }
    }
}

@Composable
fun ProfileLoading(
    modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = modifier)
    }
}