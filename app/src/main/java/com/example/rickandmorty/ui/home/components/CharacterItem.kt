package com.example.rickandmorty.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.rickandmorty.domain.model.Characters

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    item: Characters,
    onItemClicked: (Int) -> Unit
) {

    Row(
        modifier = modifier
            .clickable { onItemClicked(item.id) }
            .padding(start = 25.dp, top = 10.dp, bottom = 9.dp)
    ) {
        CharacterImageContainer(modifier = Modifier.size(64.dp)) {
            CharacterImage(item)
        }
        Spacer(Modifier
            .width(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.secondary
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.specie,
                    style = MaterialTheme.typography.body1,
                    color = Color.White,
                )
            }
        }
        Divider(modifier = Modifier.padding(top = 10.dp))
    }
}

// This function may be private
@Composable
fun CharacterImage(item: Characters) {
    Box {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.image)
                .size(Size.ORIGINAL)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// This function may be private
@Composable
fun CharacterImageContainer(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Surface(modifier
        .aspectRatio(1f)
        .border(4.dp, MaterialTheme.colors.secondary, CircleShape),
        shape = CircleShape) {
       content()
    }
}