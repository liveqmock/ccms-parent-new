package com.yunat.ccms.tradecenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.CategoryVO;
import com.yunat.ccms.tradecenter.domain.CategoryDomain;
import com.yunat.ccms.tradecenter.service.CategoryService;

@Controller
@RequestMapping(value = "/category/*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


	/**
	 * 获取字类别列表 -- 嵌套获取
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCategoryListWithChildren", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public ControlerResult getCategoryListWithChildren(@RequestBody CategoryDomain categoryDomain) throws Exception {
        List<CategoryVO> categoryVOList = categoryService.getCategoryListWithChildren(categoryDomain.getParentId(), categoryDomain.getOutId(), categoryDomain.getOutType()) ;

		return ControlerResult.newSuccess(categoryVOList);
	}


    /**
     * 获取子类别列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findByParentId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ControlerResult findByParentId(@RequestBody CategoryDomain categoryDomain) throws Exception {
        List<CategoryVO> categoryVOList = categoryService.findByParentId(categoryDomain.getParentId(), categoryDomain.getOutId(), categoryDomain.getOutType()) ;

        return ControlerResult.newSuccess(categoryVOList);
    }

    /**
     * 获取类别，包含所有父类
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getCategoryWithParent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ControlerResult getCategoryWithParent(@RequestBody CategoryDomain categoryDomain) throws Exception {
        CategoryVO categoryVO = categoryService.getCategoryWithParent(categoryDomain.getPkid()) ;

        return ControlerResult.newSuccess(categoryVO);
    }

    /**
     * 获取类别
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getCategory", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ControlerResult getCategory(@RequestBody CategoryDomain categoryDomain) throws Exception {
        CategoryVO categoryVO = categoryService.get(categoryDomain.getPkid()) ;

        return ControlerResult.newSuccess(categoryVO);
    }

    /**
     * 获取类别
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ControlerResult addCategory(@RequestBody CategoryDomain categoryDomain) throws Exception {
        categoryService.save(categoryDomain);

        return ControlerResult.newSuccess();
    }

    /**
     * 获取类别
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ControlerResult deleteCategory(@RequestBody CategoryDomain categoryDomain) throws Exception {
        categoryService.delete(categoryDomain.getPkid());

        return ControlerResult.newSuccess();
    }
}
