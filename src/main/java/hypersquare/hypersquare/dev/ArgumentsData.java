package hypersquare.hypersquare.dev;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

public interface ArgumentsData {
    HashMap<String, List<JsonObject>> getArguments();
}
