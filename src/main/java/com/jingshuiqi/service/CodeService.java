package com.jingshuiqi.service;


import org.springframework.stereotype.Service;
import com.jingshuiqi.util.Basa64;
import com.jingshuiqi.util.QRCodeUtil;

@Service
public class CodeService {
	
	public String Code(String url){
		String URL = QRCodeUtil.zxingCodeCreate(url, 430, 430);
		//http://file.houtianfu.com/
		return URL;
	}

	public String WeCode(String url) {
		return Basa64.ImageToBase64ByOnline(url);
	
	}

}
