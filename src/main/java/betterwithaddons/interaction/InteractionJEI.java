package betterwithaddons.interaction;

import java.util.List;

public class InteractionJEI implements IInteraction {
    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setEnabled(boolean active) {}

    @Override
    public List<IInteraction> getDependencies() {
        return null;
    }

    @Override
    public List<IInteraction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }
}
