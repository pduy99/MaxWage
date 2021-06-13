package com.helios.maxwage.utils

/**
 * Created by Helios on 5/31/2021.
 */

// Int

fun Int.toTimeFormat(): String {
    return "${this.toString().padStart(2, '0')}:00"
}