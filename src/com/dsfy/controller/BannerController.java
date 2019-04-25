package com.dsfy.controller;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Banner;
import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.IBannerService;
import com.dsfy.util.UploadUtil;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Banner维护
 */
@Controller
@RestController
@RequestMapping(value = "/BannerController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class BannerController {
    @Resource(name = "BannerService")
    private IBannerService bannerService;

    /**
     * 添加Banner
     *
     * @param title
     * @param image
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(//
                            @RequestParam(required = false, value = "title", defaultValue = "") String title,
                            @RequestParam(required = false, value = "image") MultipartFile image,
                            @RequestParam(required = false, value = "content") String content
    ) throws IOException {
        if (ValidateUtil.isEmpty(title)) {
            return JsonResponse.error("标题不能为空");
        }
        if (image == null || image.isEmpty()) {
            return JsonResponse.error("Banner图片不能为空");
        }
        Banner banner = new Banner();
        banner.setTitle(title);//标题
        banner.setVisible(Banner.Visible.visibal);//默认可见
        if (!ValidateUtil.isEmpty(content)) {//内容
            banner.setContent(content);
        }
        // 将图片保持到磁盘
        if (image != null && !image.isEmpty()) {//图片
            List<ImageInfo> images = new ArrayList<ImageInfo>();
            ImageInfo imageInfo = UploadUtil.saveFile(image, images);
            bannerService.batchSave(images);//保存图片
            banner.setImage(imageInfo.getUrl());
        }
        bannerService.add(banner);//保持banner信息
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("brand", banner);
        return jsonResponse;
    }

    /**
     * 删除Banner
     *
     * @param brand
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Banner brand) {
        if (brand == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (brand.getBannerId() == 0) {
            return JsonResponse.error("主键不能为空");
        }

        Banner temp = bannerService.getById(Banner.class, brand.getBannerId());
        if (temp != null) {
            bannerService.delete(Banner.class, brand.getBannerId());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("删除的Banner不存在");
    }

    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(//
                               @RequestParam(required = false, value = "title", defaultValue = "") String title,
                               @RequestParam(required = false, value = "content") String content,
                               @RequestParam(required = false, value = "visible", defaultValue = "0") int visible,
                               @RequestParam(required = false, value = "bannerId", defaultValue = "0") int bannerId,
                               @RequestParam(required = false, value = "image") MultipartFile image) throws IOException {
        if (ValidateUtil.isEmpty(title)) {
            return JsonResponse.error("标题不能为空");
        }
        if (bannerId == 0) {
            return JsonResponse.error("主键不能为空");
        }
        Banner temp = bannerService.getById(Banner.class, bannerId);
        if (temp == null) {
            return JsonResponse.error("更新的Banner不存在");
        }
        // 将图片保持到磁盘
        if (image != null && !image.isEmpty()) {//图片
            List<ImageInfo> images = new ArrayList<ImageInfo>();
            ImageInfo imageInfo = UploadUtil.saveFile(image, images);
            bannerService.batchSave(images);//保存图片
            temp.setImage(imageInfo.getUrl());
        }
        if (!ValidateUtil.isEmpty(title)) {//标题
            temp.setTitle(title);
        }
        if (!ValidateUtil.isEmpty(content)) {//内容
            temp.setContent(content);
        }
        if (visible != 0) {//是否可见
            temp.setVisible(visible);
        }
        bannerService.update(temp);
        return JsonResponse.success("更新成功");
    }

    /**
     * 获取可见的banner
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVisiable.do", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonResponse getVisiable() {
        JsonResponse response = JsonResponse.success("成功");
        List<Banner> list = bannerService.get(Banner.class, new QueryCondition("visible", QueryCondition.EQ, Banner.Visible.visibal));
        response.setData("brands", list);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) Banner brand) {
        if (brand == null || brand.getBannerId() == 0) {
            return JsonResponse.error("查询条件不能为空");
        }
        brand = bannerService.getById(Banner.class, brand.getBannerId());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("brand", brand);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do")
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        response.setData("brand", bannerService.getAll(Banner.class));
        return response;
    }
}
