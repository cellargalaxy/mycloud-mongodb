package top.cellargalaxy.util;

import top.cellargalaxy.bean.controlor.Page;

/**
 * Created by cellargalaxy on 18-4-8.
 */
public class PageUtil {
	public static final Page[] createPages(int page, int count, int len, int pagesLength) {
		int pageCount;
		if (count % len == 0) {
			pageCount = count / len;
		} else {
			pageCount = (count / len) + 1;
		}
		int start = page - (pagesLength / 2);
		int end = page + (pagesLength / 2);
		if (start < 1) {
			start = 1;
		}
		if (pageCount < 1) {
			pageCount = 1;
		}
		if (end > pageCount) {
			end = pageCount;
		}
		Page[] pages = new Page[end - start + 3];
		pages[0] = new Page("首页", "1", page == 1);
		pages[pages.length - 1] = new Page("尾页", pageCount + "", page == pageCount);
		for (int i = 1; i < pages.length - 1; i++) {
			pages[i] = new Page((start + i - 1) + "", (start + i - 1) + "", page == (start + i - 1));
		}
		return pages;
	}
}
