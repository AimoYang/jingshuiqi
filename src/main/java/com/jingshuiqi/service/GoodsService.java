package com.jingshuiqi.service;


import com.jingshuiqi.bean.Goods;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.bean.Records;
import com.jingshuiqi.bean.Sku;
import com.jingshuiqi.dao.GoodsMapper;
import com.jingshuiqi.dao.RecordsMapper;
import com.jingshuiqi.dao.SkuMapper;
import com.jingshuiqi.form.GoodsAllPage;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.RandomNum;
import com.jingshuiqi.util.ResultUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    private CodeService codeService;

    public JsonResult findGoodsInfo(String uuid, String token) {
        Map<String,Object> map = new HashMap<String, Object>(2);
        Goods goods = goodsMapper.findGoodsInfoByUuid(uuid);
        if (goods == null) {
            return ResultUtil.fail("该商品失效");
        }
        goods.setSkus(skuMapper.findSku(uuid));
        goods.setReserve(codeService.WeCode(goods.getThumb()));
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
        if (uuid == null || token == null){
            return ResultUtil.fail("信息为空");
        }
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
        Map<String, Object> map = new HashMap<String, Object>(2);
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

    public JsonResult findOneGoodsInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<Goods> list = goodsMapper.findOneGoodsInfo(pageObject);
        int row = goodsMapper.getOneGoodsInfoRow(pageObject);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findAllGoodsInfo(GoodsAllPage goodsAllPage) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<Goods> list = goodsMapper.findAllGoodsInfo(goodsAllPage);
        int row = goodsMapper.getAllGoodsInfoRow(goodsAllPage);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        goodsAllPage.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", goodsAllPage);
        return ResultUtil.success(map);
    }

    public JsonResult findHotGoodsInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<Goods> list = goodsMapper.findHotGoodsInfo(pageObject);
        int row = goodsMapper.getHotGoodsInfoRow(pageObject);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findRecommendGoodsInfo() {
        List<Goods> listAll = goodsMapper.findRecommendGoodsInfo();
        List<Goods> list = getRandomThreeInfoList(listAll);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        return ResultUtil.success(list);
    }


    public static List<Goods> getRandomThreeInfoList(List<Goods> list) {
        List<Goods> olist = new ArrayList<Goods>();
        if (list.size() <= 3) {
            return list;
        }else {
            for (int i = 0; i < 3; i++) {
                Random random = new Random();
                int num = random.nextInt(list.size());
                olist.add(list.get(num));
                list.remove(num);
            }
            return olist;
        }
    }

    public JsonResult findExchangeGoodsInfo(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<Goods> list = goodsMapper.findExchangeGoodsInfo(pageObject);
        int row = goodsMapper.getExchangeGoodsInfoRow(pageObject);
        for (Goods goods : list) {
            goods.setSkus(skuMapper.findSku(goods.getUuid()));
        }
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }

    public JsonResult findGiftGoodsInfo() {
        Goods goods = goodsMapper.findGiftGoodsInfo();
        if (goods == null){
            return ResultUtil.fail("暂无大礼包商品");
        }
        goods.setSkus(skuMapper.findSku(goods.getUuid()));
        return ResultUtil.success(goods);
    }
}
