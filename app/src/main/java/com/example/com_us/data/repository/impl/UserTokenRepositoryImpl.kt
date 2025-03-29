package com.example.com_us.data.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.com_us.base.data.BaseResponse
import com.example.com_us.data.model.auth.LoginResponse
import com.example.com_us.data.repository.UserTokenRepository
import com.example.com_us.data.source.TokenDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class UserTokenRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tokenDatasource : TokenDataSource,
    @ApplicationContext val context: Context,
    ) : UserTokenRepository{

    private object PreferencesKeys {
        val REFRESH_KEY = stringPreferencesKey("refresh_key")
        val ACCESS_KEY = stringPreferencesKey("access_key")

    }

    override suspend fun reissueToken(body: LoginResponse) : BaseResponse<LoginResponse> {
        return tokenDatasource.reissueToken(body)
    }

    override suspend fun saveRefreshToken(refreshToken : String) {
        dataStore.edit { preferences->
            preferences[PreferencesKeys.REFRESH_KEY] = refreshToken

        }
    }

    override suspend fun saveAccessToken(accessToken : String){
        dataStore.edit { preferences->
            preferences[PreferencesKeys.ACCESS_KEY] = accessToken
        }
    }

    override suspend fun getAccessToken(): Flow<String> {
       return  dataStore.data.map {
            it[PreferencesKeys.ACCESS_KEY] ?: ""
        }
    }

    override suspend fun getRefreshToken(): Flow<String> {
        return  dataStore.data.map {
            it[PreferencesKeys.REFRESH_KEY] ?: ""
        }
    }

    override val tokenPreferencesFlow : Flow<LoginResponse> = dataStore.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }
        .map { preferences ->
            val refreshToken = preferences[PreferencesKeys.REFRESH_KEY]?: ""
            val accessToken = preferences[PreferencesKeys.ACCESS_KEY]?: ""
                LoginResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

        }
}