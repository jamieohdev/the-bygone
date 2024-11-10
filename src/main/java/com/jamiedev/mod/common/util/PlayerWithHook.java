package com.jamiedev.mod.common.util;

import com.jamiedev.mod.common.entities.projectile.HookEntity;
import org.jetbrains.annotations.Nullable;

public interface PlayerWithHook {
    @Nullable HookEntity bygone$getHook();
    void bygone$setHook(@Nullable HookEntity pHook);
}
