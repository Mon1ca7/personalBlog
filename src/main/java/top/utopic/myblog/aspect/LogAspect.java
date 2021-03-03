package top.utopic.myblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
//切面类 用于处理日志
public class LogAspect {
    private final Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* top.utopic.myblog.web.*.*(..))")
    //切入点，所有web层(控制层)下面的所有类中的所有方法
    public void log(){}

    //前置通知，方法执行前执行
    @Before("log()")
    //连接点joinPoint
    public void doBefore(JoinPoint joinPoint){
        //拿到requestd对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //获取类名和方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        logger.info("Reques : {}",requestLog);
    }

    //后置通知，获取目标方法的返回值
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturning(Object result){
        logger.info("result : {}",result);
    }
    //最终通知，无论如何该方法都会执行
    @After("log()")
    public void doAfter(){
        logger.info("--------doAfter--------");
    }

    //定义内部类，存储日志信息
    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
