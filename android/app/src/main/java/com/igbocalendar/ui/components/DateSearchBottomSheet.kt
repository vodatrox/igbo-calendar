@file:OptIn(ExperimentalMaterial3Api::class)

package com.igbocalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igbocalendar.data.IgboCalendarEngine
import com.igbocalendar.data.IgboDate
import com.igbocalendar.ui.theme.igboColors
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

private val GREG_MONTHS = Month.values().map { it.getDisplayName(TextStyle.FULL, Locale.ENGLISH) }
private val IGBO_MONTH_OPTIONS = IgboCalendarEngine.IGBO_MONTHS + listOf("Ubọchị Ọmụmụ")
private const val UBOSHI_IDX = 13

@Composable
fun DateSearchBottomSheet(
    currentDate: LocalDate,
    currentIgboDate: IgboDate,
    onNavigate: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = igboColors
    var tab by remember { mutableIntStateOf(0) }  // 0 = Gregorian, 1 = Igbo

    // Gregorian form state — pre-filled with current selected date
    var gMonthIdx by remember { mutableIntStateOf(currentDate.monthValue - 1) }
    var gDay      by remember { mutableStateOf(currentDate.dayOfMonth.toString()) }
    var gYear     by remember { mutableStateOf(currentDate.year.toString()) }

    // Igbo form state — pre-filled with current Igbo date
    val initIgboMonthIdx = if (currentIgboDate.isUboshiOmumu) UBOSHI_IDX else currentIgboDate.monthIndex
    var iMonthIdx by remember { mutableIntStateOf(initIgboMonthIdx) }
    var iDay      by remember {
        mutableStateOf(
            if (currentIgboDate.isUboshiOmumu) "" else (currentIgboDate.dayWithinMonth + 1).toString()
        )
    }
    // currentIgboDate.year is the Igbo display year (base + 5000)
    var iAro by remember { mutableStateOf(currentIgboDate.year.toString()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        contentColor = colors.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .imePadding()
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Go to Date",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = colors.onSurfaceMuted)
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Tab toggle ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Gregorian", "Igbo Era").forEachIndexed { idx, label ->
                    val selected = tab == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (selected) colors.primary else Color.Transparent)
                            .clickable { tab = idx }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) colors.onPrimary else colors.onSurfaceMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Form fields ───────────────────────────────────────────────────
            if (tab == 0) {
                GregorianDateFields(
                    monthIdx = gMonthIdx, day = gDay, year = gYear,
                    onMonthChange = { gMonthIdx = it },
                    onDayChange   = { gDay = it },
                    onYearChange  = { gYear = it }
                )
            } else {
                IgboDateFields(
                    monthIdx = iMonthIdx, day = iDay, aro = iAro,
                    onMonthChange = { iMonthIdx = it; if (it == UBOSHI_IDX) iDay = "" },
                    onDayChange   = { iDay = it },
                    onAroChange   = { iAro = it }
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Validation ────────────────────────────────────────────────────
            val resolvedDate = if (tab == 0) {
                resolveGregorianDate(gMonthIdx, gDay, gYear)
            } else {
                resolveIgboDate(iMonthIdx, iDay, iAro)
            }

            val hasInput = if (tab == 0) {
                gDay.isNotEmpty() && gYear.isNotEmpty()
            } else {
                iAro.isNotEmpty() && (iMonthIdx == UBOSHI_IDX || iDay.isNotEmpty())
            }

            if (hasInput && resolvedDate == null) {
                Text(
                    "Please enter a valid date.",
                    fontSize = 12.sp,
                    color = colors.error,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Go button ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (resolvedDate != null) colors.primary else colors.surfaceVariant)
                    .then(
                        if (resolvedDate != null)
                            Modifier.clickable { onNavigate(resolvedDate); onDismiss() }
                        else Modifier
                    )
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Go to Date",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (resolvedDate != null) colors.onPrimary else colors.onSurfaceMuted
                )
            }
        }
    }
}

// ── Gregorian input fields ────────────────────────────────────────────────────

@Composable
private fun GregorianDateFields(
    monthIdx: Int, day: String, year: String,
    onMonthChange: (Int) -> Unit,
    onDayChange:   (String) -> Unit,
    onYearChange:  (String) -> Unit
) {
    val colors = igboColors
    var monthExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = monthExpanded,
        onExpandedChange = { monthExpanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = GREG_MONTHS[monthIdx],
            onValueChange = {},
            readOnly = true,
            label = { Text("Month") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = sheetTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = monthExpanded,
            onDismissRequest = { monthExpanded = false },
            modifier = Modifier.background(colors.surfaceCard)
        ) {
            GREG_MONTHS.forEachIndexed { idx, name ->
                DropdownMenuItem(
                    text = { Text(name, color = colors.onSurface) },
                    onClick = { onMonthChange(idx); monthExpanded = false }
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = day,
            onValueChange = { if (it.length <= 2 && it.all(Char::isDigit)) onDayChange(it) },
            label = { Text("Day") },
            placeholder = { Text("1–31") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f),
            colors = sheetTextFieldColors()
        )
        OutlinedTextField(
            value = year,
            onValueChange = { if (it.length <= 4 && it.all(Char::isDigit)) onYearChange(it) },
            label = { Text("Year") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(2f),
            colors = sheetTextFieldColors()
        )
    }
}

// ── Igbo input fields ─────────────────────────────────────────────────────────

@Composable
private fun IgboDateFields(
    monthIdx: Int, day: String, aro: String,
    onMonthChange: (Int) -> Unit,
    onDayChange:   (String) -> Unit,
    onAroChange:   (String) -> Unit
) {
    val colors = igboColors
    var monthExpanded by remember { mutableStateOf(false) }
    val isUboshi = monthIdx == UBOSHI_IDX

    ExposedDropdownMenuBox(
        expanded = monthExpanded,
        onExpandedChange = { monthExpanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = IGBO_MONTH_OPTIONS[monthIdx],
            onValueChange = {},
            readOnly = true,
            label = { Text("Ọnwa (Month)") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = sheetTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = monthExpanded,
            onDismissRequest = { monthExpanded = false },
            modifier = Modifier.background(colors.surfaceCard)
        ) {
            IGBO_MONTH_OPTIONS.forEachIndexed { idx, name ->
                DropdownMenuItem(
                    text = {
                        Text(
                            name,
                            color = if (idx == UBOSHI_IDX) colors.primary else colors.onSurface,
                            fontWeight = if (idx == UBOSHI_IDX) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = { onMonthChange(idx); monthExpanded = false }
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!isUboshi) {
            OutlinedTextField(
                value = day,
                onValueChange = { if (it.length <= 2 && it.all(Char::isDigit)) onDayChange(it) },
                label = { Text("Day (1–28)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = sheetTextFieldColors()
            )
        }
        OutlinedTextField(
            value = aro,
            onValueChange = { if (it.length <= 5 && it.all(Char::isDigit)) onAroChange(it) },
            label = { Text("Aro (Igbo Year)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(if (isUboshi) 1f else 2f),
            colors = sheetTextFieldColors()
        )
    }

    // Approximate Gregorian year hint
    val aroNum = aro.toIntOrNull()
    if (aroNum != null && aroNum > IgboCalendarEngine.IGBO_ERA_OFFSET) {
        Spacer(Modifier.height(4.dp))
        Text(
            "Aro $aroNum ≈ Gregorian ${aroNum - IgboCalendarEngine.IGBO_ERA_OFFSET}",
            fontSize = 11.sp,
            color = colors.onSurfaceMuted
        )
    }
}

// ── Date resolution (validation) ─────────────────────────────────────────────

private fun resolveGregorianDate(monthIdx: Int, dayStr: String, yearStr: String): LocalDate? {
    val day  = dayStr.toIntOrNull()  ?: return null
    val year = yearStr.toIntOrNull() ?: return null
    return try { LocalDate.of(year, monthIdx + 1, day) } catch (_: Exception) { null }
}

private fun resolveIgboDate(monthIdx: Int, dayStr: String, aroStr: String): LocalDate? {
    val aro = aroStr.toIntOrNull() ?: return null
    if (aro <= IgboCalendarEngine.IGBO_ERA_OFFSET) return null
    val yearBase = aro - IgboCalendarEngine.IGBO_ERA_OFFSET
    return if (monthIdx == UBOSHI_IDX) {
        IgboCalendarEngine.uboshiOmumuDate(yearBase)
    } else {
        val day = dayStr.toIntOrNull() ?: return null
        if (day < 1 || day > 28) return null
        IgboCalendarEngine.firstGregorianDateOfIgboMonth(yearBase, monthIdx)
            .plusDays((day - 1).toLong())
    }
}

// ── Text field colours matching custom theme ──────────────────────────────────

@Composable
private fun sheetTextFieldColors(): TextFieldColors {
    val colors = igboColors
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor   = colors.primary,
        unfocusedBorderColor = colors.divider,
        focusedLabelColor    = colors.primary,
        unfocusedLabelColor  = colors.onSurfaceMuted,
        focusedTextColor     = colors.onSurface,
        unfocusedTextColor   = colors.onSurface,
        cursorColor          = colors.primary
    )
}
