package com.jamiedev.bygone.core.extension;

public interface LivingEntityExtension {
    default void bygone$startHauntingsRise() {}
    boolean bygone$discardOnHauntingEnd();
    int bygone$getHauntingsFadeTicks();
}
