package com.sgf.app.permission;

import com.google.common.collect.Maps;
import com.sgf.app.domain.AuthPermission;
import com.sgf.app.domain.AuthUser;
import com.sgf.base.constant.BaseMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
    public ModelAndView toQueryList() {
        List<AuthPermission> permissions = authPermissionService.findAll(new Sort(Sort.Direction.DESC, "modifyTime"));

        return new ModelAndView("/permission/permissionList", "permissions", permissions);
    }

    @ResponseBody
    @PostMapping("/permission/create")
    public Map<String,Object> create( @RequestParam String name, @RequestParam String value, @RequestParam String url, @RequestParam String summary) {
        AuthPermission authPermission = new AuthPermission();
        authPermission.setName(name);
        authPermission.setValue(value);
        authPermission.setUrl(url);
        authPermission.setSummary(summary);
        authPermissionService.save(authPermission);

        Map<String,Object> result = Maps.newHashMap();
        result.put(BaseMessageConstant.MESSAGE,"新增权限成功！");
        return result;
    }

    @ResponseBody
    @PostMapping("/permission/update")
    public Map<String,Object> update(@RequestParam long id, @RequestParam String name, @RequestParam String value, @RequestParam String url, @RequestParam String summary) {
        AuthPermission authPermission = authPermissionService.findOne(id);
        authPermission.setName(name);
        authPermission.setValue(value);
        authPermission.setUrl(url);
        authPermission.setSummary(summary);

        authPermissionService.save(authPermission);

        Map<String,Object> result = Maps.newHashMap();
        result.put(BaseMessageConstant.MESSAGE,"修改权限成功！");
        return result;
    }

    @ResponseBody
    @PostMapping("/permission/delete")
    public Map<String,Object> delete(@RequestParam long id){
        authPermissionService.delete(id);

        Map<String,Object> result = Maps.newHashMap();
        result.put(BaseMessageConstant.MESSAGE,"删除权限成功！");
        return result;
    }

}
