package de.twisted.examshare.util.validation

enum class ErrorModeConstant(val mode: ErrorMode) {
    None(ErrorMode.None),
    OnUserInput(ErrorMode.OnUserInput),
    Always(ErrorMode.Always)
}

sealed class ErrorMode {
    object None : ErrorMode()
    object OnUserInput : ErrorMode()
    object Always : ErrorMode()

    class Once(val nextMode: ErrorMode) : ErrorMode()
}