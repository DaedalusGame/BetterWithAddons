package betterwithaddons.config;

import java.util.Arrays;
import java.util.HashSet;

public class GreyList<T> {
    boolean isWhiteList = true;
    HashSet<T> whiteList = new HashSet<>();
    T[] defaults;

    public GreyList(T[] defaults) {
        this.defaults = defaults;
        setup(defaults);
    }

    public void setWhiteList(boolean whiteList) {
        isWhiteList = whiteList;
    }

    public void setup(T[] whiteList) {
        this.whiteList.clear();
        this.whiteList.addAll(Arrays.asList(whiteList));
    }

    public void add(T value) {
        this.whiteList.add(value);
    }

    public boolean contains(T value) {
        return whiteList.contains(value) == isWhiteList;
    }
}
