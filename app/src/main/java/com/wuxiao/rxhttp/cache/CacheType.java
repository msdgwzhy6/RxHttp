package com.wuxiao.rxhttp.cache;

/**
 * 缓存目标
 */
public enum CacheType {
    Memory,
    Disk,
    MemoryAndDisk;

    public boolean supportMemory() {
        return this==Memory || this== MemoryAndDisk;
    }

    public boolean supportDisk() {
        return this==Disk || this== MemoryAndDisk;
    }

}
