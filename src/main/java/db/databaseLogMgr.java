/*     */package db;

/*     */
/*     *//*     */import java.io.RandomAccessFile;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;

/*     */
/*     */
/*     */
/*     */public class databaseLogMgr
/*     */
{
	/*     */private static synchronized boolean _$1(String paramString1, String paramString2, String paramString3)
	/*     */
	{
		/* 17 */int i = 0;
		/* 18 */String str1 = null;
		/* 19 */String str2 = null;
		/* 20 */String str3 = "WEB-INF";
		/* 21 */String str4 = "dberrlog";
		/* 22 */String str5 = "dbLog.txt";
		/*     */try
		/*     */ {
			/* 26 */if (BasicStringUtil.isNotNullString(paramString1, paramString2, paramString3))
			/*     */
			{
				/* 30 */if (!BasicFileUtil.isExistFolder(str2 = "d:\\temp\\log\\database"))
				/*     */
				{
					/* 32 */str2 = BasicFileUtil.removeLastSplit(System.getProperty("java.home"));
					/*     */}
				/*     */
				/* 35 */if ((BasicStringUtil.isNotNullString(str2))
						&& (BasicFileUtil.makeDirectory(BasicFileUtil.getMakeFilePath(str2, str3, str4))))
				/*     */
				{
					/*     */RandomAccessFile tmp121_118 = new RandomAccessFile(BasicFileUtil.getMakeFilePath(str2,
							str3, str4, BasicDateUtil.getCurrentDateString() + "-" + str5), "rw");
					/*     */RandomAccessFile localRandomAccessFile;
					/* 37 */tmp121_118/* 39 */.seek((localRandomAccessFile = tmp121_118)/* 39 */.length());
					/* 40 */str1 = BasicDateUtil.getCurrentDateTimeString() + "\t";
					/* 41 */str1 = str1 + paramString1 + "\t";
					/* 42 */str1 = str1 + paramString2 + "\t";
					/* 43 */str1 = str1 + paramString3 + "\r\n";
					/*     */
					/* 45 */localRandomAccessFile.writeBytes(BasicStringUtil.GBK2ISO(str1));
					/* 46 */localRandomAccessFile.close();
					/*     */}
				/*     */}
			/*     */
			/*     */}
		/*     */catch (Exception localException)
		/*     */
		{
			/*     */}
		/*     */
		/* 55 */return i == 0 ? false : true;
		/*     */}

	/*     */
	/*     */public static void errorLog(String paramString1, String paramString2, String paramString3)
	/*     */
	{
		/* 12 */_$1(paramString1, paramString2, paramString3);
		/*     */}

	/*     */
	/*     */public static synchronized boolean writeRSMemoryRowLockLog(String paramString1, String paramString2,
			String paramString3)
	/*     */
	{
		/* 103 */int i = 0;
		/* 104 */String str1 = null;
		/* 105 */String str2 = null;
		/* 106 */String str3 = "WEB-INF";
		/* 107 */String str4 = "rslocklog";
		/* 108 */String str5 = "lockLog.txt";
		/*     */try
		/*     */ {
			/* 112 */if (BasicStringUtil.isNotNullString(paramString1, paramString2, paramString3))
			/*     */
			{
				/* 116 */if (!BasicFileUtil.isExistFolder(str2 = "d:\\temp\\log\\database"))
				/*     */
				{
					/* 118 */str2 = BasicFileUtil.removeLastSplit(System.getProperty("java.home"));
					/*     */}
				/*     */
				/* 121 */if ((BasicStringUtil.isNotNullString(str2))
						&& (BasicFileUtil.makeDirectory(BasicFileUtil.getMakeFilePath(str2, str3, str4))))
				/*     */
				{
					/*     */RandomAccessFile tmp121_118 = new RandomAccessFile(BasicFileUtil.getMakeFilePath(str2,
							str3, str4, BasicDateUtil.getCurrentDateString() + "-" + str5), "rw");
					/*     */RandomAccessFile localRandomAccessFile;
					/* 123 */tmp121_118/* 125 */.seek((localRandomAccessFile = tmp121_118)/* 125 */.length());
					/* 126 */str1 = BasicDateUtil.getCurrentDateTimeString() + "\t";
					/* 127 */str1 = str1 + paramString1 + "\t";
					/* 128 */str1 = str1 + paramString2 + "\t";
					/* 129 */str1 = str1 + paramString3 + "\r\n";
					/*     */
					/* 131 */localRandomAccessFile.writeBytes(BasicStringUtil.GBK2ISO(str1));
					/* 132 */localRandomAccessFile.close();
					/*     */}
				/*     */}
			/*     */
			/*     */}
		/*     */catch (Exception localException)
		/*     */
		{
			/*     */}
		/*     */
		/* 141 */return i == 0 ? false : true;
		/*     */}

	/*     */
	/*     */public static synchronized boolean writeSQLInjectionLog(String paramString1, String paramString2,
			String paramString3)
	/*     */
	{
		/* 60 */int i = 0;
		/* 61 */String str1 = null;
		/* 62 */String str2 = null;
		/* 63 */String str3 = "WEB-INF";
		/* 64 */String str4 = "SQLInjection";
		/* 65 */String str5 = "dbLog.txt";
		/*     */try
		/*     */ {
			/* 69 */if (BasicStringUtil.isNotNullString(paramString1, paramString2, paramString3))
			/*     */
			{
				/* 73 */if (!BasicFileUtil.isExistFolder(str2 = "d:\\temp\\log\\database"))
				/*     */
				{
					/* 75 */str2 = BasicFileUtil.removeLastSplit(System.getProperty("java.home"));
					/*     */}
				/*     */
				/* 78 */if ((BasicStringUtil.isNotNullString(str2))
						&& (BasicFileUtil.makeDirectory(BasicFileUtil.getMakeFilePath(str2, str3, str4))))
				/*     */
				{
					/*     */RandomAccessFile tmp121_118 = new RandomAccessFile(BasicFileUtil.getMakeFilePath(str2,
							str3, str4, BasicDateUtil.getCurrentDateString() + "-" + str5), "rw");
					/*     */RandomAccessFile localRandomAccessFile;
					/* 80 */tmp121_118/* 82 */.seek((localRandomAccessFile = tmp121_118)/* 82 */.length());
					/* 83 */str1 = BasicDateUtil.getCurrentDateTimeString() + "\t";
					/* 84 */str1 = str1 + paramString1 + "\t";
					/* 85 */str1 = str1 + paramString2 + "\t";
					/* 86 */str1 = str1 + paramString3 + "\r\n";
					/*     */
					/* 88 */localRandomAccessFile.writeBytes(BasicStringUtil.GBK2ISO(str1));
					/* 89 */localRandomAccessFile.close();
					/*     */}
				/*     */}
			/*     */
			/*     */}
		/*     */catch (Exception localException)
		/*     */
		{
			/*     */}
		/*     */
		/* 98 */return i == 0 ? false : true;
		/*     */}
	/*     */
}

/*
 * Location: F:\Program
 * Files\netsmart\apache-tomcat-6.0.35\webapps\ROOT\WEB-INF\
 * lib\commons-rsmemory.jar Qualified Name: database.databaseLogMgr JD-Core
 * Version: 0.5.4
 */