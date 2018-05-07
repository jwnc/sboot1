/*     */package db;

import com.wnc.basic.BasicStringUtil;

/*     */
/*     */public class DbField/*     */ implements Cloneable
/*     */
{
	/*     */public static final String TYPE_STRING = "STRING";
	/*     */public static final String TYPE_NUMBER = "NUMBER";
	/*     */public static final String TYPE_ORIGINAL = "ORIGINAL";
	/*     */public static final String TYPE_SQL = "SQL";
	/*     */public String fieldCode;
	/*     */public String fieldValue;
	/*     */public String fieldType;

	/*     */
	/*     */public DbField(String paramString1, String paramString2)
	/*     */
	{
		/* 18 */this.fieldCode = paramString1;
		/* 19 */this.fieldValue = paramString2;
		/* 20 */this.fieldType = "STRING";
		/*     */}

	/*     */
	/*     */public DbField(String paramString1, String paramString2, String paramString3)
	/*     */
	{
		/* 25 */this.fieldCode = paramString1;
		/* 26 */this.fieldValue = paramString2;
		/* 27 */this.fieldType = paramString3;
		/*     */}

	/*     */
	/*     */@Override
	public Object clone()
	/*     */
	{
		/* 100 */DbField localDbField = null;
		/*     */try
		/*     */ {
			/* 104 */localDbField = (DbField) super.clone();
			/*     */}
		/*     */catch (Exception localException)
		/*     */
		{
			/*     */}
		/*     */
		/* 111 */return localDbField;
		/*     */}

	/*     */
	/*     */public String getFieldCode()
	/*     */
	{
		/* 42 */return this.fieldCode;
		/*     */}

	/*     */
	/*     */public String getFieldType()
	/*     */
	{
		/* 47 */return this.fieldType;
		/*     */}

	/*     */
	/*     */public String getFieldValue()
	/*     */
	{
		/* 57 */return this.fieldValue;
		/*     */}

	/*     */
	/*     */public String getSelectFieldString()
	/*     */
	{
		/* 62 */String str = null;
		/*     */
		/* 64 */if (BasicStringUtil.isNotNullString(this.fieldCode, this.fieldType))
		/*     */
		{
			/* 66 */if (this.fieldValue != null)
			/*     */
			{
				/* 68 */if ("STRING".equalsIgnoreCase(this.fieldType))
				/*     */
				{
					/* 70 */str = this.fieldCode + "='" + this.fieldValue + "'";
					/*     */}
				/* 72 */else if ("ORIGINAL".equalsIgnoreCase(this.fieldType))
				/*     */
				{
					/* 74 */str = this.fieldCode + " " + this.fieldValue;
					/*     */}
				/* 76 */else if ("NUMBER".equalsIgnoreCase(this.fieldType))
				/*     */
				{
					/* 78 */str = this.fieldCode + "=" + this.fieldValue;
					/*     */}
				/* 80 */else if ("SQL".equalsIgnoreCase(this.fieldType))
				/*     */
				{
					/* 82 */str = this.fieldValue;
					/*     */}
				/*     */else
				/*     */ {
					/* 86 */str = this.fieldCode + "='" + this.fieldValue + "'";
					/*     */}
				/*     */
				/*     */}
			/*     */else {
				/* 91 */str = this.fieldCode + "=null";
				/*     */}
			/*     */}
		/*     */
		/* 95 */return str;
		/*     */}

	/*     */
	/*     */public void setFieldCode(String paramString)
	/*     */
	{
		/* 32 */this.fieldCode = paramString;
		/*     */}

	/*     */
	/*     */public void setFieldType(String paramString)
	/*     */
	{
		/* 37 */this.fieldType = paramString;
		/*     */}

	/*     */
	/*     */public void setFieldValue(String paramString)
	/*     */
	{
		/* 52 */this.fieldValue = paramString;
		/*     */}
	/*     */
}

/*
 * Location: F:\Program
 * Files\netsmart\apache-tomcat-6.0.35\webapps\ROOT\WEB-INF\
 * lib\commons-rsmemory.jar Qualified Name: database.DbField JD-Core Version:
 * 0.5.4
 */