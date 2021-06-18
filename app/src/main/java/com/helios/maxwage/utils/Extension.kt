package com.helios.maxwage.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.helios.maxwage.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Helios on 5/31/2021.
 */

// region Fragment extensions

fun FragmentManager.push(
    fragment: Fragment,
    intAnim: Int = R.anim.abc_grow_fade_in_from_bottom,
    outAnim: Int = R.anim.abc_shrink_fade_out_from_bottom,
    container: Int,
    allowAddToBackStack: Boolean = false
) {
    beginTransaction().setCustomAnimations(intAnim, outAnim)
        .add(container, fragment, fragment::class.java.simpleName)
        .also {
            if (allowAddToBackStack) {
                it.addToBackStack(fragment::class.java.simpleName)
            }
        }
        .commitAllowingStateLoss()
}

fun FragmentManager.replace(
    fragment: Fragment,
    intAnim: Int = R.anim.abc_grow_fade_in_from_bottom,
    outAnim: Int = R.anim.abc_shrink_fade_out_from_bottom,
    container: Int,
    allowAddToBackStack: Boolean = false
) {
    beginTransaction().setCustomAnimations(intAnim, outAnim)
        .replace(container, fragment, fragment::class.java.simpleName)
        .also {
            if (allowAddToBackStack) {
                it.addToBackStack(fragment::class.java.simpleName)
            }
        }
        .commitAllowingStateLoss()
}

fun FragmentManager.remove(
    fragment: Fragment,
    intAnim: Int = R.anim.abc_grow_fade_in_from_bottom,
    outAnim: Int = R.anim.abc_shrink_fade_out_from_bottom
) {
    beginTransaction().setCustomAnimations(intAnim, outAnim)
        .remove(fragment)
        .commitAllowingStateLoss()
}

fun FragmentManager.pop(): Boolean {
    if (backStackEntryCount == 0) return false
    val fragment = getBackStackEntryAt(backStackEntryCount - 1).name?.let {
        findFragmentByTag(it)
    } ?: return false
    remove(fragment)
    return true
}

fun FragmentManager.get(name: String): Fragment? {
    return findFragmentByTag(name)
}

// endregion

// region Int extensions

fun Int.toTimeFormat(): String {
    return "${this.toString().padStart(2, '0')}:00"
}

// endregion

// region String extensions

fun String.isEmailFormat(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPhoneNumber(): Boolean {
    val regex = Regex("(84|0[3|5|7|8|9])+([0-9]{8})\\b")

    return regex.matches(this)
}

fun String.toDate(
    format: String = "dd/MM/yyyy",
    locale: Locale = Locale.getDefault()
): Date? {
    return try {
        val simpleDateFormat =
            SimpleDateFormat(format, Locale.ENGLISH)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        simpleDateFormat.parse(this)
    } catch (ex: Exception) {
        null
    }
}

/**
 * Returns a copy of this string having its first letter uppercase, other letters are lowercase using the rules of the default locale,
 * or the original string if it's empty
 */
@OptIn(ExperimentalStdlibApi::class)
@SinceKotlin("1.4")
fun String.standardizeCase(locale: Locale): String {
    return if (isEmpty()) {
        this
    } else {
        (this[0].uppercase(locale) + substring(1, this.length).lowercase(locale))
    }
}

// endregion

// region EditText

fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun TextInputEditText.validate(
    validator: (String) -> Boolean,
    messageError: String,
    onInvalid: () -> Unit = {},
    onValid: () -> Unit = {}
) {
    this.afterTextChanged {
        val textInputLayout = (this.parent.parent as? TextInputLayout)
        val error = if (validator(it)) null else messageError
        if (textInputLayout != null) {
            textInputLayout.error = error
        } else {
            this.error = if (validator(it)) null else messageError
        }
        if (error != null) {
            onInvalid.invoke()
        } else {
            onValid.invoke()
        }
    }
}

// endregion

// region DateTime

fun Date.toString(format: String = "dd/MM/yyyy", locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

// endregion