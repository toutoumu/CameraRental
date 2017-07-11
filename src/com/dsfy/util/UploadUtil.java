package com.dsfy.util;

import com.dsfy.entity.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

/**
 * <Context docBase="/Users/apple/Desktop/test" path="/CameraRental/image/data" />
 * 用户名 administrator 密码 CongCongadmin888
 *
 * @author dell
 */
public class UploadUtil {

    /** 服务器域名 */
    //private static final String domain = "http://localhost:8080/CameraRental";
    //private static final String domain = "http://115.29.54.221:8080/CameraRental";
    private static final String domain = Constant.domain;

    /** 相对网址 */
    private static final String urlPath = "/image/data";

    /** 文件上传目录 */
    private static final String docBase = "/Users/apple/Desktop/test";
    /** 上传时生成的临时文件保存目录 */
    // private static final String tempDir = "d:\\image\\temp";
    /** 最大分辨率 */
    private static int maxImageWidth = 1920;
    /** 小图分辨率 */
    private static int minImageWidth = 200;

    private static float quality = 0.8f;

    /**
     * 保存文件并返回文件名
     *
     * @param file
     * @param list 用于返回图片信息集合
     * @return 返回小图信息
     * @throws IOException
     */
    public static ImageInfo saveFile(final MultipartFile file, final List<ImageInfo> list) throws IOException {
        if (file == null) {
            return null;
        }
        String oldName = URLDecoder.decode(file.getOriginalFilename(), "utf-8");// 获取上传的文件名
        String ext = getExtName(oldName);
        String uuid = UUID.randomUUID().toString();
        String bigNewName = uuid + ext;// 新的文件名
        String minNewName = uuid + ".small" + ext;// 新的文件名
        // 通过hash计算文件目录
        int hashcode = bigNewName.hashCode();
        int dir1 = hashcode & 0xf; // 0--15
        int dir2 = (hashcode & 0xf0) >> 4; // 0-15
        // 构造新的保存目录
        String saveDir = docBase + File.separator + dir1 + File.separator + dir2;
        File dirFile = new File(saveDir);
        if (!dirFile.exists()) {// 如果目录不存在,创建目录
            dirFile.mkdirs();
        }

        String bigImagePath = saveDir + File.separator + bigNewName;// 大图全路径(带文件名)
        String minImagePath = saveDir + File.separator + minNewName;// 小图全路径(带文件名)
        // 处理图片大小
        Point bigPoint = ImageUtil.resize(file.getBytes(), new File(bigImagePath), maxImageWidth, quality);
        Point minPoint = ImageUtil.resize(file.getBytes(), new File(minImagePath), minImageWidth, quality);
        String bigUrl = domain + urlPath + "/" + dir1 + "/" + dir2 + "/" + bigNewName;
        String minUrl = domain + urlPath + "/" + dir1 + "/" + dir2 + "/" + minNewName;

        ImageInfo bigImageInfo = new ImageInfo();
        bigImageInfo.setWidth((int) bigPoint.getX());
        bigImageInfo.setHeight((int) bigPoint.getY());
        bigImageInfo.setFullPath(bigImagePath);
        bigImageInfo.setFileName(bigNewName);
        bigImageInfo.setOrgName(oldName);
        bigImageInfo.setSaveDir(saveDir);
        bigImageInfo.setUrl(bigUrl);
        bigImageInfo.setSize(ImageInfo.Size.max);
        list.add(bigImageInfo);

        ImageInfo minImageInfo = new ImageInfo();
        minImageInfo.setWidth((int) minPoint.getX());
        minImageInfo.setHeight((int) minPoint.getY());
        minImageInfo.setFullPath(minImagePath);
        minImageInfo.setFileName(minNewName);
        minImageInfo.setOrgName(oldName);
        minImageInfo.setSaveDir(saveDir);
        minImageInfo.setUrl(minUrl);
        minImageInfo.setSize(ImageInfo.Size.small);
        list.add(minImageInfo);

        return minImageInfo;
    }

    /**
     * 获取文件扩展名
     *
     * @param s abc.jpg
     * @return .jpg
     */
    public static String getExtName(String s) {
        int i = s.indexOf(".");
        int leg = s.length();
        return (i > 0 ? (i + 1) == leg ? "" : s.substring(i, s.length()) : "");
    }
}