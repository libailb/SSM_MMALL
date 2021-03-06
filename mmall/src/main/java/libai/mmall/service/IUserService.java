package libai.mmall.service;

import libai.mmall.common.ServiceResponse;
import libai.mmall.pojo.User;

public interface IUserService {
    ServiceResponse<User> login(String username, String password);
    ServiceResponse<String> register(User user);
    ServiceResponse<String> checkValid(String str,String type);
    ServiceResponse<String> selectQuestion(String username);
    ServiceResponse<String> checkAnswer(String username,String question,String answer);
    ServiceResponse<String> forgetResetPassword(String username,String passwordNew ,String forgetToken);
    ServiceResponse<String> resetPassword (String passwordOld,String passwordNew,User user);
    ServiceResponse<User> updateInformation(User user);
    ServiceResponse<User> getInformation(Integer userId);
    ServiceResponse checkAdminRole(User user);
}
