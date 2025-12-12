package funny.abbas.sokoban.util

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val message: String, val data: T? = null) : Resource<T>
    data object Loading : Resource<Nothing>
    data object IDLE : Resource<Nothing>
}
