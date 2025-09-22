package gorosheg.pulsiq.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

enum class PermissionType {
    VIBRATOR,
    BLUETOOTH,
    NOTIFICATION
}

fun Context.hasPermissions(vararg permissions: PermissionType): Boolean {
    return permissions.all { permissionType ->
        when (permissionType) {
            PermissionType.VIBRATOR -> checkVibratorPermission()
            PermissionType.BLUETOOTH -> checkBluetoothPermissions()
            PermissionType.NOTIFICATION -> checkNotificationPermissions()
        }
    }
}

fun Context.hasAllRequiredPermissions(): Boolean {
    return hasPermissions(
        PermissionType.VIBRATOR,
        PermissionType.BLUETOOTH,
        PermissionType.NOTIFICATION
    )
}

fun Context.getMissingPermissions(): List<PermissionType> {
    return listOf(
        PermissionType.VIBRATOR,
        PermissionType.BLUETOOTH,
        PermissionType.NOTIFICATION
    ).filter { !hasPermissions(it) }
}

fun Context.getMissingPermissionStrings(): Array<String> {
    return getMissingPermissions().flatMap { permissionType ->
        when (permissionType) {
            PermissionType.VIBRATOR -> listOf(Manifest.permission.VIBRATE)
            PermissionType.BLUETOOTH -> getBluetoothPermissionStrings()
            PermissionType.NOTIFICATION -> getNotificationPermissionStrings()
        }
    }.toTypedArray()
}

private fun getBluetoothPermissionStrings(): List<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }
}

private fun getNotificationPermissionStrings(): List<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyList()
    }
}

private fun Context.checkVibratorPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.VIBRATE
    ) == PackageManager.PERMISSION_GRANTED
}

private fun Context.checkBluetoothPermissions(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADMIN
                ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun Context.checkNotificationPermissions(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}