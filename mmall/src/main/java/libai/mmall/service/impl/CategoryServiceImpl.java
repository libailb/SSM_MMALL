package libai.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import libai.mmall.common.ServiceResponse;
import libai.mmall.dao.CategoryMapper;
import libai.mmall.pojo.Category;
import libai.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{
     private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
      @Autowired
      private CategoryMapper categoryMapper;
      public ServiceResponse addCategory(String categoryName,Integer parentId){
          if(parentId==null || StringUtils.isBlank(categoryName)){
              return ServiceResponse.createByErrorMessage("添加品类参数错误");
          }
          Category category=new Category();
          category.setName(categoryName);
          category.setParentId(parentId);
          category.setStatus(true);
       int rowCount=categoryMapper.insert(category);
       if(rowCount>0){
           return ServiceResponse.createBySuccessMessage("添加品类成功");
       }
       return ServiceResponse.createByErrorMessage("添加品类失败");
      }



    public ServiceResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServiceResponse.createBySuccessMessage("更新品类名称成功");
        }
        return ServiceResponse.createByErrorMessage("更新品类名称失败");
    }

    public ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
      List<Category> list=categoryMapper.selectCategoryChildrenByParentId(categoryId);
      if(CollectionUtils.isEmpty(list)){
          logger.info("未找到当前分类的子分类");
      }
      return ServiceResponse.createBySuccess(list);
    }
    //递归查询本节点的id和递归节点的id
    public ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
           Set<Category> set= Sets.newHashSet();
           findChildCategory(set,categoryId);
           List<Integer> list= Lists.newArrayList();
           if(categoryId!=null){
               for (Category c:set){
                   list.add(c.getId());
               }
           }
           return ServiceResponse.createBySuccess(list);
    }
    //实现递归方法 算出子节点
    private Set<Category> findChildCategory(Set<Category> set,Integer categoryId){
          Category category=categoryMapper.selectByPrimaryKey(categoryId);
          if(category!=null){
             set.add(category);
          }
          //查找子节点，递归算法一定要有一个退出的条件
        List<Category> list=categoryMapper.selectCategoryChildrenByParentId(categoryId);
          for (Category c:list){
              findChildCategory(set,c.getId());
          }
          return set;
    }


}
