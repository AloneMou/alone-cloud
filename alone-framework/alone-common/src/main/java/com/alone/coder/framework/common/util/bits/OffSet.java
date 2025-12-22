package com.alone.coder.framework.common.util.bits;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建一个OFFSET,用于Bits偏移
 */
@Setter
@Getter
public class OffSet {

    /**
     * 偏移量
     */
    private int off;

    public OffSet(int off) {
        this.off = off;
    }

}
