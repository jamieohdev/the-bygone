package com.jamiedev.bygone;

public interface BygoneHeightGetter {
    int get(int heightMapHeight);

    BygoneHeightGetter PASSTHROUGH = x -> x;

    record FixedHeight(int height) implements BygoneHeightGetter {
        @Override
        public int get(int heightMapHeight) {
            return height;
        }
    }
}