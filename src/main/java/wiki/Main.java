package wiki;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	public static final String BR = "<br>";

	public static Set<String> SPECIAL_ELEMENTS;

	static {
		SPECIAL_ELEMENTS = new HashSet<String>();
		SPECIAL_ELEMENTS.add("blockquote");
	}

	/**
	 * 旧版的wiki布局，以  <div id="wikipage">  这个element作为整个页面
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Element getMainPage(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document.body().getElementById("wikipage");
	}

	/**
	 * 此方法已废弃
	 * @param body
	 */
	@Deprecated
	public static Elements getTables(Element body) {
		return body.getElementsByTag("table");
	}

	/**
	 * 此方法已废弃
	 * @param body
	 */
	@Deprecated
	public static void parseTableFromBody(Element body) {
		Elements tables = body.getElementsByTag("table");
		for (int i = 0; i < tables.size(); i++) {
			Element table = tables.get(i);

			String tableName = table.previousElementSibling().text();

			Table tobe = new Table(tableName, table);
			System.out.println(tobe.generateTableContent());

		}

	}

	/**
	 * 主方法，根据旧版wiki的url生成新版wiki页面的源代码
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String genWholePage(String url) throws IOException {
		Element main = getMainPage(url);
		Elements children = getAllTopChilds(main);

		StringBuilder sb = new StringBuilder();

		for (Element child : children) {
			if (isTitle(child.tagName())) {
				sb.append(child.text());
			} else if (inSpecialElement(child)) {
				sb.append(child.text());
			} else if (isLink(child)) {
				sb.append(child.text());
			} else if (isTable(child)) {
				String tableName = "";
				Element tmpTName = child.previousElementSibling();
				if (isTitle(tmpTName.tagName())) {
					tableName = tmpTName.text();
				}
				Table table = new Table(tableName, child);
				sb.append(table.generateTableContent());
			} else if (child.hasText()) {
				sb.append(formatChildText(child, 0));
			}
		}

		return sb.toString();
	}

	private static String formatChildText(Element parent, int indent) {
		if (!parent.hasText()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
//		sb.append(BR);
//		sb.append(genIndent(indent));
		sb.append(parent.ownText());
		Elements children = parent.children();
		if (children.size() == 0) {
			return sb.toString();
		}
		for (Element child : children) {
//			int newIndent = indent + 1;
			sb.append(formatChildText(child, indent));
		}
		return sb.append(BR).toString();
	}

	public static Elements getAllTopChilds(Element body) {
		return body.children();
	}

	public static boolean isTable(Element element) {
		return element.tagName().equalsIgnoreCase("table");
	}

	public static boolean isTitle(String tagName) {
		return tagName.matches("[hH][1-6]");
	}

	public static boolean isLink(Element element) {
		return element.tagName().equals("a") && element.hasAttr("href");
	}

	public static boolean inSpecialElement(Element element) {
		return SPECIAL_ELEMENTS.contains(element.tagName());
	}

//	private static String genIndent(int indent) {
//		StringBuilder sb = new StringBuilder();
//		for(;indent > 0; indent--)
//			sb.append("*");
//		return sb.append(" ").toString();
//	}

	public static void main(String[] args) throws IOException {
		String url = "http://192.168.11.147:8000/Egret/wiki/RequestParameterV13";

//		url = "http://192.168.11.147:8000/Egret/wiki/GetDataV13";

		String result = genWholePage(url);
		System.out.println(result);


	}

}
