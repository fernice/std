package modern.std

sealed class Option<out T> {

    abstract fun isSome(): Boolean

    abstract fun isNone(): Boolean

    abstract fun expect(message: String): T

    abstract fun <R> map(mapper: (T) -> R): Option<R>

    abstract fun <R> andThen(mapper: (T) -> Option<R>): Option<R>
}

object None : Option<Nothing>() {

    override fun isSome(): Boolean {
        return false
    }

    override fun isNone(): Boolean {
        return true
    }

    override fun expect(message: String): Nothing {
        throw IllegalStateException(message)
    }

    override fun toString(): String {
        return "None"
    }

    override fun <R> map(mapper: (Nothing) -> R): Option<R> {
        return None
    }

    override fun <R> andThen(mapper: (Nothing) -> Option<R>): Option<R> {
        return None
    }

    override fun equals(other: Any?): Boolean {
        return other is None
    }

    override fun hashCode(): Int {
        return 17
    }
}

data class Some<T>(val value: T) : Option<T>() {

    override fun isSome(): Boolean {
        return true
    }

    override fun isNone(): Boolean {
        return false
    }

    override fun expect(message: String): T {
        return value
    }

    override fun toString(): String {
        return "Some($value)"
    }

    override fun <R> map(mapper: (T) -> R): Option<R> {
        return Some(mapper(value))
    }

    override fun <R> andThen(mapper: (T) -> Option<R>): Option<R> {
        return mapper(value)
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