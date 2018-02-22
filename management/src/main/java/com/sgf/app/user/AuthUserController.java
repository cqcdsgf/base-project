package com.sgf.app.user;

import com.google.common.collect.Maps;
import com.sgf.app.domain.AuthRole;
import com.sgf.app.domain.AuthUser;
import com.sgf.app.role.AuthRoleService;
import com.sgf.base.constant.BaseMessageConstant;
import com.sgf.base.constant.PageConstant;
import com.sgf.base.persistence.DynamicSpecifications;
import com.sgf.base.persistence.SearchFilter;
import com.sgf.base.web.Servlets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Value("${user.default.password}")
    private String defaultPassword;

    @GetMapping("/user/toQueryList")
    public ModelAndView toQueryList(@RequestParam(value = "pageNumber", defaultValue = PageConstant.PAGENUMBER) Integer pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = PageConstant.PAGESIZE) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Sort sort = new Sort(Sort.Direction.DESC, "modifyTime");
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);

        Map<String,SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<AuthUser> spec = DynamicSpecifications.bySearchFilter(filters.values(),AuthUser.class);

        Page<AuthUser> users = authUserService.findAll(spec,pageable);

        Map<String,Object> model = Maps.newHashMap();
        model.put("page",users);

        // 将搜索条件编码成字符串，用于排序，分页的URL
        String parames =  Servlets.encodeParameterStringWithPrefix(searchParams, "search_");
        String path = request.getServletPath();
        model.put("searchParams",path.concat("?".concat(parames)));

        return new ModelAndView("/user/userList","model",model);
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
        String username = authUser.getUsername();
        AuthUser oldAuthUser = authUserService.findByUsername(username);

        if (null == oldAuthUser) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            authUser.setPassword(encoder.encode(defaultPassword));
        }else{
            redirectAttributes.addFlashAttribute( "errorMessage", username + "_用户名重复！");
            return "redirect:/user/toCreate";
        }

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
        Long id = authUser.getId();

        String username = authUser.getUsername();
        AuthUser oldAuthUser = authUserService.findByUsername(username);

        AuthUser oldUser;
        if (null != oldAuthUser) {
            if(oldAuthUser.getId() != id) {
                redirectAttributes.addFlashAttribute("errorMessage", username + "_用户名重复！");
                return "redirect:/user/toUpdate?id=" + id;
            }else{
                oldUser = oldAuthUser;
            }
        }else{
            oldUser = authUserService.findOne(id);
        }

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
