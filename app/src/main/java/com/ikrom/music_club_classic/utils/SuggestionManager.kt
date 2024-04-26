package com.ikrom.music_club_classic.utils

import android.content.Context
import android.content.SharedPreferences

object SuggestionManager {

    private const val PREF_NAME = "suggestion_history"
    private const val MAX_HISTORY_SIZE = 10

    fun saveSuggestion(context: Context, suggestion: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val suggestionList = getSuggestionList(sharedPreferences)

        // Add the new suggestion
        suggestionList.add(0, suggestion)

        // Truncate the list if it exceeds the maximum size
        if (suggestionList.size > MAX_HISTORY_SIZE) {
            suggestionList.removeAt(suggestionList.size - 1)
        }

        // Save the updated suggestion list
        saveSuggestionList(editor, suggestionList)
        editor.apply()
    }

    private fun getSuggestionList(sharedPreferences: SharedPreferences): MutableList<String> {
        val suggestionList = mutableListOf<String>()
        val historyStringSet = sharedPreferences.getStringSet(PREF_NAME, setOf()) ?: setOf()

        suggestionList.addAll(historyStringSet)

        return suggestionList
    }

    private fun saveSuggestionList(editor: SharedPreferences.Editor, suggestionList: List<String>) {
        val historyStringSet = mutableSetOf<String>()
        historyStringSet.addAll(suggestionList)
        editor.putStringSet(PREF_NAME, historyStringSet)
    }

    fun getSuggestionHistory(context: Context): List<String> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return getSuggestionList(sharedPreferences)
    }

    fun clearSuggestionHistory(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
