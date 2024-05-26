package hypersquare.hypersquare.play;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CodeVariableScope {

    private HashMap<String, JsonObject> values = new HashMap<>();

    public void set(String name, JsonObject value) {
        values.put(name, value);
    }

    public JsonObject get(String name) {
        return values.get(name);
    }

    public void copyFrom(@NotNull CodeVariableScope old) {
        values = old.values;
    }
}
