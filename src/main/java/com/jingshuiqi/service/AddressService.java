package com.jingshuiqi.service;

import com.jingshuiqi.bean.Address;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dao.AddressMapper;
import com.jingshuiqi.form.AddressForm;
import com.jingshuiqi.util.PageObject;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 15:11
 * @Description:
 */
@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public JsonResult findUserAddressAll(PageObject pageObject) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        List<Address> list = addressMapper.findUserAddressAll(pageObject);
        int row = addressMapper.getUserAddressAllRow(pageObject);
        pageObject.setRowCount(row);
        map.put("list", list);
        map.put("pageObject", pageObject);
        return ResultUtil.success(map);
    }


    public JsonResult findUserAddressInfo(Integer id) {
        Address address = addressMapper.findUserAddressInfo(id);
        return ResultUtil.success(address);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult saveAddressInfo(AddressForm addressForm, String token) {
        Address address = new Address();
        BeanUtils.copyProperties(addressForm,address);
        address.setOpenId(token);
        address.setCreateTime(new Date());
        address.setUpdateTime(new Date());
        address.setSort(0);
        try {
            if (address.getIsDefault() == 1) {
                addressMapper.updateAddressInfoForZero(address.getOpenId());
            }
            addressMapper.insertSelective(address);
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.fail("保存失败");
        }
        return ResultUtil.success();
    }

    public JsonResult updateAddressInfo(Address address) {
        address.setIsDefault(null);
        int row = addressMapper.updateByPrimaryKeySelective(address);
        if (row <= 0){
            return ResultUtil.fail("更新失败");
        }
        return ResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult updateDefaultAddressInfo(Integer id, String token) {
        Address address = addressMapper.selectByPrimaryKey(id);
        if (address.getIsDefault() == 1) {
            int row = addressMapper.updateAddressInfoForZero(token);
            if (row <= 0){
                ResultUtil.fail("设置失败");
            }
        } else {
            try {
                addressMapper.updateAddressInfoForZero(token);
                addressMapper.updateDefaultAddressInfo(id);
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.fail("设置失败");
            }
        }
        return ResultUtil.success();
    }

}
