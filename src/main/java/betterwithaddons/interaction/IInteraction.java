package betterwithaddons.interaction;

import java.util.List;

public interface IInteraction {
    boolean isActive();

    void setEnabled(boolean active);

    List<IInteraction> getDependencies();

    List<IInteraction> getIncompatibilities();

    void preInit();

    void init();

    void postInit();
}
