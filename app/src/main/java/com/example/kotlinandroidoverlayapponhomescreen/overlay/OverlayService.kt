package com.example.kotlinandroidoverlayapponhomescreen.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.core.app.NotificationCompat
import com.example.kotlinandroidoverlayapponhomescreen.MainActivity
import com.example.kotlinandroidoverlayapponhomescreen.R
import com.example.kotlinandroidoverlayapponhomescreen.storage.TimerStorage
import com.example.kotlinandroidoverlayapponhomescreen.utils.Constants
import com.example.kotlinandroidoverlayapponhomescreen.utils.TimerUtils

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private val CHANNEL_ID = Constants.OVERLAY_CHANNEL_ID
    private val storage = TimerStorage(this)
    private val buttons = mutableListOf<ImageButton>()
    private var updateReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(Constants.OVERLAY_NOTIFICATION_ID, createNotification())

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.overlay_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.x = 0
        params.y = 100  // Moved up for less obstruction

        windowManager.addView(overlayView, params)
        setupClickListeners()
        registerUpdateReceiver()
        updateIcons()
    }

    private fun setupClickListeners() {
        overlayView?.let { view ->
            buttons.clear()
            // Setup click listeners on the 5 hourglass buttons
            for (i in 0..4) {
                val buttonId = resources.getIdentifier("hourglass_$i", "id", packageName)
                val button = view.findViewById<ImageButton>(buttonId)
                button?.let { btn ->
                    buttons.add(btn)
                    btn.setOnClickListener {
                        // Send broadcast to toggle timer
                        val intent = Intent(Constants.ACTION_OVERLAY_TIMER_TAP).apply {
                            putExtra(Constants.EXTRA_TIMER_INDEX, i)
                        }
                        sendBroadcast(intent)
                        // Also open app to show full UI
                        val activityIntent = Intent(this, MainActivity::class.java)
                        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(activityIntent)
                    }
                }
            }
        }
    }
    
    private fun registerUpdateReceiver() {
        updateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Constants.ACTION_UPDATE_OVERLAY -> updateIcons()
                }
            }
        }
        val filter = IntentFilter(Constants.ACTION_UPDATE_OVERLAY)
        registerReceiver(updateReceiver, filter)
    }
    
    private fun updateIcons() {
        val timers = storage.loadTimers() ?: return
        buttons.forEachIndexed { index, button ->
            if (index < timers.size) {
                val timer = timers[index]
                val isReady = TimerUtils.isTimerReady(timer.remainingSeconds)
                val iconName = if (isReady) "hourglass_empty" else "hourglass_full"
                val iconResId = resources.getIdentifier(iconName, "drawable", packageName)
                
                button.setImageResource(iconResId)
                
                // Apply color filter based on state
                val color = when {
                    isReady -> Constants.TIMER_READY_COLOR
                    timer.isRunning -> Constants.TIMER_RUNNING_COLOR
                    else -> Constants.TIMER_PAUSED_COLOR
                }
                button.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateReceiver?.let { unregisterReceiver(it) }
        overlayView?.let { windowManager.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(NotificationChannel(CHANNEL_ID, Constants.OVERLAY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW))
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MLBB Tracker Overlay")
            .setContentText("Overlay active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }
}
