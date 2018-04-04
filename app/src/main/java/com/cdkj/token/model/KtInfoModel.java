package com.cdkj.token.model;

import java.math.BigDecimal;

/**空投信息
 * Created by cdkj on 2018/3/14.
 */

public class KtInfoModel {

    private BigDecimal totalCount;
    private BigDecimal useCount;
    private BigDecimal useRate;

    public BigDecimal getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getUseCount() {
        return useCount;
    }

    public void setUseCount(BigDecimal useCount) {
        this.useCount = useCount;
    }

    public BigDecimal getUseRate() {
        return useRate;
    }

    public void setUseRate(BigDecimal useRate) {
        this.useRate = useRate;
    }
}
