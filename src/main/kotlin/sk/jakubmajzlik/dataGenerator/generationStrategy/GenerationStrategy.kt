package sk.jakubmajzlik.dataGenerator.generationStrategy

/**
 * A functional interface that represents a strategy for generating data.
 *
 * @param T The type of the generated data.
 */
fun interface GenerationStrategy <T> {

    /**
     * Generates data of type [T].
     * @return An instance of type T with generated data.
     */
    fun generateData(): T
}