package com.dsfy.controller;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Region;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.exception.JsonException;
import com.dsfy.service.IRegionService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/RegionService", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class RegionService {

    @Resource(name = "RegionService")
    IRegionService regionService;

    /**
     * 根据父节点的主键查询地区信息 selecte * from region where parentId = id
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByParent.do")
    public JsonResponse getByParent(@RequestParam(required = false, value = "id", defaultValue = "0") int id) {
        JsonResponse response = new JsonResponse();
        List<Region> regions = regionService.getByPrentId(id);
        response.setData("regions", regions);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) Region region) {
        if (region == null || ValidateUtil.isEmpty(region.getName()) || region.getRegionType() == 0) {
            throw new JsonException("地区名称,或地区类型不能为空");
        }

        if (region.getParentId() != 0) {
            //如果父节点不为空
            Region temp = regionService.getById(Region.class, region.getParentId());
            if (temp == null) return JsonResponse.error("父节点不存在");
            if (temp.getRegionType() >= region.getRegionType()) return JsonResponse.error("子节点的地区类型错误");
        }

        regionService.add(region);
        JsonResponse response = new JsonResponse();
        response.setData("region", region);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Region region) {
        if (region == null || region.getRegionId() == 0 || ValidateUtil.isEmpty(region.getName()) || region.getRegionType() == 0) {
            throw new JsonException("id,地区名称,或地区类型不能为空");
        }
        regionService.update(region);
        JsonResponse response = new JsonResponse();
        response.setData("region", region);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Region region) {
        if (region == null || region.getRegionId() == 0) {
            return JsonResponse.error("id不能为空");
        }
        long chard = regionService.getRecordCount(Region.class, new QueryCondition("parentId", QueryCondition.EQ, region.getRegionId()));
        if (chard > 0) {
            return JsonResponse.error("该节点存在子节点不能删除");
        }
        regionService.delete(Region.class, region.getRegionId());
        JsonResponse response = new JsonResponse();
        response.setMessage("删除成功");
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getAll.do")
    public JsonResponse getAll() {
        JsonResponse response = JsonResponse.success("成功");
        response.setData("region", regionService.getAll(Region.class));
        return response;
    }

    /**
     * 获取类别树形数据
     *
     * @param regionId 父类别id
     * @param hasEmpty 是否添加{id:0,value:'-------'}空值 非0则添加
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tree.do", method = RequestMethod.GET)
    public List<Region.TreeData> tree(//
                                      @RequestParam(value = "regionId", defaultValue = "0", required = false) int regionId, //
                                      @RequestParam(value = "hasEmpty", defaultValue = "0", required = false) int hasEmpty // 是否添加-------空值
    ) {
        List<Region> allRegion = null;
        List<Region.TreeData> retVal = new ArrayList<>();
        if (regionId != 0) {
            regionService.getByPrentId(regionId);
        } else {
            allRegion = regionService.getAll(Region.class);
        }
        Region.TreeData rootNode = new Region.TreeData();
        rootNode.setParentId(-1);
        rootNode.setId(0);
        rootNode.setText("中国");
        rootNode.setRegionType(Region.Category.nullVal);
        // 如果数据库没有数据则直接返回虚构的根节点
        if (allRegion == null || allRegion.size() == 0) {
            if (hasEmpty != 0) {
                retVal.add(rootNode);
            }
            return retVal;
        }

        // 转换为TreeData数据
        List<Region.TreeData> treeDatas = new ArrayList<>();
        for (Region categorie : allRegion) {
            treeDatas.add(new Region.TreeData(categorie));
        }

        // 将数据按照parentId分组
        Map<Integer, List<Region.TreeData>> treeDataMap = new HashMap<>();
        for (Region.TreeData treeData : treeDatas) {
            List<Region.TreeData> list = treeDataMap.get(treeData.getParentId());
            if (list == null) {
                list = new ArrayList<>();
                treeDataMap.put(treeData.getParentId(), list);
            }
            list.add(treeData);
        }

        // 添加子节点
        for (Region.TreeData treeData : treeDatas) {
            treeData.setChildren(treeDataMap.get(treeData.getId()));
        }

        // 返回父节点为0(为空)的数据
        for (Region.TreeData treeData : treeDatas) {
            if (treeData.getParentId() == 0) {
                retVal.add(treeData);
            }
        }
        rootNode.setChildren(retVal);
        List<Region.TreeData> treeData = new ArrayList<>();
        treeData.add(rootNode);
        return treeData;
    }

    /**
     * 根据城市名称获取城市代码
     *
     * @param cityName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRegionByStr.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResponse getRegionByStr(@RequestParam(value = "cityName", required = false, defaultValue = "") String cityName) {
        if (ValidateUtil.isEmpty(cityName)) return JsonResponse.error("城市名称不能为空");
        List<Region> regions = regionService.get(Region.class, new QueryCondition("name", QueryCondition.LK, cityName));
        JsonResponse response = JsonResponse.success("查询成功");
        if (regions != null && regions.size() > 0) {
            response.setData("region", regions.get(0));
        }
        return response;
    }
}
