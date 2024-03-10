import java.util.*
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

/**
 * This is a simple data generator for Kotlin classes and data classes.
 * It generates random values for all properties of a given class.
 * It supports all primitive types and nested classes.
 *
 * The generator is not meant to be used in production.
 * It's mainly for testing purposes and to help you to get started with your tests.
 *
 * Example usage:
 * ```kotlin
 * val data = DataGenerator.generateTestData(SomeClass::class)
 * ```
 */
object DataGenerator {

    /**
     * Generates random data for a given class.
     * @param kClass The class for which the data should be generated.
     * @return An instance of the given class with random data.
     */
    fun <T: Any> generateData(kClass: KClass<T>): T {
        return when {
            kClass.javaPrimitiveType != null -> generatePrimitiveType(kClass)
            else -> generateObjectType(kClass)
        }
    }

    private fun <T: Any> generatePrimitiveType(kClass: KClass<T>): T {
        return generateValueForType(kClass.starProjectedType) as? T ?: error("Failed to create instance of $kClass")
    }

    private fun <T: Any> generateObjectType(kClass: KClass<T>): T {
        val constructor = kClass.constructors.first()
        val params = constructor.parameters.associateWith { parameter -> generateValueForType(parameter.type) }
        val instance = constructor.callBy(params)

        kClass.declaredMemberProperties.forEach { kProperty ->
            if (kProperty is KMutableProperty<*> && kProperty.setter.parameters.size == 2) {
                kProperty.isAccessible = true
                kProperty.setter.call(instance, generateValueForType(kProperty.returnType))
            }
        }

        return instance
    }

    private fun generateValueForType(type: KType): Any? {
        return when (type.classifier) {
            Int::class -> Random.nextInt()
            Long::class -> Random.nextLong()
            Double::class -> Random.nextDouble()
            Float::class -> Random.nextFloat()
            Boolean::class -> Random.nextBoolean()
            Char::class -> Random.nextInt(33, 127).toChar()
            Byte::class -> Random.nextBytes(1)[0]
            Short::class -> Random.nextInt(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            String::class -> UUID.randomUUID().toString()
            List::class -> listOf(generateValueForType(type.arguments[0].type!!))
            Set::class -> setOf(generateValueForType(type.arguments[0].type!!))
            Map::class -> mapOf(generateValueForType(type.arguments[0].type!!) to generateValueForType(type.arguments[1].type!!))
            else -> {
                if ((type.classifier as? KClass<*>)?.isData == true) {
                    generateData(type.classifier as KClass<*>)
                } else {
                    throw IllegalArgumentException("Cannot generate value for type: $type The type is unsupported")
                }
            }
        }
    }
}
