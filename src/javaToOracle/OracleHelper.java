package javaToOracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;

public class OracleHelper {

	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	public static final String URL = "jdbc:oracle:thin:@116.211.31.253:8522:orcl";

	public static final String USER = "whoadb";

	public static final String PWD = "geodept3";

	private static Connection zzh = null;

	private static PreparedStatement ps = null;

	private static ResultSet rs = null;

	public static DataSource source = null;

	public static Connection getCon() {
		try {

			Class.forName(DRIVER);

		} catch (ClassNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		try {

			zzh = DriverManager.getConnection(URL, USER, PWD);
			// System.out.println("connect");

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return zzh;

	}

	public static void closeAll() {

		if (rs != null)
			try {

				rs.close();

			} catch (SQLException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		if (ps != null)
			try {

				ps.close();

			} catch (SQLException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		if (zzh != null)
			try {

				zzh.close();

			} catch (SQLException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}

	}

	public static int update(String sql) {

		int resu = 0;
		zzh = getCon();
		try {

			ps = zzh.prepareStatement(sql);
			// for(int i=0;i<pras.length;i++){

			// ps.setString(i+1,pras[i]);//

			// resu=ps.executeUpdate();
			ps.executeUpdate();

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			closeAll();

		}
		return resu;

	}

	public static ResultSet query(String sql) {

		zzh = getCon();
		String pras[] = null;
		try {

			ps = zzh.prepareStatement(sql);

			if (pras != null)
				for (int i = 0; i < pras.length; i++) {

					ps.setString(i + 1, pras[i]);

				}
			rs = ps.executeQuery();

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return rs;

	}

	static void window(String type, String x1, String y1, String x2, String y2) throws SQLException {

		String id = null;
		String pos = null;
		if (type.equals("student")) {
			id = "s_id";
			pos = "s_pos";
		} else if (type.equals("building")) {
			id = "b_id";
			pos = "b_pos";
		} else if (type.equals("tramstops")) {
			id = "t_id";
			pos = "t_pos";
			type = "tram";
		}

		String sql = "select " + id + " from student where sdo_relate( " + type + "." + pos
				+ ", mdsys.sdo_geometry(2003,NULL,NULL," + " mdsys.sdo_elem_info_array(1,1003,3),"
				+ " mdsys.sdo_ordinate_array(" + x1 + "," + y1 + ", " + x2 + "," + y2 + ")), "
				+ "'mask=inside+coveredby querytype=window')=" + "'TRUE'";
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString(id));

		}

	}

	static void within(String id, String dis) throws SQLException {

		String sql = "select BUILDING.b_id as ID" + " from BUILDING,STUDENT" + " where" + " STUDENT.s_id='" + id
				+ "' AND" + " SDO_WITHIN_DISTANCE( BUILDING.B_POS, STUDENT.S_POS, 'distance=" + dis + "')='TRUE' "
				+ " UNION" + " select  TRAM.t_id as ID" + " from TRAM, STUDENT" + " where" + " STUDENT.s_id='" + id
				+ "' AND" + " SDO_WITHIN_DISTANCE( TRAM.T_POS ,STUDENT.S_POS,'distance=" + dis + "')='TRUE'";
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("ID"));

		}

	}

	static void neighbor(String type, String id, String raw) throws SQLException {

		String idd = null;
		String type2 = null;
		String pos = null;
		if (type.equals("building")) {
			type2 = "b2";
			idd = "b_id";
			pos = "b_pos";
		} else if (type.equals("student")) {
			type2 = "s2";
			idd = "s_id";
			pos = "s_pos";
		} else if (type.equals("tramstops")) {
			type = "tram";
			type2 = "t2";
			idd = "t_id";
			pos = "t_pos";
		}

		String sql = "select " + type + "." + idd + " as ID " + " from " + type + ", " + type + " " + type2 + ""
				+ " where " + "" + type2 + "." + idd + "='" + id + "'" + " and " + "" + type + "." + idd + " !='" + id
				+ "'" + " and " + "SDO_NN(" + type + "." + pos + ", " + type2 + "." + pos
				+ ", 'sdo_batch_size=2')='TRUE' AND" + " ROWNUM<=" + raw + "";

		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("ID"));

		}

	}

	private static void fixed5() throws SQLException {

		// TODO Auto-generated method stub
		String sql = "select t.X as X ,t.Y as Y from table("
				+ "SDO_UTIL.GETVERTICES((select sdo_aggr_mbr(building.b_pos)from building where building.b_name like 'SS%')))t";
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("X") + "   " + rs.getInt("Y"));

		}

	}

	private static void fixed4() throws SQLException {

		// TODO Auto-generated method stub
		String sql = "select * from(" + " select s_id,count(s_id)as rank" + " from student, building"
				+ " where SDO_NN(s_pos,b_pos, 'sdo_num_res=1')='TRUE'" + " group by s_id" + " order by rank DESC"
				+ " ) where rownum<=5";
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("s_id") + "   " + rs.getInt("rank"));

		}

	}

	private static void fixed3() throws SQLException {

		// TODO Auto-generated method stub
		String sql = "select * from(" + " select t_id, count(b_id) as NUM" + " from tram, building"
				+ " where SDO_WITHIN_DISTANCE( TRAM.T_POS ,building.b_pos,'distance=250')='TRUE'"
				+ " group by tram.T_ID" + " order by NUM DESC)" + " where rownum=1";
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("t_id") + "   " + rs.getInt("NUM"));

		}

	}

	private static void fixed2() throws SQLException {

		// TODO Auto-generated method stub
		String sql = "select s_id, t_id" + " from student, tram" + " where SDO_NN(t_pos,s_pos, 'sdo_num_res=2')='TRUE'";
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("s_id") + "   " + rs.getString("t_id"));

		}

	}

	private static void fixed1() throws SQLException {

		// TODO Auto-generated method stub
		String sql = "select s_id as ID from student,tram where t_id='t6ssl' and SDO_INSIDE(s_pos,t_pos)='TRUE'"
				+ " intersect"
				+ " select s_id  as ID from student,tram where t_id='t2ohe' and SDO_INSIDE(s_pos,t_pos)='TRUE'"
				+ " intersect"
				+ " select b_id as ID  from building,tram where t_id='t6ssl' and SDO_INSIDE(b_pos,t_pos)='TRUE'"
				+ " intersect"
				+ " select b_id as ID  from building,tram where t_id='t2ohe' and SDO_INSIDE(b_pos,t_pos)='TRUE'";
		// String a[]=null;
		query(sql);
		while (rs.next()) {

			System.out.println(rs.getString("ID"));

		}

	}

	public static void main(String[] args) throws SQLException {
		String  sql = "slect * form  tabbaseperson where chvid = 10829";
	    ResultSet rSet = OracleHelper.query(sql);
	    ResultSetMetaData md = rSet.getMetaData(); //获得结果集结构信息,元数据  
        int columnCount = md.getColumnCount();   //获得列数   
        while (rSet.next()) {  
            for (int i = 1; i <= columnCount; i++) {
            	System.out.println(md.getColumnName(i)+":"+rSet.getObject(i));
           }  
  
        }  
		int a;
		a = 1;
		getCon();

		try {

			// neighbor("building", "b3", "5");
			if (args[0].equals("window")) {

				String type = args[1];
				String x1 = args[2];
				String y1 = args[3];
				String x2 = args[4];
				String y2 = args[5];
				window(type, x1, y1, x2, y2);

			}

			else if (args[0].equals("within")) {

				String id = args[1];
				String dis = args[2];
				within(id, dis);

			}

			else if (args[0].equals("nearest-neighbor")) {

				String type = args[1];
				String id = args[2];
				String raw = args[3];
				neighbor(type, id, raw);

			}

			else if (args[0].equals("fixed")) {

				if (args[1].equals("1")) {

					fixed1();

				} else if (args[1].equals("2")) {

					fixed2();

				} else if (args[1].equals("3")) {

					fixed3();

				} else if (args[1].equals("4")) {

					fixed4();

				} else if (args[1].equals("5")) {

					fixed5();

				}

			}

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		closeAll();

	}

}
