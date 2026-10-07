package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.theme.GoldBright
import com.example.ui.theme.TempleMaroon
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectorBar(
    selectedCalendar: Calendar,
    isToday: Boolean,
    language: Language,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onTodayClick: () -> Unit,
    onDateSelected: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd MMM yyyy, EEE", Locale.ENGLISH)
    val formattedDate = dateFormat.format(selectedCalendar.time)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("date_selector_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, if (isToday) GoldBright else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status badge (Today vs Selected Date)
                Surface(
                    color = if (isToday) GoldBright.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isToday)
                            if (language == Language.TAMIL) "இன்றைய ராசி பலன்" else "Today's Horoscope"
                        else
                            if (language == Language.TAMIL) "தேர்ந்தெடுத்த நாள்" else "Selected Date",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isToday) GoldBright else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isToday) {
                        TextButton(
                            onClick = onTodayClick,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.testTag("go_to_today_button")
                        ) {
                            Icon(Icons.Default.Today, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.TAMIL) "இன்று" else "Today",
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Calendar Picker Button
                    IconButton(
                        onClick = { showDatePickerDialog = true },
                        modifier = Modifier.testTag("open_date_picker_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Pick Date",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Navigation Row: [<]  Date  [>]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPreviousDay,
                    modifier = Modifier.testTag("prev_day_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Day"
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (language == Language.TAMIL)
                            "தேதியை மாற்ற காலண்டரைத் தொடவும்"
                        else
                            "Tap calendar icon to choose past or future date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onNextDay,
                    modifier = Modifier.testTag("next_day_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Day"
                    )
                }
            }
        }
    }

    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedCalendar.timeInMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newCal = Calendar.getInstance().apply {
                                timeInMillis = millis
                            }
                            onDateSelected(newCal)
                        }
                        showDatePickerDialog = false
                    },
                    modifier = Modifier.testTag("confirm_date_picker_button")
                ) {
                    Text(text = if (language == Language.TAMIL) "சரி (OK)" else "OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePickerDialog = false },
                    modifier = Modifier.testTag("cancel_date_picker_button")
                ) {
                    Text(text = if (language == Language.TAMIL) "ரத்து (Cancel)" else "Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = if (language == Language.TAMIL)
                            "தேதியை தேர்ந்தெடுக்கவும் (Select Date)"
                        else
                            "Select Date for Horoscope",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }
}
