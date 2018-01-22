import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import db.MybatisUtil;
import entity.User;


public class TestCase {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SqlSession session=MybatisUtil.getSqlSession();
		session.selectOne("insertUser", new User("1356666666","123456"));
		session.commit();
		session.close();
	}

}
