package com.sxnsyh.backendseed.common.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * controller日志切面
 *
 * @author wuqian
 * @date 2019/12/26
 */
@Aspect
@Slf4j
@Component
public class ControllerLogAspect {
  /** 开始时间 */
  private LocalDateTime startTime;

  @Pointcut("execution(* com.sxnsyh.backendseed.controller..*.*(..))")
  public void controllers() {}

  @Before("controllers()")
  public void deBefore(final JoinPoint joinPoint) {

    // 接收到请求，记录请求内容
    log.info("================  Controller Log Start  ===================");
    this.startTime = LocalDateTime.now();
    final ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      final HttpServletRequest request = attributes.getRequest();
      log.info("==> Request: [{}]{}", request.getMethod(), request.getRequestURL());
    }
    log.info(
        "==>  Method: {}",
        joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName());
    log.info("==>    Args: {}", Arrays.toString(joinPoint.getArgs()));
  }

  /**
   * 后置结果返回
   *
   * @param result 结果
   */
  @AfterReturning(pointcut = "controllers()", returning = "result")
  public void doAfterReturning(final Object result) {
    // 处理请求的时间差
    final long difference = ChronoUnit.MILLIS.between(this.startTime, LocalDateTime.now());
    log.info("==>   Spend: {}s", difference / 1000.0);
    log.info("==>  Return: {}", result);
    log.info("================  Controller Log End  =====================");
  }


}
