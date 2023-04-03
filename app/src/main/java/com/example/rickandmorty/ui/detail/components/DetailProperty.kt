package com.example.rickandmorty.ui.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailProperty(
    modifier: Modifier = Modifier,
    label: String,
    value: String?,
    imageVector: ImageVector
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 15.dp),
        elevation = 1.dp,
        backgroundColor = Color.DarkGray
    ) {
        Row(modifier = Modifier.padding(horizontal = 18.dp, vertical =10.dp)) {
            Icon(
                imageVector = imageVector, 
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(Modifier.width(15.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondary)
                //Spacer(Modifier.height(2.dp))
                Text(
                    text = value ?: "",
                    color = Color.White,
                    style = MaterialTheme.typography.body1)
            }
        }
    }
}