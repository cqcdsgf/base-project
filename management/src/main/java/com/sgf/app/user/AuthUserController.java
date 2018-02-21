package com.sgf.app.user;

import com.sgf.app.domain.AuthRole;
import com.sgf.app.domain.AuthUser;
import com.sgf.app.role.AuthRoleService;
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
 * Created by sgf on 2018\2\8 0008.
 */
@Controller
//@RequestMapping("/user")
public class AuthUserController {
    private static final Logger logger = LoggerFactory.getLogger(AuthUserController.class);

    @Autowired
    private AuthRoleService authRoleService;

    @Autowired
    private AuthUserService authUserService;

    @GetMapping("/user/toQueryList")
    public ModelAndView toQueryList() {
        List<AuthUser> users = authUserService.findAll(new Sort(Sort.Direction.DESC,"modifyTime"));

        return new ModelAndView("/user/userList","users",users);
    }

    @ResponseBody
    @GetMapping("/user/getRoles")
    public List<AuthRole> getRoles(@RequestParam Long id){
        AuthUser authUser = authUserService.findOne(id);
        Set<AuthRole> selectedRols = authUser.getRoles();

        List<AuthRole> allRoles = authRoleService.findAll();

        for(AuthRole authRole:selectedRols){
            allRoles.remove(authRole);
            authRole.setSelected(true);
            allRoles.add(authRole);
        }

        return allRoles;
    }

    @GetMapping("/user/toCreate")
    public ModelAndView toCreate() {
        AuthUser authUser = new AuthUser();

        List<AuthRole> authRoles = authRoleService.findAll();
        authUser.getRoles().addAll(authRoles);

        return new ModelAndView("/user/userCreate", "authUser", authUser);
    }

    @PostMapping("/user/create")
    public String createUser(AuthUser authUser, @RequestParam(value = "roleIds",required = false) List<Long> roleIds,RedirectAttributes redirectAttributes) {
        if(!CollectionUtils.isEmpty(roleIds)) {
            authUser.getRoles().clear();

            List<AuthRole> authRoles = authRoleService.findAll(roleIds);

            authUser.getRoles().addAll(authRoles);
        }
        authUserService.save(authUser);

        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE, "用户创建成功！");
        return "redirect:/user/toQueryList";
    }

    @GetMapping("/user/toUpdate")
    public ModelAndView toUpdate(@RequestParam Long id) {
        AuthUser authUser = authUserService.findOne(id);

        Set<AuthRole> selectedRols = authUser.getRoles();
        List<AuthRole> allRoles = authRoleService.findAll();

        for(AuthRole authRole:selectedRols){
            allRoles.remove(authRole);
            authRole.setSelected(true);
            allRoles.add(authRole);
        }
        authUser.getRoles().clear();
        authUser.getRoles().addAll(allRoles);

        return new ModelAndView("user/userUpdate","authUser",authUser);
    }

    @PostMapping("/user/update")
    public String update(AuthUser authUser, @RequestParam(value = "roleIds",required = false) List<Long> roleIds,RedirectAttributes redirectAttributes) {
        AuthUser oldUser = authUserService.findOne(authUser.getId());
        if(!CollectionUtils.isEmpty(roleIds)) {
            authUser.getRoles().clear();

            List<AuthRole> authRoles = authRoleService.findAll(roleIds);

            authUser.getRoles().addAll(authRoles);
        }
        authUser.setPassword(oldUser.getPassword());
        authUserService.save(authUser);

        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE, "用户修改成功！");
        return "redirect:/user/toQueryList";
    }

    @PostMapping("/user/delete")
    public String delete(@RequestParam Long id) {
        authUserService.delete(id);

        return "redirect:/user/toQueryList";
    }

}
