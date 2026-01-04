package com.example.moviesdatabase.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesdatabase.presentation.util.ImageConstants
import com.example.moviesdatabase.presentation.util.getPosterUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val movie by viewModel.movieState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Details") },
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack()}
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    movie?.let { currentMovie ->
                        IconButton(
                            onClick = {
                                shareMovie(context, currentMovie.title, currentMovie.id)
                            }
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share Movie")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            movie?.let { currentMovie ->
                FloatingActionButton(
                    onClick = { viewModel.onBookmarkClick(currentMovie) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = if (currentMovie.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Bookmark",
                        tint = if (currentMovie.isBookmarked) Color.Red else Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (movie == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val currentMovie = movie!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    var posterUrl by remember {
                        mutableStateOf(currentMovie.getPosterUrl(ImageConstants.IMAGE_SIZE_ORIGINAL))
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(posterUrl)
                            .crossfade(true)
                            .listener(
                                onError = { _, _ ->
                                    posterUrl = currentMovie.getPosterUrl(ImageConstants.IMAGE_SIZE_W500)
                                }
                            )
                            .build(),
                        contentDescription = currentMovie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = currentMovie.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Rating: ${currentMovie.voteAverage}/10",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Overview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentMovie.overview,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

fun shareMovie(context: Context, title: String, movieId: Int) {
    val deepLink = "https://www.moviedbapp.com/movie/$movieId"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Check out this movie!")
        putExtra(Intent.EXTRA_TEXT, "I found this amazing movie '$title' on MovieDB App!\n\nCheck it out here: $deepLink")
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share Movie via")
    context.startActivity(shareIntent, null)
}