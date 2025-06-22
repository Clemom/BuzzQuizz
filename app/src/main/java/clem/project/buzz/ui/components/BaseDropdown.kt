package clem.project.buzz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    var query    by remember { mutableStateOf("") }

    val filtered = remember(query, options) {
        if (query.isBlank()) options
        else options.filter { toString(it).contains(query, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value           = if (expanded) query else selectedOption?.let(toString) ?: "",
            onValueChange   = {
                query = it
                expanded = true
            },
            modifier        = Modifier
                .fillMaxWidth()
                .menuAnchor(),                            // ← ancre le menu sous ce TextField
            label           = { Text(label) },
            leadingIcon     = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon    = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            colors          = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            shape           = RoundedCornerShape(8.dp),
            singleLine      = true
        )

        ExposedDropdownMenu(
            expanded        = expanded,
            onDismissRequest= { expanded = false },
            modifier        = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            filtered.forEach { option ->
                val text = toString(option)
                val isSel = option == selectedOption

                DropdownMenuItem(
                    text = {
                        Text(
                            text,
                            color = if (isSel) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onOptionSelect(option)
                        query = text
                        expanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (isSel) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.surface
                        )
                        .padding(horizontal = 16.dp)
                )
                if (option != filtered.last()) Divider()
            }
        }
    }
}
