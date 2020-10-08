package JDBC.statementCRUD;

import JDBC.utils.JDBCUtils;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;



public class testInsert {
    @Test
    public void testInsert() throws Exception{
        long start = System.currentTimeMillis();
        Connection connection = JDBCUtils.getConnection();
        //1.设置为不自动提交数据
        connection.setAutoCommit(false);
        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 1;i<= 1000000;i++){
            ps.setString(1,"name_" + i);
            //1."攒"sql
            ps.addBatch();
            if(i % 500 == 0){
                //2.执行
                ps.executeBatch();
                //3.清空
                ps.clearBatch();
            }
        }
        //2.提交数据
        connection.commit();
        long end = System.currentTimeMillis();
        System.out.println("花费的时间为：" + (end - start));//1000000条:4978

        JDBCUtils.closeResource(connection,ps);
    }


}
