package com.jingshuiqi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChangeUtil {
	private static final String REGEX = "(?i)(<[^<]+class=[^<]*image.caption[^<]*>[^<]*</[^<]+>)|(<[^<]+>)";
	/*private static final String regEx_html = "<[^>]+>";*/
	private static final String REPLACE = "";
	private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符  /[ ]|[&nbsp;]/g, ''
	public static String BKURL = "http://101.200.37.195:81";
	
	public static String documentBody (String newsBody) {
        Element doc = Jsoup.parseBodyFragment(newsBody).body();
        Elements pngs = doc.select("img[src]");
        String httpHost = "http://101.200.37.195:81";
        for (Element element : pngs) {
            String imgUrl = element.attr("src");
            if (imgUrl.trim().startsWith("/")) { // 会去匹配我们富文本的图片的 src 的相对路径的首个字符，请注意一下
                imgUrl =httpHost + imgUrl;
                element.attr("src", imgUrl);
            }
        }
      return newsBody = doc.toString();
    }
	
	
	public static String clearHTMLTags(String content, int length) {
		if(length > 0) {
			Pattern p = Pattern.compile(REGEX);
			Matcher m = p.matcher(content);
			String value = m.replaceAll(REPLACE);
			if(length < value.length()) {
				value = value.substring(0, length).concat("...");
			} else {
				value = value.substring(0, value.length());
			}
			Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
	        Matcher m_space = p_space.matcher(value);  
	        value = m_space.replaceAll(""); // 过滤空格回车标签  
			return value.trim();
		} else {
			return "";
		}
	}
}
