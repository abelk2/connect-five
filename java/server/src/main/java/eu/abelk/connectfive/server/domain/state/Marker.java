package eu.abelk.connectfive.server.domain.state;

import lombok.Getter;

public enum Marker {

    X("x"),

    O("o"),

    EMPTY(" ");

    @Getter
    private final String text;

    Marker(String text) {
        this.text = text;
    }

}
