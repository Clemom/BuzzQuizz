package clem.project.buzz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BaseDropdown(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    toString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    // Filtre selon la saisie
    val filtered = remember(query, options) {
        if (query.isBlank()) options
        else options.filter { toString(it).contains(query, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        // Champ texte éditable + icônes
        OutlinedTextField(
            value = if (expanded) query else selectedOption?.let(toString) ?: "",
            onValueChange = {
                query = it
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(label) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            singleLine = true
        )

        // Menu déroulant standard Material3
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            filtered.forEach { option ->
                DropdownMenuItem(
                    text = { Text(toString(option)) },
                    onClick = {
                        onOptionSelect(option)
                        query = toString(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
