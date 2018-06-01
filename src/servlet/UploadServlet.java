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
import entity.FileDescription;
import entity.HttpResult;
import entity.User;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String userHomeDir=System.getProperty("user.home");
       
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
        HttpResult<String> res=new HttpResult<String>();
  
        // 创建文件项目工厂对象  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
  
        // 设置文件上传路径  
        String fileDir = userHomeDir+"/confoldatas/upload/file/";
        //用户头像保存路径
        String imageDir= userHomeDir+"/confoldatas/upload/image/";
        // 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹  
        String temp = userHomeDir+"/confoldatas/temp/"; 
        // 设置缓冲区大小为 5M  
        factory.setSizeThreshold(1024 * 1024 * 5);  
        // 设置临时文件夹为temp  
        factory.setRepository(new File(temp));  
        // 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求  
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setHeaderEncoding("utf-8");
  
        FileDescription fileDes=null;
        String savePath = null;
        String fileName = null;
        SqlSession session=MybatisUtil.getSqlSession();
        // 解析结果放在List中  
        try {  
            List<FileItem> list = servletFileUpload.parseRequest(request);  
  
            for (FileItem item : list) {  
                String name = item.getFieldName();  
                if(name.equals("description")){
                	fileDes=GsonUtil.getGson().fromJson(item.getString(), FileDescription.class);
                	System.out.println("文件描述："+fileDes.toString());
                }else if(name.equals("file")){
                	InputStream is = item.getInputStream();
                	String nameRaw=item.getName();
                	System.out.println("nameRaw:"+nameRaw);
                	
                	if(fileDes.getType()==FileDescription.TYPE_USER_HEAD_IMAGE){
                		String suffix=nameRaw.substring(nameRaw.indexOf("."));
                		fileName=fileDes.getAdditionInfo()+"headImage"+System.currentTimeMillis()+suffix;
                		savePath=imageDir+fileName;
                	}else{
                		fileName=nameRaw;
                		savePath=fileDir + fileName;
                	}
                	
                	try {
						inputStream2File(is,savePath );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
                	}
            }
            
            if(fileDes.getType()==FileDescription.TYPE_CONF_FILE){
            	ConfFile file=new ConfFile();
            	file.setName(fileName);
            	file.setConfChannelId(fileDes.getAdditionInfo());
            	file.setPath(savePath);
            	session.insert("insertConfFile",file);
            	System.out.println("插入文件："+file.toString());
            	session.commit();
            	res.setCode(0);
            	res.setMsg("success");
            	res.setResult(savePath);
            	
            }else if(fileDes.getType()==FileDescription.TYPE_USER_HEAD_IMAGE){
            	String phoneNumber=fileDes.getAdditionInfo();
            	User user=session.selectOne("selectUserByPhone",phoneNumber);
            	if(user==null){
            		res.setCode(-1);
            		res.setMsg("user not found");
            	}else{
            		user.setHeadImageUrl(savePath);
            		session.update("updateUserInfo", user);
            		session.commit();
            		res.setCode(0);
            		res.setMsg("update user headImage infomation success");
            		res.setResult(savePath);
            	}
            }
            
            
  
            
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
	
  
    // 流转化成文件  
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
