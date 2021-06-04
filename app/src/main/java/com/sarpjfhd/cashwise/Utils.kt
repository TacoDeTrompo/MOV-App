package com.sarpjfhd.cashwise

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import java.io.ByteArrayOutputStream

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

object  ImageUtilities{
    init{

    }
    //Obtener una imagen desde la carpeta res y convertirla a byte array
    fun getByteArrayFromResourse(idImage:Int, content: Context):ByteArray{
        var bitmap = BitmapFactory.decodeResource(content.resources, idImage)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream)
        return stream.toByteArray()
    }

    //Obtiene un byte array desde un bitmap
    fun getByteArrayFromBitmap(bitmap: Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream)
        return stream.toByteArray()
    }

    //Obtiene un byte arrya desde un drawable
    fun getByteArrayFromDrawable(drawable: Drawable, content: Context):ByteArray{
        var bitMap =  drawable.toBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,null)
        val stream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG,100, stream)
        return stream.toByteArray()
    }

    //Obtiene un bitamap desde byteArray
    fun getBitMapFromByteArray(data:ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data,0,data.size)
    }
}