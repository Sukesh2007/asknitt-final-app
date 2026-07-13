package com.example.final_nitt

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(text: String?, placeholder: String, isSecurity: Boolean = true,type: (String)->Unit){
    var isClick by remember {mutableStateOf(false)}

    TextField(
        value = text ?: "",
        onValueChange = {
            type(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = Color(0xFF626262),
                fontSize = 22.sp
            )
        },
        modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth().onFocusChanged{
            isClick = it.isFocused
        }.focusBorder(isClick),
        visualTransformation = if(isSecurity)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFF1F4FF), focusedContainerColor = Color(0xFFF1F4FF))

    )
}

@Composable
fun PasswordTextField(
    password: String?,
    onPasswordChange: (String) -> Unit,
    label: String = "New Password"
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = password ?: "",
        onValueChange = {
            onPasswordChange(it)
        },
        placeholder = { Text(label) },
        singleLine = true,
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),

        trailingIcon = {
            val icon =
                if (passwordVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(icon, contentDescription = "Toggle password visibility")
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFF1F4FF), focusedContainerColor = Color(0xFFF1F4FF))
    )
}

fun Modifier.focusBorder(isClick: Boolean): Modifier{
    return if(isClick){
        this.border(width = 1.2.dp, color = Color.Blue)
    }else{
        this.border(width = 0.dp, color = Color.White)
    }
}
