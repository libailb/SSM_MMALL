package libai.mmall.controller.backend;

import libai.mmall.common.Const;
import libai.mmall.common.ServiceResponse;
import libai.mmall.pojo.User;
import libai.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/user")
public class UserManagerController {
   @Autowired
   private IUserService iUserService;
@RequestMapping(value = "login.do",method = RequestMethod.POST)
@ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session){
        ServiceResponse<User> response=iUserService.login(username,password);
        if (response.isSuccess()){
              User user=response.getData();
              if(user.getRole()== Const.Role.ROLE_ADMIN){
                  //说明登录的是管理员
                  session.setAttribute(Const.CURRENT_USER,user);
                  return response;
              }else {
                  return ServiceResponse.createByErrorMessage("不是管理员，无法登陆");
              }
        }
        return response;
    }









}
