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
 * �������Ա��е�ͼƬ�ļ���Ϣ
 * ���أ�"ͼƬ�ϴ�ʧ��"
 * �򷵻أ�ͼƬ��IP��ַ��������Ϣ���ϳ�ͬһ��
 * */



public class UploadImageServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		����ͼƬ
		uploadImage(request, response);
//		����ͼƬ���û�Id
//		changeUserImage(request, response);
	}

	// �ϴ�ͼƬ�ļ�
	private void uploadImage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String message = "";
		String resultMessage = null;
		String absolutePath = "";
		try{//·�����ɣ��ļ��ϴ����ⲿtry
			DiskFileItemFactory dff = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(dff);
			List<FileItem> items = sfu.parseRequest(request);
			// ��ȡ�ϴ��ֶ�
			FileItem fileItem = items.get(0);
			// �����ļ���ΪΨһ��
			String filename = fileItem.getName();
			if (filename != null) {
				filename = IdGenertor.generateGUID() + "." + FilenameUtils.getExtension(filename);
			}
			// ���ɴ洢·��
			String storeDirectory = getServletContext().getRealPath("/files/images");
			System.out.println(storeDirectory);
			File file = new File(storeDirectory);
			if (!file.exists()) {
				file.mkdir();
			}
			String path = genericPath(filename, storeDirectory);//���ɹ�ϣ���ļ���ַ
			//System.out.println("path = " + path);
			// �����ļ����ϴ�
			try {
				fileItem.write(new File(storeDirectory + path, filename));//��ͼƬд���ļ�
				
				//�ɾ���·���õ�ͼƬ�ķ�����
				absolutePath = storeDirectory + path + "/" +filename;
				System.out.println("absolute path = " + absolutePath);
				GetCommand GC = new GetCommand(absolutePath);
				resultMessage= GC.getResult();
			    System.out.println(resultMessage);
				
			  //���·�����ɷ�����������
				String filePath = "/files/images" + path + "/" + filename;
				message = filePath;
				System.out.println("filePath = " + filePath);
				
				
				
				 
			} catch (Exception e) {
				message = "�ϴ�ͼƬʧ��";
			}
			
			
			
		} catch (Exception e) {
			message = "�ϴ�ͼƬʧ��";
		} finally {
			
			response.getWriter().write(message);
			if(resultMessage != null){
				response.getWriter().write(resultMessage);
			}
			
		}
	}
	
	//�����ļ��Ĵ��Ŀ¼
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
