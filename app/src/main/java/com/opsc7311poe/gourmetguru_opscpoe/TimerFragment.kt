package com.opsc7311poe.gourmetguru_opscpoe

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.util.concurrent.TimeUnit
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable2
import android.media.RingtoneManager
import android.os.Build
import android.widget.NumberPicker
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TimerFragment : Fragment() {

    private lateinit var btnBack: ImageView
    private lateinit var myRecipesHeaderImage: ImageView
    private lateinit var txtRecipeTimer: TextView
    private lateinit var txtElapsedTime: TextView
    private lateinit var btnStart: Button
    private lateinit var btnReset: TextView
    private var countdownTimer: CountDownTimer? = null
    private var timeInMillis: Long = 0 // To store user input time in milliseconds
    private var timeRemaining: Long = 0L
    private var isTimerRunning: Boolean = false
    private lateinit var btnPause: Button
    private lateinit var clockImg : ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        btnBack = view.findViewById(R.id.btnBack)
        myRecipesHeaderImage = view.findViewById(R.id.MyRecipesHeaderImage)
        txtRecipeTimer = view.findViewById(R.id.txtRecipeTimer)
        txtElapsedTime = view.findViewById(R.id.txtElapsedTime)
        btnStart = view.findViewById(R.id.btnStartTimer)
        btnReset = view.findViewById(R.id.btnReset)
        btnPause = view.findViewById(R.id.btnPauseTimer)
        clockImg = view.findViewById(R.id.imgAnimatedClock)


        // Check if notification permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // For Android 13+
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request notification permission
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            } else {
                // Proceed with the operation that requires permission
                createNotificationChannel()
            }
        } else {
            // For lower Android versions, directly proceed
            createNotificationChannel()
        }


        // Handle selecting the time
        txtElapsedTime.setOnClickListener {
            showTimeInputDialog()
        }

        // Set onClickListener for Start Timer Button
        btnStart.setOnClickListener {
            val inputTime = txtElapsedTime.text.toString()
            val totalTime = parseTimeInput(inputTime)

            if (totalTime > 0) {
                startTimer(totalTime)
                btnStart.visibility = View.GONE
                btnPause.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Please enter a valid time.", Toast.LENGTH_SHORT).show()
            }
        }


        // Set onClickListener for Pause Timer Button
        btnPause.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
                btnStart.visibility = View.VISIBLE
                btnPause.visibility = View.GONE
            }
        }

        btnReset.setOnClickListener {
            resetTimer()
        }



    }




    // Function to show the input dialog with TimePicker for hours and minutes, and a custom NumberPicker for seconds
    private fun showTimeInputDialog() {
        var selectedHour = 0
        var selectedMinute = 0
        var selectedSecond = 0

        // Create TimePickerDialog for hours and minutes
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            selectedHour = hourOfDay
            selectedMinute = minute

            // After selecting hours and minutes, show custom dialog for seconds
            showSecondsPickerDialog { second ->
                selectedSecond = second

                // Update txtElapsedTime with the selected time
                val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, selectedSecond)
                timeInMillis = parseTimeInput(formattedTime) // Save the time in milliseconds
                txtElapsedTime.text = formattedTime
            }
        }, 0, 0, true)

        // Show TimePickerDialog
        timePickerDialog.show()
    }

    // Function to show a custom NumberPicker for selecting seconds
    private fun showSecondsPickerDialog(onSecondSelected: (Int) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Set Seconds")

        // Create a NumberPicker for seconds (0-59)
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 59
            wrapSelectorWheel = true
        }

        builder.setView(numberPicker)

        builder.setPositiveButton("OK") { _, _ ->
            val selectedSecond = numberPicker.value
            onSecondSelected(selectedSecond) // Pass the selected second back
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }





    /* // Function to show the input dialog
     private fun showTimeInputDialog() {
         val builder = AlertDialog.Builder(requireContext())
         builder.setTitle("Set Countdown Time (HH:MM:SS)")

         // Create EditText to input time
         val input = EditText(requireContext())
         input.hint = "00:00:00"
         builder.setView(input)

         builder.setPositiveButton("OK") { _, _ ->
             val timeInput = input.text.toString()
             timeInMillis = parseTimeInput(timeInput)
             txtElapsedTime.text = timeInput
         }

         builder.setNegativeButton("Cancel") { dialog, _ ->
             dialog.cancel()
         }

         builder.show()
     }*/


    // Function to parse time input in HH:mm:ss format
    private fun parseTimeInput(timeInput: String): Long {
        val parts = timeInput.split(":")
        return if (parts.size == 3) {
            val hours = parts[0].toLongOrNull() ?: 0
            val minutes = parts[1].toLongOrNull() ?: 0
            val seconds = parts[2].toLongOrNull() ?: 0
            (hours * 3600000) + (minutes * 60000) + (seconds * 1000)
        } else {
            0
        }
    }

    // Function to start the timer
    private fun startTimer(milliseconds: Long) {
        isTimerRunning = true
        countdownTimer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                updateTimerDisplay(timeRemaining)
                val drawable = clockImg.drawable
                if (drawable is Animatable2) {
                    drawable.start()
                }
            }

            override fun onFinish() {
                isTimerRunning = false
                Toast.makeText(requireContext(), "Timer Finished!", Toast.LENGTH_SHORT).show()
                btnStart.visibility = View.VISIBLE
                btnPause.visibility = View.GONE
                // Send push notification
                sendNotification()
                // Play a sound when the timer is finished
                playSound()

                val drawable = clockImg.drawable
                if (drawable is Animatable2) {
                    drawable.stop()
                }
            }
        }.start()
    }

    // Function to update the timer display
    private fun updateTimerDisplay(millis: Long) {
        val hours = millis / 3600000
        val minutes = (millis % 3600000) / 60000
        val seconds = (millis % 60000) / 1000
        txtElapsedTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    // Function to pause the timer
    private fun pauseTimer() {
        countdownTimer?.cancel()
        isTimerRunning = false
        val drawable = clockImg.drawable
        if (drawable is Animatable2) {
            drawable.stop()
        }
    }

    // Function to reset the timer
    private fun resetTimer() {
        countdownTimer?.cancel() // Stop the timer if it's running
        timeInMillis = 0
        txtElapsedTime.text = "00:00:00" // Reset the displayed time
        val drawable = clockImg.drawable
        if (drawable is Animatable2) {
            drawable.stop()
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TimerChannel"
            val descriptionText = "Channel for Timer Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("timer_channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    // Method to send the notification
    private fun sendNotification() {
        val builder = NotificationCompat.Builder(requireContext(), "timerChannelId")
            .setSmallIcon(R.drawable.clock) // Set your app's timer icon
            .setContentTitle("Gourmet Guru")
            .setContentText("Your recipe timer is done!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Close notification on tap

        val notificationManager = NotificationManagerCompat.from(requireContext())
        notificationManager.notify(1, builder.build())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with creating the notification channel
            createNotificationChannel()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    // Method to play sound when the timer finishes
    private fun playSound() {
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(requireContext(), notificationSound)
        ringtone.play()
    }


    // Function to replace the current fragment
    private fun replaceFragment(fragment: Fragment) {
        Log.d("CustomerFragment", "Replacing fragment: ${fragment::class.java.simpleName}")
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
