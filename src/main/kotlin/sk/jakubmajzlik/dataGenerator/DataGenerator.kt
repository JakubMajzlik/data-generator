package sk.jakubmajzlik.dataGenerator

import sk.jakubmajzlik.dataGenerator.generationStrategy.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

/**
 * A class that generates random data for a given class.
 * The class uses predefined strategies for primitive types and allows to register custom strategies for other classes.
 *
 * Example usage:
 * ```
 * val dataGenerator = DataGenerator()
 * val data = dataGenerator.generateData(Data::class)
 * ```
 *
 * @property strategies A map of strategies for primitive types.
 * @constructor Creates a new instance of DataGenerator.
 * @see GenerationStrategy
 */
class DataGenerator {

    private val strategies = mutableMapOf<KClass<*>, GenerationStrategy<*>>(
        Int::class to RandomIntGenerateStrategy(),
        Long::class to RandomLongGenerateStrategy(),
        Float::class to RandomFloatGenerateStrategy(),
        Double::class to RandomDoubleGenerateStrategy(),
        Byte::class to RandomByteGenerateStrategy(),
        Short::class to RandomShortGenerateStrategy(),
        Char::class to RandomCharGenerateStrategy(),
        Boolean::class to RandomBooleanGenerateStrategy(),
        String::class to RandomStringGenerateStrategy(),
        UUID::class to RandomUUIDGenerateStrategy()
    )

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
        @Suppress("UNCHECKED_CAST")
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
        val strategy = strategies[type.classifier as KClass<*>]
        return when {
            strategy != null -> strategy.generateData()
            type.classifier == List::class -> listOf(generateValueForType(type.arguments[0].type!!))
            type.classifier == Set::class -> setOf(generateValueForType(type.arguments[0].type!!))
            type.classifier == Map::class -> mapOf(generateValueForType(type.arguments[0].type!!) to generateValueForType(type.arguments[1].type!!))
            else -> generateData(type.classifier as KClass<*>)
        }
    }

    /**
     * Registers a custom generation strategy for a given class.
     * @param kClass The class for which the strategy should be used.
     * @param strategy The strategy to be used for the given class.
     */
    fun setGenerationStrategy(kClass: KClass<*>, strategy: GenerationStrategy<*>) {
        strategies[kClass] = strategy
    }
}
