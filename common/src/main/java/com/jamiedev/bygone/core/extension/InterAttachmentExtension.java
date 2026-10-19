package com.jamiedev.bygone.core.extension;

import com.jamiedev.bygone.core.platform.InterAttachmentType;

public interface InterAttachmentExtension{
    // java functions may be overloaded given different function arguments, so, we're safe to use these names
    default <T> T get(InterAttachmentType<T> type){ throw new IllegalStateException(); }
    default <T> boolean has(InterAttachmentType<T> type){ throw new IllegalStateException(); }
    default <T> void set(InterAttachmentType<T> type, T value){ throw new IllegalStateException(); }
    default <T> void remove(InterAttachmentType<T> type){ throw new IllegalStateException(); }
}
