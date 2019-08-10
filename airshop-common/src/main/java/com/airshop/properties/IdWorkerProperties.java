package com.airshop.properties;

import com.airshop.utils.IdWorker;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 15:03
 */
@ConfigurationProperties(prefix = "airshop.idworker")
@RefreshScope
public class IdWorkerProperties {

    private long workerId;

    private long dataCenterId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}
