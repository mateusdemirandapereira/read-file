package spc_serasa;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class DaoUtil {
	
	public static LocalDate getDate(ResultSet rs, int coluna) throws SQLException {
		Date data = rs.getDate(coluna);
		return data == null ? null : data.toLocalDate();
	}
	
	public static LocalTime getTime(ResultSet rs, int coluna) throws SQLException {
		Time time = rs.getTime(coluna);
		return time == null ? null : time.toLocalTime();
	}
	
	public static Double getDouble(ResultSet rs, int coluna) throws SQLException {
		BigDecimal bigDecimal = rs.getBigDecimal(coluna);
		return bigDecimal == null ? 0.0 : bigDecimal.doubleValue();
	}
	
	public static long getLong(ResultSet rs, int coluna) throws SQLException {
		return rs.getObject(coluna) == null ? 0L : rs.getLong(coluna);
	}
	
	public static int getInt(ResultSet rs, int coluna) throws SQLException {
		return rs.getObject(coluna) == null ? 0 : rs.getInt(coluna);
	}

}
