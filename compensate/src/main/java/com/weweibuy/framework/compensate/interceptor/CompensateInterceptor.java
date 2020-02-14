package com.weweibuy.framework.compensate.interceptor;


import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateInfo;
import com.weweibuy.framework.compensate.core.CompensateStore;
import com.weweibuy.framework.compensate.support.CompensateAnnotationMetaDataParser;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 补偿切面
 *
 * @author durenhao
 * @date 2020/2/13 21:06
 **/
public class CompensateInterceptor implements MethodInterceptor {

    private final CompensateStore compensateStore;

    private final CompensateAnnotationMetaDataParser metaDataParser;

    public CompensateInterceptor(CompensateStore compensateStore,
                                 CompensateAnnotationMetaDataParser metaDataParser) {
        this.compensateStore = compensateStore;
        this.metaDataParser = metaDataParser;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            Compensate annotation = methodInvocation.getMethod().getAnnotation(Compensate.class);
            if (metaDataParser.shouldCompensate(annotation, e)) {
                CompensateInfo compensateInfo = metaDataParser.toCompensateInfo(annotation, methodInvocation.getThis(),
                        methodInvocation.getMethod(), methodInvocation.getArguments());
                compensateStore.saveCompensateInfo(compensateInfo);
            }
            throw e;
        }
    }
}
