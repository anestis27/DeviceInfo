package com.example.orion.leitourgika;

/**
 * Created by Orion on 4/1/2015.
 */
public class Space {

    private String internal;
    private String external;

    public Space() {
    }

    public Space(String internal, String external) {
        this.internal = internal;
        this.external = external;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }
}
