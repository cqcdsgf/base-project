package com.sgf.base.exception;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

//@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BaseErrorController extends AbstractErrorController {
    private final ErrorProperties errorProperties;
    private static final Logger logger = LoggerFactory.getLogger(BaseErrorController.class);

    public BaseErrorController(ErrorAttributes errorAttributes,
                                  ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
        this.errorProperties.setIncludeStacktrace(ErrorProperties.IncludeStacktrace.ALWAYS);
    }

    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request,HttpServletResponse response) throws Exception {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        String queryString = request.getQueryString();

        String errorCode = errorLog(model,queryString);

        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        if(modelAndView == null){
            modelAndView =  new ModelAndView("/error/error", model);
        }

        if(StringUtils.isNotBlank(queryString)) {
            modelAndView.addObject("path", model.get("path").toString().concat("?").concat(queryString));
        }
        modelAndView.addObject("errorCode",errorCode);
        return modelAndView;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) throws Exception{
        Map<String, Object> body = getErrorAttributes(request,isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        String queryString = request.getQueryString();

        String errorCode = errorLog(body,queryString);
        body.put("errorCode",errorCode);

        if(StringUtils.isNotBlank(queryString)){
            body.put("path",body.get("path").toString().concat("?").concat(queryString));
        }
        return new ResponseEntity(body, status);
    }

    private boolean isIncludeStackTrace(HttpServletRequest request,MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    private String errorLog(Map<String,Object> model,String queryString) throws IOException {
        LocalDate currentDate = new LocalDate();
        String temp = currentDate.toString("yyyy/MM/dd") + "——";
        Random random = new Random();
        String errCode = temp + random.nextInt(1000000000);

        String status = model.get("status").toString();
        String path = (String) model.get("path");
        String error = (String) model.get("error");
        String message = (String) model.get("message");
        String trace = (String) model.get("trace");

        if(StringUtils.isNotBlank(queryString)){
            path = path.toString().concat("?").concat(queryString);
        }

        logger.error("出现异常： "
                + "errCode : {} " +System.getProperty("line.separator")
                + "path : {} " +System.getProperty("line.separator")
                + "status : {} " +System.getProperty("line.separator")
                + "error : {} " +System.getProperty("line.separator")
                + "message : {} " +System.getProperty("line.separator")
                + "errCode : {} " +System.getProperty("line.separator")
                + "trace : {} " +System.getProperty("line.separator")
                ,errCode,path,status,error,message,errCode,trace);
        return errCode;
    }
}

