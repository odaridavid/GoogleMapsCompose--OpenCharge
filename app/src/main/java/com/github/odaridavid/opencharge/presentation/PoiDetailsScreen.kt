package com.github.odaridavid.opencharge.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.odaridavid.opencharge.R

@Composable
fun PoiDetailsScreen(state: PoiDetailsViewState, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar() {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "backIcon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            Text(
                text = stringResource(R.string.charging_location),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h5
            )
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.weight(0.5f))
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }

        Row(Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location_pin_24),
                contentDescription = "Charging Icon",
                modifier = Modifier
                    .padding(16.dp)
            )
            Column(Modifier.fillMaxWidth()) {
                state.poi?.title?.let { text ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                }

                state.poi?.address?.addressLineOne?.let { addressLine ->
                    Text(
                        text = addressLine,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                state.poi?.address?.addressLineTwo?.let { addressLine ->
                    Text(
                        text = addressLine,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                state.poi?.address?.postCode?.let { postCode ->
                    Text(
                        text = postCode,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }

        AvailableChargingPoints(state)
    }
}

@Composable
private fun AvailableChargingPoints(state: PoiDetailsViewState) {
    state.poi?.noOfChargingPoints?.let { noOfChargingPoints ->
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_battery_charging_full_24),
                contentDescription = "Charging Icon",
                modifier = Modifier
                    .padding(16.dp)
            )
            Text(
                text = stringResource(R.string.available_charging_points, noOfChargingPoints),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
