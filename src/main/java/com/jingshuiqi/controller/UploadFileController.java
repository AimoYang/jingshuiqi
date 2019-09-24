package com.jingshuiqi.controller;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.util.QiniuyunUtils;
import com.jingshuiqi.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/4 0004 13:11
 * @Description:
 */
@Api(value = "七牛云图片上传", tags = "七牛云图片上传")
@RestController
@RequestMapping("/images")
public class UploadFileController {

    @ApiOperation(value="文件上传", notes="根据url来上传文件") //@ApiOperation：描述一个类的一个方法，或者说一个接口
    @RequestMapping(value = "saveRepairType", method = RequestMethod.POST)
    public JsonResult saveRepairType(@ApiParam(value ="上传文件") @RequestParam("file")MultipartFile file, MultipartHttpServletRequest multipartRequest, HttpServletRequest request) throws Exception {
        String family_img = "";
        try {
            System.out.println("第"+1+"photo的BEGIN上传");
            //String fileName3 = photo.getOriginalFilename();
            //用getOriginalFilename的得到的文件名字；
            String fileName3 = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            //使文件名不重复
            fileName3 = UUID.randomUUID().toString().replaceAll("-", "") + fileName3;
            MultipartFile uploadfile = file;
            if (uploadfile.isEmpty()) {
                return ResultUtil.fail( "图片不存在!");
            }
            //将图片的信息转换为字节数
            //七牛云上传开始
            Boolean flag = QiniuyunUtils.upload(file,fileName3);
            if(flag){
                family_img = QiniuyunUtils.DOMAIN_OF_BUCKET + fileName3;
                return ResultUtil.success(family_img);
            }else {
                return ResultUtil.fail( "图片上传失败!");
            }
        } catch (Exception e) {
            System.out.println("异常是什么？++=="+e);
            return ResultUtil.fail( "图片上传失败!");
        }
    }


    @ApiOperation(value="文件上传", notes="根据url来上传文件") //@ApiOperation：描述一个类的一个方法，或者说一个接口
    @RequestMapping(value = "savePhoto", method = RequestMethod.POST)
    public JsonResult savePhoto(@ApiParam(value ="上传文件") @RequestParam("file")MultipartFile file, MultipartHttpServletRequest multipartRequest, HttpServletRequest request) throws Exception {
        String family_img = "";
        try {
            System.out.println("第"+1+"photo的BEGIN上传");
            String fileName3 = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            //使文件名不重复
            fileName3 = UUID.randomUUID().toString().replaceAll("-", "") + fileName3;
            MultipartFile uploadfile = file;
            if (uploadfile.isEmpty()) {
                return ResultUtil.fail( "图片不存在!");
            }
            //将图片的信息转换为字节数
            //七牛云上传开始
            Boolean flag = QiniuyunUtils.upload(file,fileName3);
            if(flag){
                family_img = QiniuyunUtils.DOMAIN_OF_BUCKET + fileName3;
                return ResultUtil.success(family_img);
            }else {
                return ResultUtil.fail( "图片上传失败!");
            }
        } catch (Exception e) {
            System.out.println("异常是什么？++=="+e);
            return ResultUtil.fail( "图片上传失败!");
        }
    }

    @ApiOperation(value="多文件上传", notes="多文件上传")
    @PostMapping("/savefiles")
    public JsonResult saveFiles(MultipartFile[] files) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] strings = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isEmpty()) {
                continue;
            }
            String fileExt = files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf('.'));
            String fullName = UUID.randomUUID().toString().replaceAll("-", "") + fileExt;
            //将图片的信息转换为字节数组
            QiniuyunUtils.upload(files[i],fullName);
            sb.append(fullName + ",");
            strings[i] = QiniuyunUtils.DOMAIN_OF_BUCKET + fullName;
        }
        String result = StringUtils.join(strings, ",");
        return ResultUtil.success(result);
    }

}
