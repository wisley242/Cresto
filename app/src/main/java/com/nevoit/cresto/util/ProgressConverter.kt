package com.nevoit.cresto.util

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sign

fun interface ProgressConverter {

    fun convert(progress: Float): Float

    companion object {

        val Default: ProgressConverter =
            ProgressConverter { progress ->
                (1f - exp(-abs(progress * 0.5f))) * progress.sign
            }
    }
}