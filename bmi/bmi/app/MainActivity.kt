package com.example.bmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = BmiDatabase.getDatabase(this)
        val repository = BmiRepository(db.bmiDao())
        val viewModel: BmiViewModel by viewModels { BmiViewModelFactory(repository) }

        setContent {
            BmiScreen(viewModel)
        }
    }
}

@Composable
fun BmiScreen(viewModel: BmiViewModel) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf("") }
    val bmiRecords by viewModel.bmiRecords.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("BMI Calculator", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (m)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val weightValue = weight.toFloatOrNull()
                val heightValue = height.toFloatOrNull()
                if (weightValue != null && heightValue != null) {
                    val bmi = weightValue / (heightValue * heightValue)
                    bmiResult = "Your BMI: %.2f".format(bmi)
                    viewModel.insertBmi(weightValue, heightValue)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate BMI")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = bmiResult, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Previous Records", style = MaterialTheme.typography.headlineSmall)
        
        LazyColumn {
            items(bmiRecords) { record ->
                Text("Weight: ${record.weight}kg, Height: ${record.height}m, BMI: ${record.bmi}")
            }
        }
    }
}
