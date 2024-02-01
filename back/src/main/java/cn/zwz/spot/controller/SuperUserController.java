package cn.zwz.spot.controller;

import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.security.SecurityUserDetails;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.Role;
import cn.zwz.data.entity.User;
import cn.zwz.data.entity.UserRole;
import cn.zwz.data.service.IRoleService;
import cn.zwz.data.service.IUserRoleService;
import cn.zwz.data.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "网站登录管理接口")
@RequestMapping("/zwz/superUser")
@Transactional
public class SuperUserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有用户")
    public Result<List<User>> getAll() {
        return new ResultUtil<List<User>>().setData(iUserService.list());
    }

    @RequestMapping(value = "/loginOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "网站前台登陆")
    public Result<String> loginOnWeb(@RequestParam String userName, @RequestParam String password){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",userName);
        List<User> userList = iUserService.list(qw);
        if(userList.size() < 1) {
            return ResultUtil.error("用户不存在");
        }
        User user = userList.get(0);
        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return ResultUtil.error("密码不正确");
        }
        String accessToken = securityUtil.getToken(user.getUsername(), true);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new SecurityUserDetails(user), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResultUtil<String>().setData(accessToken);
    }

    @RequestMapping(value = "/reOnWeb", method = RequestMethod.GET)
    @ApiOperation(value = "网站前台注册")
    public Result<String> reOnWeb(@RequestParam String userName,@RequestParam String mobile,@RequestParam String password){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",userName);
        qw.and(wrapper -> wrapper.eq("username", userName).or().eq("mobile",mobile));
        Long userCount = iUserService.count(qw);
        if(userCount > 0) {
            return ResultUtil.error("用户已存在");
        }
        User u = new User();
        u.setUsername(userName);
        u.setNickname(userName);
        u.setMobile(mobile);
        u.setEmail(u.getMobile() + "@qq.com");
        String encryptPass = new BCryptPasswordEncoder().encode(password);
        u.setPassword(encryptPass).setType(0);
        iUserService.saveOrUpdate(u);
        QueryWrapper<Role> roleQw = new QueryWrapper<>();
        roleQw.eq("default_role",true);
        List<Role> roleList = iRoleService.list(roleQw);
        if(roleList.size() > 0){
            for(Role role : roleList) {
                iUserRoleService.saveOrUpdate(new UserRole().setUserId(u.getId()).setRoleId(role.getId()));
            }
        }
        String accessToken = securityUtil.getToken(u.getUsername(), true);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new SecurityUserDetails(u), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResultUtil<String>().setData(accessToken);
    }

    @RequestMapping(value = "/getMyInfo", method = RequestMethod.GET)
    @ApiOperation(value = "查询我的信息")
    public Result<User> getMyInfo(){
        return new ResultUtil<User>().setData(iUserService.getById(securityUtil.getCurrUser().getId()));
    }

    @RequestMapping(value = "/setMyInfo", method = RequestMethod.GET)
    @ApiOperation(value = "查询我的信息")
    public Result<User> setMyInfo(@RequestParam String id,@RequestParam String nickname,@RequestParam String mobile,@RequestParam String sex,@RequestParam String address,@RequestParam String email){
        User user = iUserService.getById(id);
        if(user == null) {
            return ResultUtil.error("用户不存在");
        }
        user.setNickname(nickname);
        user.setMobile(mobile);
        user.setSex(sex);
        user.setAddress(address);
        user.setEmail(email);
        iUserService.saveOrUpdate(user);
        return new ResultUtil<User>().setData(user);
    }
}
