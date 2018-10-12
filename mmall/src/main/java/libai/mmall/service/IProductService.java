package libai.mmall.service;

import com.github.pagehelper.PageInfo;
import libai.mmall.common.ServiceResponse;
import libai.mmall.pojo.Product;
import libai.mmall.vo.ProductDetailVo;

public interface IProductService {

    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse<String> setSaleStatus(Integer productId,Integer status);
    //获取产品详情
    ServiceResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServiceResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServiceResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServiceResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);



}