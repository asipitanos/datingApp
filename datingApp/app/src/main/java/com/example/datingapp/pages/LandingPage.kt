package com.example.datingapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LandingPage() {
    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LandingPagePreview() {
    LandingPage()
}