package fernice.std

sealed class Option<out T> {

    abstract fun isSome(): Boolean

    abstract fun isNone(): Boolean
}

object None : Option<Nothing>() {

    override fun isSome(): Boolean {
        return false
    }

    override fun isNone(): Boolean {
        return true
    }

    override fun toString(): String {
        return "None"
    }
}

data class Some<T>(val value: T) : Option<T>() {

    override fun isSome(): Boolean {
        return true
    }

    override fun isNone(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Some($value)"
    }
}

inline fun <T, R> Option<T>.map(mapper: (T) -> R): Option<R> {
    return when (this) {
        is Some -> Some(mapper(this.value))
        is None -> None
    }
}

inline fun <T, R> Option<T>.andThen(mapper: (T) -> Option<R>): Option<R> {
    return when (this) {
        is Some -> mapper(this.value)
        is None -> None
    }
}

fun <T> Option<T>.expect(message: String): T {
    return when (this) {
        is Some -> this.value
        is None -> throw IllegalStateException(message)
    }
}

inline fun <T> Option<T>.let(block: (T) -> Unit) {
    if (this is Some) {
        block(this.value)
    }
}

inline fun <T> Option<T>.ifLet(block: (T) -> Unit) {
    if (this is Some) {
        block(this.value)
    }
}

inline fun <T> Option<T>.ifLet(precondition: (T) -> Boolean, block: (T) -> Unit) {
    if (this is Some && precondition(this.value)) {
        block(this.value)
    }
}

fun <T> T?.into(): Option<T> {
    return if (this != null) {
        Some(this)
    } else {
        None
    }
}