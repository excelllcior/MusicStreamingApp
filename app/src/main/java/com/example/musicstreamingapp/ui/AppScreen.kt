package com.example.musicstreamingapp.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicstreamingapp.ui.component.PlayerBottomSheet
import com.example.musicstreamingapp.ui.viewmodels.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    val playerState by viewModel.playerState.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()

    Box(modifier = Modifier.fillMaxSize()) {
        AppBottomSheetScaffold(
            sheetContent = {
                PlayerBottomSheet(
                    playerState = playerState,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                )
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 120.dp,
        ) { innerPadding ->
            Box{
                content(innerPadding)
            }
        }

        val bottomBarOffset by animateDpAsState(
            targetValue = when (scaffoldState.bottomSheetState.currentValue) {
                SheetValue.PartiallyExpanded -> 0.dp
                else -> (60).dp
            },
            animationSpec = tween(durationMillis = 300),
            label = "BottomBarAnimation"
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(60.dp)
                .offset(y = bottomBarOffset)
                .background(Color.Cyan),
            contentAlignment = Alignment.Center
        ) {
            Text("Bottom Bar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheetScaffold(
    sheetContent: @Composable (ColumnScope.() -> Unit),
    scaffoldState: BottomSheetScaffoldState,
    sheetPeekHeight: Dp,
    content: @Composable ((PaddingValues) -> Unit)
) {
    BottomSheetScaffold(
        sheetContent = sheetContent,
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(0.dp),
        sheetDragHandle = {},
        containerColor = MaterialTheme.colorScheme.surface,
        content = content
    )
}