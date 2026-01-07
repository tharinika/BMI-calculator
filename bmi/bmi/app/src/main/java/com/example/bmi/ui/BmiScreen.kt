package com.example.bmi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmi.R

fun classifyBmi(bmi: Float, gender: String): Pair<String, String> {
    return if (gender == "Male") {
        when {
            bmi < 18.5 -> "Underweight" to "Increase protein, dairy, and whole grains."
            bmi in 18.5..24.9 -> "Normal" to "Maintain a balanced diet."
            bmi in 25.0..29.9 -> "Overweight" to "Reduce sugar and processed food intake."
            else -> "Obese" to "Follow a calorie-controlled diet with high fiber."
        }
    } else {
        when {
            bmi < 18.0 -> "Underweight" to "Eat more healthy carbs and protein."
            bmi in 18.0..24.0 -> "Normal" to "Maintain a well-balanced diet."
            bmi in 24.1..29.0 -> "Overweight" to "Exercise regularly and avoid junk food."
            else -> "Obese" to "Consult a nutritionist for a proper diet plan."
        }
    }
}

@Composable
fun GenderSelection(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        GenderButton("Male", selectedGender, onGenderSelected)
        GenderButton("Female", selectedGender, onGenderSelected)
    }
}

@Composable
fun GenderButton(gender: String, selectedGender: String, onGenderSelected: (String) -> Unit) {
    Button(
        onClick = { onGenderSelected(gender) },
        colors = ButtonDefaults.buttonColors(
            if (gender == selectedGender) Color.Blue else Color.Gray
        ),
        modifier = Modifier.clip(RoundedCornerShape(50))
    ) {
        Text(text = gender, color = Color.White)
    }
}

@Composable
fun AgeWeightHeightInput(
    age: Int,
    weight: String,
    height: String,
    onAgeChange: (Int) -> Unit,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Age: $age", color = Color.White)
                Row {
                    Button(onClick = { if (age > 0) onAgeChange(age - 1) }) { Text("-") }
                    Button(onClick = { onAgeChange(age + 1) }) { Text("+") }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Weight (kg)", color = Color.White)
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .width(100.dp)
                ) {
                    BasicTextField(
                        value = weight,
                        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onWeightChange(it) }
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Height (cm)", color = Color.White)
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .width(100.dp)
                ) {
                    BasicTextField(
                        value = height,
                        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onHeightChange(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun BmiScreen() {
    var selectedGender by rememberSaveable { mutableStateOf("Male") }
    var age by rememberSaveable { mutableStateOf(30) }
    var weight by rememberSaveable { mutableStateOf("") }
    var height by rememberSaveable { mutableStateOf("") }
    var bmiResult by rememberSaveable { mutableStateOf("") }
    var bmiCategory by rememberSaveable { mutableStateOf("") }
    var dietRecommendation by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bmi_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BMI CALCULATOR",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            GenderSelection(selectedGender) { selectedGender = it }
            Spacer(modifier = Modifier.height(16.dp))

            AgeWeightHeightInput(
                age = age,
                weight = weight,
                height = height,
                onAgeChange = { age = it },
                onWeightChange = { weight = it },
                onHeightChange = { height = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val weightValue = weight.toFloatOrNull() ?: -1f
                    val heightValue = height.toFloatOrNull() ?: -1f

                    if (weightValue > 0 && heightValue > 0) {
                        val bmi = weightValue / ((heightValue / 100) * (heightValue / 100))
                        bmiResult = "Your BMI: %.2f".format(bmi)

                        val (category, recommendation) = classifyBmi(bmi, selectedGender)
                        bmiCategory = category
                        dietRecommendation = recommendation
                    } else {
                        bmiResult = "Invalid input! Please enter valid weight and height."
                        bmiCategory = ""
                        dietRecommendation = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate BMI")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (bmiResult.isNotEmpty()) {
                Text(text = bmiResult, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Category: $bmiCategory", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Yellow)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Diet Recommendation: $dietRecommendation", fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.Green)
            }
        }
    }
}