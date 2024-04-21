package hypersquare.hypersquare.play.error;

public enum CodeErrorType {
    RUNTIME_ERROR("Runtime error!", false),
    FAILED_CONTEXT("Failed to create context", true),
    INVALID_ACT("Invalid action", true),                              // Codeblock/Codeline data is invalid
    FAILED_TARGET("Unable to find target for codeblock", true),       // Target is invalid
    INVALID_EVENT("Invalid event", true),                             // Triggered event that doesn't exist
    MISSING_PARAM("Missing parameter", true),                         // Required parameter not specified
    INVALID_PARAM("Invalid parameter", true),                         // Valid type but invalid input
    INDEX_OUT_OF_BOUNDS("Invalid parameter, index out of bounds", true),// Valid type but invalid input, specifically for invalid indices
    CODEBLOCK_THROWN("Error thrown while executing an action", true), // Error while executing the codeblock itself
    INTERNAL_ERROR("Error thrown while executing code", true),        // Error while going through actions and triggering them
    RUN_LIMIT("Reached running threads limit", true),                 // Plot has too many threads running
    LOW_MSPT("Low world MSPT while executing code", false),           // World is lagging
    ;

    public final String message;
    public final boolean sendLoc;

    CodeErrorType(String message, boolean sendLoc) {
        this.message = message;
        this.sendLoc = sendLoc;
    }
}
