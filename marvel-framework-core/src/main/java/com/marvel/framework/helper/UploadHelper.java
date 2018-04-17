package com.marvel.framework.helper;
/*
 * Copyright [2018] [Marveliu]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.marvel.framework.bean.FileParam;
import com.marvel.framework.bean.FormParam;
import com.marvel.framework.bean.Param;
import com.marvel.framework.util.CollectionUtil;
import com.marvel.framework.util.FileUtil;
import com.marvel.framework.util.StreamUtil;
import com.marvel.framework.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传助手类
 * @author Marveliu
 * @since 17/04/2018
 **/

public final class UploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    // apache common fileupload 提供的servlet上传文件对象
    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext){
        // todo:临时文件池
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        LOGGER.info("temp upload repository:"+repository.getAbsolutePath());
        // servletFileUpload对象
        servletFileUpload = new ServletFileUpload(
                new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
        // 上传限制
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if(uploadLimit != 0){
            // 最大上传文件大小 1024*1024 = M
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
        }
    }

    /**
     * 判断是否为mutipart,多个文件
     * @param request
     * @return
     */
    public static boolean isMutipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    public static Param createParam(HttpServletRequest request) throws IOException{
        List<FileParam> fileParamList = new ArrayList<FileParam>();
        List<FormParam> formParamList = new ArrayList<FormParam>();
        try {
            // servletFileUpload解析request
            Map<String,List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            // 遍历解析结果
            if(CollectionUtil.isNotEmpty(fileItemListMap)){
                for (Map.Entry<String,List<FileItem>> fileItemListEntry:fileItemListMap.entrySet()){
                    // 字段
                    String fieldName = fileItemListEntry.getKey();
                    // 字段值
                    List<FileItem> fileItems = fileItemListEntry.getValue();
                    // 判断字段值是否为空
                    if(CollectionUtil.isNotEmpty(fileItems)){
                        for (FileItem fileItem : fileItems){
                            // 字段值属于表单字段
                            if(fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));
                            }else {
                                // 字段值属于文件字段
                                String fileName = FileUtil.getRealFileName(
                                        new String(fileItem.getName().getBytes(), "UTF-8"));
                                // 检验文件名是否为空
                                if(StringUtil.isNotEmpty(fileName)){
                                    // 获取文件相关信息
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream =fileItem.getInputStream();
                                    fileParamList.add(
                                            new FileParam(fieldName,fileName,fileSize,contentType,inputStream)
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("Create param failure...",e);
            throw new RuntimeException(e);
        }
        return new Param(formParamList,fileParamList);
    }

    //文件上传交付框架使用者调用

    /**
     * 上传文件
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath,FileParam fileParam){
        try {
            if(fileParam != null){
                String filePath = basePath +fileParam.getFileName();
                LOGGER.info("upload file at:"+filePath);
                // 创建文件
                FileUtil.createFile(filePath);
                // BufferIO操作
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream,outputStream);
            }
        }catch (Exception e){
            LOGGER.error("upload file failure..",e);
            throw  new RuntimeException(e);
        }
    }


    public static void uploadFile(String basePath,List<FileParam> fileParamList){
        try {

        }catch (Exception e){
            LOGGER.error("upload file failure..",e);
            throw  new RuntimeException(e);
        }
    }

}
