package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.CollectedRentalInfo;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.relation.PK_User_Rental;
import com.dsfy.service.ICollectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/CollectController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class CollectController {
    @Resource(name = "CollectService")
    private ICollectService collectService;

    /**
     * 添加收藏
     *
     * @param collect
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) CollectedRentalInfo collect) {
        if (collect == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (collect.getUserId() == 0 || collect.getRentalId() == 0) {
            return JsonResponse.error("用户id或租赁信息id不能为空");
        }
        PK_User_Rental pk = new PK_User_Rental();
        pk.setRentalId(collect.getRentalId());
        pk.setUserId(collect.getUserId());
        CollectedRentalInfo temp = collectService.getById(CollectedRentalInfo.class, pk);
        if (temp == null) {//如果收藏不存在,添加到收藏
            RentalInfo rentalInfo = collectService.getById(RentalInfo.class, collect.getRentalId());
            if (rentalInfo == null) {
                return JsonResponse.error("租赁信息不存在");
            }
            // 填充租赁信息
            collect.setScore(rentalInfo.getScore());//评分
            collect.setLensId(rentalInfo.getLensId());//镜头ID
            collect.setCameraId(rentalInfo.getCameraId());//相机ID
            collect.setBrand(rentalInfo.getBrand());//品牌
            collect.setBrandId(rentalInfo.getBrandId());//品牌ID
            collect.setModel(rentalInfo.getModel());//型号
            collect.setAddress(rentalInfo.getAddress());//地址
            collect.setCity(rentalInfo.getCity());//城市
            collect.setCityId(rentalInfo.getCityId());//城市代码
            collect.setPrice(rentalInfo.getPrice());//价格
            collect.setRentalCount(rentalInfo.getRentalCount());//租赁次数
            collect.setCollectTime(new Date().getTime());//收藏时间
            collect.setCover(rentalInfo.getCover());//封面照片
            collectService.add(collect);
        }
        JsonResponse response = JsonResponse.success("收藏成功");
        response.setData("collect", collect);
        return response;
    }

    /**
     * 取消收藏
     *
     * @param collect
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) CollectedRentalInfo collect) {
        if (collect == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (collect.getUserId() == 0 || collect.getRentalId() == 0) {
            return JsonResponse.error("用户id或租赁信息id不能为空");
        }
        collectService.deCollect(collect.getUserId(), collect.getRentalId());
        return JsonResponse.success("取消收藏成功");
    }

    /**
     * 获取用户的收藏
     *
     * @param collect
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByUser.do")
    public JsonResponse getByUser(@RequestBody(required = false) CollectedRentalInfo collect) {
        JsonResponse response = new JsonResponse();
        if (collect == null || collect.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        List<CollectedRentalInfo> rentalInfos = collectService.getByUser(collect.getUserId());
        response.setData("collect", rentalInfos);
        return response;
    }

    /**
     * 获取租赁信息被那些用户收藏
     *
     * @param collect
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByRental.do")
    public JsonResponse getByRental(@RequestBody(required = false) CollectedRentalInfo collect) {
        JsonResponse response = new JsonResponse();
        if (collect == null || collect.getRentalId() == 0) {
            return JsonResponse.error("租赁信息id不能为空");
        }
        List<SysmanUser> users = collectService.getByRental(collect.getRentalId());
        response.setData("users", users);
        return response;
    }

    /**
     * 分页查询收藏信息(userId,rentalId传一个即可,连个都传只能查出来一个数据)
     *
     * @param userId      用户id(可选)
     * @param rentalId    租赁信息ID(可选)
     * @param currentPage 当前页 从1开始
     * @param pageSize    也大小
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/page.do", method = RequestMethod.POST)
    public JsonResponse page(@RequestParam(value = "userId", required = true, defaultValue = "0") int userId,
                             @RequestParam(value = "rentalId", required = false, defaultValue = "0") int rentalId,
                             @RequestParam(value = "currentPage", required = true, defaultValue = "1") int currentPage,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "30") int pageSize) {
        if (userId == 0 && rentalId == 0) {
            return JsonResponse.error("查询条件不能为空");
        }

        Pagination<CollectedRentalInfo> pagination = collectService.page(userId, rentalId, currentPage, pageSize);
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("pagination", pagination);
        return response;
    }
}
