package com.kevin.imageuploadserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/*
 * 接收来自表单中的图片文件信息
 * 返回："图片上传失败"
 * 或返回：图片的IP地址及分类信息，合成同一行
 * */



public class UploadImageServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		接收图片
		uploadImage(request, response);
//		接收图片与用户Id
//		changeUserImage(request, response);
	}

	// 上传图片文件
	private void uploadImage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String message = "";
		String resultMessage = null;
		String absolutePath = "";
		try{//路径生成，文件上传的外部try
			DiskFileItemFactory dff = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(dff);
			List<FileItem> items = sfu.parseRequest(request);
			// 获取上传字段
			FileItem fileItem = items.get(0);
			// 更改文件名为唯一的
			String filename = fileItem.getName();
			if (filename != null) {
				filename = IdGenertor.generateGUID() + "." + FilenameUtils.getExtension(filename);
			}
			// 生成存储路径
			String storeDirectory = getServletContext().getRealPath("/files/images");
			System.out.println(storeDirectory);
			File file = new File(storeDirectory);
			if (!file.exists()) {
				file.mkdir();
			}
			String path = genericPath(filename, storeDirectory);//生成哈希码文件地址
			//System.out.println("path = " + path);
			// 处理文件的上传
			try {
				fileItem.write(new File(storeDirectory + path, filename));//将图片写入文件
				
				//由绝对路径得到图片的分类结果
				absolutePath = storeDirectory + path + "/" +filename;
				System.out.println("absolute path = " + absolutePath);
				GetCommand GC = new GetCommand(absolutePath);
				resultMessage= GC.getResult();
			    System.out.println(resultMessage);
				
			  //相对路径，由服务器名往后
				String filePath = "/files/images" + path + "/" + filename;
				message = filePath;
				System.out.println("filePath = " + filePath);
				
				
				
				 
			} catch (Exception e) {
				message = "上传图片失败";
			}
			
			
			
		} catch (Exception e) {
			message = "上传图片失败";
		} finally {
			
			response.getWriter().write(message);
			if(resultMessage != null){
				response.getWriter().write(resultMessage);
			}
			
		}
	}
	
	//计算文件的存放目录
	private String genericPath(String filename, String storeDirectory) {
		int hashCode = filename.hashCode();
		int dir1 = hashCode&0xf;//0-15
		int dir2 = (hashCode&0xf0)>>4;//0-15
		
		String dir = "/"+dir1+"/"+dir2;
		
		File file = new File(storeDirectory,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
