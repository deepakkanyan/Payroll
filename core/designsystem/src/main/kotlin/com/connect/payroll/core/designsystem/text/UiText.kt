package com.connect.payroll.core.designsystem.text

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A string a ViewModel can emit without holding an Android [android.content.Context]. [Resource]
 * is resolved from strings.xml at render time via [asString]; [Dynamic] carries an already-final
 * value, e.g. an exception message, or a domain-layer validation message — the domain layer stays
 * free of Android string resources by convention, so it should never construct a [Resource] itself.
 */
sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(@param:StringRes val resId: Int, val args: List<Any> = emptyList()) : UiText
}

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> stringResource(resId, *args.toTypedArray())
}
