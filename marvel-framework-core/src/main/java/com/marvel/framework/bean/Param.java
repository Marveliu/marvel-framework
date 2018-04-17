package com.marvel.framework.bean;
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

import com.marvel.framework.util.CastUtil;
import com.marvel.framework.util.CollectionUtil;
import com.marvel.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求参数对象
 * 1. FileParam:文件参数
 * 2. FormParam:表单参数
 *
 * @author Marveliu
 * @since 11/04/2018
 **/

public class Param {

    private List<FileParam> fileParamList;
    private List<FormParam> formParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList,List<FileParam> fileParamList) {
        this.fileParamList = fileParamList;
        this.formParamList = formParamList;
    }

    /**
     * 获取请求参数的映射
     *
     * @return
     */
    public Map<String, Object> getFieldMap() {
        Map<String, Object> feildMap = new HashMap<String, Object>();
        if (CollectionUtil.isNotEmpty(formParamList)) {
            for (FormParam formParam : formParamList) {
                String feildName = formParam.getFieldName();
                Object feildValue = formParam.getFieldValue();
                // 检验是否已经存在字段名称
                if (feildMap.containsKey(feildName)) {
                    // 加入字段值，","进行分割
                    feildValue = feildMap.get(feildName) + StringUtil.SEPARATOR + feildValue;
                }
                feildMap.put(feildName, feildValue);
            }
        }
        return feildMap;
    }


    /**
     * 获取上传文件映射
     * @return
     */
    public Map<String, List<FileParam>> getFileMap() {
        Map<String, List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
        // 判断文件参数映射是否为空
        if (CollectionUtil.isNotEmpty(fileParamList)) {
            for (FileParam fileParam : fileParamList) {
                // 文件参数字段
                String fieldName = fileParam.getFieldName();
                // 对于单字段多文件参数进行处理
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName)) {
                    fileParamList = fileMap.get(fieldName);
                } else {
                    fileParamList = new ArrayList<FileParam>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName, fileParamList);
            }
        }
        return  fileMap;
    }

    /**
     * 获得所有上传文件
     * @param fieldName
     * @return
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获得单个文件
     * @param fieldName
     * @return
     */
    public FileParam getFile(String fieldName){
        List<FileParam> fileParamList = getFileList(fieldName);
        // 如果有多个文件返回null
        if(CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() == 1){
            return  fileParamList.get(0);
        }
        return null;
    }

    /**
     * 验证参数是否为空
     * @return
     */
    public boolean isEmpty(){
        // form参数和file参数全部为空，返回true
        return CollectionUtil.isEmpty(fileParamList) && CollectionUtil.isEmpty(formParamList);
    }


    /**
     * 根据参数名获取 String 型参数值
     */
    public String getString(String name) {
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 double 型参数值
     */
    public double getDouble(String name) {
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 long 型参数值
     */
    public long getLong(String name) {
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 int 型参数值
     */
    public int getInt(String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 boolean 型参数值
     */
    public boolean getBoolean(String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }
}
