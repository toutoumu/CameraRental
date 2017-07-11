package com.dsfy.controller;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Category;
import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.equipment.CameraLens;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.ICameraLensService;
import com.dsfy.service.ICategoryService;
import com.dsfy.service.IImageService;
import com.dsfy.util.UploadUtil;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/CameraLensController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class CameraLensController {
    @Resource(name = "CategoryService")
    private ICategoryService categoryService;
    @Resource(name = "CameraLensService")
    private ICameraLensService cameraLensService;
    @Resource(name = "ImageService")
    private IImageService imageService;

    /**
     * 添加镜头
     *
     * @param cameraLens
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) CameraLens cameraLens) {
        if (cameraLens == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cameraLens.getBrandId() == 0) {
            return JsonResponse.error("品牌ID不能为空");
        }
        if (cameraLens.getDeposit() <= 0) {
            return JsonResponse.error("镜头押金不能为空");
        }
        if (ValidateUtil.isEmpty(cameraLens.getBrand())) {
            return JsonResponse.error("品牌名称不能为空");
        }
        if (ValidateUtil.isEmpty(cameraLens.getModel())) {
            return JsonResponse.error("型号名称不能为空");
        }
        cameraLensService.add(cameraLens);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("cameraLens", cameraLens);
        return jsonResponse;
    }

    /**
     * 删除镜头
     *
     * @param cameraLens
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) CameraLens cameraLens) {
        if (cameraLens == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cameraLens.getLensId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        long count = cameraLensService.getRecordCount(RentalInfo.class, new QueryCondition("lensId", QueryCondition.EQ, cameraLens.getLensId()));
        if (count > 0) {
            return JsonResponse.error("有租赁信息引用了该镜头,不能删除");
        }
        CameraLens temp = cameraLensService.getById(CameraLens.class, cameraLens.getLensId());
        if (temp != null) {
            cameraLensService.delete(CameraLens.class, cameraLens.getLensId());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("删除的型号不存在");
    }

    /**
     * 更新镜头
     *
     * @param cameraLens
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) CameraLens cameraLens) {
        if (cameraLens == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cameraLens.getLensId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (cameraLens.getBrandId() == 0) {
            return JsonResponse.error("品牌ID不能为空");
        }
        if (ValidateUtil.isEmpty(cameraLens.getBrand())) {
            return JsonResponse.error("品牌名称不能为空");
        }
        if (ValidateUtil.isEmpty(cameraLens.getModel())) {
            return JsonResponse.error("型号名称不能为空");
        }
        CameraLens temp = cameraLensService.getById(CameraLens.class, cameraLens.getLensId());
        if (temp == null) {
            return JsonResponse.error("更新的型号不存在");
        }
        cameraLensService.update(cameraLens);
        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("cameraLens", temp);
        return response;
    }

    /**
     * 查询镜头
     *
     * @param cameraLens
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query.do")
    public JsonResponse query(@RequestBody(required = false) CameraLens cameraLens) {
        if (cameraLens == null) {
            return JsonResponse.error("查询参数不能为空");
        }
        List<CameraLens> list = cameraLensService.query(cameraLens);
        JsonResponse response = JsonResponse.success("成功");
        response.setData("cameraLens", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) CameraLens lens) {
        if (lens == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (lens.getLensId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        // 查询相机
        lens = cameraLensService.getById(CameraLens.class, lens.getLensId());
        // 查询相机图片
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.lensImage, "" + lens.getLensId());
        lens.setDemos(demos);
        // 组织返回数据
        JsonResponse response = JsonResponse.success("成功");
        response.setData("lens", lens);
        return response;
    }

    /**
     * 上传镜头样图
     *
     * @param images
     * @param lensId
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadDemo.do")
    public JsonResponse uploadDemo(//
                                   @RequestParam(value = "images", required = false) MultipartFile[] images,
                                   @RequestParam(value = "lensId", required = false, defaultValue = "0") int lensId
    ) throws IOException {
        if (images == null || images.length == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        int count = 0;
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            count++;
        }
        if (count == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        if (lensId == 0) {
            return JsonResponse.error("相机ID不能为空");
        }
        // 检测相机是否存在
        CameraLens lens = cameraLensService.getById(CameraLens.class, lensId);
        if (lens == null) {
            return JsonResponse.error("相机信息不存在");
        }
        // 查询已有样图
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.lensImage, "" + lensId);
        count = 0;
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
            info.setCategory(ImageInfo.Category.lensImage);
            info.setForeignKey("" + lensId);
            if (info.getSize() == ImageInfo.Size.small) {
                demos.add(info);
            }
        }
        imageService.batchSave(imageInfos);
        // 组织返回数据
        lens.setDemos(demos);
        JsonResponse response = JsonResponse.success("");
        response.setData("lens", lens);
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
        if (image.getCategory() != ImageInfo.Category.lensImage) {
            return JsonResponse.error("删除的照片的类型不正确");
        }
        if (ValidateUtil.isEmpty(image.getForeignKey()) || "0".equals(image.getForeignKey())) {
            return JsonResponse.error("主键不能为空");
        }
        if (image.getImageId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        //删除图片
        imageService.delete(ImageInfo.class, image.getImageId());
        CameraLens camera = cameraLensService.getById(CameraLens.class, Integer.parseInt(image.getForeignKey()));
        // 查询相机图片
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.lensImage, image.getForeignKey());
        camera.setDemos(demos);
        // 组织返回数据
        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("lens", camera);
        return response;
    }

    /** query方法可以代替下面的方法 */

    @ResponseBody
    @RequestMapping(value = "/getByBrand.do")
    public JsonResponse getByBrand(@RequestBody(required = false) CameraLens cameraLens) {
        if (cameraLens == null || cameraLens.getBrandId() == 0) {
            return JsonResponse.error("品牌ID不能为空");
        }
        List<CameraLens> list = cameraLensService.getByBrand(cameraLens.getBrandId());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("cameraLens", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        List<CameraLens> list = cameraLensService.getAll(CameraLens.class);
        response.setData("cameraLens", list);
        return response;
    }


    /**
     * 获取类别树形数据
     *
     * @param categoryId   父类别id
     * @param hasEmpty     是否添加{id:0,value:'-------'}空值 非0则添加
     * @param cameraLensId 镜头的ID,根据这个id查找关联的类别,并在返回值中设置选中的类别(可以为空)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tree.do", method = RequestMethod.GET)
    public List<Category.TreeData> tree(//
                                        @RequestParam(value = "categoryId", defaultValue = "0", required = false) int categoryId, //
                                        @RequestParam(value = "empty", defaultValue = "0", required = false) int hasEmpty,// 是否添加-------空值
                                        @RequestParam(value = "cameraLensId", defaultValue = "0", required = false) int cameraLensId) {
        List<Category> allCategory = categoryService.getAll(Category.class);
        if (allCategory == null || allCategory.size() == 0) {
            return null;
        }

        // 转换为TreeData数据
        List<Category.TreeData> treeDatas = new ArrayList<>();
        for (Category categorie : allCategory) {
            treeDatas.add(new Category.TreeData(categorie));
        }

        if (cameraLensId != 0) {
            // 获取相机已有的类别
            List<Category> selectedCategory = cameraLensService.getCategoryByCameraLens(cameraLensId);
            // 设置是否选中
            if (selectedCategory != null) {
                for (Category category : selectedCategory) {
                    for (Category.TreeData treeData : treeDatas) {
                        if (treeData.getId() == category.getCategoryId()) {
                            treeData.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }

        // 将数据按照parentId分组
        Map<Integer, List<Category.TreeData>> treeDataMap = new HashMap<>();
        for (Category.TreeData treeData : treeDatas) {
            List<Category.TreeData> list = treeDataMap.get(treeData.getParentId());
            if (list == null) {
                list = new ArrayList<>();
                treeDataMap.put(treeData.getParentId(), list);
            }
            list.add(treeData);
        }

        // 添加子节点
        for (Category.TreeData treeData : treeDatas) {
            treeData.setChildren(treeDataMap.get(treeData.getId()));
        }

        // 返回父节点为0(为空)的数据
        List<Category.TreeData> retVal = new ArrayList<>();
        if (hasEmpty != 0) {
            Category.TreeData empty = new Category.TreeData();
            empty.setText("--------------");
            retVal.add(empty);
        }
        for (Category.TreeData treeData : treeDatas) {
            if (treeData.getParentId() == 0) {
                retVal.add(treeData);
            }
        }
        return retVal;
    }
}
