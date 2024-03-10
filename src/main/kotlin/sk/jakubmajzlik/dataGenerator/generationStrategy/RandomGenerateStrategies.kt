package sk.jakubmajzlik.dataGenerator.generationStrategy

import java.util.*
import kotlin.random.Random

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Int].
 */
class RandomIntGenerateStrategy: GenerationStrategy<Int> {
    override fun generateData(): Int {
        return Random.nextInt()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Long].
 */
class RandomLongGenerateStrategy: GenerationStrategy<Long> {
    override fun generateData(): Long {
        return Random.nextLong()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Float].
 */
class RandomFloatGenerateStrategy: GenerationStrategy<Float> {
    override fun generateData(): Float {
        return Random.nextFloat()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Double].
 */
class RandomDoubleGenerateStrategy: GenerationStrategy<Double> {
    override fun generateData(): Double {
        return Random.nextDouble()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Byte].
 */
class RandomByteGenerateStrategy: GenerationStrategy<Byte> {
    override fun generateData(): Byte {
        return Random.nextInt(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).toByte()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Short].
 */
class RandomShortGenerateStrategy: GenerationStrategy<Short> {
    override fun generateData(): Short {
        return Random.nextInt(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Char].
 */
class RandomCharGenerateStrategy: GenerationStrategy<Char> {
    override fun generateData(): Char {
        return Random.nextInt(Char.MIN_VALUE.code, Char.MAX_VALUE.code).toChar()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [Boolean].
 */
class RandomBooleanGenerateStrategy: GenerationStrategy<Boolean> {
    override fun generateData(): Boolean {
        return Random.nextBoolean()
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [String].
 */
class RandomStringGenerateStrategy: GenerationStrategy<String> {
    override fun generateData(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

/**
 * Implementation of [GenerationStrategy] for generating random data for primitive type [UUID].
 */
class RandomUUIDGenerateStrategy: GenerationStrategy<UUID> {
    override fun generateData(): UUID {
        return UUID.randomUUID()
    }
}