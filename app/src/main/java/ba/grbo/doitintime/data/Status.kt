package ba.grbo.doitintime.data

enum class Status(val identifier: String) {
    Active("Active"),
    Completed("Completed"),
    OnHold("On hold");

    companion object {
        fun valueOf(identifier: String) = when (identifier) {
            "Active" -> Active
            "Completed" -> Completed
            "On hold" -> OnHold
            else -> throw IllegalArgumentException("Unknown identifier: $identifier")
        }
    }
}