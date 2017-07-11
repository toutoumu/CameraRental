package com.dsfy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsfy.entity.Category;
import com.dsfy.entity.Category.TreeData;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.ICamera_CategoryService;
import com.dsfy.service.ICategoryService;
import com.dsfy.service.IUserService;
import com.dsfy.util.ValidateUtil;

@Controller
@RequestMapping(value = "/CategoryController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class CategoryController {
    @Resource(name = "CategoryService")
    private ICategoryService categoryService;
    @Resource(name = "UserService")
    private IUserService userService;
    @Resource(name = "Camera_CategoryService")
    ICamera_CategoryService upService;

    /**
     * 添加类别
     *
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) Category category) {
        if (category == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(category.getName())) {
            return JsonResponse.error("类别名称不能为空");
        }
        category.setLevel(1);
        if (category.getParentId() > 0) {// 如果有父节点那么设置他的层级
            Category temp = categoryService.getById(Category.class, category.getParentId());
            if (temp == null) {
                return JsonResponse.error("父节点不存在");
            }
            category.setLevel(temp.getLevel() + 1);
        }

        categoryService.add(category);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("category", category);
        return jsonResponse;
    }

    /**
     * 删除类别
     *
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Category category) {
        if (category == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (category.getCategoryId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        Category temp = categoryService.getById(Category.class, category.getCategoryId());
        if (temp != null) {
            categoryService.deleteCategory(category.getCategoryId());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("删除的类别不存在");
    }

    /**
     * 更新类别
     *
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Category category) {
        if (category == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (category.getCategoryId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (ValidateUtil.isEmpty(category.getName())) {
            return JsonResponse.error("类别名称不能为空");
        }
        if (category.getParentId() > 0) {
            Category temp = categoryService.getById(Category.class, category.getParentId());
            if (temp == null) {
                return JsonResponse.error("父节点不存在");
            }
            // 重新设置层级
            category.setLevel(temp.getLevel() + 1);
        }
        Category temp = categoryService.getById(Category.class, category.getCategoryId());
        if (temp != null) {
            categoryService.update(category);
            return JsonResponse.success("更新成功");
        }
        return JsonResponse.error("更新的类别不存在");
    }

    /**
     * 根据父节点ID获取子节点
     *
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByParent.do")
    public JsonResponse getByParent(@RequestBody(required = false) Category category) {
        JsonResponse response = new JsonResponse();
        if (category == null) {
            return JsonResponse.error("查询条件不能为空");
        }
        List<Category> regions = categoryService.getByPrentId(category.getCategoryId());
        response.setData("categorys", regions);
        return response;
    }

    /**
     * 获取所有类别
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAll.do")
    public JsonResponse getAll() {
        List<Category> categories = categoryService.getAll(Category.class);
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("categorys", categories);
        return response;
    }

    /**
     * 获取类别树形数据
     *
     * @param categoryId 父类别id
     * @param hasEmpty   是否添加{id:0,value:'-------'}空值 非0则添加
     * @param cameraId   相机的ID,根据这个id查找关联的类别,并在返回值中设置选中的类别(可以为空)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tree.do", method = RequestMethod.GET)
    public List<TreeData> tree(//
                               @RequestParam(value = "categoryId", defaultValue = "0", required = false) int categoryId, //
                               @RequestParam(value = "empty", defaultValue = "0", required = false) int hasEmpty,// 是否添加-------空值
                               @RequestParam(value = "cameraId", defaultValue = "0", required = false) int cameraId) {
        List<Category> allCategory = categoryService.getAll(Category.class);
        if (allCategory == null || allCategory.size() == 0) {
            return null;
        }

        // 转换为TreeData数据
        List<TreeData> treeDatas = new ArrayList<>();
        for (Category categorie : allCategory) {
            treeDatas.add(new TreeData(categorie));
        }

        if (cameraId != 0) {
            // 获取相机已有的类别
            List<Category> selectedCategory = upService.getCategoryByCamera(cameraId);
            // 设置是否选中
            if (selectedCategory != null) {
                for (Category category : selectedCategory) {
                    for (TreeData treeData : treeDatas) {
                        if (treeData.getId() == category.getCategoryId()) {
                            treeData.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }

        // 将数据按照parentId分组
        Map<Integer, List<TreeData>> treeDataMap = new HashMap<>();
        for (TreeData treeData : treeDatas) {
            List<TreeData> list = treeDataMap.get(treeData.getParentId());
            if (list == null) {
                list = new ArrayList<>();
                treeDataMap.put(treeData.getParentId(), list);
            }
            list.add(treeData);
        }

        // 添加子节点
        for (TreeData treeData : treeDatas) {
            treeData.setChildren(treeDataMap.get(treeData.getId()));
        }

        // 返回父节点为0(为空)的数据
        List<TreeData> retVal = new ArrayList<>();
        if (hasEmpty != 0) {
            TreeData empty = new TreeData();
            empty.setText("--------------");
            retVal.add(empty);
        }
        for (TreeData treeData : treeDatas) {
            if (treeData.getParentId() == 0) {
                retVal.add(treeData);
            }
        }
        return retVal;
    }

}
