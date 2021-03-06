package com.kako351.healthconnectsample.ui.top

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.records.BodyTemperature
import com.kako351.healthconnectsample.ui.theme.HealthConnectSampleTheme

@Composable
fun TopScreen(
    onClick: (value: Double) -> Unit,
    onClickHealthConncet: () -> Unit,
    list: List<BodyTemperature>
) {
    val bodyTemperature = remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "体温",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp),
            textAlign = TextAlign.Left,
            fontSize = 20.sp
        )
        BasicTextField(
            bodyTemperature.value,
            onValueChange = {
                bodyTemperature.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp)),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    innerTextField()
                }
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { onClick(bodyTemperature.value.toDouble()) }
        ) {
            Text(
                text = "保存",
                modifier = Modifier,
                fontSize = 16.sp
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "直近のデータ",
                textAlign = TextAlign.Left,
                fontSize = 20.sp
            )

            TextButton(onClick = onClickHealthConncet) {
                Text(
                    text = "HealthConnectでデータを見る",
                    fontSize = 16.sp
                )
            }

        }

        LazyColumn {
            items(list, key = {
                "${it.time.nano}"
            }) {
                Text(
                    text = it.temperatureDegreesCelsius.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 22.sp
                )
                Divider()
            }
        }
    }
}

@Preview
@Composable
fun PreviewTopScreen() {
    HealthConnectSampleTheme {
        TopScreen(onClick = {}, onClickHealthConncet = {}, list = emptyList())
    }
}
