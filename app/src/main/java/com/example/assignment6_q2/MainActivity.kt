package com.example.assignment6_q2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment6_q2.ui.theme.Assignment6_Q2Theme

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment6_Q2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PolylineApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PolylineApp(modifier: Modifier = Modifier) {
// For user customization
    var polylineColor by remember { mutableStateOf(Color.Blue) }
    var polylineWidth by remember { mutableStateOf(6f) }

    var polygonColor by remember { mutableStateOf(Color(0xFF66BB6A)) }
    var polygonWidth by remember { mutableStateOf(3f) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

//    LatLng points list for polyline
    val polylinePoints = remember {
        listOf(
            LatLng(42.3505, -71.1054), // Boston University
            LatLng(42.3521, -71.1000), // Esplanade
            LatLng(42.3467, -71.0972), // Fenway Park
            LatLng(42.3503, -71.0780), // Newbury Street
            LatLng(42.3570, -71.0644)  // Boston Common
        )
    }


//    LatLng points list for polygon
    val polygonPoints = remember {
//        Park area near BU
        listOf(
            LatLng(42.3512, -71.1070),
            LatLng(42.3525, -71.1050),
            LatLng(42.3520, -71.1025),
            LatLng(42.3508, -71.1035),
            LatLng(42.3502, -71.1055)
        )
    }

// Create a CameraPositionState to set the initial map view - From Lecture 6 Examples
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(polylinePoints.first(), 16f) // Zoom to the first point
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = modifier.fillMaxSize().padding(paddingValues)) {
            // Customization controls
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                Text("Customize Polyline", style = MaterialTheme.typography.titleMedium)

                Text("Color:")
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { polylineColor = Color.Blue }, modifier = Modifier.padding(end = 4.dp)) {
                        Text("Blue")
                    }
                    Button(onClick = { polylineColor = Color.Red }, modifier = Modifier.padding(end = 4.dp)) {
                        Text("Red")
                    }
                    Button(onClick = { polylineColor = Color.Green }) {
                        Text("Green")
                    }
                }

                Text("Width: ${polylineWidth.toInt()}")

                Slider(
                    value = polylineWidth,
                    onValueChange = { polylineWidth = it },
                    valueRange = 2f..12f,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text("Customize Polygon", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))

                Text("Color:")
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { polygonColor = Color(0xFF66BB6A) }, modifier = Modifier.padding(end = 4.dp)) {
                        Text("Green")
                    }
                    Button(onClick = { polygonColor = Color(0xFF26C6DA) }, modifier = Modifier.padding(end = 4.dp)) {
                        Text("Teal")
                    }
                    Button(onClick = { polygonColor = Color(0xFFFFA726) }) {
                        Text("Orange")
                    }
                }

                Text("Width: ${polygonWidth.toInt()}")

                Slider(
                    value = polygonWidth,
                    onValueChange = { polygonWidth = it },
                    valueRange = 2f..10f,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            GoogleMap(
                modifier = Modifier.fillMaxSize().weight(1f),
                cameraPositionState = cameraPositionState
            ) {
                Polyline(
                    points = polylinePoints,
                    color = polylineColor,
                    width = polylineWidth,
                    clickable = true,
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Trail: BU -> Esplanade -> Fenway -> Newbury -> Boston Common")
                        }
                    }
                )

                Polygon(
                    points = polygonPoints,
                    fillColor = polygonColor,
                    strokeColor = Color.DarkGray,
                    strokeWidth = polygonWidth,
                    clickable = true,
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Park Area: Charles River Esplanade near BU")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assignment6_Q2Theme {
        PolylineApp()
    }
}