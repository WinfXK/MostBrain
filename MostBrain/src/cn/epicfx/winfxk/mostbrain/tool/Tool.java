package cn.epicfx.winfxk.mostbrain.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.nukkit.Server;

/**
 * @author Winfxk
 */
public class Tool implements X509TrustManager, HostnameVerifier {
	private static String colorKeyString = "123456789abcdef";
	private static String randString = "-+abcdefghijklmnopqrstuvwxyz_";

	/**
	 * 获取服务器当前的语言
	 * 
	 * @return
	 */
	public static String getLanguage() {
		return getLanguages().get(Server.getInstance().getLanguage().getName());
	}

	/**
	 * 返回对应语言的简写
	 * 
	 * @return
	 */
	public static Map<String, String> getLanguages() {
		Map<String, String> Languages = new HashMap<>();
		Languages.put("English", "eng");
		Languages.put("中文(简体)", "chs");
		Languages.put("中文(繁體)", "cht");
		Languages.put("日本語", "jpn");
		Languages.put("Pyccĸий", "rus");
		Languages.put("Español", "spa");
		Languages.put("Polish", "pol");
		Languages.put("Português", "bra");
		Languages.put("한국어", "kor");
		Languages.put("Українська", "ukr");
		Languages.put("Deutsch", "deu");
		Languages.put("Lietuvių", "ltu");
		Languages.put("Indonesian", "idn");
		Languages.put("Czech", "cze");
		Languages.put("Turkish", "tur");
		Languages.put("Suomi", "fin");
		return Languages;
	}

	/**
	 * 将一个不知道什么玩意转换为Long
	 * 
	 * @param obj
	 * @return
	 */
	public static long objToLong(Object obj) {
		return objToLong(obj, 0L);
	}

	/**
	 * 将一个不知道什么玩意转换为Long
	 * 
	 * @param obj
	 * @param d
	 * @return
	 */
	public static long objToLong(Object obj, long d) {
		String string = String.valueOf(obj);
		if (obj == null || string.isEmpty())
			return d;
		try {
			return Long.valueOf(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 将一个不知道什么玩意转换为双精
	 * 
	 * @param obj
	 * @return
	 */
	public static float objToFloat(Object obj) {
		return objToFloat(obj, 0f);
	}

	/**
	 * 将一个不知道什么玩意转换为双精
	 * 
	 * @param obj
	 * @param d
	 * @return
	 */
	public static float objToFloat(Object obj, Float d) {
		String string = String.valueOf(obj);
		if (obj == null || string.isEmpty())
			return d;
		try {
			return Float.valueOf(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 将一个不知道什么玩意转换为双精
	 * 
	 * @param obj
	 * @return
	 */
	public static double objToDouble(Object obj) {
		return objToDouble(obj, 0d);
	}

	/**
	 * 将一个不知道什么玩意转换为双精
	 * 
	 * @param obj
	 * @param d
	 * @return
	 */
	public static double objToDouble(Object obj, Double d) {
		String string = String.valueOf(obj);
		if (obj == null || string.isEmpty())
			return d;
		try {
			return Double.valueOf(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 求最大公约数
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static long getGys(long num1, long num2) {
		num1 = Math.abs(num1);
		num2 = Math.abs(num2);
		while (num2 != 0) {
			long remainder = num1 % num2;
			num1 = num2;
			num2 = remainder;
		}
		return num1;
	}

	/**
	 * 获取小数长度
	 * 
	 * @param f
	 * @return
	 */
	public static int getDecimalsLength(float f) {
		String string = String.valueOf(f);
		if (f == 0 || string.indexOf(".") < 0)
			return 0;
		return string.substring(string.indexOf(".") + 1).length();
	}

	/**
	 * 小数转分数
	 * 
	 * @param f int[分子,分母]
	 * @return
	 */
	public static long[] getGrade(float f, int floatLength) {
		if (f == 0)
			return new long[] { 0, 0 };
		String string = String.valueOf(f);
		if (string.indexOf(".") < 0)
			return new long[] { (long) f, 1 };
		String sint = string.substring(0, string.indexOf("."));
		String sfloat = string.substring(string.indexOf(".") + 1);
		long Fenmu = 1;
		for (int k = 0; k < floatLength; k++)
			Fenmu *= 10;
		long Fenzi = Long.parseLong(sint + sfloat);
		long lXs = (Fenzi < Fenmu) ? Fenzi : Fenmu, j = 1;
		for (j = lXs; j > 1; j--)
			if (Fenzi % j == 0 && Fenmu % j == 0)
				break;
		Fenzi = Fenzi / j;
		Fenmu = Fenmu / j;
		return new long[] { Fenzi, Fenmu };
	}

	/**
	 * Object对象转换为String
	 * 
	 * @param obj
	 * @return
	 */
	public static String objToString(Object obj) {
		return objToString(obj, null);
	}

	/**
	 * Object对象转换为String
	 * 
	 * @param obj
	 * @param string
	 * @return
	 */
	public static String objToString(Object obj, String string) {
		if (obj == null)
			return string;
		return String.valueOf(obj);
	}

	/**
	 * 将秒长度转换为日期长度
	 * 
	 * @param time 秒长度
	 * @return
	 */
	public static String getTimeBy(double time) {
		int y = (int) (time / 31556926);
		time = time % 31556926;
		int d = (int) (time / 86400);
		time = time % 86400;
		int h = (int) (time / 3600);
		time = time % 3600;
		int i = (int) (time / 60);
		double s = time % 60;
		return (y > 0 ? y + "年" : "") + (d > 0 ? d + "天" : "") + (h > 0 ? h + "小时" : "") + (i > 0 ? i + "分钟" : "")
				+ (s > 0 ? Double2(s, 1) + "秒" : "");
	}

	/**
	 * 判断两个ID是否匹配，x忽略匹配
	 * 
	 * @param ID1 第一个ID
	 * @param ID2 第二个ID
	 * @return
	 */
	public static boolean isMateID(Object id1, Object id2) {
		String ID1 = String.valueOf(id1), ID2 = String.valueOf(id2);
		if (ID1 == null || ID2 == null)
			return false;
		if (!ID1.contains(":"))
			ID1 += ":0";
		if (!ID2.contains(":"))
			ID2 += ":0";
		String[] ID1s = ID1.split(":"), ID2s = ID2.split(":");
		if (ID1s[0].equals("x") || ID2s[0].equals("x") || ID1s[0].equals(ID2s[0]))
			if (ID1s[1].equals("x") || ID2s[1].equals("x") || ID2s[1].equals(ID1s[1]))
				return true;
			else
				return false;
		else
			return false;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		return time.format(new Date());
	}

	/**
	 * 返回当前时间 <年-月-日>
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
		return data.format(new Date());
	}

	/**
	 * 自动检查ID是否包含特殊值，若不包含则默认特殊值为0后返回数组
	 * 
	 * @param ID 要检查分解的ID
	 * @return int[]{ID, Damage}
	 */
	public static int[] IDtoFullID(Object ID) {
		return IDtoFullID(ID, 0);
	}

	/**
	 * 自动检查ID是否包含特殊值，若不包含则设置特殊值为用户定义值后返回数组
	 * 
	 * @param ID     要检查的ID
	 * @param Damage 要默认设置的特殊值
	 * @return int[]{ID, Damage}
	 */
	public static int[] IDtoFullID(Object obj, int Damage) {
		String ID = "0";
		if (obj != null && !String.valueOf(obj).isEmpty())
			ID = String.valueOf(obj);
		if (!ID.contains(":"))
			ID += ":" + Damage;
		String[] strings = ID.split(":");
		try {
			return new int[] { Integer.valueOf(strings[0]), Integer.valueOf(strings[1]) };
		} catch (Exception e) {
			return new int[] { 0, 0 };
		}
	}

	/**
	 * 获取随机数
	 * 
	 * @param min 随机数的最小值
	 * @param max 随机数的最大值
	 * @return
	 */
	public static int getRand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	/**
	 * 获取随机数
	 * 
	 * @return
	 */
	public static int getRand() {
		return getRand(0, (Integer.MAX_VALUE - 1) / 2);
	}

	/**
	 * 返回一个随机颜色代码
	 * 
	 * @return
	 */
	public static String getRandColor() {
		return getRandColor(colorKeyString);
	}

	/**
	 * 返回一个随机颜色代码
	 * 
	 * @param ColorFont 可以随机到的颜色代码
	 * @return
	 */
	public static String getRandColor(String ColorFont) {
		int rand = Tool.getRand(0, ColorFont.length() - 1);
		return "§" + ColorFont.substring(rand, rand + 1);
	}

	/**
	 * 将字符串染上随机颜色
	 * 
	 * @param Font 要染色的字符串
	 * @return
	 */
	public static String getColorFont(String Font) {
		return getColorFont(Font, colorKeyString);
	}

	/**
	 * 返回一个随机字符
	 * 
	 * @return 随机字符
	 */
	public static String getRandString() {
		return getRandString(randString);
	}

	/**
	 * 返回一个随机字符
	 * 
	 * @param string 要随机字符的范围
	 * @return 随机字符
	 */
	public static String getRandString(String string) {
		int r1 = getRand(0, string.length() - 1);
		return string.substring(r1, r1 + 1);
	}

	/**
	 * 将字符串染上随机颜色
	 * 
	 * @param Font      要染色的字符串
	 * @param ColorFont 随机染色的颜色代码
	 * @return
	 */
	public static String getColorFont(String Font, String ColorFont) {
		String text = "";
		for (int i = 0; i < Font.length(); i++) {
			int rand = Tool.getRand(0, ColorFont.length() - 1);
			text += "§" + ColorFont.substring(rand, rand + 1) + Font.substring(i, i + 1);
		}
		return text;
	}

	/**
	 * 判断字符串是否是整数型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(Object str) {
		try {
			Float.valueOf(String.valueOf(str)).intValue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断一段字符串中是否只为纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9[.]]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 字符串转换Unicode
	 * 
	 * @param string 要转换的字符串
	 * @return
	 */
	public static String StringToUnicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++)
			unicode.append("\\u" + Integer.toHexString(string.charAt(i)));
		return unicode.toString();
	}

	/**
	 * unicode 转字符串
	 * 
	 * @param unicode 全为 Unicode 的字符串
	 * @return
	 */
	public static String UnicodeToString(String unicode) {
		StringBuffer string = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++)
			string.append((char) Integer.parseInt(hex[i], 16));
		return string.toString();
	}

	/**
	 * 设置小数长度</br>
	 * 默认保留两位小数</br>
	 * 
	 * @param d 要设置的数值
	 * @return
	 */
	public static double Double2(double d) {
		return Double2(d, 2);
	}

	/**
	 * 设置小数长度</br>
	 * 
	 * @param d      要设置的数
	 * @param length 要保留的小数的长度
	 * @return
	 */
	public static double Double2(double d, int length) {
		if (d == 0)
			return 0;
		String s = "#.0";
		for (int i = 1; i < length; i++)
			s += "0";
		DecimalFormat df = new DecimalFormat(s);
		return Double.valueOf(df.format(d));
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param httpUrl 请求地址
	 * @param param   请求的内容
	 * @return
	 * @throws Exception
	 */
	public static String getHttp(String httpUrl) throws Exception {
		return getHttp(httpUrl, "POST", null);
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param httpUrl 请求地址
	 * @param param   请求的内容
	 * @return
	 * @throws Exception
	 */
	public static String getHttp(String httpUrl, String param) throws Exception {
		return getHttp(httpUrl, "POST", param);
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param httpUrl 请求地址
	 * @param Type    请求的方式
	 * @param param   请求的内容
	 * @return
	 * @throws Exception
	 */
	public static String getHttp(String httpUrl, String Type, String param) throws Exception {
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		URL url = new URL(httpUrl);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(Type);
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(60000);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
		if (param != null && !param.isEmpty()) {
			OutputStream os = null;
			os = connection.getOutputStream();
			os.write(param.getBytes());
			os.close();
		}
		if (connection.getResponseCode() == 200) {
			is = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer sbf = new StringBuffer();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sbf.append(temp);
				sbf.append("\r\n");
			}
			result = sbf.toString();
		}
		if (null != br)
			br.close();
		if (null != is)
			is.close();
		connection.disconnect();
		return result;
	}

	/**
	 * 从一段字符内截取另一段字符
	 * 
	 * @param Context 要截取字符的原文
	 * @param text1   要截取的第一段文字
	 * @param text2   要截取的第二段文字
	 * @return 截取完毕的内容
	 */
	public static String cutString(String Context, String strStart, String strEnd) {
		int strStartIndex = Context.indexOf(strStart);
		int strEndIndex = Context.lastIndexOf(strEnd);
		if (strStartIndex < 0 || strEndIndex < 0)
			return null;
		return Context.substring(strStartIndex, strEndIndex).substring(strStart.length());
	}

	/**
	 * 下载文件
	 * 
	 * @param urlStr   要下载的文件的连接
	 * @param fileName 下载后文件的名字
	 * @param savePath 要保存的位置
	 * @throws IOException
	 */
	public static void DownFile(String urlStr, String fileName, String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3 * 1000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);
		File saveDir = new File(savePath);
		if (!saveDir.exists())
			saveDir.mkdir();
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		fos.close();
		if (inputStream != null)
			inputStream.close();
	}

	/**
	 * 保存字节流
	 * 
	 * @param inputStream 文件流
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1)
			bos.write(buffer, 0, len);
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 将一段不知道什么玩意转化为纯整数
	 * 
	 * @param object
	 * @return
	 */
	public static int ObjectToInt(Object object) {
		return ObjectToInt(object, 0);
	}

	/**
	 * 讲一段不知道什么玩意转化为纯数字
	 * 
	 * @param object
	 * @param i      若不是纯数字将默认转化的值
	 * @return
	 */
	public static int ObjectToInt(Object object, int i) {
		if (object == null)
			return i;
		String string = String.valueOf(object);
		if (string.isEmpty() || !isInteger(object))
			return i;
		return string.contains(".") ? Float.valueOf(object.toString()).intValue() : Integer.valueOf(string);
	}

	/**
	 * 一个Object值转换为bool值，转化失败返回false
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean ObjToBool(Object obj) {
		return ObjToBool(obj, false);
	}

	/**
	 * 一个Object值转换为bool值，转化失败返回false
	 * 
	 * @param obj
	 * @param Del
	 * @return
	 */
	public static boolean ObjToBool(Object obj, boolean Del) {
		if (obj == null)
			return Del;
		try {
			return Boolean.valueOf(String.valueOf(obj));
		} catch (Exception e) {
			return Del;
		}
	}

	/**
	 * 发送Https请求
	 * 
	 * @param requestUrl 请求的地址
	 * @return
	 * @throws KeyManagementException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getHttps(String requestUrl)
			throws KeyManagementException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
		return getHttps(requestUrl, "POST", null);
	}

	/**
	 * 发送Https请求
	 * 
	 * @param requestUrl 请求的地址
	 * @param outputStr  请求的参数值
	 * @return
	 * @throws KeyManagementException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getHttps(String requestUrl, String outputStr)
			throws KeyManagementException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
		return getHttps(requestUrl, "POST", outputStr);
	}

	/**
	 * 发送Https请求
	 * 
	 * @param requestUrl    请求的地址
	 * @param requestMethod 请求的方式
	 * @param outputStr     请求的参数值
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHttps(String requestUrl, String requestMethod, String outputStr)
			throws UnsupportedEncodingException, IOException, KeyManagementException, NoSuchAlgorithmException {
		StringBuffer buffer = null;
		SSLContext sslContext = SSLContext.getInstance("SSL");
		TrustManager[] tm = { new Tool() };
		sslContext.init(null, tm, new java.security.SecureRandom());
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		URL url = new URL(requestUrl);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(requestMethod);
		conn.setSSLSocketFactory(ssf);
		conn.connect();
		if (null != outputStr && !outputStr.isEmpty()) {
			OutputStream os = conn.getOutputStream();
			os.write(outputStr.getBytes("utf-8"));
			os.close();
		}
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		buffer = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null)
			buffer.append(line);
		return buffer.toString();
	}

	/**
	 * 替换掉字符中的html标签
	 * 
	 * @param string
	 * @return
	 */
	public static String delHtmlString(String htmlStr) {
		if (htmlStr == null || htmlStr.isEmpty())
			return htmlStr;
		htmlStr = htmlStr.replace("<p>", "\r\n\t").replace("<span>", "\r\n\t").replace("<br>", "\r\n").replace("</br>",
				"\r\n");
		Pattern p_script = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll("");
		Pattern p_style = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll("");
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll("");
		return htmlStr.replaceAll("&nbsp;", "").trim();
	}

	/**
	 * Https下载文件
	 * 
	 * @param urlStr   要下载的文件链接
	 * @param fileName 要保存的文件的名字
	 * @param savePath 文件的保存位置
	 * @throws Exception
	 */
	public static void downLoadFromUrlHttps(String urlStr, String fileName, String savePath) throws Exception {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		TrustManager[] tm = { new Tool() };
		sslContext.init(null, tm, new java.security.SecureRandom());
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setHostnameVerifier(new Tool());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setSSLSocketFactory(ssf);
		conn.connect();
		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);
		File saveDir = new File(savePath);
		if (!saveDir.exists())
			saveDir.mkdirs();
		FileOutputStream fos = new FileOutputStream(new File(saveDir, fileName));
		fos.write(getData);
		fos.close();
		if (inputStream != null)
			inputStream.close();
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	@Override
	public boolean verify(String arg0, SSLSession arg1) {
		return true;
	}

	/**
	 * 将未知参数转换为小数
	 * 
	 * @param obj
	 * @param double1
	 * @return
	 */
	public static Double ObjectToDouble(Object obj) {
		return ObjectToDouble(obj, 0d);
	}

	/**
	 * 将未知参数转换为小数
	 * 
	 * @param obj
	 * @param double1
	 * @return
	 */
	public static Double ObjectToDouble(Object obj, Double double1) {
		if (obj == null)
			return double1;
		double d;
		try {
			String S = String.valueOf(obj);
			if (S == null || S.isEmpty())
				return double1;
			d = Double.valueOf(S);
		} catch (Exception e) {
			return double1;
		}
		return d;
	}

	/**
	 * 将Map按数据升序排列
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int compare = (o1.getValue()).compareTo(o2.getValue());
				return compare;
			}
		});
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());
		return result;
	}

	/**
	 * 将Map降序排序
	 * 
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int compare = (o1.getValue()).compareTo(o2.getValue());
				return -compare;
			}
		});
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());
		return result;
	}
}
