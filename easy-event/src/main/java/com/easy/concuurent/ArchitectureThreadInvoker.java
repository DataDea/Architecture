package com.easy.concuurent;

import java.io.Serializable;

/**
 * @author yanghai
 * @since 2021/7/22 23:34
 */
public class ArchitectureThreadInvoker<T> implements Serializable {
    private static final long serialVersionUID = 3493395215321024342L;


    public final String futureId;

    public final DefaultArchitectureFuture<T> future;

    public ArchitectureThreadInvoker() {
        this("");
    }

    public ArchitectureThreadInvoker(String futureId) {
        this.future = new DefaultArchitectureFuture<>(false);
        this.futureId = futureId;
    }
}
