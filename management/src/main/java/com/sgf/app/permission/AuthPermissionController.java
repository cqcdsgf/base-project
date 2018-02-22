package com.sgf.app.permission;

import com.google.common.collect.Maps;
import com.sgf.app.domain.AuthPermission;
import com.sgf.base.constant.BaseMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Created by sgf on 2018/2/11.
 */
@Controller
//@RequestMapping("/permission")
public class AuthPermissionController {
    private static final Logger logger = LoggerFactory.getLogger(AuthPermissionController.class);

    @Autowired
    private AuthPermissionService authPermissionService;

    @GetMapping("/permission/toQueryList")
    public ModelAndView toQueryList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "15") Integer size) {
        /*List<AuthPermission> permissions = authPermissionService.findAll(new Sort(Sort.Direction.DESC, "modifyTime"));*/

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        Page<AuthPermission> permissions = authPermissionService.findAll(pageable);

        return new ModelAndView("/permission/permissionList", "permissions", permissions);
    }

    @GetMapping("/permission/toCreate")
    public ModelAndView toCreate() {
        AuthPermission permission = new AuthPermission();

        return new ModelAndView("/permission/permissionCreate", "permission", permission);
    }

    @PostMapping("/permission/create")
    public String createRole(AuthPermission authPermission, RedirectAttributes redirectAttributes) {
        authPermissionService.save(authPermission);

        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE, "权限创建成功！");
        return "redirect:/permission/toQueryList";
    }

    @GetMapping("/permission/toUpdate")
    public ModelAndView toUpdate(@RequestParam Long id,@ModelAttribute("errorMessage")String  errorMessage) {
        Map<String,Object> model = Maps.newHashMap();

        AuthPermission permission = authPermissionService.findOne(id);
        model.put("permission",permission);
        model.put(BaseMessageConstant.ERROR_MESSAGE,errorMessage);

        return new ModelAndView("permission/permissionUpdate",model);
    }

    @PostMapping("/permission/update")
    public String update(AuthPermission authPermission,RedirectAttributes redirectAttributes) {
        authPermissionService.save(authPermission);

        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE, "权限修改成功！");
        return "redirect:/permission/toQueryList";
    }

    @PostMapping("/permission/delete")
    public String delete(@RequestParam long id,RedirectAttributes redirectAttributes){
        try {
            authPermissionService.delete(id);
        }
        catch (DataIntegrityViolationException exception){
            String tip = "删除ID为_"+id + "_的权限时，其它角色拥有此权限，请先解除关联关系后再进行删除权限操作！";
            logger.error(tip);
            redirectAttributes.addFlashAttribute(BaseMessageConstant.ERROR_MESSAGE, tip);
            return "redirect:/permission/toUpdate?id="+id;
        }
        catch (Exception exception){
            String tip = "删除权限时，_ "+id + "_权限删除失败！";
            logger.error(tip);
            return "redirect:/permission/toUpdate?id="+id;
        }

        return "redirect:/permission/toQueryList";
    }

    @ResponseBody
    @PostMapping("/permission/deleteByAjax")
    public Map<String,Object> deleteByAjax(@RequestParam long id){
        Map<String,Object> result = Maps.newHashMap();
        try {
            authPermissionService.delete(id);
        }
        catch (DataIntegrityViolationException exception){
            String tip = "删除ID为_\"+id + \"_的权限时，其它角色拥有此权限，请先解除关联关系后再进行删除权限操作！";
            result.put(BaseMessageConstant.ERROR_MESSAGE,tip);
            logger.error(tip);
            return result;
        }
        catch (Exception exception){
            String tip = "删除权限时，_ "+id + "_权限删除失败！";
            result.put(BaseMessageConstant.ERROR_MESSAGE,tip);
            logger.error(tip);
            return result;
        }

        result.put(BaseMessageConstant.MESSAGE,"删除权限成功！");
        return result;
    }

}
