package converter


enum class Units(
    val unit: Regex, val singularName: String, val pluralName: String, val converter: Double, val type: String
) {
    KM("""km|kilometer(s?)""".toRegex(), "kilometer", "kilometers", 1000.0, "m"),
    M("""m|meter(s?)""".toRegex(), "meter", "meters", 1.0, "m"),
    MM("""mm|millimeter(s?)""".toRegex(), "millimeter", "millimeters", 0.001, "m"),
    CM("""cm|centimeter(s?)""".toRegex(), "centimeter", "centimeters", 0.01, "m"),
    MI("""mi|mile(s?)""".toRegex(), "mile", "miles", 1609.350, "m"),
    YD("""yd|yard(s?)""".toRegex(), "yard", "yards", 0.9144, "m"),
    IN("""in|inch(es)?""".toRegex(), "inch", "inches", 0.0254, "m"),
    FT("""ft|foot|feet""".toRegex(), "foot", "feet", 0.3048, "m"),
    G("""g|gram|grams""".toRegex(), "gram", "grams", 1.0, "g"),
    KG("""kg|kilogram|kilograms""".toRegex(), "kilogram", "kilograms", 1000.0, "g"),
    MG("""mg|milligram|milligrams""".toRegex(), "milligram", "milligrams", 0.001, "g"),
    LB("""lb|pound|pounds""".toRegex(), "pound", "pounds", 453.592, "g"),
    OZ("""oz|ounce|ounces""".toRegex(), "ounce", "ounces", 28.3495, "g"),
    F(
        """f|df|degree fahrenheit|degrees fahrenheit|fahrenheit""".toRegex(), "degree Fahrenheit",
        "degrees Fahrenheit", 32.0, "t"
    ),
    C(
        """c|dc|degree celsius|degrees celsius|celsius""".toRegex(), "degree Celsius",
        "degrees Celsius", 1.0, "t"
    ),
    K("""k|kelvin|kelvins""".toRegex(), "kelvin", "kelvins", 273.15, "t");

    companion object {
        fun getUnit(name: String): Units? =
            values().firstOrNull { unit -> unit.unit.matches(name)}
        fun temperatureTo(unitValue: Double, unit: Units?, unit2: Units?): Double {
            return when (unit to unit2) {
                F to C -> (unitValue - 32) / 1.8
                F to K -> (unitValue + 459.67) / 1.8
                C to F -> unitValue * 1.8 + 32
                C to K -> unitValue + 273.15
                K to C -> unitValue - 273.15
                K to F -> unitValue * 1.8 - 459.67
                else -> unitValue
            }
        }
    }
}

fun unitsTo(unitValue: Double, unit: Double): Double {
    return unitValue * unit
}
fun checkConversion(unit: Units?, unit2: Units?): Boolean {
    if (unit == null && unit2 == null) {
        println("Conversion from ??? to ??? is impossible")
        return true
    }
    if (unit2 == null) {
        println("Conversion from ${unit?.pluralName} to ??? is impossible")
        return true
    }
    if (unit == null) {
        println("Conversion from ??? to ${unit2.pluralName} is impossible")
        return true
    }
    if (unit.type != unit2.type) {
        println("Conversion from ${unit.pluralName} to ${unit2.pluralName} is impossible")
        return true
    }
    return false
}

fun isNegative(unit: Units?, unitValue: Double): Boolean {
    if (unit?.type == "m" && unitValue < 0) {
        println("Length shouldn't be negative")
        return true
    }
    if (unit?.type == "g" && unitValue < 0) {
        println("Weight shouldn't be negative")
        return true
    }
    return false
}

fun printResult(unitValue: Double, result: Double, unit: Units?, unit2: Units?) {
    println(
        "$unitValue ${if (unitValue == 1.0) unit?.singularName else unit?.pluralName} is " +
                "$result ${if (result == 1.0) unit2?.singularName else unit2?.pluralName}"
    )
}

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val action = readln()
        if (action == "exit") return
        if (!action.matches("""-?\d+\.?\d*.+""".toRegex())) {
            println("Parse error")
            continue
        }

        val list = action.split(" ").map { it.lowercase() }
        val unitValue = list[0]
        val sourceUnit: String
        val targetUnit: String

        if (list.size == 4) {
            sourceUnit = list[1]
            targetUnit = list[3]
        } else if (list.size == 5 && (list[1] == "degree" || list[1] == "degrees")) {
            sourceUnit = list.subList(1, 3).joinToString(" ")
            targetUnit = list[4]
        } else if (list.size == 5 && (list[3] == "degree" || list[3] == "degrees")) {
            sourceUnit = list[1]
            targetUnit = list.subList(3, 5).joinToString(" ")
        } else {
            sourceUnit = list.subList(1, 3).joinToString(" ")
            targetUnit = list.subList(4, 6).joinToString(" ")
        }

        val unit = Units.getUnit(sourceUnit)
        val unit2 = Units.getUnit(targetUnit)

        if (checkConversion(unit, unit2)) continue
        if (isNegative(unit, unitValue.toDouble())) continue

        try {
            if (unit?.type != "t") {
                val result = unitsTo(unitValue.toDouble(), unit!!.converter) / unit2!!.converter
                printResult(unitValue.toDouble(), result, unit, unit2)
            } else {
                val result = Units.temperatureTo(unitValue.toDouble(), unit, unit2)
                printResult(unitValue.toDouble(), result, unit, unit2)
            }
        } catch (e: Exception) {
            println("Parse error")
        }
        println()
    }
}
