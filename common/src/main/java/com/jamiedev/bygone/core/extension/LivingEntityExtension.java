package com.jamiedev.bygone.core.extension;

public interface LivingEntityExtension {
    // defaults needed cause we extend LivingEntity in some of our classes
    default void bygone$startHauntingsRise(){ throw new IllegalStateException(); }
    default boolean bygone$discardOnHauntingEnd(){ throw new IllegalStateException(); }
    default int bygone$getHauntingsFadeTicks(){ throw new IllegalStateException(); }
}
