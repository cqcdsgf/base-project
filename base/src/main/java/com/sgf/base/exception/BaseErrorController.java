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
    @Autowired
    private ApplicationContext applicationContext;

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
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
                request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());

        String stackTrace = (String) model.get("trace");
        String errorCode = errorLog(stackTrace);

        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        if(modelAndView == null){
            modelAndView =  new ModelAndView("/error/error", model);
        }
        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)) {
            modelAndView.addObject("path", model.get("path").toString().concat(queryString));
        }
        modelAndView.addObject("errorCode",errorCode);
        return modelAndView;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) throws Exception{
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);

        String stackTrace = (String) body.get("trace");
        if(StringUtils.isBlank(stackTrace)){
            stackTrace = body.get("status") + ("-").concat((String) body.get("error")).concat("-").concat((String) body.get("path"));
        }
        String errorCode = errorLog(stackTrace);
        body.put("errorCode",errorCode);
        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
        body.put("path",body.get("path").toString().concat(queryString));
        }
        return new ResponseEntity(body, status);
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    private String errorLog(String stackTrace) throws IOException {
        LocalDate currentDate = new LocalDate();
        String temp = currentDate.toString("yyyy/MM/dd") + "——";
        Random random = new Random();
        String errCode = temp + random.nextInt(1000000000);

        logger.error(errCode + " : " + stackTrace);
        return errCode;
    }
}

