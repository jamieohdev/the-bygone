package com.jamiedev.bygone;

import com.jamiedev.bygone.entities.projectile.HookEntity;
import org.jetbrains.annotations.Nullable;

public interface PlayerWithHook {
    @Nullable HookEntity bygone$getHook();
    void bygone$setHook(@Nullable HookEntity pHook);
}
