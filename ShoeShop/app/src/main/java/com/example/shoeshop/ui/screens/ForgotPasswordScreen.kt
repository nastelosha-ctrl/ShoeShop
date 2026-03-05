package com.example.shoeshop.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoeshop.R
import com.example.shoeshop.ui.components.BackButton
import com.example.shoeshop.ui.components.PasswordResetAlertDialog
import com.example.shoeshop.ui.theme.AppTypography
import com.example.shoeshop.ui.theme.ShoeShopTheme
import com.example.shoeshop.ui.viewmodel.ForgotPasswordViewModel
import com.example.shoeshop.ui.viewmodel.PasswordRecoveryState
import kotlinx.coroutines.launch
import kotlin.text.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onNavigateToOtpVerification: (email: String) -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val recoveryState by viewModel.passwordRecoveryState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isEmailValid by viewModel.isEmailValid.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // Следим за состоянием
    LaunchedEffect(recoveryState) {
        when (recoveryState) {
            is PasswordRecoveryState.Success -> {
                successMessage = (recoveryState as PasswordRecoveryState.Success).message
                showSuccessDialog = true
                viewModel.resetState()
            }
            is PasswordRecoveryState.Error -> {
                val error = (recoveryState as PasswordRecoveryState.Error).message
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = error,
                        duration = SnackbarDuration.Long
                    )
                }
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // Диалог успеха
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Код отправлен") },
            text = { Text(successMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateToOtpVerification(email)
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(23.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Кнопка назад
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BackButton(onClick = onBackClick)
            }

            // Заголовок
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = AppTypography.headingRegular32,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.enter_email_to_reset),
                    style = AppTypography.subtitleRegular16,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = 54.dp)
                )
            }

            // Поле для ввода email
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.updateEmail(it) },
                placeholder = {
                    Text(
                        "example@mail.com",
                        style = AppTypography.bodyRegular14,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (email.isNotEmpty() && !isEmailValid)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.outline,
                    focusedBorderColor = if (isEmailValid)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textStyle = AppTypography.bodyRegular16,
                singleLine = true,
                isError = email.isNotEmpty() && !isEmailValid,
                supportingText = {
                    if (email.isNotEmpty() && !isEmailValid) {
                        Text(
                            text = "Введите корректный email адрес",
                            color = MaterialTheme.colorScheme.error,
                            style = AppTypography.bodyRegular12
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка отправки
            Button(
                onClick = { viewModel.recoverPassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isEmailValid && recoveryState !is PasswordRecoveryState.Loading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (recoveryState is PasswordRecoveryState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.send),
                        style = AppTypography.bodyMedium16
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ShoeShopTheme {
        ForgotPasswordScreen(
            onBackClick = {},
            onNavigateToOtpVerification = {}
        )
    }
}