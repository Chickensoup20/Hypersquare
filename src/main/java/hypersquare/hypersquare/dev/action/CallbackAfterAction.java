package hypersquare.hypersquare.dev.action;

import hypersquare.hypersquare.play.CodeSelection;
import hypersquare.hypersquare.play.execution.ExecutionContext;

public interface CallbackAfterAction {

    void after(ExecutionContext ctx, CodeSelection targetSel);

}
