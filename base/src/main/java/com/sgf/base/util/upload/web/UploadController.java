package com.sgf.base.util.upload.web;

import com.google.common.collect.Maps;
import com.sgf.base.utils.mapper.JsonMapper;
import com.sgf.base.util.upload.plupload.Plupload;
import com.sgf.base.util.upload.plupload.PluploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by sgf on 2018\1\30 0030.
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Value("${upload.type}")
    private String uploadType;


    @Value("${upload.path}")
    private String uploadPath;

    /**上传处理方法*/
    @PostMapping(value="/upload")
    @ResponseBody
    public String upload(Plupload plupload, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(uploadType,"上传方式不能为空！");
        Assert.notNull(uploadPath,"上传路径不能为空！");

        if(!uploadType.endsWith("local")){
            //暂时只支持local上传。
            return null;
        }

        Map<String, Object> resultMap = Maps.newHashMap();

        plupload.setRequest(request);
        //文件存储路径
        File dir = new File(uploadPath);

        logger.info("上传文件路径为： " + dir.getPath());

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
            resultMap.put("url","http://" + request.getServerName() + "/"+lastFileName);

        } catch (IllegalStateException e) {
            logger.error("上传文件时出现IllegalStateException！",e);
        } catch (IOException e) {
            logger.error("上传文件时出现IOException！",e);
        }

        return JsonMapper.nonEmptyMapper().toJson(resultMap);
    }

}
