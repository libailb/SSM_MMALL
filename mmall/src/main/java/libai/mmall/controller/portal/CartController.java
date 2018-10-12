package libai.mmall.controller.portal;
import libai.mmall.common.Const;
import libai.mmall.common.ResponseCode;
import libai.mmall.common.ServiceResponse;
import libai.mmall.pojo.User;
import libai.mmall.service.ICartService;
import libai.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;



import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }


    @RequestMapping("add.do")
    @ResponseBody
    public ServiceResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.add(user.getId(), productId, count);

    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServiceResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {

            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        }

        return iCartService.update(user.getId(), productId, count);

    }


    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServiceResponse<CartVo> deleteProduct(HttpSession session, String productIds) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {

            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        }

        return iCartService.deleteProduct(user.getId(), productIds);

    }

    //全选
    @RequestMapping("select_all.do")

    @ResponseBody

    public ServiceResponse<CartVo> selectAll(HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {

            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        }

        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);

    }

    //全反选
    @RequestMapping("un_select_all.do")

    @ResponseBody

    public ServiceResponse<CartVo> unSelectAll(HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {

            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        }

        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);

    }

    //单独选
    @RequestMapping("select.do")
    @ResponseBody
    public ServiceResponse<CartVo> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServiceResponse<CartVo> unSelect(HttpSession session, Integer productId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {

            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

        }

        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);

    }

    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServiceResponse<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());

    }
}




























