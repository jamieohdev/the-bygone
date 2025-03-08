package com.jamiedev.bygone.common.util;

import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import org.jetbrains.annotations.Nullable;

public interface PlayerWithHook {
    @Nullable HookEntity bygone$getHook();
    void bygone$setHook(@Nullable HookEntity pHook);
}
