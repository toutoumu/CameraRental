package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.CollectedRentalInfo;
import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.RejectTime;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.equipment.Camera;
import com.dsfy.entity.equipment.CameraLens;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.Order;
import com.dsfy.service.ICollectService;
import com.dsfy.service.IImageService;
import com.dsfy.service.IRejectTimeService;
import com.dsfy.service.IRentalService;
import com.dsfy.util.UploadUtil;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/RentalController ", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class RentalController {

    @Resource(name = "RentalService")
    private IRentalService rentalService;
    @Resource(name = "CollectService")
    private ICollectService collectService;
    @Resource(name = "ImageService")
    private IImageService imageService;
    @Resource(name = "RejectTimeService")
    private IRejectTimeService rejectTimeService;

    /**
     * 添加租赁信息
     *
     * @param rental
     * @return
     */
    @ResponseBody
    @RequestMapping("/add.do")
    public JsonResponse add(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null) return JsonResponse.error("租赁信息不能为空");
        if (rental.getUserId() == 0) return JsonResponse.error("机主ID不能为空");
        if (rental.getCityId() == 0) return JsonResponse.error("请选择城市信息");
        if (ValidateUtil.isEmpty(rental.getCity())) return JsonResponse.error("请选择城市信息");
        if (ValidateUtil.isEmpty(rental.getAddress())) return JsonResponse.error("请填写地址信息");
        if (rental.getCameraId() == 0 && rental.getLensId() == 0) return JsonResponse.error("请选择租赁信息相关的镜头或机身");
        if (rental.getPrice() <= 0) return JsonResponse.error("第一天的租赁价格不能为空");

        // 验证用户信息
        SysmanUser user = rentalService.getById(SysmanUser.class, rental.getUserId());
        if (user == null) return JsonResponse.error("用户信息有误");
        if (ValidateUtil.isEmpty(user.getRealName()) || ValidateUtil.isEmpty(user.getUserName())) {
            return JsonResponse.error("请完善您的个人信息后再发布租赁信息");
        }
        if (user.getLocked() != SysmanUser.Locked.unlocked) {
            return JsonResponse.error("您的用户信息已经被锁定,请联系客服");
        }
        if (user.getVerify() != SysmanUser.Verify.verify) {
            return JsonResponse.error("您的用户信息还未通过审核");
        }

        rental.setUserId(user.getPid());//机主用户ID
        rental.setName(user.getRealName());//机主姓名

        // 设置押金和相机型号信息
        double deposit = 0;
        Camera camera = null;
        if (rental.getCameraId() != 0) {//机身押金
            camera = rentalService.getById(Camera.class, rental.getCameraId());
            if (camera == null) {
                return JsonResponse.error("您选择的相机不存在");
            }
            deposit += camera.getDeposit();
        }
        CameraLens lens = null;
        if (rental.getLensId() != 0) {//镜头押金
            lens = rentalService.getById(CameraLens.class, rental.getLensId());
            if (lens == null) {
                return JsonResponse.error("您选择的镜头不存在");
            }
            deposit += lens.getDeposit();
        }
        if (deposit <= 0) return JsonResponse.error("押金不能为0");
        if (camera == null && lens == null) {
            return JsonResponse.error("您未选择的机身或镜头");
        }

        //rental.setAutoAccept(RentalInfo.AutoAccept.yes);//默认自动接单
        rental.setDeposit(deposit);//押金(来自相机数据维护)
        // 相机品牌信息,如果没有填充镜头的
        if (camera != null) {
            rental.setBrand(camera.getBrand());//品牌名称
            rental.setBrandId(camera.getBrandId());//品牌id
            rental.setModel(camera.getModel());//型号
        } else {
            rental.setBrand(lens.getBrand());//品牌名称
            rental.setBrandId(lens.getBrandId());//品牌ID
            rental.setModel(lens.getModel());//型号
        }
        // 镜头品牌信息
        if (lens != null) {
            rental.setLensId(lens.getLensId());
            rental.setLensBrand(lens.getBrand());
            rental.setLensModel(lens.getModel());
        }
        rental.setLocked(RentalInfo.Locked.unlocked);//未锁定
        rental.setVerify(RentalInfo.Verify.unVerify);//未审核
        rental.setRentalCount(0);//默认是0次租赁
        rental.setScore(0);//默认评分0
        rental.setCreateTime(new Date().getTime());//订单发布时间
        rentalService.add(rental);
        JsonResponse jsonResponse = JsonResponse.success("添加成功");
        jsonResponse.setData("rental", rental);
        return jsonResponse;
    }

    /**
     * 编辑租赁信息
     *
     * @param rental
     * @return
     */
    @ResponseBody
    @RequestMapping("/edit.do")
    public JsonResponse edit(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null || rental.getRentalId() == 0) {
            return JsonResponse.error("租赁ID不能为空");
        }
        RentalInfo temp = rentalService.getById(RentalInfo.class, rental.getRentalId());
        if (temp == null) return JsonResponse.error("编辑的租赁信息不存在");

        // 设置押金和相机型号信息
        double deposit = 0;
        Camera camera = null;
        // 这里如果参数里面传递了机身信息那么需要重新计算押金
        int cameraId = temp.getCameraId();
        if (rental.getCameraId() != 0) cameraId = rental.getCameraId();//
        if (cameraId != 0) {//如果传递了机身ID
            temp.setCameraId(rental.getCameraId());//相机ID
            camera = rentalService.getById(Camera.class, rental.getCameraId());
            if (camera == null) {
                return JsonResponse.error("您选择的相机不存在");
            }
            deposit += camera.getDeposit();
        }
        CameraLens lens = null;
        // 这里如果参数里面传递了镜头信息那么需要重新计算押金
        int lensID = temp.getLensId();
        if (rental.getLensId() != 0) lensID = rental.getLensId();
        if (lensID != 0) {
            temp.setLensId(rental.getLensId());//镜头ID
            lens = rentalService.getById(CameraLens.class, rental.getLensId());
            if (lens == null) {
                return JsonResponse.error("您选择的镜头不存在");
            }
            deposit += lens.getDeposit();
        }
        if (deposit <= 0) return JsonResponse.error("押金不能为0");
        if (camera == null && lens == null) {
            return JsonResponse.error("您未选择的机身或镜头");
        }

        temp.setDeposit(deposit);//押金(来自相机数据维护)
        if (camera != null) {
            temp.setBrand(camera.getBrand());//品牌名称
            temp.setBrandId(camera.getBrandId());//品牌id
            temp.setModel(camera.getModel());//型号
        } else {
            temp.setBrand(lens.getBrand());//品牌名称
            temp.setBrandId(lens.getBrandId());//品牌ID
            temp.setModel(lens.getModel());//型号
        }
        // 镜头品牌信息
        if (lens != null) {
            rental.setLensId(lens.getLensId());
            rental.setLensBrand(lens.getBrand());
            rental.setLensModel(lens.getModel());
        }
        //if (!ValidateUtil.isEmpty(rental.getBrand())) temp.setBrand(rental.getBrand()); //品牌名称
        //if (rental.getBrandId() != 0) temp.setBrandId(rental.getBrandId());// 品牌ID
        //if (!ValidateUtil.isEmpty(rental.getModel())) temp.setModel(rental.getModel()); //型号名称
        //if (rental.getCameraId() != 0) temp.setCameraId(rental.getCameraId());//型号ID
        //if (rental.getCameraId() != 0) temp.setCameraId(rental.getCameraId());//型号ID

        if (!ValidateUtil.isEmpty(rental.getSnNumber())) temp.setSnNumber(rental.getSnNumber()); //SN号
        if (rental.getPrice() != 0) temp.setPrice(rental.getPrice());// 租赁价格
        if (!ValidateUtil.isEmpty(rental.getSerialNumber())) temp.setSerialNumber(rental.getSerialNumber()); //器材编号
        if (rental.getCityId() != 0) temp.setCityId(rental.getCityId());//城市编码
        if (!ValidateUtil.isEmpty(rental.getCity())) temp.setCity(rental.getCity()); //城市
        if (!ValidateUtil.isEmpty(rental.getAddress())) temp.setAddress(rental.getAddress()); //地址
        if (rental.getMinRental() != 0) temp.setMinRental(rental.getMinRental());// 最小租期
        if (rental.getMaxRental() != 0) temp.setMaxRental(rental.getMaxRental());// 最大租期
        if (rental.getAutoAccept() != 0) temp.setAutoAccept(rental.getAutoAccept());// 是否自动接单
        if (rental.getPurchaseDate() != 0) temp.setPurchaseDate(rental.getPurchaseDate());// 购买日期
        if (!ValidateUtil.isEmpty(rental.getLongitude())) temp.setLongitude(rental.getLongitude());//经度
        if (!ValidateUtil.isEmpty(rental.getLatitude())) temp.setLatitude(rental.getLatitude());//纬度
        if (!ValidateUtil.isEmpty(rental.getMark())) temp.setMark(rental.getMark());//备注

        // 机身信息
        if (!ValidateUtil.isEmpty(rental.getMemory())) temp.setMemory(rental.getMemory());
        if (rental.getBattery() != 0) temp.setBattery(rental.getBattery());
        if (rental.getCharger() != 0) temp.setCharger(rental.getCharger());
        if (rental.getStrap() != 0) temp.setStrap(rental.getStrap());
        if (rental.getCameraBag() != 0) temp.setCameraBag(rental.getCameraBag());
        if (rental.getCardReader() != 0) temp.setCardReader(rental.getCardReader());
        if (rental.getWirelessShutter() != 0) temp.setWirelessShutter(rental.getWirelessShutter());
        if (rental.getCleaningTool() != 0) temp.setCleaningTool(rental.getCleaningTool());
        if (rental.getWaterproofShell() != 0) temp.setWaterproofShell(rental.getWaterproofShell());
        if (rental.getBuckle() != 0) temp.setBuckle(rental.getBuckle());
        if (rental.getRemoteControl() != 0) temp.setRemoteControl(rental.getRemoteControl());
        if (rental.getAircraft() != 0) temp.setAircraft(rental.getAircraft());

        // 镜头信息
        if (rental.getLensHood() != 0) temp.setLensHood(rental.getLensHood());
        if (rental.getUv() != 0) temp.setUv(rental.getUv());
        if (rental.getLenBag() != 0) temp.setLenBag(rental.getLenBag());
        if (rental.getPolarizer() != 0) temp.setPolarizer(rental.getPolarizer());
        if (rental.getNdMirror() != 0) temp.setNdMirror(rental.getNdMirror());
        if (rental.getGrayGradient() != 0) temp.setGrayGradient(rental.getGrayGradient());

        // 更新
        rentalService.update(temp);
        JsonResponse jsonResponse = JsonResponse.success("更新成功");
        jsonResponse.setData("rental", temp);
        return jsonResponse;
    }

    /**
     * 删除租赁信息
     *
     * @param rental
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete.do")
    public JsonResponse delete(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null || rental.getRentalId() == 0) {
            return JsonResponse.error("租赁信息不能为空");
        }
        RentalInfo temp = rentalService.getById(RentalInfo.class, rental.getRentalId());
        if (temp == null) {
            return JsonResponse.error("删除的租赁信息不存在");
        }

        List<Order> orders = rentalService.get(Order.class, new QueryCondition("rentalId", QueryCondition.EQ, temp.getRentalId()));
        if (orders != null && orders.size() > 0) {
            for (Order order : orders) {
                if (order.getState() < Order.State.isclosed) {
                    return JsonResponse.error("租赁信息管理的订单还未处理完成,不能删除");
                }
            }
        }

        temp.setDeleted(RentalInfo.DeleteState.deleted);
        rentalService.update(temp);
        //rentalService.delete(RentalInfo.class, rental.getRentalId());
        return JsonResponse.success("删除成功");
    }

    /**
     * 通过租赁认证
     *
     * @param rental
     * @return
     */
    @RequestMapping(value = "/verify.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse verify(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null || rental.getRentalId() == 0) {
            return JsonResponse.error("租赁信息不能为空");
        }
        JsonResponse response = updateUserState(rental, RentalInfo.Verify.verify);
        response.setMessage("认证成功");
        return response;
    }

    /**
     * 认证失败
     *
     * @param rental
     * @return
     */
    @RequestMapping(value = "/verifyField.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse verifyField(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null || rental.getRentalId() == 0) {
            return JsonResponse.error("租赁信息不能为空");
        }
        if (ValidateUtil.isEmpty(rental.getReason())) {
            return JsonResponse.error("审核失败原因不能为空");
        }
        JsonResponse response = updateUserState(rental, RentalInfo.Verify.verifyField);
        response.setMessage("已经设置租赁认证失败");
        return response;
    }

    /**
     * 锁定租赁
     *
     * @param rental
     * @return
     */
    @RequestMapping(value = "/lock.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse lock(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null || rental.getUserId() == 0) {
            return JsonResponse.error("租赁信息不能为空");
        }
        rental = rentalService.getById(RentalInfo.class, rental.getUserId());
        if (rental == null) {
            return JsonResponse.error("更新的租赁不存在");
        }
        rental.setLocked(RentalInfo.Locked.locked);
        rentalService.update(rental);
        JsonResponse response = new JsonResponse();
        response.setData("rental", rental);
        return response;
    }

    /**
     * 锁定租赁
     *
     * @param rental
     * @return
     */
    @RequestMapping(value = "/unLock.do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse unLock(@RequestBody(required = false) RentalInfo rental) {
        if (rental == null || rental.getUserId() == 0) {
            return JsonResponse.error("租赁信息不能为空");
        }
        rental = rentalService.getById(RentalInfo.class, rental.getUserId());
        if (rental == null) {
            return JsonResponse.error("更新的租赁不存在");
        }
        rental.setLocked(RentalInfo.Locked.unlocked);
        rentalService.update(rental);
        JsonResponse response = new JsonResponse();
        response.setData("rental", rental);
        return response;
    }


    /**
     * 根据ID查找,里面包含图片信息
     *
     * @param rental
     * @return
     */
    @ResponseBody
    @RequestMapping("/getById.do")
    public JsonResponse getById(@RequestBody(required = false) RentalInfo rental, HttpSession session) {
        if (rental == null || rental.getRentalId() == 0) {
            return JsonResponse.error("租赁ID不能为空");
        }
        // 1. 查询租赁信息
        rental = rentalService.getById(RentalInfo.class, rental.getRentalId());
        // 2. 查询租赁信息图片
        rental.setDemos(imageService.query(ImageInfo.Category.rentalImage, "" + rental.getRentalId()));
        // 3. 查询是否已经收藏
        SysmanUser user = (SysmanUser) session.getAttribute("adminCurrentUser");
        if (user != null) {
            boolean b = collectService.isCollect(user.getPid(), rental.getRentalId());
            rental.setIsCollected(b);
        }
        // 4. 查询拒绝租赁时间
        RejectTime rejectTime = new RejectTime();
        rejectTime.setUserId(rental.getUserId());//机主id
        rejectTime.setRentalId(rental.getRentalId());//租赁信息ID
        List<RejectTime> rejectTimes = rejectTimeService.getRentalRejectTime(rejectTime);
        rental.setRejectTimes(rejectTimes);

        // 5. 拒绝接单和接单超时的订单数量
        String sql = "SELECT COUNT(*) FROM Order where rentalId = ? and (state = ? or state = ?)";
        long count = rentalService.getRecordCountByJpql(sql, rental.getRentalId(), Order.State.reject, Order.State.acceptTimeout);
        rental.setReject((int) count);

        // 6. 组织返回数据
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("rental", rental);
        return response;
    }

    /**
     * 根据条件查询租赁租赁信息</br>
     * 不传查询条件默认分页查询所有
     *
     * @param pagination
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query.do", method = {RequestMethod.POST})
    public JsonResponse query(HttpSession session, @RequestBody(required = false) Pagination<RentalInfo> pagination) {
        if (pagination == null) {
            return JsonResponse.error("请求信息不能为空");
        }
        if (pagination.getData() == null) pagination.setData(new RentalInfo());
        pagination = rentalService.query(pagination);
        SysmanUser user = (SysmanUser) session.getAttribute("adminCurrentUser");
        if (user != null) {
            isCollect(user.getPid(), pagination);
        }
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("pagination", pagination);
        return response;
    }

    /**
     * 上传租赁信息样图
     *
     * @param images
     * @param rentalId
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadDemo.do")
    public JsonResponse uploadDemo(//
                                   @RequestParam(value = "images", required = false) MultipartFile[] images,
                                   @RequestParam(value = "rentalId", required = false, defaultValue = "0") int rentalId
    ) throws IOException {
        if (rentalId == 0) {
            return JsonResponse.error("租赁信息ID不能为空");
        }
        if (images == null || images.length == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        int i = 0;
        for (MultipartFile image : images) {
            if (!image.isEmpty()) i++;
        }
        if (i == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        // 检测租赁信息是否存在
        RentalInfo rentalInfo = rentalService.getById(RentalInfo.class, rentalId);
        if (rentalInfo == null) {
            return JsonResponse.error("租赁信息信息不存在");
        }
        // 查询已有样图
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.rentalImage, "" + rentalId);
        //保存图片做多10张
        int count = 0;
        if (demos != null) count = demos.size();
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            UploadUtil.saveFile(image, imageInfos);
            count++;
            // 最多保持10张
            // if (count == 10) break;
        }
        // 这个是必须的要不然查不到
        for (ImageInfo info : imageInfos) {
            info.setCategory(ImageInfo.Category.rentalImage);//租赁信息图片
            info.setForeignKey("" + rentalId);//外键
            if (info.getSize() == ImageInfo.Size.small) {
                demos.add(info);
            }
        }
        imageService.batchSave(imageInfos);
        // 组织返回数据
        rentalInfo.setDemos(demos);
        JsonResponse response = JsonResponse.success("");
        response.setData("rentalInfo", rentalInfo);
        return response;
    }

    /**
     * 删除租赁信息原始照片
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
        if (image.getCategory() != ImageInfo.Category.rentalImage) {
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
        // 组织返回数据
        return JsonResponse.success("删除成功");
    }


    /**
     * 关键字分页搜索
     *
     * @param session
     * @param keys        关键字
     * @param pageSize    页大小
     * @param currentPage 当前页
     * @return
     */
    @ResponseBody
    @RequestMapping("/search.do")
    public JsonResponse search(
            HttpSession session,
            // 这个注释用于参数换行
            @RequestParam(value = "keys", required = false) String keys,
            @RequestParam(value = "pageSize", required = false, defaultValue = "30") int pageSize,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage)//
    {
        Pagination<RentalInfo> pagination = rentalService.search(keys, currentPage, pageSize);
        SysmanUser user = (SysmanUser) session.getAttribute("adminCurrentUser");
        if (user != null) {
            isCollect(user.getPid(), pagination);
        }
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("pagination", pagination);
        return response;
    }

    /**
     * 查询距离最近的
     *
     * @param longitude   经度
     * @param latitude    纬度
     * @param cityId      城市代码
     * @param pageSize    页面大小
     * @param currentPage 第几页
     * @return
     */
    @ResponseBody
    @RequestMapping("/getByDistance.do")
    public JsonResponse getByDistance(
            @RequestParam(value = "longitude", required = false, defaultValue = "0") String longitude,
            @RequestParam(value = "latitude", required = false, defaultValue = "0") String latitude,
            @RequestParam(value = "cityId", required = false, defaultValue = "0") int cityId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "30") int pageSize,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage

    ) {
        if ("0".equals(longitude) || "0".equals(latitude) || ValidateUtil.isEmpty(longitude) || ValidateUtil.isEmpty(latitude))
            return JsonResponse.error("地理位置信息不能为空");
        Pagination<RentalInfo> pagination = rentalService.getByDistance(longitude, latitude, cityId, pageSize, currentPage);
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("pagination", pagination);
        return response;
    }

    JsonResponse updateUserState(RentalInfo rentalInfo, int verify) {
        String reason = rentalInfo.getReason();
        rentalInfo = rentalService.getById(RentalInfo.class, rentalInfo.getRentalId());
        if (rentalInfo == null) {
            return JsonResponse.error("更新的租赁信息不存在");
        }
        rentalInfo.setReason(reason);
        rentalInfo.setVerify(verify);
        rentalService.update(rentalInfo);
        JsonResponse response = JsonResponse.success("更新成功");
        response.setData("rental", rentalInfo);
        return response;
    }

    /**
     * 添加租赁信息
     *
     * @param userId
     * @param brandId
     * @param cameraId
     * @param lensId
     * @param cover
     * @param snNumber
     * @param price
     * @param deposit
     * @param serialNumber
     * @param cityId
     * @param city
     * @param address
     * @param minRental
     * @param maxRental
     * @param autoAccept
     * @param purchaseDate
     * @param longitude
     * @param latitude
     * @param mark
     * @param memory
     * @param battery
     * @param charger
     * @param strap
     * @param cameraBag
     * @param cardReader
     * @param wirelessShutter
     * @param cleaningTool
     * @param waterproofShell
     * @param buckle
     * @param remoteControl
     * @param aircraft
     * @param lensHood
     * @param uv
     * @param lenBag
     * @param polarizer
     * @param ndMirror
     * @param grayGradient
     * @return
     */
    @ResponseBody
    @RequestMapping("/addRental.do")
    JsonResponse addRental(
            @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,
            @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
            @RequestParam(value = "cameraId", required = false, defaultValue = "0") int cameraId,
            @RequestParam(value = "lensId", required = false, defaultValue = "0") int lensId,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            @RequestParam(value = "snNumber", required = false, defaultValue = "") String snNumber,
            @RequestParam(value = "price", required = false, defaultValue = "0") double price,
            @RequestParam(value = "deposit", required = false, defaultValue = "0") double deposit,
            @RequestParam(value = "serialNumber", required = false, defaultValue = "") String serialNumber,
            @RequestParam(value = "cityId", required = false, defaultValue = "0") int cityId,
            @RequestParam(value = "city", required = false, defaultValue = "") String city,
            @RequestParam(value = "address", required = false, defaultValue = "") String address,
            @RequestParam(value = "minRental", required = false, defaultValue = "0") int minRental,
            @RequestParam(value = "maxRental", required = false, defaultValue = "0") int maxRental,
            @RequestParam(value = "autoAccept", required = false, defaultValue = "0") int autoAccept,
            @RequestParam(value = "purchaseDate", required = false, defaultValue = "0") long purchaseDate,
            @RequestParam(value = "longitude", required = false, defaultValue = "") String longitude,
            @RequestParam(value = "latitude", required = false, defaultValue = "") String latitude,
            @RequestParam(value = "mark", required = false, defaultValue = "") String mark,
            @RequestParam(value = "memory", required = false, defaultValue = "") String memory,
            @RequestParam(value = "battery", required = false, defaultValue = "0") int battery,
            @RequestParam(value = "charger", required = false, defaultValue = "0") int charger,
            @RequestParam(value = "strap", required = false, defaultValue = "0") int strap,
            @RequestParam(value = "cameraBag", required = false, defaultValue = "0") int cameraBag,
            @RequestParam(value = "cardReader", required = false, defaultValue = "0") int cardReader,
            @RequestParam(value = "wirelessShutter", required = false, defaultValue = "0") int wirelessShutter,
            @RequestParam(value = "cleaningTool", required = false, defaultValue = "0") int cleaningTool,
            @RequestParam(value = "waterproofShell", required = false, defaultValue = "0") int waterproofShell,
            @RequestParam(value = "buckle", required = false, defaultValue = "0") int buckle,
            @RequestParam(value = "remoteControl", required = false, defaultValue = "0") int remoteControl,
            @RequestParam(value = "aircraft", required = false, defaultValue = "0") int aircraft,
            @RequestParam(value = "lensHood", required = false, defaultValue = "0") int lensHood,
            @RequestParam(value = "uv", required = false, defaultValue = "0") int uv,
            @RequestParam(value = "lenBag", required = false, defaultValue = "0") int lenBag,
            @RequestParam(value = "polarizer", required = false, defaultValue = "0") int polarizer,
            @RequestParam(value = "ndMirror", required = false, defaultValue = "0") int ndMirror,
            @RequestParam(value = "grayGradient", required = false, defaultValue = "0") int grayGradient) throws IOException {


        if (userId == 0) return JsonResponse.error("机主ID不能为空");
        if (cityId == 0) return JsonResponse.error("请选择城市信息");
        if (ValidateUtil.isEmpty(city)) return JsonResponse.error("请选择城市信息");
        if (ValidateUtil.isEmpty(address)) return JsonResponse.error("请填写地址信息");
        if (cameraId == 0 && lensId == 0) return JsonResponse.error("请选择租赁信息相关的镜头或机身");
        if (price <= 0) return JsonResponse.error("租赁价格不能为空");
        if (cover == null || cover.isEmpty()) return JsonResponse.error("请上传封面照片");

        // 验证用户信息
        SysmanUser user = rentalService.getById(SysmanUser.class, userId);
        if (user == null) {
            return JsonResponse.error("用户信息有误");
        }
        if (ValidateUtil.isEmpty(user.getRealName()) || ValidateUtil.isEmpty(user.getUserName())) {
            return JsonResponse.error("请完善您的个人信息后再发布租赁信息");
        }
        if (user.getLocked() != SysmanUser.Locked.unlocked) {
            return JsonResponse.error("您的用户信息已经被锁定,请联系客服");
        }
        /*if (user.getVerify() != SysmanUser.Verify.verify) {
            return JsonResponse.error("您的用户信息还未通过审核");
        }*/

        RentalInfo rental = new RentalInfo();
        // 租赁信息的封面图片
        List<ImageInfo> imageInfos = new ArrayList<>();
        if (cover != null && !cover.isEmpty()) {
            ImageInfo imageInfo = UploadUtil.saveFile(cover, imageInfos);
            rental.setCover(imageInfo.getUrl());
        }

        rental.setBrandId(brandId);//品牌ID
        rental.setCameraId(cameraId);//相机ID
        rental.setLensId(lensId);//镜头id
        rental.setSnNumber(snNumber);//SN号
        rental.setPrice(price);//租赁价格
        rental.setDeposit(deposit);//押金
        rental.setSerialNumber(serialNumber);//序列号
        rental.setCityId(cityId);//城市ID
        rental.setCity(city);//城市名称
        rental.setAddress(address);//地址
        rental.setMinRental(minRental);//最小租期
        rental.setMaxRental(maxRental);//最大租期
        rental.setAutoAccept(autoAccept);//是否自动接单
        rental.setPurchaseDate(purchaseDate);//购买日期
        rental.setLongitude(longitude);//经度
        rental.setLatitude(latitude);//维度
        rental.setMark(mark);//备注

        /************************以下是机身信息********************/
        rental.setMemory(memory);//存储卡容量
        rental.setBattery(battery);//电池数量
        if (charger != RentalInfo.YesNo.nullVal) rental.setCharger(charger);//充电器
        if (strap != RentalInfo.YesNo.nullVal) rental.setStrap(strap);//肩带
        if (cameraBag != RentalInfo.YesNo.nullVal) rental.setCameraBag(cameraBag);//相机包
        if (cardReader != RentalInfo.YesNo.nullVal) rental.setCardReader(cardReader);//读卡器
        if (wirelessShutter != RentalInfo.YesNo.nullVal) rental.setWirelessShutter(wirelessShutter);//无线快门
        if (cleaningTool != RentalInfo.YesNo.nullVal) rental.setCleaningTool(cleaningTool);//清洁工具
        if (waterproofShell != RentalInfo.YesNo.nullVal) rental.setWaterproofShell(waterproofShell);//防水壳
        if (buckle != RentalInfo.YesNo.nullVal) rental.setBuckle(buckle);//扣具类
        if (remoteControl != RentalInfo.YesNo.nullVal) rental.setRemoteControl(remoteControl);//遥控器
        if (aircraft != RentalInfo.YesNo.nullVal) rental.setAircraft(aircraft);//飞行器

        /******************************************以下是镜头的***********************************/
        if (lensHood != RentalInfo.YesNo.nullVal) rental.setLensHood(lensHood);//遮光罩
        if (uv != RentalInfo.YesNo.nullVal) rental.setUv(uv);//UV镜
        if (lenBag != RentalInfo.YesNo.nullVal) rental.setLenBag(lenBag);//镜头袋
        if (polarizer != RentalInfo.YesNo.nullVal) rental.setPolarizer(polarizer);//偏光镜
        if (ndMirror != RentalInfo.YesNo.nullVal) rental.setNdMirror(ndMirror);//减光镜
        if (grayGradient != RentalInfo.YesNo.nullVal) rental.setGrayGradient(grayGradient);//灰渐变

        /********************************以下是默认值,和需要根据条件计算的值******************/
        rental.setReason("");//争议原因填写
        rental.setUserId(user.getPid());//机主用户ID
        rental.setName(user.getRealName());//机主姓名
        rental.setDeleted(RentalInfo.DeleteState.unDelete);//是否删除
        rental.setLocked(RentalInfo.Locked.unlocked);//未锁定
        rental.setVerify(RentalInfo.Verify.unVerify);//未审核
        rental.setRentalCount(0);//默认是0次租赁
        rental.setScore(0);//默认评分0
        rental.setCreateTime(new Date().getTime());//订单发布时间

        // 设置押金和相机型号信息
        double iDeposit = 0;
        Camera camera = null;
        if (rental.getCameraId() != 0) {//机身押金
            camera = rentalService.getById(Camera.class, rental.getCameraId());
            if (camera == null) {
                return JsonResponse.error("您选择的相机不存在");
            }
            iDeposit += camera.getDeposit();
        }
        CameraLens lens = null;
        if (rental.getLensId() != 0) {//镜头押金
            lens = rentalService.getById(CameraLens.class, rental.getLensId());
            if (lens == null) {
                return JsonResponse.error("您选择的镜头不存在");
            }
            iDeposit += lens.getDeposit();
        }
        if (iDeposit <= 0) return JsonResponse.error("押金不能为0");
        if (camera == null && lens == null) {
            return JsonResponse.error("您未选择的机身或镜头");
        }

        //rental.setAutoAccept(RentalInfo.AutoAccept.yes);//默认自动接单
        rental.setDeposit(iDeposit);//押金(来自相机数据维护)
        // 基本信息
        if (camera != null) {
            rental.setBrand(camera.getBrand());//品牌名称
            rental.setBrandId(camera.getBrandId());//品牌id
            rental.setModel(camera.getModel());//型号
        } else {
            rental.setBrand(lens.getBrand());//品牌名称
            rental.setBrandId(lens.getBrandId());//品牌ID
            rental.setModel(lens.getModel());//型号
        }

        // 镜头品牌信息
        if (lens != null) {
            rental.setLensId(lens.getLensId());
            rental.setLensBrand(lens.getBrand());
            rental.setLensModel(lens.getModel());
        }

        rentalService.add(rental);
        rentalService.batchSave(imageInfos);
        JsonResponse response = JsonResponse.success("添加成功");
        response.setData("rental", rental);
        return response;
    }

    /**
     * 更新封面图片
     *
     * @param rentalId 租赁信息ID
     * @param cover    封面图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateCover.do")
    public JsonResponse updateCover(
            @RequestParam(value = "rentalId", required = false, defaultValue = "0") int rentalId,
            @RequestParam(value = "cover", required = false) MultipartFile cover) throws IOException {
        if (rentalId == 0) return JsonResponse.error("租赁信息ID不能为空");
        if (cover == null || cover.isEmpty()) return JsonResponse.error("封面图片不能为空");
        RentalInfo temp = rentalService.getById(RentalInfo.class, rentalId);
        if (temp == null) {
            return JsonResponse.error("编辑的租赁信息不存在");
        }
        // 租赁信息的封面图片
        List<ImageInfo> imageInfos = new ArrayList<>();
        if (cover != null && !cover.isEmpty()) {
            ImageInfo imageInfo = UploadUtil.saveFile(cover, imageInfos);
            temp.setCover(imageInfo.getUrl());
        }
        rentalService.update(temp);
        JsonResponse response = JsonResponse.success("添加成功");
        response.setData("rental", temp);
        return response;
    }


    /**
     * 构造租赁信息是否已经被收藏
     *
     * @param userId
     * @param rentalInfos
     */
    private void isCollect(int userId, Pagination<RentalInfo> rentalInfos) {
        // 获取用户收藏的租赁信息
        List<CollectedRentalInfo> controllers = collectService.getByUser(userId);
        if (controllers == null || controllers.size() == 0) {
            return;
        }
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        for (CollectedRentalInfo collectedRentalInfo : controllers) {
            map.put(collectedRentalInfo.getRentalId(), collectedRentalInfo);
        }
        for (RentalInfo rentalInfo : rentalInfos.getRecordList()) {
            if (map.get(rentalInfo.getRentalId()) != null) {
                rentalInfo.setIsCollected(true);
            }
        }
    }
}
