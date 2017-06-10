package betterwithaddons.interaction;

import java.util.List;

public class InteractionJEI extends Interaction {
    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setEnabled(boolean active) {}

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
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
