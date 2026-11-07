package com.example.apex.presentation.locale

import java.util.Locale

actual fun setAppLanguage(language: String) {
    Locale.setDefault(Locale.forLanguageTag(language))
}