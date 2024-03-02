package eu.abelk.connectfive.server.service;

import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;

public interface GameService {

    JoinResponse join(JoinRequest joinRequest);

    StateResponse getState(StateRequest stateRequest);

    void step(StepRequest stepRequest);

    void disconnect(DisconnectRequest disconnectRequest);

}
