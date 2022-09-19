fun debugLog(vararg others: Any) {
    val stackTraceElement = Thread.currentThread().stackTrace[2]

    println("FILE :: ${stackTraceElement.fileName} :: LINE :: ${stackTraceElement.lineNumber} :: ${others.joinToString(separator = " :: ")}")
}