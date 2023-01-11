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
    OZ("""oz|ounce|ounces""".toRegex(), "ounce", "ounces", 28.3495, "g")
}

fun unitsTo(unitValue: Double, unit: Double): Double {
    return unitValue * unit
}

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val action = readln()
        if (action == "exit") return
        val (unitValue, fromUnit, operator, toUnit) = action.split(" ").map { it.lowercase() }
        var unit: Units? = null
        var unit2: Units? = null

        Units.values().forEach {
            if (fromUnit.matches(it.unit)) unit = it
            if (toUnit.matches(it.unit)) unit2 = it
        }

        if (unit == null && unit2 == null) {
            println("Conversion from ??? to ??? is impossible")
            continue
        }
        if (unit2 == null) {
            println("Conversion from ${unit?.pluralName} to ??? is impossible")
            continue
        }
        if (unit == null) {
            println("Conversion from ??? to ${unit2?.pluralName} is impossible")
            continue
        }
        if (unit?.type != unit2?.type) {
            println("Conversion from ${unit?.pluralName} to ${unit2?.pluralName} is impossible")
            continue
        }

        val result = unitsTo(unitValue.toDouble(), unit!!.converter) / unit2!!.converter
        println(
            "${unitValue.toDouble()} ${
                if (unitValue.toDouble() == 1.0) unit?.singularName else unit?.pluralName
            } is " + "$result ${if (result == 1.0) unit2?.singularName else unit2?.pluralName}"
        )
    }
}
