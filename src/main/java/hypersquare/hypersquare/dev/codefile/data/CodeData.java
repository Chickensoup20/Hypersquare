package hypersquare.hypersquare.dev.codefile.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.dev.value.impl.VariableValue;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeData {
    public final List<CodeLineData> codelines = new ArrayList<>();

    public CodeData(JsonArray code) {
        for (JsonElement elm : code) {
            codelines.add(new CodeLineData(elm.getAsJsonObject()));
        }
    }

    public static void parseArgsAndTags(
        @NotNull JsonObject data,
        @NotNull HashMap<String, List<JsonObject>> arguments, @NotNull HashMap<String, Pair<String, VariableValue.HSVar>> tags
    ) {
        if (data.has("arguments")) {
            for (Map.Entry<String, JsonElement> argument : data.get("arguments").getAsJsonObject().entrySet()) {
                List<JsonObject> values = new ArrayList<>();
                for (JsonElement value : argument.getValue().getAsJsonArray()) {
                    values.add(value.getAsJsonObject());
                }
                arguments.put(argument.getKey(), values);
            }
        }
        if (data.has("tags")) {
            for (Map.Entry<String, JsonElement> tag : data.get("tags").getAsJsonObject().entrySet()) {
                JsonObject obj = tag.getValue().getAsJsonObject();
                VariableValue.HSVar var = null;
                if (obj.has("var")) {
                    var = (VariableValue.HSVar) CodeValues.VARIABLE.fromJson(obj.get("var").getAsJsonObject());
                }
                tags.put(tag.getKey(), new Pair<>(obj.get("value").getAsString(), var));
            }
        }
    }

    public static void argsAndTagsToJson(
        @NotNull JsonObject data,
        @NotNull HashMap<String, List<JsonObject>> arguments, @NotNull HashMap<String, Pair<String, VariableValue.HSVar>> tags
    ) {
        if (!arguments.isEmpty()) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, List<JsonObject>> entry : arguments.entrySet()) {
                JsonArray values = new JsonArray();

                for (JsonObject value : entry.getValue()) {
                    values.add(value);
                }

                obj.add(entry.getKey(), values);
            }
            data.add("arguments", obj);
        }
        if (!tags.isEmpty()) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, Pair<String, VariableValue.HSVar>> entry : tags.entrySet()) {
                JsonObject tag = new JsonObject();
                tag.addProperty("value", entry.getValue().getA());
                if (entry.getValue().getB() != null)
                    tag.add("var", CodeValues.VARIABLE.getVarItemData(entry.getValue().getB()));
                obj.add(entry.getKey(), tag);
            }
            data.add("tags", obj);
        }
    }

    public JsonArray toJson() {
        JsonArray out = new JsonArray();
        for (CodeLineData line : codelines) {
            out.add(line.toJson());
        }
        return out;
    }
}
