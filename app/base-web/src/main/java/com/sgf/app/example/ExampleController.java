package com.sgf.app.example;

import com.sgf.app.example.message.Message;
import com.sgf.base.utils.plupload.Plupload;
import com.sgf.base.utils.plupload.PluploadUtil;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

/**
 * Created by sgf on 2017/11/2.
 */
@Controller
public class ExampleController {
    public static final String FileDir = "uploadfile/";

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

    @GetMapping(value = "demoUploadImg")
    public ModelAndView demoUploadImg(){
        return new ModelAndView("example/demo-upload-img","message","test");
    }

    @GetMapping(value = "demoUpload")
    public ModelAndView demoUpload(){
        return new ModelAndView("example/demo-plupload","message","test");
    }

    /**上传处理方法*/
    @PostMapping(value="/upload")
    public String upload(Plupload plupload, HttpServletRequest request, HttpServletResponse response) {

        //System.out.println(plupload.getChunk() + "===" + plupload.getName() + "---" + plupload.getChunks());

        plupload.setRequest(request);
        //文件存储路径
        File dir = new File(plupload.getRequest().getSession().getServletContext().getRealPath("/") + FileDir);

        System.out.println(dir.getPath());

        try {
            //上传文件
            PluploadUtil.upload(plupload, dir);
            //判断文件是否上传成功（被分成块的文件是否全部上传完成）
            if (PluploadUtil.isUploadFinish(plupload)) {
                System.out.println(plupload.getName() + "----");
            }

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "login.upload";
    }
}
