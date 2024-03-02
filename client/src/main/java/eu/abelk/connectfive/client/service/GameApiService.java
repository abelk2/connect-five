package eu.abelk.connectfive.client.service;

import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;

public interface GameApiService {

    JoinResponse join(JoinRequest joinRequest);

    StateResponse getState(StateRequest stateRequest);

    void step(StepRequest stepRequest);

    void disconnect(DisconnectRequest disconnectRequest);

}
