package com.sgf.app.role;

import com.sgf.app.domain.AuthPermission;
import com.sgf.app.domain.AuthRole;
import com.sgf.app.domain.AuthUser;
import com.sgf.app.permission.AuthPermissionService;
import com.sgf.base.constant.BaseMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
    public ModelAndView toQueryList() {
        List<AuthRole> roles = authRoleService.findAll(new Sort(Sort.Direction.DESC,"modifyTime"));

        return new ModelAndView("/role/roleList","roles",roles);
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
