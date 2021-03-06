package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.SpeechSyncManager;

import org.apache.ibatis.session.SqlSession;

import util.GsonUtil;
import util.Util;
import db.MybatisUtil;
import entity.ConfIng;
import entity.HttpResult;

/**
 * Servlet implementation class ConfServlet
 */
@WebServlet("/conf_ing")
public class ConfIngServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfIngServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String selectType=request.getParameter("selectType");
		String channelId=request.getParameter("channelId");
		SqlSession session=MybatisUtil.getSqlSession();
		
		HttpResult<List<ConfIng>> httpRes=new HttpResult<List<ConfIng>>();
		List<ConfIng> dbRes = null;
		switch(selectType){
		case "all":
			dbRes=session.selectList("selectAllConfIng");
			break;
		case "one":
			dbRes=session.selectList("selectConfIngByChannelId", channelId);
			break;
		default:
			break;
		}
		session.commit();
		session.close();
		
		if(dbRes!=null){
			httpRes.setCode(0);
			httpRes.setMsg("query success");
			httpRes.setResult(dbRes);
		}else{
			httpRes.setCode(-1);
			httpRes.setMsg("query error");
		}
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(httpRes));
		out.flush();
		out.close();
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//��������
		String title=request.getParameter("title");
		int type=Integer.valueOf(request.getParameter("type"));
		String password=request.getParameter("password");
		String channelId=request.getParameter("channel_id");
		int capacity=Integer.valueOf(request.getParameter("capacity"));
		String creator=request.getParameter("creator");
		boolean hasfile=Boolean.valueOf(request.getParameter("hasfile"));
		long createTime=System.currentTimeMillis();
		
		ConfIng confIng=new ConfIng();
		confIng.setTitle(title);
		confIng.setType(type);
		confIng.setPassword(password);
		confIng.setChannelId(channelId);
		confIng.setCapacity(capacity);
		confIng.setCreator(creator);
		confIng.setHasfile(hasfile);
		confIng.setCreateTime(createTime);
		
		
		SqlSession session=MybatisUtil.getSqlSession();
		int i=session.insert("insertConfIng",confIng);
		confIng=session.selectOne("selectLatestConfIng");
		session.commit();
		session.close();
		
		HttpResult<ConfIng> res=new HttpResult<ConfIng>();
		res.setCode(0);
		res.setMsg("create success");
		res.setResult(confIng);
		
		SpeechSyncManager.getIntance().createRoom(confIng.getId());
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
		
		
	}

}
