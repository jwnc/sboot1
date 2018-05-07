package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wnc.basic.BasicStringUtil;

public class DbFieldSqlUtil {
	public String tableCode;
	public String selectField;
	public String orderSql;
	public String addWhereFieldSql;

	public List<DbField> vcWhereField;
	public List<DbField> vcUpdateField;
	public List<DbField> vcInsertField;

	private Map map1;
	private Map map2;
	private Map map3;

	public DbFieldSqlUtil(String paramString1, String paramString2) {
		this.vcWhereField = new ArrayList<DbField>();
		this.vcUpdateField = new ArrayList<DbField>();
		this.vcInsertField = new ArrayList<DbField>();

		this.map1 = new HashMap<String, String>();
		this.map2 = new HashMap<String, String>();
		this.map3 = new HashMap<String, String>();

		this.tableCode = paramString1;

		this.orderSql = paramString2;
		this.addWhereFieldSql = "";

	}

	public boolean addSelectField(DbField paramDbField) {
		boolean b = false;
		if ((paramDbField != null)
				&& !(BasicStringUtil.isNull2String(paramDbField.fieldCode, paramDbField.fieldType))) {
			if (!this.map3.containsKey(paramDbField.fieldCode)) {
				if (BasicStringUtil.isNullString(paramDbField.fieldValue)) {
					selectField += paramDbField.fieldCode + ",";
				} else {
					selectField += paramDbField.fieldCode + " AS " + paramDbField.fieldValue.replace("'", "") + ",";
				}
				this.map3.put(paramDbField.fieldCode, "true");
				b = true;
			} else {
			}

		} else {
		}
		return b;
	}

	public boolean addInsertField(DbField paramDbField) {
		int i = 0;

		if ((paramDbField != null)
				&& !(BasicStringUtil.isNull2String(paramDbField.fieldCode, paramDbField.fieldType))) {
			if (!this.map1.containsKey(paramDbField.fieldCode)) {
				this.vcInsertField.add(paramDbField);
				this.map1.put(paramDbField.fieldCode, "true");
				i = 1;
			} else {
			}

		} else {
		}

		return i == 0 ? false : true;
	}

	public boolean addUpdateField(DbField paramDbField) {
		int i = 0;

		if ((paramDbField != null)
				&& !(BasicStringUtil.isNull2String(paramDbField.fieldCode, paramDbField.fieldType))) {
			if (!this.map2.containsKey(paramDbField.fieldCode)) {
				this.vcUpdateField.add(paramDbField);
				this.map2.put(paramDbField.fieldCode, "true");
				i = 1;
			} else {
			}

		} else {
		}

		return i == 0 ? false : true;
	}

	public boolean addWhereField(DbField paramDbField) {
		int i = 0;

		if ((paramDbField != null)
				&& !(BasicStringUtil.isNull2String(paramDbField.fieldCode, paramDbField.fieldType))) {
			this.vcWhereField.add(paramDbField);
			i = 1;
		} else {
			System.out.println("部分参数不能为空!");
		}

		return i == 0 ? false : true;
	}

	public String getDeleteSql() {
		String str1 = null;
		String str2 = null;

		try {
			if (!BasicStringUtil.isNullString(this.tableCode)) {
				str2 = getWhereSql();

				str1 = "DELETE FROM " + this.tableCode;

				if (!BasicStringUtil.isNullString(str2)) {
					str1 = str1 + " WHERE " + str2;
				}
			}
		} catch (Exception localException) {
			System.out.println(localException);
		}

		return str1;
	}

	public String getInsertSql() {
		String str1 = null;

		String str2 = "";
		String str3 = "";

		DbField localDbField = null;

		for (int i = 0; i < this.vcInsertField.size(); ++i) {
			localDbField = this.vcInsertField.get(i);
			if (!BasicStringUtil.isNull2String(localDbField.fieldCode, localDbField.fieldType)) {
				if (localDbField.fieldValue != null) {
					if ("STRING".equalsIgnoreCase(localDbField.fieldType)) {
						str2 = str2 + localDbField.fieldCode + ",";
						str3 = str3 + "'" + localDbField.fieldValue + "',";
					} else if ("ORIGINAL".equalsIgnoreCase(localDbField.fieldType)) {
						str2 = str2 + localDbField.fieldCode + ",";
						str3 = str3 + localDbField.fieldValue + ",";
					} else if ("NUMBER".equalsIgnoreCase(localDbField.fieldType)) {
						str2 = str2 + localDbField.fieldCode + ",";
						str3 = str3 + localDbField.fieldValue + ",";
					} else {
						str2 = str2 + localDbField.fieldCode + ",";
						str3 = str3 + "'" + localDbField.fieldValue + "',";
					}

				} else {
					str2 = str2 + localDbField.fieldCode + ",";
					str3 = str3 + "null,";
				}
			} else {
				return null;
			}
		}
		if (!BasicStringUtil.isNull3String(this.tableCode, str2, str3)) {
			str1 = "INSERT INTO " + this.tableCode + "(" + str2.substring(0, str2.length() - 1) + ")VALUES(";
			str1 = str1 + str3.substring(0, str3.length() - 1) + ")";
		}

		return str1;
	}

	public String getOrderSql() {
		return this.orderSql;
	}

	// / <summary>
	// / 获取查询的某些字段
	// / </summary>
	// / <returns></returns>
	public String getSelectField() {
		return this.selectField;
	}

	// / <summary>
	// / 获取查询语句
	// / </summary>
	// / <returns></returns>
	public String getSelectSql() {
		String str1 = null;
		String str2 = null;
		try {
			if (!BasicStringUtil.isNullString(this.tableCode)) {
				str2 = getWhereSql();

				if (!BasicStringUtil.isNullString(this.selectField)) {
					str1 = "SELECT " + this.selectField + " FROM " + this.tableCode;
				} else {
					str1 = "SELECT * FROM " + this.tableCode;
				}
				if (!BasicStringUtil.isNullString(str2)) {
					str1 = str1 + " WHERE " + str2;
				}
				if (!BasicStringUtil.isNullString(this.orderSql)) {
					str1 = str1 + " ORDER BY " + this.orderSql;
				}
			}

		} catch (Exception localException) {
			System.out.println(localException);
		}

		return str1;
	}

	public String getTableCode() {
		return this.tableCode;
	}

	public String getUpdateSql() {
		String str1 = null;
		Object localObject = null;
		String str2 = "";
		String str3 = null;

		DbField localDbField = null;
		try {
			if (!BasicStringUtil.isNullString(this.tableCode)) {
				for (int i = 0; i < this.vcUpdateField.size(); ++i) {
					if (!BasicStringUtil
							.isNullString(str3 = (localDbField = this.vcUpdateField.get(i)).getSelectFieldString())) {
						if (!BasicStringUtil.isNullString((String) localObject)) {
							localObject = localObject + "," + str3;
						} else {
							localObject = str3;
						}
					} else {
						return null;
					}
				}

				if (!BasicStringUtil.isNullString((String) localObject)) {
					str2 = getWhereSql();

					str1 = "UPDATE " + this.tableCode + " SET " + (String) localObject;

					if (!BasicStringUtil.isNullString(str2)) {
						str1 = str1 + " WHERE " + str2;
					}
				}
			}
		} catch (Exception localException) {
			System.out.println(localException);
		}

		return str1;
	}

	public String getWhereSql() {
		Object localObject = null;
		String str = null;

		DbField localDbField = null;

		for (int i = 0; i < this.vcWhereField.size(); ++i) {
			if (!BasicStringUtil.isNullString(str = (localDbField = this.vcWhereField.get(i)).getSelectFieldString())) {
				if (!BasicStringUtil.isNullString((String) localObject)) {
					if ("SQL".equalsIgnoreCase(localDbField.getFieldType())) {
						localObject = localObject + str;
					} else {
						localObject = localObject + " AND " + str;
					}
				} else {
					localObject = str;
				}
			} else {
				System.out.println("where语句参数不全!");
			}
		}

		if (!BasicStringUtil.isNullString(this.addWhereFieldSql)) {
			if (!BasicStringUtil.isNullString((String) localObject)) {
				localObject = this.addWhereFieldSql + " AND " + (String) localObject;
			} else {
				localObject = this.addWhereFieldSql;
			}
		}

		return (String) localObject;
	}

	public void setOrderField(String paramString) {
		this.orderSql = paramString;
	}

	public void setSelectField(String paramString) {
		this.selectField = paramString;
	}

	public void setTableCode(String paramString) {
		this.tableCode = paramString;
	}
}