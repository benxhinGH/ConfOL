package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.SpeechSyncManager;

import org.apache.ibatis.session.SqlSession;

import util.GsonUtil;
import util.IdConverter;
import db.MybatisUtil;
import entity.ConfIng;
import entity.ConfOver;
import entity.HttpResult;
import entity.User;

/**
 * Servlet implementation class RoomServlet
 */
@WebServlet("/room")
public class RoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoomServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String channelId=request.getParameter("channel_id");
		String phonenumber=request.getParameter("phonenumber");
		SqlSession session=MybatisUtil.getSqlSession();
		User user=session.selectOne("selectUserByPhone",phonenumber);
		ConfIng confIng=session.selectOne("selectConfIngByChannelId",channelId);
		HttpResult res=new HttpResult();
		if(user==null){
			res.setCode(-1);
			res.setMsg("user not found");
		}else if(confIng==null){
			res.setCode(-2);
			res.setMsg("conference not found");
		}else{
			if(confIng.getOnline()==1&&user.getId()==Integer.valueOf(confIng.getMember())){
				closeRoom(confIng);
				res.setCode(1);
				res.setMsg("leave room success and room closed");
			}else{
				IdConverter member=new IdConverter(confIng.getMember());
				if(member.remove(user.getId())){
					confIng.setMember(member.toString());
					confIng.setOnline(confIng.getOnline()-1);
					session.update("updateConfIng",confIng);
					res.setCode(0);
					res.setMsg("leave success");
				}else{
					res.setCode(-3);
					res.setMsg("unknown error");
				}
			}
		}
		session.commit();
		session.close();
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String channelId=request.getParameter("channel_id");
		String phonenumber=request.getParameter("phonenumber");
		System.out.println("用户："+phonenumber+"请求进入房间："+channelId);
		
		HttpResult res=new HttpResult();
		SqlSession session=MybatisUtil.getSqlSession();
		ConfIng confIng=session.selectOne("selectConfIngByChannelId",channelId);
		User user=session.selectOne("selectUserByPhone",phonenumber);
		if(confIng==null){
			res.setCode(-1);
			res.setMsg("room not exist");
		}else if(user==null){
			res.setCode(-3);
			res.setMsg("user not exist");
		}else if(confIng.getCapacity()<=confIng.getOnline()){
			res.setCode(-2);
			res.setMsg("room boom");
		}else{
			IdConverter member=new IdConverter(confIng.getMember());
			IdConverter participator=new IdConverter(confIng.getParticipator());
			System.out.println("房间当前成员："+member.toString()+"房间参会成员:"+participator.toString());
			if(!member.add(user.getId())){
				res.setCode(-4);
				res.setMsg("unknown error");
			}else{
				System.out.println("修改member后：member="+member.toString());
				confIng.setOnline(confIng.getOnline()+1);
				confIng.setMember(member.toString());
				if(participator.add(user.getId())){
					confIng.setParticipator(participator.toString());
				}
				session.update("updateConfIng",confIng);
				session.commit();
				session.close();
				res.setCode(0);
				res.setMsg("enter room success");
			}
			
		}
		
		
		PrintWriter out=response.getWriter();
		out.write(GsonUtil.getGson().toJson(res));
		out.flush();
		out.close();
		
	}
	
	private void closeRoom(ConfIng conf) throws IOException{
		long t1=conf.getCreateTime();
		long t2=System.currentTimeMillis();
		
		int duration=(int)(t2-t1);
		System.out.println("会议创建时间："+t1+"当前系统时间:"+t2+"duration："+duration);
		ConfOver confOver=new ConfOver(conf.getTitle(),conf.getCreator(),conf.getCreateTime(),duration,conf.getParticipator());
		SqlSession session=MybatisUtil.getSqlSession();
		session.delete("deleteConfIngById",conf.getId());
		session.insert("insertConfOver",confOver);
		session.commit();
		session.close();
	}

}
