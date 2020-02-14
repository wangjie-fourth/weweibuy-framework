package com.weweibuy.framework.compensate.core;

import java.util.List;

/**
 * 补偿存储器
 *
 * @author durenhao
 * @date 2020/2/13 20:02
 **/
public interface CompensateStore {

    /**
     * 保存补偿信息
     *
     * @param compensateInfo
     */
    int saveCompensateInfo(CompensateInfo compensateInfo);

    /**
     * 查询补偿信息
     *
     * @return
     */
    List<CompensateInfo> queryCompensateInfo();

    /**
     * 更新
     *
     * @param id
     * @param compensateInfo
     * @return
     */
    int updateCompensateInfo(String id, CompensateInfo compensateInfo);

}
