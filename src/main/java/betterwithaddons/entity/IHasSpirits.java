package betterwithaddons.entity;

public interface IHasSpirits {
    int getSpirits();

    void setSpirits(int n);

    default boolean canAbsorbSpirits() { return false; };

    default int absorbSpirits(int n) {
        return n;
    }
}
