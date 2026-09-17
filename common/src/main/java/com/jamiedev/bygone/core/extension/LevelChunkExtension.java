package com.jamiedev.bygone.core.extension;

import com.jamiedev.bygone.common.weather.InvertedHeightmap;

public interface LevelChunkExtension {
    default InvertedHeightmap bygone$getInvertedHeightmap() {
        return null;
    }
}
