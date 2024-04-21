package hypersquare.hypersquare.play;

import com.google.gson.JsonObject;
import hypersquare.hypersquare.dev.value.CodeValues;
import hypersquare.hypersquare.dev.value.impl.VariableValue;
import hypersquare.hypersquare.play.execution.ExecutionContext;

public class CodeVariable {

    public final String name;
    public final VariableValue.Scope scope;
    private ExecutionContext ctx;

    public CodeVariable(String name, VariableValue.Scope scope) {
        this.name = name;
        this.scope = scope;
    }

    public JsonObject getJson() {
        return ctx.getScope(scope).get(name);
    }

    public void setJson(JsonObject value) {
        ctx.getScope(scope).set(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(CodeValues target) {
        JsonObject json = getJson();
        if (json == null) return null;
        if (target == null) target = CodeValues.getType(json);
        if (target.isType(json)) return (T) target.realValue(target.fromJson(json));
        return (T) target.realValue(target.coerce(json));
    }

    public void set(Object obj) {
        setJson(CodeValues.toJson(obj));
    }

    public void bind(ExecutionContext ctx) {
        this.ctx = ctx;
    }
}
