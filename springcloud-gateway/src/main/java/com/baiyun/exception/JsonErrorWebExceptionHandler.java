package com.baiyun.exception;

import com.baiyun.utils.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 *      ErrorWebExceptionHandler的实现，可以直接参考DefaultErrorWebExceptionHandler，
 * 甚至直接继承DefaultErrorWebExceptionHandler，覆盖对应的方法即可。这里直接把异常信息封装成下面格式的Response返回
 * {
 *   "code": 200,
 *   "message": "描述信息",
 *   "path" : "请求路径",
 *   "method": "请求方法"
 * }
 * 最后需要渲染成JSON格式：
 */
@Slf4j
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                        ResourceProperties resourceProperties,
                                        ErrorProperties errorProperties,
                                        ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 封装异常属性
     * @param request
     * @param includeStackTrace
     * @return
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        // 这里其实可以根据异常类型进行定制化逻辑
        Throwable error = super.getError(request);
        //处理自定义异常
        if(error instanceof Exception){
            //这里返回自定义的数据，存放到Map中，如msg->xxx,code->xxx,data->xxx
            Map<String,Object> errorAttributes = new HashMap<>();
            errorAttributes.put("message", error.getMessage());
            errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorAttributes.put("method", request.methodName());
            errorAttributes.put("path", request.path());
            return errorAttributes;
        }else{
            return super.getErrorAttributes(request,includeStackTrace);
        }
    }

    /**
     * 返回路由方法基于ServerResponse的对象
     * @param errorAttributes
     * @return
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * HTTP响应状态码的封装，原来是基于异常属性的status属性进行解析的
     * @param errorAttributes 错误的信息
     * @return http 状态码
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        // 这里其实可以根据errorAttributes里面的属性定制HTTP响应码
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }



}
