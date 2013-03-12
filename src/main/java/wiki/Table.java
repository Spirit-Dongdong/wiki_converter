package wiki;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Table {

	public static final String CRLF = "\r\n";
	private static final String COL_SEPARTOR = "||";
	private static final String TABLE_END = "|}";
	private static final String TABLE_BEGIN = "{|border=\"1\" cellspacing=\"0\" align=\"middle\"" + CRLF;




	public String[][] tableContent;
	public String tableName;
	public int row;//总的行数，包括表头行，所以有效行从1开始
	public int column;

	public String[] row_names;

	public Table(String tableName, Element table) {
		this.tableName = tableName;
		parseTable(table);
	}

//	public Table(Element tableName, Element table) {
//		this.tableName = tableName;
//		parseTable(table);
//	}

	public String generateTableContent() {
		StringBuilder sb = new StringBuilder();
//		sb.append(tableName);
		sb.append(CRLF);
		sb.append(TABLE_BEGIN);
		sb.append(generateTableName());//表头文字
		sb.append(generateTableRow(tableContent[0], true));//第一列，各列的列名
		for (int i = 1; i < row; i++) {
			sb.append(generateTableRow(tableContent[i], false));
		}
		sb.append(TABLE_END);
		return sb.append(Main.BR).toString();
	}

	public String bold(String content) {
		return "'''" + content + "'''";
	}

	public String link(String content) {
		return "[" + content + "]";
	}

	public String generateTableName() {
		return "|+ " + bold(tableName) + CRLF;
	}

	public String generateTableRow(String[] rows, boolean first) {
		StringBuilder sb = new StringBuilder();
		if (first) {
			sb.append("!");
		} else {
			sb.append("|");
		}

		for (String row : rows) {
			sb.append(" ");
			sb.append(row);
			sb.append(" ");
			sb.append(COL_SEPARTOR);

		}

		removeLastColSep(sb);
		sb.append(CRLF).append("|-").append(CRLF);

		return sb.toString();
	}

	private void removeLastColSep(StringBuilder sb) {
		sb.delete(sb.length() - 3, sb.length());
	}

	public void parseTable(Element table) {
		Elements trs = table.getElementsByTag("tr");
		row = trs.size();
		column = trs.get(0).getElementsByTag("td").size();

		tableContent = new String[row][column];

		for (int j = 0; j < row; j++) {
			Element tr = trs.get(j);
			Elements tds = tr.getElementsByTag("td");
			for (int k = 0; k < column; k++) {
				Element td = tds.get(k);
				tableContent[j][k] = td.text();
			}
		}
	}

	public void processLink(Element link) {
		if (!link.hasAttr("href")) {
			return;
		}
		String href = link.attr("href");
	}

	private boolean isAbosultLink(String href) {
		return href.startsWith("/");
	}

	public static void test(Element table) {
		Elements sm = table.getElementsByIndexLessThan(table.siblingIndex());
		Elements gt = table.getElementsByIndexGreaterThan(table.siblingIndex());
	}


	public static Elements interact(Elements s, Elements t) {
		Elements result = s.clone();
		result.retainAll(t);
		return result;
	}




}
