package com.sgf.app.role;

import com.google.common.collect.Maps;
import com.sgf.app.domain.AuthPermission;
import com.sgf.app.domain.AuthRole;
import com.sgf.app.domain.AuthUser;
import com.sgf.app.permission.AuthPermissionService;
import com.sgf.base.constant.BaseMessageConstant;
import com.sgf.base.constant.PageConstant;
import com.sgf.base.persistence.DynamicSpecifications;
import com.sgf.base.persistence.SearchFilter;
import com.sgf.base.web.Servlets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sgf on 2018/2/12.
 */
@Controller
public class AuthRoleController {
    private static final Logger logger = LoggerFactory.getLogger(AuthRoleController.class);

    @Autowired
    private AuthRoleService authRoleService;

    @Autowired
    private AuthPermissionService authPermissionService;

    @GetMapping("/role/toQueryList")
    public ModelAndView toQueryList(@RequestParam(value = "pageNumber", defaultValue = PageConstant.PAGENUMBER) Integer pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = PageConstant.PAGESIZE) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Sort sort = new Sort(Sort.Direction.DESC, "modifyTime");
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);

        Map<String,SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<AuthRole> spec = DynamicSpecifications.bySearchFilter(filters.values(),AuthRole.class);

        Page<AuthRole> roles = authRoleService.findAll(spec,pageable);

        Map<String,Object> model = Maps.newHashMap();
        model.put("page",roles);

        // 将搜索条件编码成字符串，用于排序，分页的URL
        String parames =  Servlets.encodeParameterStringWithPrefix(searchParams, "search_");
        String path = request.getServletPath();
        model.put("searchParams",path.concat("?".concat(parames)));

        return new ModelAndView("/role/roleList","model",model);
    }

    @ResponseBody
    @GetMapping("/role/getPermissions")
    public List<AuthPermission> getPermissions(@RequestParam Long id){
        AuthRole authRole = authRoleService.findOne(id);
        Set<AuthPermission> selectedPermissions = authRole.getPermissions();

        List<AuthPermission> allPermissions = authPermissionService.findAll();

        for(AuthPermission authPermission:selectedPermissions){
            allPermissions.remove(authPermission);
            authPermission.setSelected(true);
            allPermissions.add(authPermission);
        }

        return allPermissions;
    }

    @GetMapping("/role/toCreate")
    public ModelAndView toCreate() {
        AuthRole authRole = new AuthRole();

        List<AuthPermission> authPermissions = authPermissionService.findAll();
        authRole.getPermissions().addAll(authPermissions);

        return new ModelAndView("/role/roleCreate", "authRole", authRole);
    }

    @PostMapping("/role/create")
    public String createRole(AuthRole authRole, @RequestParam(value = "permissionIds",required = false) List<Long> permissionIds,RedirectAttributes redirectAttributes) {
        if(!CollectionUtils.isEmpty(permissionIds)) {
            authRole.getPermissions().clear();

            List<AuthPermission> authPermissions = authPermissionService.findAll(permissionIds);

            authRole.getPermissions().addAll(authPermissions);
        }
        authRoleService.save(authRole);

        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE, "角色创建成功！");
        return "redirect:/role/toQueryList";
    }

    @GetMapping("/role/toUpdate")
    public ModelAndView toUpdate(@RequestParam Long id) {
        AuthRole authRole = authRoleService.findOne(id);

        Set<AuthPermission> selectedPermissions = authRole.getPermissions();
        List<AuthPermission> allPermissions = authPermissionService.findAll();

        for(AuthPermission authPermission:selectedPermissions){
            allPermissions.remove(authPermission);
            authPermission.setSelected(true);
            allPermissions.add(authPermission);
        }

        authRole.getPermissions().clear();
        authRole.getPermissions().addAll(allPermissions);

        return new ModelAndView("role/roleUpdate","authRole",authRole);
    }

    @PostMapping("/role/update")
    public String update(AuthRole authRole, @RequestParam(value = "permissionIds",required = false) List<Long> permissionIds,RedirectAttributes redirectAttributes) {
        if(!CollectionUtils.isEmpty(permissionIds)) {
            authRole.getPermissions().clear();

            List<AuthPermission> authPermissions = authPermissionService.findAll(permissionIds);

            authRole.getPermissions().addAll(authPermissions);
        }
        authRoleService.save(authRole);

        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE, "角色修改成功！");
        return "redirect:/role/toQueryList";
    }

    @PostMapping("/role/delete")
    public String delete(@RequestParam Long id) {
        authRoleService.delete(id);

        return "redirect:/role/toQueryList";
    }

}
