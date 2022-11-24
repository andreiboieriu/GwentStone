package game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public interface Command {

    /**
     * @param actionsInput input
     * @return object node containing output messages
     *
     * runs action from given input
     */
    ObjectNode execute(ActionsInput actionsInput);
}
