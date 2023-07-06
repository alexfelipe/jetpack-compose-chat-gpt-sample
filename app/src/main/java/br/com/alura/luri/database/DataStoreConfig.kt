package br.com.alura.luri.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "settings")

val OPEN_AI_KEY = stringPreferencesKey("OPEN_AI_KEY")