package ru.nsu.bobrofon.easysshfs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.topjohnwu.superuser.Shell
import ru.nsu.bobrofon.easysshfs.mountpointlist.MountPointsList

class MountReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        val mountPointsList = MountPointsList.instance(context)
        mountPointsList.autoMount(Shell.getShell())
    }
}
