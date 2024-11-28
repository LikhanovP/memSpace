package xm.space.ultimatememspace.core.uikit.components.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerItem(value: Float) = SpacerByValue(value = value)

@Composable
private fun SpacerByValue(value: Float) = Spacer(modifier = Modifier.height(value.dp))

@Composable
fun SpacerCustom(modifier: Modifier = Modifier) = Spacer(modifier = modifier)