package com.dsfy.controller;

import com.dsfy.entity.Camera_Category;
import com.dsfy.entity.Category;
import com.dsfy.entity.equipment.Camera;
import com.dsfy.entity.equipment.CameraLens;
import com.dsfy.entity.http.JsonRequest;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.relation.PK_Camera_Category;
import com.dsfy.service.ILens_CategoryService;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/Lens_CategoryController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class Lens_CategoryController {

    @Resource(name = "Lens_CategoryService")
    ILens_CategoryService lens_categoryService;

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
        CameraLens camera = jsonRequest.getData("cameraLens", CameraLens.class);
        List<Category> categories = jsonRequest.getData("categorys", new TypeToken<List<Category>>() {
        }.getType());

        if (camera == null || camera.getLensId() == 0) {
            return JsonResponse.error("相机主键不能为空");
        }
        if (categories == null) {
            return JsonResponse.error("请选择类别");
        }

        if (lens_categoryService.getById(CameraLens.class, camera.getLensId()) == null) {
            return JsonResponse.error("相机不存在");
        }

        // 更新关联关系
        lens_categoryService.updateRelation(camera.getLensId(), categories);

        JsonResponse response = JsonResponse.success("关系添加成功");
        return response;
    }

}
