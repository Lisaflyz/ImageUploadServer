package com.kevin.imageuploadserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/*
 * ����ͼƬ��·��String
 * ����getResult�ɵõ�ͼƬ������ID��
 * 
 */

public class GetCommand {
	static String resultLine = "the image is classified as:";
	static final int aheadLength = GetCommand.resultLine.length();
	private String filename;
	public GetCommand(String filename){
		this.filename = filename;
	}
	
	public String getResult(){
		String[]  command = new String[6];
		command[0] = "D:\\bin\\caffe_classification_1.exe";
		command[1] = "D:\\bin\\deploy_110_2.prototxt";
		command[2] = "D:\\bin\\samsung_110_refine_train_iter_70000.caffemodel";
		command[3] = "D:\\bin\\imagenet_mean_110.binaryproto";
		command[4] = "D:\\bin\\synset_words.txt";
		command[5] = filename;
		StringBuffer sb = new StringBuffer();
		String resultId = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);//���������Ϣ
			Process process = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			
			while (null != (line = br.readLine())) {//����һ�������
				System.out.println(line);
				if(line.length() > aheadLength){
					
					String aheadStr = line.substring(0, aheadLength);
					if(aheadStr.equals(GetCommand.resultLine)){
						resultId = line.substring(aheadLength, aheadLength + 1);
					}
					
				}
				sb.append(line);
				
				
			}
			process.destroy();
			br.close();
			//return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultId;
	}
		
	public static void main(String[] args) {
		
		//System.out.println(GetCommand.resultLine.length());
		GetCommand GC = new GetCommand("E:\\project\\data\\testData\\test_img2011\\img_1.jpg");
		String resultId= GC.getResult();
		System.out.println(resultId);
		
	}
	
	
	
}



