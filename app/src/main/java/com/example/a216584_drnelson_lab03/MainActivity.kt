package com.example.a216584_drnelson_lab03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import kotlinx.coroutines.launch

// ===== THEME =====
private val TransitPrimary = Color(0xFF1B5E20)
private val TransitSecondary = Color(0xFF4CAF50)
private val TransitTertiary = Color(0xFFE8F5E9)



@Composable
fun TransitMateTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = TransitPrimary,
        secondary = TransitSecondary,
        tertiary = TransitTertiary
    )
    MaterialTheme(colorScheme = colorScheme, content = content)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransitMateTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchInput by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                drawerContainerColor = TransitTertiary,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "TransitMate Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TransitPrimary
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = TransitPrimary) {
            HomeScreen(
                currentSearch = searchInput,
                onSearchChange = { searchInput = it },
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }
    }
}

@Composable
fun HomeScreen(
    currentSearch: String,
    onSearchChange: (String) -> Unit,
    onOpenDrawer: () -> Unit
) {
    var searchedResult by remember { mutableStateOf("") }
    var mapImage by remember { mutableIntStateOf(R.drawable.bus_map) }

    val estimatedTime = when {
        searchedResult.contains("utm", ignoreCase = true) -> "15 min"
        searchedResult.contains("larkin", ignoreCase = true) -> "25 min"
        searchedResult.contains("paradigm", ignoreCase = true) -> "10 min"
        else -> "---"
    }

    val estimatedDistance = when {
        searchedResult.contains("utm", ignoreCase = true) -> "12.4 km"
        searchedResult.contains("larkin", ignoreCase = true) -> "18.2 km"
        searchedResult.contains("paradigm", ignoreCase = true) -> "5.7 km"
        else -> "---"
    }

    // Dropdown States
    var showSavedMenu by remember { mutableStateOf(false) }
    var showNotifMenu by remember { mutableStateOf(false) }
    var showHelpMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onOpenDrawer) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
                Column {
                    Text("TransitMate", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Public transport made easy", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }

            Row {
                Box {
                    IconButton(onClick = { showSavedMenu = true }) {
                        Icon(Icons.Default.Star, contentDescription = "Saved", tint = Color.White)
                    }
                    DropdownMenu(expanded = showSavedMenu, onDismissRequest = { showSavedMenu = false }) {
                        DropdownMenuItem(text = { Text("Home (Larkin)") }, onClick = { onSearchChange("Larkin"); showSavedMenu = false })
                        DropdownMenuItem(text = { Text("College (UTM)") }, onClick = { onSearchChange("UTM"); showSavedMenu = false })
                    }
                }
                Box {
                    IconButton(onClick = { showNotifMenu = true }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notif", tint = Color.White)
                    }
                    DropdownMenu(expanded = showNotifMenu, onDismissRequest = { showNotifMenu = false }) {
                        DropdownMenuItem(text = { Text("Bus arrive in 5 mins") }, onClick = { showNotifMenu = false })
                        DropdownMenuItem(text = { Text("Update your Apps") }, onClick = { showNotifMenu = false })
                    }
                }
                Box {
                    IconButton(onClick = { showHelpMenu = true }) {
                        Icon(Icons.Default.Info, contentDescription = "Help", tint = Color.White)
                    }
                    DropdownMenu(expanded = showHelpMenu, onDismissRequest = { showHelpMenu = false }) {
                        DropdownMenuItem(text = { Text("User Guide") }, onClick = { showHelpMenu = false })
                        DropdownMenuItem(text = { Text("Contact TransitMate") }, onClick = { showHelpMenu = false })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SEARCH SECTION ---
        TextField(
            value = currentSearch,
            onValueChange = onSearchChange,
            placeholder = { Text("Where are you going?") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                searchedResult = currentSearch
                mapImage = when {
                    currentSearch.contains("utm", ignoreCase = true) -> R.drawable.map_utm
                    currentSearch.contains("larkin", ignoreCase = true) -> R.drawable.map_larkin
                    currentSearch.contains("paradigm", ignoreCase = true) -> R.drawable.map_paradigm
                    else -> R.drawable.bus_map
                }
            },

            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = TransitSecondary)
        ) {
            Text("Search")
        }

        
        if (searchedResult.isNotEmpty()) {
            Text(
                text = "Showing route to: $searchedResult",
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- MAP CARD ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = mapImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                    Surface(
                        color = Color.Black.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxSize()
                    ) {}





                    if (searchedResult.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = estimatedTime,
                                    fontWeight = FontWeight.Bold,
                                    color = TransitPrimary,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = estimatedDistance,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- QUICK ACTION BOXES ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ActionBox("Home", "🏠")
            ActionBox("Work", "🏢")
            ActionBox("Saved", "⭐")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- EXPANDABLE LIST ---
        LocationCard("Terminal Bas Larkin", "Bus every 15 mins. Platform A1-A5.")
        LocationCard("Universiti Teknologi Malaysia", "Shuttle Bus every 10 mins.")
        LocationCard("Paradigm Mall JB", "Bus Stop in front of Main Gate.")

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Powered by TransitMate",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun LocationCard(name: String, details: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
        colors = CardDefaults.cardColors(containerColor = TransitTertiary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = name, fontWeight = FontWeight.Bold, color = TransitPrimary, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null, tint = TransitPrimary
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = details, color = Color(0xFF2E7D32))
            }
        }
    }
}

@Composable
fun ActionBox(title: String, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = TransitSecondary)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(icon, fontSize = 26.sp)
            }
        }
        Text(title, fontSize = 14.sp, color = Color.White, modifier = Modifier.padding(top = 4.dp))
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransitMatePreview() {
    TransitMateTheme {
        MainAppContainer()
    }
}