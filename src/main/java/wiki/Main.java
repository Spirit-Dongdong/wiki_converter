package wiki;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {



	public static Element getBody(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document.body();
	}

	public static int getNumOfTable(Element body) {
		Elements tables = body.getElementsByTag("table");
		return tables.size();
	}

	public static Elements getTables(Element body) {
		return body.getElementsByTag("table");
	}

	public static void parseTableFromBody(Element body) {
		Elements tables = body.getElementsByTag("table");
		for (int i = 0; i < tables.size(); i++) {
			Element table = tables.get(i);
//			System.out.println(table.elementSiblingIndex());
//			System.err.println("表头：" + table.previousElementSibling());
			String tableName = table.previousElementSibling().text();

			Table tobe = new Table(tableName, table);
			System.out.println(tobe.generateTableContent());

			System.out.println();
			System.out.println();
		}

	}

	public static void main(String[] args) throws IOException {
		String url = "http://192.168.11.147:8000/Egret/wiki/RequestParameterV13";

//		url = "http://192.168.11.147:8000/Egret/wiki/NewV13Parameter#dty2搜索结果x_map";

		parseTableFromBody(getBody(url));
	}

}
