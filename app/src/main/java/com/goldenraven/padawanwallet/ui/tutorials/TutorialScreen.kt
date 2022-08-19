package com.goldenraven.padawanwallet.ui.tutorials

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.tutorial.Tutorial
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.standardBorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// TODO Add Finish Button
// TODO Add transition between pages smoother (pages & progress bar)
// TODO Add tutorial images

@Composable
fun TutorialsScreen(
    tutorialId: Int,
    tutorialViewModel: TutorialViewModel,
    navController: NavHostController
) {
    val tutorialPages = tutorialViewModel.getTutorialPage(id = tutorialId)
    var tutorialData = Tutorial(id = 0, title = "", type = "", difficulty = "", completion = 0)
    LaunchedEffect(key1 = true) { tutorialData = tutorialViewModel.getTutorialData(id = tutorialId) }
    val currentPage = remember { mutableStateOf(value = tutorialData.completion) }
    val pageScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(color = padawan_theme_background_secondary)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(color = padawan_theme_tutorial_background)
                .border(BorderStroke(1.dp, SolidColor(padawan_theme_onPrimary)))
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = padawan_theme_onPrimary,
                        strokeWidth = 15f,
                        start = Offset(x = 0f, y = size.height),
                        end = Offset(x = size.width, y = size.height)
                    )
                }
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                TutorialAppBar(navController = navController)
                TutorialProgressBar(completion = currentPage, total = tutorialPages.size)
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(pageScrollState)
                .padding(all = 32.dp)
                .fillMaxSize()
        ) {
            TutorialPage(tutorialPages = tutorialPages, currentPage = currentPage)
            TutorialButtons(
                tutorialPagesSize = tutorialPages.size,
                currentPage = currentPage,
                pageScrollState = pageScrollState,
                coroutineScope = coroutineScope,
                tutorialViewModel = tutorialViewModel,
                tutorialId = tutorialId,
            )
        }
    }
}

@Composable
fun TutorialButtons(
    tutorialPagesSize: Int,
    currentPage: MutableState<Int>,
    pageScrollState: ScrollState,
    coroutineScope: CoroutineScope,
    tutorialViewModel: TutorialViewModel,
    tutorialId: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        if (currentPage.value != 0) {
            Button(
                onClick = {
                    currentPage.value -= 1
                    tutorialViewModel.setCompletion(id = tutorialId - 1, completion = currentPage.value)
                    scrollUp(pageScrollState = pageScrollState, coroutineScope = coroutineScope)
                },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_secondary),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .standardShadow()
                    .weight(weight = 0.5f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Previous Tutorial Icon"
                    )
                    Spacer(modifier = Modifier.width(width = 16.dp))
                    Text(
                        text = "Prev",
                        style = PadawanTypography.labelLarge,
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(weight = 0.5f))
        }

        if (tutorialPagesSize > currentPage.value) {
            Button(
                onClick = {
                    currentPage.value += 1
                    tutorialViewModel.setCompletion(id = tutorialId - 1, completion = currentPage.value)
                    scrollUp(pageScrollState = pageScrollState, coroutineScope = coroutineScope)
                },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .standardShadow()
                    .weight(weight = 0.5f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Next",
                        style = PadawanTypography.labelLarge,
                    )
                    Spacer(modifier = Modifier.width(width = 16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_front),
                        contentDescription = "Next Tutorial Icon"
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(weight = 0.5f))
        }
    }
}

fun scrollUp(pageScrollState: ScrollState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        pageScrollState.animateScrollTo(value = 0, animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing))
    }
}

@Composable
internal fun TutorialAppBar(navController: NavHostController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_drag_left),
                contentDescription = "Back Icon",
                tint = padawan_theme_onPrimary
            )
        }
        Text(
            text = "Back to lessons",
            style = PadawanTypography.bodyMedium,
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
}

@Composable
fun TutorialProgressBar(
    height: Dp = 8.dp,
    spacer: Float = 30f,
    incompleteColor: Color = padawan_theme_progressbar_background,
    completeColor: Color = padawan_theme_tutorial,
    completion: MutableState<Int>,
    total: Int,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .height(height = height)
    ) {
        val progressBarLen = ((size.width + spacer) / total) - spacer

        for (i in 0 until total) {
            drawLine(
                color = if (completion.value + 1 > i) completeColor else incompleteColor,
                strokeWidth = size.height,
                start = Offset(x = i * (progressBarLen + spacer), y = 0f),
                end = Offset(x = (i + 1) * (progressBarLen + spacer) - spacer, y = 0f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TutorialPage(tutorialPages: List<List<TutorialElement>>, currentPage: MutableState<Int>) {
    if (tutorialPages.size != currentPage.value) {
        for (element in tutorialPages[currentPage.value]) {
            when (element.elementType) {
                ElementType.TITLE -> {
                    Text(
                        text = stringResource(id = element.resourceId),
                        style = PadawanTypography.headlineSmall,
                    )
                }
                ElementType.SUBTITLE -> {
                    Text(
                        text = stringResource(id = element.resourceId),
                        style = PadawanTypography.labelLarge,
                        fontSize = 18.sp
                    )
                }
                ElementType.BODY -> {
                    Text(
                        text = stringResource(id = element.resourceId),
                        style = PadawanTypography.bodyMedium,
                        color = padawan_theme_text_faded_secondary
                    )
                }
                ElementType.RESOURCE -> {
                    Card(
                        containerColor = padawan_theme_button_secondary,
                        border = standardBorder,
                        modifier = Modifier
                            .height(height = 150.dp)
                            .fillMaxWidth()
                            .standardShadow()
                    ) {  }
                }
            }
            Spacer(modifier = Modifier.height(height = 24.dp))
        }
    }
}