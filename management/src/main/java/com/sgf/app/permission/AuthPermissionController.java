package com.sgf.app.permission;

import com.google.common.collect.Maps;
import com.sgf.app.domain.AuthPermission;
import com.sgf.base.constant.BaseMessageConstant;
import com.sgf.base.constant.PageConstant;
import com.sgf.base.persistence.DynamicSpecifications;
import com.sgf.base.persistence.SearchFilter;
import com.sgf.base.web.Servlets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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
    public ModelAndView toQueryList(@RequestParam(value = "pageNumber", defaultValue = PageConstant.PAGENUMBER) Integer pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = PageConstant.PAGESIZE) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Sort sort = new Sort(Sort.Direction.DESC, "modifyTime");
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);

        Map<String,SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<AuthPermission> spec = DynamicSpecifications.bySearchFilter(filters.values(),AuthPermission.class);

        Page<AuthPermission> permissions = authPermissionService.findAll(spec,pageable);

        Map<String,Object> model = Maps.newHashMap();
        model.put("page",permissions);

        // 将搜索条件编码成字符串，用于排序，分页的URL
        String parames =  Servlets.encodeParameterStringWithPrefix(searchParams, "search_");
        String path = request.getServletPath();
        model.put("searchParams",path.concat("?".concat(parames)));

        return new ModelAndView("/permission/permissionList", "model", model);
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
