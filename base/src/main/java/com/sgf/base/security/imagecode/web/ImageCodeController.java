package com.sgf.base.security.imagecode.web;

import com.google.common.base.Optional;
import com.sgf.base.constant.SessionConstant;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by sgf on 2018\1\19 0019.
 */
@Controller
@RequestMapping(value = "/imageCode")
public class ImageCodeController {

    private int width = 120;

    private int height = 34;

    private int codeCount = 4;

    private int xx = 20;

    private int fontHeight = 25;

    private int codeY = 28;

    private char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

    @RequestMapping(value = "/getCode")
    public void getCode(HttpServletRequest req, HttpServletResponse resp,String imageCodeType) {
        //String imageCodeType = req.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);
        Assert.notNull(imageCodeType,"校验码类型不允许为空！");

        // 长
        String w = WebUtils.findParameterValue(req, "w");
        // 宽
        String h = WebUtils.findParameterValue(req, "h");
        // 验证码个数
        String n = WebUtils.findParameterValue(req, "n");

        width = Integer.parseInt(Optional.fromNullable(w).or(String.valueOf(width)));
        height = Integer.parseInt(Optional.fromNullable(h).or(String.valueOf(height)));
        codeCount = Integer.parseInt(Optional.fromNullable(n).or(String.valueOf(codeCount)));

        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);

        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(31)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);

            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession(false);
        session.setAttribute(imageCodeType + SessionConstant.SESSION_IMAGECODE, randomCode.toString());

        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

        resp.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
