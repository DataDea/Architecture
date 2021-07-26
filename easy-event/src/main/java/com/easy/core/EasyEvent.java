package com.easy.core;

import java.util.EventObject;

/**
 * @author yanghai
 * @since 2021/7/8 23:48
 */
public abstract class EasyEvent extends EventObject {

    private static final long serialVersionUID = 2655413181304470679L;

    private final boolean fair;

    public EasyEvent() {
        this(false);
    }

    public EasyEvent(boolean fair) {
        super(System.currentTimeMillis());
        this.fair = fair;
    }

    public boolean isFair() {
        return fair;
    }

    public long getCreateTime() {
        return (Long) getSource();
    }
}
