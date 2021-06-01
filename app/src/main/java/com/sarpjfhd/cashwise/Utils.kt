package com.sarpjfhd.cashwise

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

class Utils {
}

fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(resId, args, navOptions, navExtras)
    }
}

fun NavController.navigateSafe(nav: NavDirections) {
    val action = currentDestination?.getAction(nav.actionId) ?: graph.getAction(nav.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(nav)
    } else {
        Log.e("Navigation", action.toString())
    }
}