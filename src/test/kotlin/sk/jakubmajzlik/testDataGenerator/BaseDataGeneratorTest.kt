import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import sk.jakubmajzlik.dataGenerator.DataGenerator

class BaseDataGeneratorTest {

    @Test
    fun `generateTestData creates instance of class`() {
        class SomeClass(val nullableInt: Int?, val nullableString: String?)
        val dataGenerator = DataGenerator()
        val data = dataGenerator.generateData(SomeClass::class)

        assertTrue(data is SomeClass)
    }

    @Test
    fun `generateTestData creates instance of nested class`() {
        data class NestedClass(val id: Int = 0, val name: String = "")
        data class SomeClass(val nested: NestedClass = NestedClass())


        val dataGenerator = DataGenerator()
        val data = dataGenerator.generateData(SomeClass::class)
        assertTrue(data is SomeClass)
    }

    @Test
    fun `generateTestData handles nullable types correctly`() {
        data class NullableData(val nullableInt: Int?, val nullableString: String?)
        val dataGenerator = DataGenerator()
        val nullableData = dataGenerator.generateData(NullableData::class)
        assertTrue(nullableData is NullableData)
    }

    @Test
    fun `generateTestData handles list types correctly`() {
        data class ListData(val intList: List<Int>, val stringList: List<String>)
        val dataGenerator = DataGenerator()
        val listData = dataGenerator.generateData(ListData::class)
        assertTrue(listData is ListData)
    }

    @Test
    fun `generateTestData handles data classes with no primary constructor`() {
        class NoPrimaryConstructor {
            var id: Int = 0
            var name: String = ""

            constructor(id: Int, name: String) {
                this.id = id
                this.name = name
            }
        }
        val dataGenerator = DataGenerator()
        val noPrimaryConstructor = dataGenerator.generateData(NoPrimaryConstructor::class)
        assertTrue(noPrimaryConstructor is NoPrimaryConstructor)
    }
}