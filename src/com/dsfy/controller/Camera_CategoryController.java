package com.dsfy.controller;

import com.dsfy.entity.Camera_Category;
import com.dsfy.entity.Category;
import com.dsfy.entity.equipment.Camera;
import com.dsfy.entity.http.JsonRequest;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.relation.PK_Camera_Category;
import com.dsfy.service.ICamera_CategoryService;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 相机类别与相机关联操作
 */
@Controller
@RequestMapping(value = "/Camera_CategoryController", method = RequestMethod.POST, produces = {
        "application/json;charset=UTF-8"})
public class Camera_CategoryController {

    @Resource(name = "Camera_CategoryService")
    ICamera_CategoryService upService;

    /**
     * 更新单个相机与多个类别之间的关联关系</br>
     * 先删除已有的关联关系,然后添加关联关系
     *
     * @param jsonRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateRelationCamera.do")
    public JsonResponse updateRelationCamera(@RequestBody(required = false) JsonRequest jsonRequest) {
        Camera camera = jsonRequest.getData("camera", Camera.class);
        List<Category> categories = jsonRequest.getData("categorys", new TypeToken<List<Category>>() {
        }.getType());

        if (camera == null || camera.getCameraId() == 0) {
            return JsonResponse.error("相机主键不能为空");
        }
        if (categories == null) {
            return JsonResponse.error("请选择类别");
        }

        if (upService.getById(Camera.class, camera.getCameraId()) == null) {
            return JsonResponse.error("相机不存在");
        }

        // 更新关联关系
        upService.updateRelation(camera.getCameraId(), categories);

        JsonResponse response = JsonResponse.success("关系添加成功");
        return response;
    }

    /*******************************下面的接口没测试*********************************/
    /**
     * 添加多个相机与类别的关系关系
     *
     * @param jsonRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addRelationByCategory.do")
    public JsonResponse addRelationByCategory(@RequestBody(required = false) JsonRequest jsonRequest) {
        Category category = jsonRequest.getData("category", Category.class);
        List<Camera> cameras = jsonRequest.getData("cameras", new TypeToken<List<Camera>>() {
        }.getType());

        if (category == null || category.getCategoryId() == 0) {
            return JsonResponse.error("类别键不能为空");
        }
        if (cameras == null) {
            return JsonResponse.error("请选择相机");
        }
        if (upService.getById(Category.class, category.getCategoryId()) == null) {
            return JsonResponse.error("类别不存在");
        }

        // 循环
        PK_Camera_Category pk = new PK_Camera_Category();

        for (Camera camera : cameras) {
            pk.setCameraId(camera.getCameraId());
            pk.setCategoryId(category.getCategoryId());
            if (upService.getById(Camera_Category.class, pk) != null) {
                continue;
            }
            Camera_Category cc = new Camera_Category();
            cc.setCameraId(pk.getCameraId());
            cc.setCategoryId(pk.getCategoryId());
            upService.add(cc);
        }

        JsonResponse response = JsonResponse.success("关系添加成功");
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/removeRelationByCategory.do")
    public JsonResponse removeRelationByCategory(@RequestBody(required = false) JsonRequest jsonRequest) {
        Category category = jsonRequest.getData("category", Category.class);
        List<Camera> cameras = jsonRequest.getData("cameras", new TypeToken<List<Camera>>() {
        }.getType());

        if (category == null || category.getCategoryId() == 0) {
            return JsonResponse.error("类别键不能为空");
        }
        if (cameras == null) {

        }
        if (upService.getById(Category.class, category.getCategoryId()) == null) {
            return JsonResponse.error("类别不存在");
        }

        // 循环
        PK_Camera_Category pk = new PK_Camera_Category();

        for (Camera camera : cameras) {
            pk.setCameraId(camera.getCameraId());
            pk.setCategoryId(category.getCategoryId());
            if (upService.getById(Camera_Category.class, pk) != null) {
                continue;
            }
            Camera_Category cc = new Camera_Category();
            cc.setCameraId(pk.getCameraId());
            cc.setCategoryId(pk.getCategoryId());
            upService.add(cc);
        }

        JsonResponse response = JsonResponse.success("关系添加成功");
        return response;
    }
}
