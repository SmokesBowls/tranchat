@Singleton
class RealTranslationService @Inject constructor(
    private val translationApi: TranslationApi,
    private val translationCache: TranslationCache,
    @ApplicationContext private val context: Context
) : TranslationService {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val retryPolicy = ExponentialBackoff(maxAttempts = 3)

    override suspend fun translate(
        text: String, 
        targetLanguage: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Check cache first
            translationCache.get(text, targetLanguage)?.let {
                return@withContext Result.success(it)
            }

            // Perform translation
            val translation = retryPolicy.execute {
                translationApi.translate(text, targetLanguage)
            }

            // Cache successful translation
            translationCache.put(text, targetLanguage, translation)
            Result.success(translation)

        } catch (e: Exception) {
            Result.failure(TranslationError.ApiError(e.message ?: "Translation failed"))
        }
    }

    override suspend fun detectLanguage(text: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val language = translationApi.detectLanguage(text)
                Result.success(language)
            } catch (e: Exception) {
                Result.failure(TranslationError.LanguageDetectionError(e.message))
            }
        }

    override fun getSupportedLanguages(): List<Language> =
        translationApi.getSupportedLanguages()

    companion object {
        private const val CACHE_EXPIRY_HOURS = 24L
    }
}

sealed class TranslationError : Exception() {
    data class ApiError(override val message: String?) : TranslationError()
    data class LanguageDetectionError(override val message: String?) : TranslationError()
    data class UnsupportedLanguageError(val language: String) : TranslationError()
}

data class Language(
    val code: String,
    val name: String,
    val isSupported: Boolean = true
)