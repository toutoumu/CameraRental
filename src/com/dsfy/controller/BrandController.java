package com.dsfy.controller;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.equipment.Brand;
import com.dsfy.entity.equipment.Camera;
import com.dsfy.entity.equipment.CameraLens;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.IBrandService;
import com.dsfy.util.ValidateUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/BrandController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class BrandController {
    @Resource(name = "BrandService")
    private IBrandService brandService;

    /**
     * 添加品牌
     *
     * @param brand
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) Brand brand) {
        if (brand == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(brand.getName())) {
            return JsonResponse.error("品牌名称不能为空");
        }
        brandService.add(brand);
        JsonResponse jsonResponse = JsonResponse.success("成功");
        jsonResponse.setData("brand", brand);
        return jsonResponse;
    }

    /**
     * 删除品牌
     *
     * @param brand
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Brand brand) {
        if (brand == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (brand.getBrandId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        Brand temp = brandService.getById(Brand.class, brand.getBrandId());
        if (temp == null) {
            return JsonResponse.error("删除的品牌不存在");
        }

        // 检查是否有地方引用了品牌
        long count = brandService.getRecordCount(Camera.class, new QueryCondition("brandId", QueryCondition.EQ, temp.getBrandId()));
        if (count > 0) {
            return JsonResponse.error("不能删除该品牌,有相机引用了此品牌");
        }

        count = brandService.getRecordCount(CameraLens.class, new QueryCondition("brandId", QueryCondition.EQ, temp.getBrandId()));
        if (count > 0) {
            return JsonResponse.error("不能删除该品牌,有镜头引用了此品牌");
        }

        count = brandService.getRecordCount(RentalInfo.class, new QueryCondition("brandId", QueryCondition.EQ, temp.getBrandId()));
        if (count > 0) {
            return JsonResponse.error("不能删除该品牌,有租赁信息引用了此品牌");
        }

        brandService.delete(Brand.class, brand.getBrandId());
        return JsonResponse.success("删除成功");
    }

    /**
     * 更新品牌
     *
     * @param brand
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Brand brand) {
        if (brand == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (brand.getBrandId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (ValidateUtil.isEmpty(brand.getName())) {
            return JsonResponse.error("品牌名称不能为空");
        }

        brand = brandService.update(brand);
        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("brand", brand);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        List<Brand> list = brandService.getAll(Brand.class);
        response.setData("brands", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) Brand brand) {
        if (brand == null || brand.getBrandId() == 0) {
            return JsonResponse.error("查询条件不能为空");
        }
        brand = brandService.getById(Brand.class, brand.getBrandId());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("brand", brand);
        return response;
    }
}
