package org.d3if3143.assesment1.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3143.assesment1.R
import org.d3if3143.assesment1.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.volt),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(80.dp)
                                .padding(start = 20.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.app_name),
                            tint = Color(0xFFE7BC45),
                        )
                    }
                },
            )
        },
        containerColor = Color(0xFFF4F3F3)
    ) { padding ->
        ArunoScreenContent(modifier = Modifier.padding(padding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArunoScreenContent(modifier: Modifier){

    var voltage by rememberSaveable {
        mutableStateOf("")
    }
    var voltageError by rememberSaveable {
        mutableStateOf(false)
    }

    var conversion by rememberSaveable {
        mutableStateOf("")
    }

    var expandedFromUnit by rememberSaveable { mutableStateOf(false) }
    var selectedFromUnit by rememberSaveable { mutableStateOf(VoltageUnit.VOLT) }

    var expandedToUnit by rememberSaveable { mutableStateOf(false) }
    var selectedToUnit by rememberSaveable { mutableStateOf(VoltageUnit.KILOVOLT) }

    var temp by rememberSaveable {
        mutableStateOf(VoltageUnit.MEGAVOLT)
    }

    val context = LocalContext.current

    Column(
        modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = stringResource(R.string.voltage_conversion),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = voltage,
            onValueChange = {
                voltage = it
            },
            isError = voltageError,
            placeholder = {
                Text(text = stringResource(R.string.voltage))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = "")
            },
            trailingIcon = { IconPicker(isError = voltageError, unit = "") },
            supportingText = {
                ErrorHint(isError = voltageError)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.unit_voltage),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.size(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedFromUnit,
                onExpandedChange = { expandedFromUnit = !expandedFromUnit },
                modifier = Modifier.weight(2f)
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedFromUnit.toString(),
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFromUnit) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                )
                ExposedDropdownMenu(
                    expanded = expandedFromUnit,
                    onDismissRequest = { expandedFromUnit = false },
                ) {
                    VoltageUnit.entries.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.toString()) },
                            onClick = {
                                selectedFromUnit = unit
                                expandedFromUnit = false
                            },
                            enabled = unit != selectedToUnit
                        )
                    }
                }
            }

            IconButton(onClick = {
                temp = selectedFromUnit
                selectedFromUnit = selectedToUnit
                selectedToUnit = selectedFromUnit
            }) {
                Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = "", modifier = Modifier.weight(1f))
            }

            ExposedDropdownMenuBox(
                expanded = expandedToUnit,
                onExpandedChange = { expandedToUnit = !expandedToUnit },
                modifier = Modifier.weight(2f)
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedToUnit.toString(),
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedToUnit) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                )
                ExposedDropdownMenu(
                    expanded = expandedToUnit,
                    onDismissRequest = { expandedToUnit = false },
                ) {
                    VoltageUnit.entries.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.toString()) },
                            onClick = {
                                selectedToUnit = unit
                                expandedToUnit = false
                            },
                            enabled = unit != selectedToUnit
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = {
            conversion = convertVoltage(
                selectedFromUnit,
                selectedToUnit,
                voltage.toDouble()
            )
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE7BC45),
                contentColor = Color.Black
            )
            ) {
            Text(text = stringResource(R.string.calculate_conversion))
        }
        Spacer(modifier = Modifier.size(20.dp))

       if(conversion != "" && conversion != "0"){
           Text(
               text = stringResource(R.string.conversition_result, conversion),
               modifier = Modifier.fillMaxWidth(),
               textAlign = TextAlign.Center
           )
           Spacer(modifier = Modifier.size(10.dp))
           Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp)
           ){
               Button(onClick = {
                   voltage = ""
                   voltageError = false
                   selectedFromUnit = VoltageUnit.VOLT
                   selectedToUnit = VoltageUnit.KILOVOLT
                   temp = VoltageUnit.MEGAVOLT
                   conversion = ""
               }, modifier = Modifier
                   .weight(1f),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = Color(0xFFE7BC45),
                       contentColor = Color.Black
                   )) {
                   Text(text = stringResource(R.string.reset))
               }
               Button(onClick = {
                    shareData(context,
                        context.getString(
                            R.string.share_template,
                            voltage,
                            selectedFromUnit,
                            selectedToUnit,
                            conversion
                        )
                    )
               }, modifier = Modifier
                   .weight(1f), colors = ButtonDefaults.buttonColors(
                   containerColor = Color(0xFFE7BC45),
                   contentColor = Color.Black
               )) {
                   Icon(imageVector = Icons.Default.Share, contentDescription = "")
               }

           }
       }
    }
}

enum class VoltageUnit {
    VOLT,
    KILOVOLT,
    MEGAVOLT
}

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if(shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}

fun convertVoltage(fromUnit: VoltageUnit, toUnit: VoltageUnit, value: Double): String {
    val result = when {
        fromUnit == VoltageUnit.VOLT && toUnit == VoltageUnit.KILOVOLT ->
            value / 1000.0
        fromUnit == VoltageUnit.VOLT && toUnit == VoltageUnit.MEGAVOLT ->
            value / 1_000_000.0
        fromUnit == VoltageUnit.KILOVOLT && toUnit == VoltageUnit.VOLT ->
            value * 1000.0
        fromUnit == VoltageUnit.KILOVOLT && toUnit == VoltageUnit.MEGAVOLT ->
            value / 1000.0
        fromUnit == VoltageUnit.MEGAVOLT && toUnit == VoltageUnit.VOLT ->
            value * 1_000_000.0
        fromUnit == VoltageUnit.MEGAVOLT && toUnit == VoltageUnit.KILOVOLT ->
            value * 1000.0
        else ->
            throw IllegalArgumentException("Invalid unit")
    }

    val formattedResult = "%.2f".format(result) // Memformat hasil dengan dua desimal

    return when (toUnit) {
        VoltageUnit.VOLT -> "$formattedResult V"
        VoltageUnit.KILOVOLT -> "$formattedResult kV"
        VoltageUnit.MEGAVOLT -> "$formattedResult MV"
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String){
    if(isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean){
    if(isError){
        Text(text = stringResource(id = R.string.input_invalid))
    }
}


@Preview(showBackground = true)
@Composable
fun PrevAruno(){
    MainScreen(navController = rememberNavController())
}