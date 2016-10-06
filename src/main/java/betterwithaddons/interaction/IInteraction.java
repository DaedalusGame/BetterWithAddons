package betterwithaddons.interaction;

/**
 * Created by Christian on 03.08.2016.
 */
public interface IInteraction {
    boolean isActive();

    void preInit();

    void init();

    void postInit();
}
