package com.jingshuiqi.service;

import com.jingshuiqi.bean.Floor;
import com.jingshuiqi.bean.Goods;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dao.*;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import com.jingshuiqi.vo.IndexFloor;
import com.jingshuiqi.vo.IndexFloorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 16:48
 * @Description:
 */
@Service
public class BannerService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private FloorMapper floorMapper;
    @Autowired
    private FloorBindMapper floorBindMapper;

    public JsonResult findBannerById(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<Goods> list = goodsMapper.findBannerById(pageObject);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        int row = goodsMapper.getBannerRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findIndexFloorInfo(PageObject pageObject) {

        Map<String, Object> map = new HashMap<String, Object>(2);
        List<IndexFloor> list = floorMapper.findIndexFloorInfo(pageObject);
        for (IndexFloor floor : list) {
            List<IndexFloorInfo> indexFloorInfos = floorBindMapper.findFloorBind(floor.getId());
            floor.setIndexFloorInfos(indexFloorInfos);
        }
        int row = floorMapper.getIndexFloorRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findIndexFloorInfoById(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<IndexFloorInfo> list = floorBindMapper.findIndexFloorInfoById(pageObject);
        int row = floorBindMapper.getIndexFloorInfoByIdRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

}
