package betterwithaddons.interaction;

import java.util.List;

public abstract class Interaction {
    boolean isActive() {
        return false;
    }

    void setEnabled(boolean active) {}

    List<Interaction> getDependencies() {
        return null;
    }

    List<Interaction> getIncompatibilities() {
        return null;
    }

    void preInit() {}

    void preInitEnd() {}

    void init() {}

    void postInit() {}
}
