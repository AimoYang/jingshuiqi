package com.jingshuiqi.util.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.jingshuiqi.util.UUIDGenerator;

public class UploadFileUtils {
	public static String saveImgToQinniuyun(MultipartFile photo){
		String msg = "";
		try {
			String fileName3 = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf('.'));
			// 用getOriginalFilename的得到的文件名字；
			fileName3 = UUIDGenerator.generate() + "_" + fileName3;// 使文件名不重复
			System.out
					.println(" String fileName = photo[i].getOriginalFilename()获得的参数==="
							+ fileName3);
			MultipartFile uploadfile = photo;
			System.out
					.println("MultipartFile uploadfile=photo[i];uploadfile的值为：=~~~~~~~~~~~~~==="
							+ uploadfile);
			if (uploadfile.isEmpty()) {
				throw new Exception("图片不存在！");
			}
			// 将图片的信息转换为字节数组
			byte[] fileByte = uploadfile.getBytes();
			System.out.println("fileByte的值为~~~~~~~" + fileByte);
			// 七牛云上传开始
			Boolean flag = new QiniuyunUtils().uploadFile(fileByte, fileName3);
			if (flag) {
				msg = fileName3;
			} else {
				throw new Exception("上传异常");
			}
			System.out.println("第~~~~~~" + 1 + "张图片是否上传成功" + flag);
			System.out.println("第~~~~" + 1 + "photo的上传END~~~");
			System.out.println("..................................."
					+ photo.toString());
		} catch (Exception e) {
			System.out.println("异常是什么？++==" + e);
			msg = "图片上传失败";
		}
		return msg;
	}

	public static String saveImgToLocal(MultipartFile file , HttpServletRequest request) {
		String msg = "";
		// 获取路径
		String pathRoot = request.getSession().getServletContext()
				.getRealPath("/");
		System.out.println(pathRoot);
		String path = "";
		if (!file.isEmpty()) {
			// 生成uuid
			String uuid = UUIDGenerator.generate();
			// 上传类型
			String contentType = file.getContentType();
			// 上传后缀
			String imageName = contentType
					.substring(contentType.indexOf("/") + 1);
			// 保存路径
			path = "/upload/" + uuid + "." + imageName;
			File dest = new File(pathRoot + path);
			if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
				dest.getParentFile().mkdir();
			}
			try {
				file.transferTo(new File(pathRoot + path));
				msg = path;
			} catch (Exception e) {
				e.printStackTrace();
				msg = "服务器异常";
			}
		} else {
			msg = "图片不存在";
		}
		return msg;
	}
	
	public static String saveLocalImgToQin(String path , String fileName){
		String filePath = "";
		boolean flag = new QiniuyunUtils().uploadLocalImg(path, fileName);
		if (flag) {
			filePath = fileName;
		} else {
			filePath = "上传失败";
		}
		return filePath;
	}

}
