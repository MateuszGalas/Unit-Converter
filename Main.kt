package converter

enum class Units(val unit: Regex, val singularName: String, val pluralName: String, val converter: Double) {
    KM("""km|kilometer(s?)""".toRegex(), "kilometer", "kilometers", 1000.0),
    M("""m|meter(s?)""".toRegex(), "meter", "meters", 1.0),
    MM("""mm|millimeter(s?)""".toRegex(), "millimeter", "millimeters",0.001),
    CM("""cm|centimeter(s?)""".toRegex(), "centimeter", "centimeters", 0.01),
    MI("""mi|mile(s?)""".toRegex(), "mile", "miles",1609.35),
    YD("""yd|yard(s?)""".toRegex(), "yard", "yards",0.9144),
    IN("""in|inch(es)?""".toRegex(), "inch", "inches", 0.0254),
    FT("""ft|foot|feet""".toRegex(), "foot", "feet", 0.3048),
}

fun unitsToMeters(name: String, nameTo: String, unitValue: Double, unit: Double) {
    println("$unitValue $name is ${unitValue * unit} " + if (unitValue * unit == 1.0) nameTo else "${nameTo}s")
}

fun main() {
    print("Enter a number and a measure of length: ")
    val (unitValue, action) = readln().split(" ").map { it.lowercase() }

    Units.values().forEach {
        when {
            action.matches(it.unit) -> {
                unitsToMeters(
                    if (unitValue.toDouble() == 1.0) it.singularName else it.pluralName,
                    "meter", unitValue.toDouble(), it.converter
                )
                return
            }
        }
    }
    println("Wrong input. Unknown unit $action")
}
