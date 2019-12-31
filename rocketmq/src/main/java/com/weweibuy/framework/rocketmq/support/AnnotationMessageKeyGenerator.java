/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;

/**
 * MessageKeyGenerator 默认实现
 *
 * @author durenhao
 * @date 2019/12/31 17:35
 **/
public class AnnotationMessageKeyGenerator implements MessageKeyGenerator {

    @Override
    public String generatorKey(RocketMethodMetadata metadata, Object... args) {
        int keyIndex = metadata.getKeyIndex();
        if (keyIndex != -1) {
            return args[keyIndex].toString();
        }
        return "";
    }
}
