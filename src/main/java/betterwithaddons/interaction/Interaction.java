package betterwithaddons.interaction;

import betterwithaddons.util.IDisableable;

import java.util.List;

public abstract class Interaction {
    IDisableable[] associatedItems;

    boolean isActive() {
        return false;
    }

    void setEnabled(boolean active) {
        for (IDisableable item : associatedItems) {
            item.setDisabled(!active);
        }
    }

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
