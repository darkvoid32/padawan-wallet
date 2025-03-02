/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.data.WalletRepository
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.ui.PadawanAppBar
import com.goldenraven.padawanwallet.ui.standardBorder

@Composable
internal fun RecoveryPhraseScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val seedPhrase: String = WalletRepository.getMnemonic()
    val wordList: List<String> = seedPhrase.split(" ")

    Column (
        modifier = Modifier
            .background(padawan_theme_background_secondary)
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ){
        PadawanAppBar(navController = navController, title = "Your recovery phrase")
        wordList.forEachIndexed { index, item ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            ) {
                Text(text = "${index + 1}: ")
                Card(
                    border = standardBorder,
                ) {
                    Text(
                        text = item,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}
