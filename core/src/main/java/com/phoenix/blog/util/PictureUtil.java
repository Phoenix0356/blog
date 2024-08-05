package com.phoenix.blog.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class PictureUtil {
    public static String saveOrUpdateFile(String pictureBase64, String originalPath, String path, boolean isAvatar){
        String alterFileName;
        try {
            //delete the uploaded image
            deleteImage(originalPath);

            //String resourcesPath = new ClassPathResource(path).getFile().getAbsolutePath();
            String resourcesPath = System.getProperty("user.dir")+path;

            // if user don't upload an image
            if (DataUtil.isEmptyData(pictureBase64)){
                if (isAvatar) {
                    return "default.jpg";
                } else return null;
            }
            // if user uploads an image
            else {
                //create save path and decode the Base64
                alterFileName = UUID.randomUUID().toString()+ ".jpg";
                String newPath = resourcesPath + alterFileName ;
                byte[] pictureBytes = Base64.getDecoder().decode(pictureBase64);
                //save the new image
                Files.write(Paths.get(newPath), pictureBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return alterFileName;
    }

    //if there is already an uploaded picture,delete it.
    public static void deleteImage(String path) throws IOException {
        if (!(path == null || path.isEmpty()) && !path.contains("default")) {
            Files.deleteIfExists(Paths.get(path));
        }
    }
}
