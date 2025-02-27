// SPDX-License-Identifier: MIT
package ru.nsu.bobrofon.easysshfs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.os.Handler
import android.util.Log
import androidx.core.content.IntentCompat
import com.topjohnwu.superuser.Shell

import ru.nsu.bobrofon.easysshfs.mountpointlist.MountPointsList

private const val TAG = "InternetStateChange"
private const val AUTO_MOUNT_DELAY_MILLIS: Long = 5000

class InternetStateChangeReceiver(
    private val handler: Handler
) : BroadcastReceiver() {

    private val shell: Shell by lazy { EasySSHFSActivity.initNewShell() }

    override fun onReceive(context: Context, intent: Intent) {
        val mountPointsList = MountPointsList.instance(context)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
    
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            Log.d(TAG, "Network is connected")
            // Handle network connected state, either WiFi or Ethernet
            handler.postDelayed({ autoMount(mountPointsList, shell) }, AUTO_MOUNT_DELAY_MILLIS)
        } else {
            Log.d(TAG, "No network connection")
            // Handle network disconnected state
            handler.post { forceUmount(mountPointsList, shell) }
        }
    }
    

    private fun autoMount(mountPointsList: MountPointsList, shell: Shell) {
        Log.d(TAG, "check auto-mount")
        if (mountPointsList.needAutomount()) {
            Log.d(TAG, "auto-mount required")
            mountPointsList.autoMount(shell)
        }
    }

    private fun forceUmount(mountPointsList: MountPointsList, shell: Shell) {
        Log.d(TAG, "force umount everything")
        mountPointsList.umount(shell)
    }
}