import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BaseDataGeneratorTest {

    private data class Data(
        val id: Int = 0,
        val name: String = "",
        val isTest: Boolean = false,
        val nested: NestedData = NestedData(),
        val nestedList: MutableList<NestedData> = mutableListOf()
    )

    private data class NestedData(
        val id: Int = 0, val name: String = ""
    )

    @Test
    fun `generateTestData creates instance of class`() {
        class SomeClass(val nullableInt: Int?, val nullableString: String?)
        val data = DataGenerator.generateData(SomeClass::class)

        assertTrue(data is SomeClass)
    }

    @Test
    fun `generateTestData creates instance of Data class`() {
        val data = DataGenerator.generateData(Data::class)
        assertTrue(data is Data)
    }

    @Test
    fun `generateTestData creates instance of NestedData class`() {
        val nestedData = DataGenerator.generateData(NestedData::class)
        assertTrue(nestedData is NestedData)
    }

    @Test
    fun `generateTestData handles nullable types correctly`() {
        data class NullableData(val nullableInt: Int?, val nullableString: String?)
        val nullableData = DataGenerator.generateData(NullableData::class)
        assertTrue(nullableData is NullableData)
    }

    @Test
    fun `generateTestData handles list types correctly`() {
        data class ListData(val intList: List<Int>, val stringList: List<String>)
        val listData = DataGenerator.generateData(ListData::class)
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
        val noPrimaryConstructor = DataGenerator.generateData(NoPrimaryConstructor::class)
        assertTrue(noPrimaryConstructor is NoPrimaryConstructor)
    }
}