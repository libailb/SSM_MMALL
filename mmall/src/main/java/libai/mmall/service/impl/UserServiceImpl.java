package libai.mmall.service.impl;

import libai.mmall.common.Const;
import libai.mmall.common.ServiceResponse;
import libai.mmall.common.TokenCache;
import libai.mmall.dao.UserMapper;
import libai.mmall.pojo.User;
import libai.mmall.service.IUserService;
import libai.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);
        if(resultCount==0){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        //密码登录MD5
        String md5Password=MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLogin(username,md5Password);
        if(user==null){
            return ServiceResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功",user);
    }

    public ServiceResponse<String> register(User user){
        ServiceResponse vaildReaponse=this.checkValid(user.getUsername(),Const.USERNAME);
        if (!vaildReaponse.isSuccess()){
            return vaildReaponse;
        }
        vaildReaponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if (!vaildReaponse.isSuccess()){
            return vaildReaponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int   resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServiceResponse.createByErrorMessage("注册失败");
        }
        return  ServiceResponse.createBySuccessMessage("注册成功");
    }
    public ServiceResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
           if(Const.USERNAME.equals(type)){
               int resultCount=userMapper.checkUsername(str);
               if(resultCount>0){
                   return ServiceResponse.createByErrorMessage("用户名已存在");
               }
           }
           if (Const.EMAIL.equals(type)){
               int resultCount=userMapper.checkEmail(str);
               if(resultCount>0){
                   return ServiceResponse.createByErrorMessage("email已存在");
               }
           }
        }else{
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccessMessage("校验成功");
    }

    public ServiceResponse<String> selectQuestion(String username){
       ServiceResponse vaildResponse=this.checkValid(username,Const.USERNAME);
       if(vaildResponse.isSuccess()){
          //用户不存在
           return ServiceResponse.createByErrorMessage("用户不存在");
       }
       String question=userMapper.selectQuestionByUsername(username);
       if (StringUtils.isNotBlank(question)){
           return ServiceResponse.createBySuccess(question);
       }
       return ServiceResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServiceResponse<String> checkAnswer(String username,String question,String answer){
       int resultCount=userMapper.checkAnswer(username,question,answer);
       if(resultCount>0){
           //说明问题及问题答案正确
           String foegwtToken= UUID.randomUUID().toString();
           TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,foegwtToken);
           return ServiceResponse.createBySuccess(foegwtToken);
       }
       return ServiceResponse.createByErrorMessage("问题答案错误");
    }

    public ServiceResponse<String> forgetResetPassword(String username,String passwordNew ,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createByErrorMessage("参数错误，Token需要传递");
        }
        ServiceResponse vaildResponse=this.checkValid(username,Const.USERNAME);
        if(vaildResponse.isSuccess()){
            //用户不存在
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServiceResponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken,token)){
            String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount>0){
                return  ServiceResponse.createBySuccessMessage("修改密码成功");
            }
        }
        else {
            return ServiceResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return  ServiceResponse.createByErrorMessage("修改密码失败");
    }

public ServiceResponse<String> resetPassword (String passwordOld,String passwordNew,User user){
        //防止横向越权，要检验一下这个用户的旧密码
     int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
     if(resultCount==0){
        return ServiceResponse.createByErrorMessage("旧密码出错");
     }
     user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
     int updateCount= userMapper.updateByPrimaryKeySelective(user);
     if(updateCount>0){
       return ServiceResponse.createBySuccessMessage("密码更新成功");
      }
      return ServiceResponse.createByErrorMessage("密码更新失败");
}

public ServiceResponse<User> updateInformation(User user){
//username 不能被更新 email不能重复
int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
if(resultCount>0){
    return ServiceResponse.createByErrorMessage("email已存在，请更换email再重新尝试");
}
User updatUser=new User();
updatUser.setId(user.getId());
updatUser.setEmail(user.getEmail());
updatUser.setPhone(user.getPhone());
updatUser.setQuestion(user.getQuestion());
updatUser.setAnswer(user.getAnswer());

int updateCount=userMapper.updateByPrimaryKeySelective(updatUser);
if(updateCount>0){
    return ServiceResponse.createBySuccessMessage("更新个人信息成功");
}
return ServiceResponse.createByErrorMessage("更新个人信息失败");
}

public ServiceResponse<User> getInformation(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServiceResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);
}

//backend后台管理
    //校验是否是管理员
    public ServiceResponse checkAdminRole(User user){
        if(user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServiceResponse.createBySuccess();
        }
      return ServiceResponse.createByError();
    }





}
