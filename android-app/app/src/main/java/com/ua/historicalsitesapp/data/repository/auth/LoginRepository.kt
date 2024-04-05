package com.ua.historicalsitesapp.data.repository.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ua.historicalsitesapp.data.model.auth.LoggedInUser
import com.ua.historicalsitesapp.data.model.auth.LoginDetails
import com.ua.historicalsitesapp.data.model.auth.RegistrationDetails
import com.ua.historicalsitesapp.data.model.auth.RegistrationResult
import com.ua.historicalsitesapp.data.repository.auth.LoginDataSource.LogoutResult
import com.ua.historicalsitesapp.util.Result
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Class that requests authentication and user information from the remote data source and maintains
 * an in-memory cache of login status and user credentials information.
 */
class LoginRepository(
    private val dataSource: LoginDataSource,
    private val dataStore: DataStore<Preferences>,
) {
  // in-memory cache of the loggedInUser object
  var user: LoggedInUser? = null
    private set

  val isLoggedIn: Boolean
    get() = user != null

  init {
    user = runBlocking {
      return@runBlocking getAuthTokensFromDataStore()
    }
  }

  fun logout(): Boolean {
    if (user == null) {
      return false
      // or true idk
    }

    val result: LogoutResult
    runBlocking { result = dataSource.logout(user!!) }

    return when (result) {
      is LogoutResult.Success -> {
        runBlocking {
          clearCachedTokens()
          user = null
        }
        true
      }
      else -> {
        false
      }
    }
  }

  suspend fun login(userDetails: LoginDetails): Result<LoggedInUser> {
    val result = dataSource.login(userDetails)

    if (result is Result.Success) {
      setLoggedInUser(result.data)
    }

    return result
  }

  private suspend fun clearCachedTokens() {
    val datastoreAccessTokenKey = stringPreferencesKey("accessToken")
    val datastoreRefreshTokenKey = stringPreferencesKey("refreshToken")

    dataStore.edit {
      it.remove(datastoreAccessTokenKey)
      it.remove(datastoreRefreshTokenKey)
    }
  }

  suspend fun register(userDetails: RegistrationDetails): RegistrationResult {
    return dataSource.register(userDetails)
  }

  private fun setLoggedInUser(loggedInUser: LoggedInUser) {
    this.user = loggedInUser
    runBlocking { storeAuthTokens(loggedInUser) }
  }

  private suspend fun storeAuthTokens(tokens: LoggedInUser) {
    val datastoreAccessTokenKey = stringPreferencesKey("accessToken")
    val datastoreRefreshTokenKey = stringPreferencesKey("refreshToken")
    val encryptedAccessToken = encrypt(tokens.accessToken, "accessToken")
    val encryptedRefreshToken = encrypt(tokens.refreshToken, "refreshToken")

    dataStore.edit {
      it[datastoreAccessTokenKey] =
          encryptedAccessToken.let { encrypted -> Base64.encodeToString(encrypted, Base64.DEFAULT) }
      it[datastoreRefreshTokenKey] =
          encryptedRefreshToken.let { encrypted ->
            Base64.encodeToString(encrypted, Base64.DEFAULT)
          }
    }
  }

  private suspend fun getAuthTokensFromDataStore(): LoggedInUser? {
    val datastoreAccessTokenKey = stringPreferencesKey("accessToken")
    val datastoreRefreshTokenKey = stringPreferencesKey("refreshToken")

    val encryptedAccessTokenString = dataStore.data.first()[datastoreAccessTokenKey]
    val encryptedRefreshTokenString = dataStore.data.first()[datastoreRefreshTokenKey]

    if (encryptedAccessTokenString == null || encryptedRefreshTokenString == null) {
      return null
    }

    val encryptedAccessToken = Base64.decode(encryptedAccessTokenString, Base64.DEFAULT)
    val encryptedRefreshToken = Base64.decode(encryptedRefreshTokenString, Base64.DEFAULT)

    val decryptedAccessToken = decrypt(encryptedAccessToken, "accessToken")
    val decryptedRefreshToken = decrypt(encryptedRefreshToken, "refreshToken")
    // hasConfirmed = true, because the user only gets tokens if they login, and the user can only
    // login if they confirm their email
    return LoggedInUser(
        accessToken = decryptedAccessToken,
        refreshToken = decryptedRefreshToken,
        isFirstLogin = false,
        hasConfirmedEmail = true,
    )
  }

  private suspend fun storeInitBuffer(
      bytes: ByteArray,
      forProp: String,
  ) {
    val dsIvKey = stringPreferencesKey("iv$forProp")
    val base64EncodedIv = Base64.encodeToString(bytes, Base64.DEFAULT)

    dataStore.edit { it[dsIvKey] = base64EncodedIv }
  }

  private suspend fun getInitBuffer(forProp: String): ByteArray? {
    val dsIvKey = stringPreferencesKey("iv$forProp")
    val base64EncodedIv = dataStore.data.first()[dsIvKey] ?: return null

    return Base64.decode(base64EncodedIv, Base64.DEFAULT)
  }

  private fun generateSecretKey(): SecretKey {
    val secretKeyAlias = "userJwt"
    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
    val spec =
        KeyGenParameterSpec.Builder(
                secretKeyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setKeySize(256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

    keyGenerator.init(spec)
    return keyGenerator.generateKey()
  }

  private fun getSecretKey(): SecretKey? {
    val secretKeyAlias = "userJwt"
    val keyStore =
        KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            ?: throw Exception("Null keystore")

    val secretKeyEntry = keyStore.getEntry(secretKeyAlias, null) ?: return generateSecretKey()
    return (secretKeyEntry as KeyStore.SecretKeyEntry).secretKey
  }

  private fun encrypt(
      data: String,
      forProp: String,
  ): ByteArray? {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
    runBlocking { storeInitBuffer(cipher.iv, forProp) }
    return cipher.doFinal(data.toByteArray())
  }

  private fun decrypt(
      encrypted: ByteArray,
      forProp: String,
  ): String {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val iv = runBlocking { getInitBuffer(forProp) }
    val spec = GCMParameterSpec(256, iv)
    cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
    val decoded = cipher.doFinal(encrypted)
    return String(decoded, Charsets.UTF_8)
  }
}
