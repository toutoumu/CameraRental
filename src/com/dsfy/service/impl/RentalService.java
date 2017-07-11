package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.RentalInfo;
import com.dsfy.service.IRentalService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RentalService")
public class RentalService extends BaseService implements IRentalService {

    @Override
    public Pagination<RentalInfo> query(Pagination<RentalInfo> pagination) {
        RentalInfo rentalInfo = pagination.getData();
        String jpql = "from RentalInfo c where 1=1 ";
        jpql += " and c.deleted=" + RentalInfo.DeleteState.unDelete;//未删除的

        if (rentalInfo != null) {
            if (rentalInfo.getCategory() != 0) {// 所属类别
                jpql += " and EXISTS ( select up.cameraId from Camera_Category up where up.cameraId = c.cameraId and up.categoryId = "
                        + rentalInfo.getCategory() + ")";
            }
            if (rentalInfo.getBrandId() != 0) {// 品牌
                jpql += " and c.brandId = " + rentalInfo.getBrandId();
            }

            if (rentalInfo.getUserId() != 0) {//用户ID
                jpql += " and c.userId = " + rentalInfo.getUserId();
            }
            if (rentalInfo.getCityId() != 0) {//城市ID
                jpql += " and c.cityId = " + rentalInfo.getCityId();
            }
            if (!ValidateUtil.isEmpty(rentalInfo.getCity())) {//城市名称
                jpql += " and c.city like '%" + rentalInfo.getCity() + "%'";
            }
            if (rentalInfo.getRentalId() != 0) {//租赁信息ID
                jpql += " and c.rentalId = " + rentalInfo.getRentalId();
            }
            if (!ValidateUtil.isEmpty(rentalInfo.getBrand())) {//品牌名称
                jpql += "  and (c.brand like '%" + rentalInfo.getBrand() + "%'";
                jpql += " or c.lensBrand like '%" + rentalInfo.getBrand() + "%')";
            }
            if (!ValidateUtil.isEmpty(rentalInfo.getName())) {//用户名
                jpql += " and c.name like '%" + rentalInfo.getName() + "%'";
            }
            if (!ValidateUtil.isEmpty(rentalInfo.getModel())) {//型号名称
                jpql += " and (c.model like '%" + rentalInfo.getModel() + "%'";
                jpql += " or c.lensModel like '%" + rentalInfo.getModel() + "%')";
            }
        }

        List<RentalInfo> rentalInfos = baseDao.getPageQuery(pagination.getCurrentPage(), pagination.getPageSize(), jpql);
        long count = baseDao.getRecordCountByJpql("select count(*) " + jpql.toString());
        pagination = new Pagination<>(pagination.getCurrentPage(), pagination.getPageSize(), count, rentalInfos);
        return pagination;
    }

    @Override
    public Pagination<RentalInfo> search(String keys, int currentPage, int pageSize) {
        if (ValidateUtil.isEmpty(keys)) {
            long count = baseDao.getRecordCountByJpql("select count(*) from RentalInfo where deleted=" + RentalInfo.DeleteState.unDelete);
            List<RentalInfo> rentalInfos = baseDao.getPageQuery(currentPage, pageSize, "from RentalInfo where deleted=" + RentalInfo.DeleteState.unDelete);
            Pagination<RentalInfo> pagination = new Pagination<>(currentPage, pageSize, count, rentalInfos);
            return pagination;
        }
        String[] keyArray = keys.split(" ");
        StringBuilder sql = new StringBuilder("from RentalInfo where ");
        StringBuilder where = new StringBuilder("(");
        int whereCount = 0;
        for (String string : keyArray) {
            if (!ValidateUtil.isEmpty(string)) {
                if (whereCount != 0) {
                    where.append(" or ");
                }
                where.append(" concat(ifnull(brand,''), ',', ifnull(model,''), ',', ifnull(snNumber,''), ',', ifnull(serialNumber,'')) like '%" + string.trim() + "%'");
                whereCount++;
                if (whereCount == 3) {
                    break;
                }
            }
        }
        where.append(")");
        where.append(" and deleted=" + RentalInfo.DeleteState.unDelete);
        List<RentalInfo> rentalInfos = baseDao.getPageQuery(currentPage, pageSize, sql.append(where).toString());
        long count = baseDao.getRecordCountByJpql("select count(*) " + sql.toString());
        Pagination<RentalInfo> pagination = new Pagination<>(currentPage, pageSize, count, rentalInfos);
        return pagination;
    }

    @Override
    public boolean isCollected(int rentalId, int userId) {
        String jpql = "select count(p.*) from RentalInfo  p where p.rentalId = ? and EXISTS ( select up.rentalId  from CollectedRentalInfo up where up.rentalId  = p.rentalId and up.userId = ?)";
        return baseDao.getRecordCountByJpql(jpql, rentalId, userId) > 0;

    }

    @Override
    public Pagination<RentalInfo> getByDistance(String longitude, String latitude, int cityId, int pageSize, int currentPage) {
        String jpal = "";
        long count = 0;
        if (cityId != 0) {
            jpal = "from RentalInfo where cityId=" + cityId + " order by abs(longitude -" + longitude + "), abs(latitude -" + latitude + ")";
            count = baseDao.getRecordCountByJpql("select count(*) from RentalInfo where cityId=" + cityId);
        } else {
            jpal = "from RentalInfo order by abs(longitude -" + longitude + "), abs(latitude -" + latitude + ")";
            count = baseDao.getRecordCountByJpql("select count(*) from RentalInfo");
        }

        List<RentalInfo> list = baseDao.getPageQuery(currentPage, pageSize, jpal);
        Pagination<RentalInfo> pagination = new Pagination<>(currentPage, pageSize, count, list);
        return pagination;
    }
}
