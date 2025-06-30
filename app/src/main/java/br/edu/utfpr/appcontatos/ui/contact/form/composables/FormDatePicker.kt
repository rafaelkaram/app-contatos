package br.edu.utfpr.appcontatos.ui.contact.form.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.appcontatos.ui.theme.AppContatosTheme
import br.edu.utfpr.appcontatos.utils.format
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePicker(
    modifier: Modifier = Modifier,
    value: LocalDate,
    onValueChanged: (LocalDate) -> Unit,
    label: String,
    errorMessage: String = "",
    enabled: Boolean = true
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    FormTextField(
        modifier = modifier,
        value = value.format(),
        onValueChanged = {},
        label = label,
        readOnly = true,
        enabled = enabled,
        errorMessage = errorMessage,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Selecione a data"
                )
            }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = value
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = !showDatePicker },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant
                            .ofEpochMilli(it)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        onValueChanged(date)
                    }
                    showDatePicker = !showDatePicker
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormDatePickerPreview() {
    AppContatosTheme {
        var value by remember { mutableStateOf(LocalDate.now()) }
        FormDatePicker(
            modifier = Modifier.padding(20.dp),
            value = value,
            onValueChanged = { newValue ->
                value = newValue
            },
            label = "Data de nascimento"
        )
    }
}