package edu.epam.pagination;

public class Paginator {

	public static void getPages(StringBuilder builder, int activePage, int lastPage) {
		int elements = 3;
		switch (lastPage) {
		case 1:
			break;
		case 2:
			elements = 4;
			break;
		case 3:
			elements = 5;
			break;
		case 4:
			elements = 6;
			break;
		case 5:
			if(activePage == 1 || activePage == lastPage) {
				elements = 6;
			}
			else {
				elements = 7;
			}
			break;
		case 6:
			if(activePage == 1 || activePage == lastPage) {
				elements = 6;
			}
			else if(activePage == 2 || activePage == lastPage - 1) {
				elements = 7;
			}
			else {
				elements = 8;
			}
			break;
			
		default:
			if(activePage == 1 || activePage == lastPage) {
				elements = 6;
			}
			else if(activePage == 2 || activePage == lastPage - 1) {
				elements = 7;
			}
			else if(activePage == 3 || activePage == lastPage - 2) {
				elements = 8;
			}
			else {
				elements = 9;
			}
			break;
		}
		double paddingPercentage = 50 - ((125 * elements)/53.953);
		
		builder.append("<div class=\"card\">");
		builder.append("<div class=\"row\">");
		builder.append("<div class=\"col s12\" style=\"padding-left: ");
		builder.append(paddingPercentage);
		builder.append("%;\">");
		builder.append("<ul class=\"pagination\">");
		
		appendLeftSide(builder, activePage);
		appendActive(builder, activePage);
		appendRightSide(builder, activePage, lastPage);
			
		builder.append("</ul></div></div></div>");
	}
	
	private static String appendLeftSide(StringBuilder builder, int activePage) {
		builder.append("<li class=\"prev");
		if(activePage == 1) {
			builder.append(" disabled");
			builder.append("\">");
			builder.append("<a href=\"#!\">");
		}
		else {
			builder.append(" waves-effect");
			builder.append("\">");
			builder.append("<a onclick=\"goToPage(");
			int prev = activePage - 1;
			if(prev == 0) prev++;
			builder.append(prev);
			builder.append(")\">");
		}

		builder.append("<i class=\"material-icons\">chevron_left</i></a></li>");
		
		switch (activePage) {
		case 1:
			builder.append("");
			break;
		case 2:
			appendFirst(builder);
			break;
		case 3:
			appendFirst(builder);
			appendPreActive(builder, activePage);
			break;

		default:
			appendFirst(builder);
			appendThreeDots(builder);
			appendPreActive(builder, activePage);
			break;
		}
		return builder.toString();
	}

	private static void appendRightSide(StringBuilder builder, int activePage,
			int lastPage) {
		if(activePage == lastPage) {
			builder.append("");
		}
		else if(activePage == lastPage - 1) {
			appendLast(builder, lastPage);
		}
		else if(activePage == lastPage - 2) {
			appendAfterActive(builder, activePage);
			appendLast(builder, lastPage);
		}
		else {
			appendAfterActive(builder, activePage);
			appendThreeDots(builder);
			appendLast(builder, lastPage);
		}
		
		builder.append("<li class=\"next");
		if(activePage == lastPage) {
			builder.append(" disabled");
			builder.append("\">");
			builder.append("<a href=\"#!\">");
		}
		else {
			builder.append(" waves-effect");
			builder.append("\">");
			builder.append("<a onclick=\"goToPage(");
			int next = activePage + 1;
			if(next == 0) next++;
			builder.append(next);
			builder.append(")\">");
		}
		
		builder.append("<i class=\"material-icons\">chevron_right</i></a></li>");
	}
	
	private static void appendFirst(StringBuilder builder) {
		builder.append("<li id=\"page_number");
		builder.append(1);
		builder.append("\" class=\"waves-effect\">");
		builder.append("<a onclick=\"goToPage(");
		builder.append(1);
		builder.append(")\">");
		builder.append(1);
		builder.append("</a></li>");
	}
	
	private static void appendThreeDots(StringBuilder builder) {
		builder.append("<li class=\"disabled\">");
		builder.append("<a href=\"#!\">");
		builder.append("...");
		builder.append("</a></li>");
	}

	private static void appendPreActive(StringBuilder builder, int activePage) {
		builder.append("<li id=\"page_number");
		builder.append(activePage - 1);
		builder.append("\" class=\"waves-effect\">");
		builder.append("<a onclick=\"goToPage(");
		builder.append(activePage - 1);
		builder.append(")\">");
		builder.append(activePage - 1);
		builder.append("</a></li>");
	}
	
	private static void appendActive(StringBuilder builder, int activePage) {
		builder.append("<li id=\"page_number");
		builder.append(activePage);
		builder.append("\" class=\"active\">");
		builder.append("<a onclick=\"goToPage(");
		builder.append(activePage);
		builder.append(")\">");
		builder.append(activePage);
		builder.append("</a></li>");
	}
	
	private static void appendAfterActive(StringBuilder builder, int activePage) {
		builder.append("<li id=\"page_number");
		builder.append(activePage + 1);
		builder.append("\" class=\"waves-effect\">");
		builder.append("<a onclick=\"goToPage(");
		builder.append(activePage + 1);
		builder.append(")\">");
		builder.append(activePage + 1);
		builder.append("</a></li>");
	}

	private static void appendLast(StringBuilder builder, int lastPage) {
		builder.append("<li id=\"page_number");
		builder.append(lastPage);
		builder.append("\" class=\"waves-effect\">");
		builder.append("<a onclick=\"goToPage(");
		builder.append(lastPage);
		builder.append(")\">");
		builder.append(lastPage);
		builder.append("</a></li>");
	}
}
