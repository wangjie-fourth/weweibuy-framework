package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.annotation.Compensate;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2020/2/16 11:23
 **/
public class CompensateCachedExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<ExpressionKey, Expression> bizIdExpressionCache = new ConcurrentHashMap<>();


    public String evaluatorBizId(Compensate annotation, Object target, Method method, Object[] args) {
        Expression expression = getExpression(bizIdExpressionCache, new AnnotatedElementKey(method, target.getClass()), annotation.bizId());
        Object value = expression.getValue(createEvaluationContext(target, method, args));
        return value.toString();
    }


    private EvaluationContext createEvaluationContext(Object target, Method method, Object[] args) {

        CompensateExpressionRootObject root = new CompensateExpressionRootObject(args, method, target);
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(
                root, method, args, getParameterNameDiscoverer());
        return evaluationContext;
    }

}