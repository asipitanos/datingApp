package com.example.datingapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.datingapp.R
import com.example.datingapp.data.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Conversations") },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .navigationBarsPadding()
                    .padding(paddingValues),
        ) {
            items(SampleData.contactNames) { name ->
                ContactRow(name = name) {
                    navController.navigate("chat/$name")
                }
                HorizontalDivider(Modifier.padding(horizontal = 16.dp), DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}

@Composable
fun ContactRow(
    name: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(R.drawable.ic_woman),
            contentDescription = "$name's profile picture",
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showSystemUi = true)
@Composable
fun LandingPagePreview() {
    LandingPage(
        navController = rememberNavController(),
    )
}
