package com.example.banktraining.utils

fun List<Map<String, Any>>.toAsciiTable(): String {
    if (isEmpty()) return "No data available"

    val columns = first().keys
    val colWidths = columns.associateWith { col ->
        maxOf(col.length, this.maxOf { row -> row[col]?.toString()?.length ?: 0 })
    }

    return buildString {
        // Header
        append("|")
        columns.forEach { col ->
            append(" ${col.padEnd(colWidths[col]!!)} |")
        }
        append("\n")

        // Separator
        append("|")
        columns.forEach { col ->
            append("-".repeat(colWidths[col]!! + 2))
            append("|")
        }
        append("\n")

        // Rows
        this@toAsciiTable.forEach { row ->
            append("|")
            columns.forEach { col ->
                append(" ${(row[col]?.toString() ?: "null").padEnd(colWidths[col]!!)} |")
            }
            append("\n")
        }
    }
}