package ba.grbo.doitintime.data

enum class Status(val identifier: String) {
    Active("Active"),
    OnHold("On hold"),
    Completed("Completed");

    companion object {
        fun valueOf(identifier: String) = when (identifier) {
            "Active" -> Active
            "On hold" -> OnHold
            "Completed" -> Completed
            else -> throw IllegalArgumentException("Unknown identifier: $identifier")
        }
    }
}