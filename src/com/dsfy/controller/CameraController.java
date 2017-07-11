package com.dsfy.controller;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.equipment.Camera;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.ICameraService;
import com.dsfy.service.IImageService;
import com.dsfy.util.UploadUtil;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/CameraController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class CameraController {
    @Resource(name = "CameraService")
    private ICameraService cameraService;
    @Resource(name = "ImageService")
    private IImageService imageService;

    /** 添加相机 */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) Camera camera) {
        if (camera == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (camera.getBrandId() == 0) {
            return JsonResponse.error("品牌ID不能为空");
        }
        if (ValidateUtil.isEmpty(camera.getBrand())) {
            return JsonResponse.error("品牌名称不能为空");
        }
        if (ValidateUtil.isEmpty(camera.getModel())) {
            return JsonResponse.error("型号名称不能为空");
        }
        if (camera.getDeposit() <= 0) {
            return JsonResponse.error("请填写相机押金");
        }
        cameraService.add(camera);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("camera", camera);
        return jsonResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Camera camera) {
        if (camera == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (camera.getCameraId() == 0) {
            return JsonResponse.error("主键不能为空");
        }

        long count = cameraService.getRecordCount(RentalInfo.class, new QueryCondition("cameraId", QueryCondition.EQ, camera.getCameraId()));
        if (count > 0) {
            return JsonResponse.error("有租赁信息引用了该品牌,不能删除");
        }
        Camera temp = cameraService.getById(Camera.class, camera.getCameraId());
        if (temp != null) {
            cameraService.delete(Camera.class, camera.getCameraId());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("删除的型号不存在");
    }

    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Camera camera) {
        if (camera == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (camera.getCameraId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (camera.getBrandId() == 0) {
            return JsonResponse.error("品牌ID不能为空");
        }
        if (ValidateUtil.isEmpty(camera.getBrand())) {
            return JsonResponse.error("品牌名称不能为空");
        }
        if (ValidateUtil.isEmpty(camera.getModel())) {
            return JsonResponse.error("型号名称不能为空");
        }

        camera = cameraService.update(camera);

        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("camera", camera);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) Camera camera) {
        if (camera == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (camera.getCameraId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        // 查询相机
        camera = cameraService.getById(Camera.class, camera.getCameraId());
        // 查询相机图片
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.cameraImage, "" + camera.getCameraId());
        camera.setDemos(demos);
        // 组织返回数据
        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("camera", camera);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/query.do")
    public JsonResponse query(@RequestBody(required = false) Camera camera) {
        if (camera == null) {
            return JsonResponse.error("查询条件不能为空");
        }
        List<Camera> list = cameraService.query(camera);
        JsonResponse response = JsonResponse.success("成功");
        response.setData("cameras", list);
        return response;
    }

    /**
     * 上传相机样图
     *
     * @param images
     * @param cameraId
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadDemo.do")
    public JsonResponse uploadDemo(//
                                   @RequestParam(value = "images", required = false) MultipartFile[] images,
                                   @RequestParam(value = "cameraId", required = false, defaultValue = "0") int cameraId
    ) throws IOException {
        if (images == null || images.length == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        if (cameraId == 0) {
            return JsonResponse.error("相机ID不能为空");
        }
        int i = 0;
        for (MultipartFile image : images) {
            if (!image.isEmpty()) i++;
        }
        if (i == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        // 检测相机是否存在
        Camera camera = cameraService.getById(Camera.class, cameraId);
        if (camera == null) {
            return JsonResponse.error("相机信息不存在");
        }
        // 查询已有样图
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.cameraImage, "" + cameraId);
        int count = 0;
        if (demos != null) count = demos.size();
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            UploadUtil.saveFile(image, imageInfos);
            count++;
            //保存图片做多10张
            //if (count == 10) break;
        }
        // 这个是必须的要不然查不到
        for (ImageInfo info : imageInfos) {
            info.setCategory(ImageInfo.Category.cameraImage);
            info.setForeignKey("" + cameraId);
            if (info.getSize() == ImageInfo.Size.small) {
                demos.add(info);
            }
        }
        imageService.batchSave(imageInfos);
        // 组织返回数据
        camera.setDemos(demos);
        JsonResponse response = JsonResponse.success("");
        response.setData("camera", camera);
        return response;
    }

    /**
     * 删除样张
     *
     * @param image
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteDemo.do")
    public JsonResponse deleteDemo(@RequestBody(required = false) ImageInfo image) {
        if (image == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (image.getCategory() != ImageInfo.Category.cameraImage) {
            return JsonResponse.error("删除的照片的类型不正确");
        }
        if (ValidateUtil.isEmpty(image.getForeignKey()) || "0".equals(image.getForeignKey())) {
            return JsonResponse.error("主键不能为空");
        }
        //删除图片
        imageService.delete(ImageInfo.class, image.getImageId());
        Camera camera = cameraService.getById(Camera.class, Integer.parseInt(image.getForeignKey()));
        // 查询相机图片
        // 查询已有样图
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.cameraImage, "" + camera.getCameraId());
        camera.setDemos(demos);
        // 组织返回数据
        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("camera", camera);
        return response;
    }

    /** 下面的可以用query代替 */
    /**
     * @param camera
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByBrand.do")
    public JsonResponse getByBrand(@RequestBody(required = false) Camera camera) {
        if (camera == null || camera.getBrandId() == 0) {
            return JsonResponse.error("品牌ID不能为空");
        }
        List<Camera> list = cameraService.getByBrand(camera.getBrandId());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("brands", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        List<Camera> list = cameraService.getAll(Camera.class);
        response.setData("brands", list);
        return response;
    }

}
