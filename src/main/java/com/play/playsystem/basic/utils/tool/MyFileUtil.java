package com.play.playsystem.basic.utils.tool;


public class MyFileUtil {

    /**
     * 根据文件扩展名判断文件类型
     * @param extension 文件扩展名
     * @return 文件类型
     */
    public static String getFileType(String extension) {
        String document = "txt doc pdf ppt pps xlsx xls docx csv";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        String compress = "zip gzip rar 7z";
        if (image.contains(extension)) {
            return "image";
        } else if (document.contains(extension)) {
            return "document";
        } else if (music.contains(extension)) {
            return "music";
        } else if (video.contains(extension)) {
            return "video";
        } else if (compress.contains(extension)) {
            return "compress";
        } else {
            return "other";
        }
    }
}
