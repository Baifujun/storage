package com.baixs.storage.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author baifujun
 * @date 2021-09-07
 * @time 18:48
 */
@RestController
@RequestMapping("/storage")
@Validated
public class StorageController {
    private static final String FILE_ROOT_PATH = "/root/files/";

    @PostMapping
    public JSONObject uploadFile(@NotNull(message = "文件不能为空") MultipartFile file) {
        JSONObject response = new JSONObject();
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            File localFile = new File(FILE_ROOT_PATH
                    + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now())
                    + "_"
                    + file.getOriginalFilename());
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(localFile);
            IOUtils.copy(inputStream, outputStream);
            response.put("success", true);
            response.put("code", 0);
            response.put("msg", "上传成功");
            response.put("data", localFile.getName());
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("code", 400);
            response.put("msg", "上传失败");
            response.put("data", e.getMessage());
            return response;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    @GetMapping
    public void downloadFile(@NotNull(message = "文件名不能为空") String filename, HttpServletResponse response) throws IOException {
        InputStream fileInputStream = null;
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''"
                    + URLEncoder.encode(filename, "UTF-8"));
            fileInputStream = new FileInputStream(FILE_ROOT_PATH + filename);
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("code", 400);
            resp.put("msg", "下载失败");
            resp.put("data", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resp.toJSONString());
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(response.getOutputStream());
        }
    }

    @GetMapping("/list")
    public JSONObject listFile() {
        JSONObject response = new JSONObject();
        try {
            File directory = new File(FILE_ROOT_PATH);
            List<JSONObject> data = new LinkedList<>();
            for (String item : Arrays.stream(directory.listFiles())
                    .sorted(Comparator.comparing(file -> Long.valueOf(file.getName().split("_")[0])))
                    .sorted(Comparator.reverseOrder())
                    .map(file -> file.getName())
                    .collect(Collectors.toList())) {
                JSONObject json = new JSONObject();
                json.put("filename", item);
                data.add(json);
            }
            response.put("success", true);
            response.put("code", 0);
            response.put("msg", "查询成功");
            response.put("count", data.size());
            response.put("data", data);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("code", 400);
            response.put("msg", "请求失败");
            response.put("data", e.getMessage());
            return response;
        }
    }
}
