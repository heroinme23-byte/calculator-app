package com.kalex.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape


// THIS KT FILE IS FOR THE UI!!




val buttonList = listOf(
    "C", "(", ")", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    "AC", "0", ".", "%",
    "sin", "cos", "tan", ",",
    "nPr", "nCr", "="
)
@Composable
fun Calculator(modifier: Modifier = Modifier, viewModel: CalculatorViewModel ) {

    val equationText = viewModel.equationText.observeAsState()
    val resultText = viewModel.resultText.observeAsState()


    Box(modifier= modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text= equationText.value?: "",
                style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp,
                        textAlign = TextAlign . End
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = resultText.value?: "",
                style = androidx.compose.ui.text.TextStyle(fontSize = 60.sp,
                    textAlign = TextAlign . End
                ),
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(10.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
            ) {
                items(
                    items = buttonList,
                    span = { btn ->
                        if (btn == "=") GridItemSpan(2) else GridItemSpan(1)
                    }
                ) { btn ->
                    CalculatorButton(
                        btn = btn,
                        onClick = { viewModel.onButtonClick(btn) },
                        isWide = (btn == "=")
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(btn: String, onClick: () -> Unit, isWide: Boolean = false) {
    Box(modifier = Modifier.padding(10.dp)) {
        FloatingActionButton(
            onClick = onClick,
            modifier = if (isWide) {
                Modifier.fillMaxWidth().height(80.dp)
            } else {
                Modifier.size(80.dp)
            },
            shape = if (isWide) RoundedCornerShape(40.dp) else CircleShape,
            contentColor = Color.White,
            containerColor = getColor(btn)
        ) {
            Text(text = btn, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun getColor(btn: String): Color{
    if (btn == "C" || btn == "AC")
        return Color(0xFF044867)
    if (btn == "(" || btn == ")")
        return Color(0xFF0D29C2)
    if (btn == "+" || btn == "/" || btn == "*" || btn == "-")
        return Color(0xFF03A9F4)
    return Color(0xFF1A2023)
}
