package com.jingshuiqi.service;


import com.jingshuiqi.bean.Goods;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.bean.Records;
import com.jingshuiqi.dao.GoodsMapper;
import com.jingshuiqi.dao.RecordsMapper;
import com.jingshuiqi.dao.SkuMapper;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/19 0019 18:33
 * @Description:
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private RecordsMapper recordsMapper;

    public JsonResult findGoodsInfo(String uuid, String token) {
        Map<String,Object> map = new HashMap<>(2);

        Goods goods = goodsMapper.findGoodsInfoByUuid(uuid);
        if (goods == null) {
            return ResultUtil.fail("该商品失效");
        }
        goods.setSkus(skuMapper.findSku(uuid));

        int rows = recordsMapper.findRecordsInfo(uuid, token);
        if (rows == 0) {
            map.put("isCollect", 0);
        }else {
            map.put("isCollect", 1);
        }
        map.put("goodsInfo", goods);
        return ResultUtil.success(map);
    }

    public JsonResult updateCollectInfo(String uuid, String token) {
        int rows = recordsMapper.findRecordsInfo(uuid, token);
        if (rows == 0) {
            Records records = new Records();
            records.setGoodsUuid(uuid);
            records.setOpenId(token);
            int row = recordsMapper.insertSelective(records);
            if (row <= 0) {
                return ResultUtil.fail("操作失败");
            }
        }else {
            int row = recordsMapper.deleteByCollectInfo(uuid, token);
            if (row <= 0) {
                return ResultUtil.fail("操作失败");
            }
        }
        return ResultUtil.success();
    }

    public JsonResult findCollectInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<>(2);
        List<Goods> list = goodsMapper.findCollectInfo(pageObject);
        int row = goodsMapper.getCollectInfoRow(pageObject);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }
}
