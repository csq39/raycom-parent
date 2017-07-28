package io.raycom.common.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Spring 单元测试基类
 * @author caoshengquan
 * @version 2013-05-15
 */
@ActiveProfiles("production")
@ContextConfiguration(locations = {"/spring-context.xml"})
public class SpringTransactionalContextTests extends AbstractTransactionalJUnit4SpringContextTests {

	protected DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.dataSource = dataSource;
	}
	
	

	public static void main(String[] args) {
		//   TODO Auto-generated method stub
      int arr[]= new int[]{3,4,1,8,5,3,2,7};
      int index[]= new int[]{2,0,1,3,3,3,1,3,0,0,0};
      String guess="";
      for (int i : index) {
    	  guess+=arr[i];
      }
      System.out.println(guess);
	}

}
