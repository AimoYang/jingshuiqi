package com.jingshuiqi.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jingshuiqi.dao.CodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingshuiqi.util.UUIDGenerator;
import com.jingshuiqi.bean.Code;
import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.bean.Share;
import com.jingshuiqi.bean.ShareLink;
import com.jingshuiqi.bean.UserBase;
import com.jingshuiqi.dao.CodeMapper;
import com.jingshuiqi.dao.ShareMapper;
import com.jingshuiqi.dao.ShareLinkMapper;
import com.jingshuiqi.dao.UserBaseMapper;
import com.jingshuiqi.util.StatusCode;
import com.jingshuiqi.util.WeixinUtil;
import com.jingshuiqi.util.template.AccessToken;

@Service
public class LoginService {
	
	@Autowired
	private UserBaseMapper userBaseDao;
	@Autowired
	private RedisService redisService;
	@Autowired
	private CodeMapper codeDao;
	@Autowired
	private ShareMapper shareDao;
	@Autowired
	private ShareLinkMapper shareLinkDao;
	@Autowired 
	private CodeService codeService;
	@Autowired
	private CustomService customService;

	public Map<String, Object> findUserInfoByCode(String code) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		Code code2 = new Code();
		String uuid = UUIDGenerator.generate();
		//获取accessToken
		AccessToken accessToken = WeixinUtil.getCodeAccessToken(code);
		code2.setOpenid(accessToken.getOpenid());
		code2.setUuid(uuid);
		code2.setReCode(accessToken.getRefreshToken());
		//存入刷新code
		codeDao.saveCode(code2);
		//通过accessToken获取用户信息
		UserBase userBase = WeixinUtil.findUserBase(accessToken.getToken(), accessToken.getOpenid());
		JsonResult r = updateUserInfo(userBase);
		map.put("uuid",uuid);
		map.put("userBase", r.getData());
		return map;
	}

	public JsonResult shareBind(String bindUuid, String token) {
		JsonResult r = new JsonResult();
		ShareLink shareLink = shareLinkDao.findOpenidByBindUuid(bindUuid);
		String oneOpenid = shareLink.getOpenid();
		Share share = shareDao.findUserInfoForBind(token);
		if (token.equals(oneOpenid)) {
			r.setResult(StatusCode.FAIL);
			r.setMsg("绑定错误");
			return r;
		}
		if (share != null) {
			r.setResult(StatusCode.FAIL);
			r.setMsg("已绑定");
			return r;
		}else {
			UserBase usr = userBaseDao.findUserInfo(oneOpenid);
			if (usr == null) {
				r.setResult(StatusCode.FAIL);
				r.setMsg("分享无效");
				return r;
			}

			Share share2 = new Share();

			share2.setOpenId(token);
			share2.setParentOpenId(oneOpenid);
			share2.setCreateTime(new Date());
			
			
			try {
				shareDao.saveShareBind(share2);
			} catch (Exception e) {
				r.setResult(StatusCode.FAIL);
				r.setMsg("绑定失败");
				return r;
			}
			
			Share share3 = shareDao.findUserInfoForBind(oneOpenid);
			if (share3 == null) {
				share3 = new Share();
				share3.setOpenId(oneOpenid);
				share3.setCreateTime(new Date());
				shareDao.saveShareBind(share3);
			}
			
			UserBase userBase2 = (UserBase)redisService.get(token);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			customService.content(oneOpenid , df.format(new Date()) + " : " + userBase2.getNickName() + "成为你的粉丝" );
			
			r.setResult(StatusCode.SUCCESS);
			r.setMsg("绑定成功");	
		}
		return r;
	}
	
	private JsonResult updateUserInfo(UserBase userBase) {
		JsonResult r = new JsonResult();
		try {
			UserBase userBase2 = userBaseDao.findUserInfo(userBase.getOpenId());
			if (userBase2 != null) {
				//老用户更新信息
				userBase2.setLastLogin(new Date());
				userBase2.setIcon(userBase.getIcon());
				userBase2.setNickName(userBase.getNickName());
				int row = userBaseDao.updateUserInfo(userBase);
				if (row > 0) {
					boolean flag = redisService.set(userBase.getOpenId(), userBase2);
					if (flag) {
						r.setResult(StatusCode.SUCCESS);
					}else {
						r.setResult(StatusCode.FAIL);
						r.setMsg("缓存失败");
					}	
					r.setResult(StatusCode.SUCCESS);
					r.setMsg("用户更新成功");
				}else {
					r.setResult(StatusCode.FAIL);
					r.setMsg("用户更新失败");
				}
				if (userBase2.getIsEnable() == 1) {
					r.setResult(StatusCode.FAIL);
					r.setMsg("用户不合法");
					return r;
				}
				
				if (userBase2.getUserType() != 0) {
					ShareLink shareLink = shareLinkDao.findBindUuid(userBase.getOpenId());
					if (shareLink == null) {
						userBase2.setBindUuid(UUIDGenerator.generate());
						ShareLink shareLink2 = new ShareLink();
						shareLink2.setOpenid(userBase2.getOpenId());
						shareLink2.setUuid(userBase2.getBindUuid());
						shareLinkDao.saveBindUuid(shareLink2);
					}else {
						userBase2.setBindUuid(shareLink.getUuid());
					}
				}
				
				userBase2.setIcon(codeService.WeCode(userBase2.getIcon()));
				r.setData(userBase2);
			}else {
				//新用户注册
				userBase.setCreateTime(new Date());
				userBase.setLastLogin(new Date());
				userBase.setUserType(0);
				int row = userBaseDao.saveUserInfo(userBase);
				if (row > 0) {
					boolean flag = redisService.set(userBase.getOpenId(), userBase);
					if (flag) {
						r.setResult(StatusCode.SUCCESS);
					}else {
						r.setResult(StatusCode.FAIL);
						r.setMsg("缓存失败");
					}	
					r.setResult(StatusCode.SUCCESS);
					r.setMsg("用户保存成功");
				}else {
					r.setResult(StatusCode.FAIL);
					r.setMsg("用户保存失败");
				}
				
				userBase.setIcon(codeService.WeCode(userBase.getIcon()));
				r.setData(userBase);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	public JsonResult findUserInfoByReCode(String uuid) {
		JsonResult r = new JsonResult();
		Code code = codeDao.findCode(uuid);
		//通过刷新code获取accessToken
		AccessToken accessToken = WeixinUtil.refreshCodeAccessToken(code.getReCode());
		//刷新code拥有30天有效期若失效重新授权
		if (accessToken == null) {
			r.setResult(StatusCode.INVALID);
			r.setMsg("授权失效");
			return r;
		}
		UserBase userBase = WeixinUtil.findUserBase(accessToken.getToken(), accessToken.getOpenid());
		r = updateUserInfo(userBase);
		return r;
	}
	
	
}
