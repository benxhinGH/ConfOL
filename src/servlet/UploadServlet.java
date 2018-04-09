package servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;

import db.MybatisUtil;
import util.GsonUtil;
import entity.ConfFile;
import entity.HttpResult;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");  
        PrintWriter out = response.getWriter();  
        HttpResult res=new HttpResult();
  
        // �����ļ���Ŀ��������  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
  
        // �����ļ��ϴ�·��  
        String uploadDir = "D:\\confol\\upload\\";
        // ��ȡϵͳĬ�ϵ���ʱ�ļ�����·������·��ΪTomcat��Ŀ¼�µ�temp�ļ���  
        String temp = "D:\\confol\\temp\\"; 
        // ���û�������СΪ 5M  
        factory.setSizeThreshold(1024 * 1024 * 5);  
        // ������ʱ�ļ���Ϊtemp  
        factory.setRepository(new File(temp));  
        // �ù���ʵ�����ϴ����,ServletFileUpload ���������ļ��ϴ�����  
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setHeaderEncoding("utf-8");
  
        String channelId = null;
        String savePath = null;
        String fileName = null;
        SqlSession session=MybatisUtil.getSqlSession();
        // �����������List��  
        try {  
            List<FileItem> list = servletFileUpload.parseRequest(request);  
  
            for (FileItem item : list) {  
                String name = item.getFieldName();  
                if(name.equals("description")){
                	channelId=item.getString();
                }else if(name.equals("file")){
                	InputStream is = item.getInputStream();
                	fileName=item.getName();
                	savePath=uploadDir + fileName;
                	try {
						inputStream2File(is,savePath );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
                	}
            }
            
            ConfFile file=new ConfFile();
            file.setName(fileName);
            file.setConfChannelId(channelId);
            file.setPath(savePath);
            session.insert("insertConfFile",file);
            System.out.println("�����ļ���"+file.toString());
            session.commit();
  
            res.setCode(0);
            res.setMsg("success");
        } catch (FileUploadException e) {  
            e.printStackTrace();  
            res.setCode(1);
            res.setMsg("failure");
        }finally{
        	session.close();
        }
  
        out.write(GsonUtil.getGson().toJson(res));
        out.flush();  
        out.close();  
	}
	
  
    // ��ת�����ļ�  
    public static void inputStream2File(InputStream is, String savePath)  
            throws Exception {  
        System.out.println("the file path is  :" + savePath);  
        File file = new File(savePath);  
        InputStream inputSteam = is;  
        BufferedInputStream fis = new BufferedInputStream(inputSteam);  
        FileOutputStream fos = new FileOutputStream(file);  
        int f;  
        while ((f = fis.read()) != -1) {  
            fos.write(f);  
        }  
        fos.flush();  
        fos.close();  
        fis.close();  
        inputSteam.close();  
    }  

}
