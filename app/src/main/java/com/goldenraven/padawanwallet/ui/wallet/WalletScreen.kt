/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.noRippleClickable
import com.goldenraven.padawanwallet.ui.shadowModifier
import com.goldenraven.padawanwallet.ui.shadowModifierButton

@Composable
internal fun WalletScreen(
    walletViewModel: WalletViewModel = viewModel(),
    navController: NavHostController
) {
    val balance by walletViewModel.balance.observeAsState()
    val isRefreshing by walletViewModel.isRefreshing.collectAsState()
    val openFaucetDialog by walletViewModel.openFaucetDialog
    val transactionList by walletViewModel.readAllData.observeAsState(initial = emptyList())
    val context = LocalContext.current

    if (openFaucetDialog) {
        FaucetDialog(
            walletViewModel = walletViewModel
        )
    }

    if (walletViewModel.isOnline(context = context) && !Wallet.isBlockChainCreated()) {
        Wallet.createBlockchain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(padawan_theme_background_secondary, shape = BackgroundShape())
            .padding(all = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MainBox(navController = navController, balance = balance.toString())
            TransactionListBox(transactionList = transactionList)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBox(navController: NavHostController, balance: String) {
    Card(
        modifier = shadowModifier.fillMaxWidth(),
        border = BorderStroke(2.dp, SolidColor(padawan_theme_onPrimary)),
        shape = RoundedCornerShape(20.dp),
        containerColor = padawan_theme_onBackground_secondary
    ) {
        ConstraintLayout(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .fillMaxWidth()) {
            val (cardName, currencyToggle, balanceText, currencyText, buttonRow) = createRefs()
            val currencyToggleState = remember { mutableStateOf(value = false) }
            Text(
                text = "bitcoin testnet",
                modifier = Modifier.constrainAs(cardName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
            Box(modifier = Modifier
                .constrainAs(currencyToggle) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .background(color = padawan_theme_button_secondary, shape = RoundedCornerShape(size = 10.dp))
                .noRippleClickable { currencyToggleState.value = !currencyToggleState.value }
            ) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "btc",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(all = 8.dp),
                        color = if (!currencyToggleState.value) padawan_theme_disabled else padawan_theme_onPrimary
                    )
                    Divider(
                        color = padawan_theme_disabled,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(3.dp)
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "sats",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(all = 8.dp),
                        color = if (currencyToggleState.value) padawan_theme_disabled else padawan_theme_onPrimary
                    )
                }
            }
            Text(
                text = balance,
                modifier = Modifier.constrainAs(balanceText) {
                    top.linkTo(cardName.bottom)
                    start.linkTo(parent.start)
                },
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "sats",
                modifier = Modifier
                    .padding(all = 8.dp)
                    .constrainAs(currencyText) {
                        start.linkTo(balanceText.end)
                        bottom.linkTo(balanceText.bottom)
                    }
            )
            Row(
                modifier = Modifier
                    .constrainAs(buttonRow) {
                        top.linkTo(balanceText.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(Screen.ReceiveScreen.route) },
                    modifier = shadowModifierButton.weight(weight = 0.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_secondary),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(2.dp, SolidColor(padawan_theme_onPrimary))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(text = "Receive")
                        Icon(painter = painterResource(id = R.drawable.ic_receive), contentDescription = "Receive Icon")
                    }
                }
                Button(
                    onClick = { navController.navigate(Screen.SendScreen.route) },
                    modifier = shadowModifierButton.weight(weight = 0.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(2.dp, SolidColor(padawan_theme_onPrimary))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(text = "Send")
                        Icon(painter = painterResource(id = R.drawable.ic_send), contentDescription = "Send Icon")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListBox(transactionList: List<Tx>) {
    Row(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
        Text(
            text = "Transactions",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(weight = 0.5f)
                .align(Alignment.Bottom)
        )
        Text(
            text = "View all transactions",
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(weight = 0.5f)
                .align(Alignment.Bottom)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, SolidColor(padawan_theme_onPrimary)),
        shape = RoundedCornerShape(20.dp),
        containerColor = padawan_theme_background_secondary
    ) {
        if (transactionList.isEmpty()) {
            Row(modifier = Modifier.padding(all = 32.dp)) {
                Column(modifier = Modifier.weight(weight = 0.70f)) {
                    Text(
                        text = "Hey! Your transaction list is empty, get some coins so you can fill it up.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Button(
                        onClick = {  },
                        modifier = shadowModifierButton,
                        colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, SolidColor(padawan_theme_onPrimary))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(text = "Get coins")
                            Icon(painter = painterResource(id = R.drawable.ic_receive), contentDescription = "Get Coins Icon")
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.diagram_wip),
                    contentDescription = "No Transactions Diagram",
                    modifier = Modifier
                        .weight(weight = 0.30f)
                        .align(Alignment.CenterVertically)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(color = padawan_theme_lazyColumn_background)
                    .padding(all = 24.dp)
            ) {
                items(transactionList) { txn ->
                    Column {
                        Row {
                            Text(
                                text = "${txn.txid.take(n = 5)}.....${txn.txid.takeLast(n = 5)}",
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                            Text(
                                text = "${if (txn.isPayment) txn.valueOut.toString() else txn.valueIn.toString()} sats",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                        }
                        Row {
                            Text(
                                text = txn.date,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                            Text(
                                text = if (txn.isPayment) "Send" else "Receive",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

class BackgroundShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                addOval(oval = Rect(
                    top = size.height / 5,
                    bottom = size.height / 2,
                    left = size.width / -6,
                    right = size.width + size.width / 6,
                    )
                )
                addRect(rect = Rect(
                    top = (size.height / 20) * 7,
                    bottom = size.height,
                    left = 0f,
                    right = size.width,
                    )
                )
                close()
            }
        )
    }
}

@Composable
private fun FaucetDialog(walletViewModel: WalletViewModel) {
    AlertDialog(
        backgroundColor = md_theme_dark_lightBackground,
        onDismissRequest = {},
        title = {
            Text(
                text = "Hello there!",
                style = PadawanTypography.headlineMedium,
                color = md_theme_dark_onLightBackground
            )
        },
        text = {
            Text(
                text = "We notice it is your first time opening Padawan wallet. Would you like Padawan to send you some testnet coins to get your started?",
                color = md_theme_dark_onLightBackground
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    walletViewModel.onPositiveDialogClick()
                },
            ) {
                Text(
                    text = "Yes please!",
                    color = md_theme_dark_onLightBackground
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    walletViewModel.onNegativeDialogClick()
                },
            ) {
                Text(
                    text = "Not right now",
                    color = md_theme_dark_onLightBackground
                )
            }
        },
    )
}
