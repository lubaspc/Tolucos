package com.lubaspc.traveltolucos.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lubaspc.traveltolucos.R

@ExperimentalMaterialApi
@Composable
fun cardCheck(
    isChecked: Boolean,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        role = Role.Checkbox,
        elevation = 10.dp,
        modifier = modifier.padding(4.dp),
        backgroundColor = colorResource(id = if (isChecked) R.color.purple_700 else R.color.teal_200)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = description,
            fontSize = 18.sp,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun textBody(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 24.sp,
        modifier = modifier.padding(10.dp, 4.dp),
    )
}

@Composable
fun textTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 24.sp,
        modifier = modifier.padding(10.dp, 4.dp),
        fontWeight = FontWeight.Bold,
    )
}



