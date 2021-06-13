package com.helios.maxwage.utils.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.helios.maxwage.R

/**
 * Created by Helios on 3/16/2021.
 */

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

fun Fragment.hideSystemUI(): Int {
    return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE)
}

fun Fragment.showSystemUI(): Int {
    return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}
