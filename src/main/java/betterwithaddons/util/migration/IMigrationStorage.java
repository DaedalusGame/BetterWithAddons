package betterwithaddons.util.migration;

public interface IMigrationStorage {
    Migration deserialize(String type);
}
