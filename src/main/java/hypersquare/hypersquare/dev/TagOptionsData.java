package hypersquare.hypersquare.dev;

import hypersquare.hypersquare.dev.value.impl.VariableValue;
import oshi.util.tuples.Pair;

import java.util.HashMap;

public interface TagOptionsData {
    HashMap<String, Pair<String, VariableValue.HSVar>> getTags();
}
