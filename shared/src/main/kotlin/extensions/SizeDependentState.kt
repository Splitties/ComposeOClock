package org.splitties.compose.oclock.sample.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.Stable
import androidx.compose.runtime.cache
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import kotlin.reflect.KProperty

@Composable fun <T> T.rememberAsStateWithSize(
    key1: Any? = Unit,
    key2: Any? = Unit,
    key3: Any? = Unit,
    calculation: SizeDependentState.Scope.(T) -> Unit
): SizeDependentState<T> = currentComposer.let {
    val t = this
    it.cache(
        it.changed(value = key1) or
                it.changed(value = key2) or
                it.changed(value = key3) or
                it.changed(value = calculation)
    ) {
        SizeDependentState(
            calculation = { calculation(t);t },
            policy = neverEqualPolicy()
        )
    }
}

@Composable
fun <T> rememberStateWithSize(
    calculation: SizeDependentState.Scope.() -> T
): SizeDependentState<T> = remember(calculation) {
    SizeDependentState(calculation)
}

@Composable
fun <T> rememberStateWithSize(
    key1: Any?,
    calculation: SizeDependentState.Scope.() -> T
): SizeDependentState<T> = remember(
    key1 = key1,
    key2 = calculation
) {
    SizeDependentState(calculation)
}

@Composable
fun <T> rememberStateWithSize(
    key1: Any?,
    key2: Any?,
    calculation: SizeDependentState.Scope.() -> T
): SizeDependentState<T> = remember(
    key1 = key1,
    key2 = key2,
    key3 = calculation
) {
    SizeDependentState(calculation)
}

@Composable
fun <T> rememberStateWithSize(
    vararg keys: Any?,
    calculation: SizeDependentState.Scope.() -> T
): SizeDependentState<T> = remember(*keys, calculation) {
    SizeDependentState(calculation)
}

@Stable
class SizeDependentState<T>(
    private val calculation: Scope.() -> T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
) {

    interface Scope : Density {
        val size: Size
        val center: Offset
    }

    context (Scope)
    operator fun getValue(thisRef: Nothing?, property: KProperty<*>?): T = get()

    context (DrawScope)
    operator fun getValue(thisRef: Nothing?, property: KProperty<*>?): T = get()

    context (DrawScope, Scope)
    operator fun getValue(thisRef: Nothing?, property: KProperty<*>?): T = get()

    context (Scope)
    fun provideDelegate(thisRef: Nothing?, property: KProperty<*>?): SizeDependentState<T> {
        return this
    }

    context (DrawScope)
    fun provideDelegate(thisRef: Nothing?, property: KProperty<*>?): SizeDependentState<T> {
        get() // Ensure it's activated if declared, in case side-effects are needed at draw time.
        return this
    }

    context (DrawScope, Scope)
    fun provideDelegate(thisRef: Nothing?, property: KProperty<*>?): SizeDependentState<T> {
        get() // Ensure it's activated if declared, in case side-effects are needed at draw time.
        return this
    }

    context (DrawScope)
    fun pro(thisRef: Nothing?, property: KProperty<*>): T = get()

    context (Scope)
    fun get(): T = get(size, density, fontScale)

    context (DrawScope)
    private fun getDrawScopeOnly(): T = get(size, density, fontScale)

    context (DrawScope)
    fun get(): T = get(size, density, fontScale)

    context (DrawScope, Scope)
    fun get(): T = getDrawScopeOnly()

    private fun get(size: Size, density: Float, fontScale: Float): T {
        scope.also {
            it.size = size
            it.density = density
            it.fontScale = fontScale
        }
        return value
    }

    private val value by derivedStateOf(policy = policy) { calculation(scope) }

    private val scope = object : Scope {
        override var density: Float by mutableFloatStateOf(1f)
        override var fontScale: Float by mutableFloatStateOf(1f)
        override var size: Size by mutableStateOf(Size.Unspecified)
        override val center: Offset get() = size.center
    }
}
