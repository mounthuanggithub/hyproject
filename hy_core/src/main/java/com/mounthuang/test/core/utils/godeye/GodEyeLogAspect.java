package com.mounthuang.test.core.utils.godeye;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
public class GodEyeLogAspect {
    @Pointcut(value = "within(@com.mounthuang.test.core.utils.godeye.GodEyeLog *) "
            + "|| @annotation(com.mounthuang.test.core.utils.godeye.GodEyeLog)")
    public void godEyeLogPoint() {
    }

    @Around(value = "godEyeLogPoint()")
    public Object godEyeLogPointAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String className = pjp.getTarget().getClass().getSimpleName();
        try {
            LoggerFactory.getLogger("GodEye").info(className + "-" + method.getName()
                    + ", input--> " + JSON.toJSONString(pjp.getArgs()));
        } finally {
            Object ret = pjp.proceed();
            try {
                LoggerFactory.getLogger("GodEye").info(className + "-" + method.getName()
                        + ", output==> " + JSON.toJSONString(ret));
            } finally {
                return ret;
            }
        }
    }
}
