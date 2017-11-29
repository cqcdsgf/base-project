package com.sgf.app.example;

import com.google.common.collect.Maps;
import com.sgf.app.example.message.Message;
import com.sgf.base.mapper.JsonMapper;
import com.sgf.base.utils.plupload.Plupload;
import com.sgf.base.utils.plupload.PluploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by sgf on 2017/11/2.
 */
@Controller
public class ExampleController {
    private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @GetMapping(value = "demoValidate")
    public ModelAndView demoValidate(){

        return new ModelAndView("example/demo-validate","message","test");

    }

    @GetMapping(value = "demoDate")
    public ModelAndView demoDate(){
        return new ModelAndView("example/demo-datetime","message","test");
    }


    @PostMapping(value = "/demoPostDate")
    public ModelAndView demoPostDate(HttpServletRequest request, String choosedDate,String currentDate,  RedirectAttributes redirect){

        return new ModelAndView("example/demo-datetime","choosedDate","2017-11-11");
    }

    @GetMapping(value = "demoRichText")
    public ModelAndView demoRichText(){
        return new ModelAndView("example/demo-rich-text","message","test");
    }

    @GetMapping(value = "demoCity")
    public ModelAndView demoCity(){
        return new ModelAndView("example/demo-city","message","test");
    }

    @GetMapping(value = "demoUpload")
    public ModelAndView demoUploadImg(){
        return new ModelAndView("example/demo-upload","message","test");
    }

    /**上传处理方法*/
    @PostMapping(value="/localUpload")
    @ResponseBody
    public String upload(Plupload plupload, HttpServletRequest request, HttpServletResponse response) {
        String fileDir = "D:\\temp\\img";
        Map<String, Object> resultMap = Maps.newHashMap();

        //System.out.println(plupload.getChunk() + "===" + plupload.getName() + "---" + plupload.getChunks());

        plupload.setRequest(request);
        //文件存储路径
        File dir = new File(fileDir);

        logger.info("当前文件路径： " + dir.getPath());

        try {

            String lastFileName ="";
            //上传文件
            String fileName = PluploadUtil.upload(plupload, dir);

            //判断文件是否上传成功（被分成块的文件是否全部上传完成）
            if (plupload.getChunks() == -1 && PluploadUtil.isUploadFinish(plupload)) {
                logger.info("当前文件名为： " + fileName);
                lastFileName = fileName;
            }else{
                lastFileName = fileName;
            }
            resultMap.put("url","http://localhost/"+lastFileName);

        } catch (IllegalStateException e) {
            logger.error("上传文件时出现IllegalStateException！",e);
        } catch (IOException e) {
            logger.error("上传文件时出现IOException！",e);
        }

        return JsonMapper.nonEmptyMapper().toJson(resultMap);
    }
}
