package com.zohaibmajeed.noveldownloader.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BookInfo {
	private String url;
	private String slug;
	private int pagesCount;
	private String bookId;

	private class IdHolder {
		public ArrayList<String> ids;
	}

	public BookInfo(String providedUrl) throws IOException, InterruptedException {
		String[] urlSegments = providedUrl.split("\\?")[0].split("\\/");
		slug = urlSegments[urlSegments.length - 1];
		url = "https://www.rekhta.org/ebooks/" + slug;
		scrapePage();
	}

	private void scrapePage() throws IOException, InterruptedException {
		String contentHtml = HttpClient.newHttpClient()
				.send(HttpRequest.newBuilder().GET().uri(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString())
				.body();

		Matcher bookIdMatcher = Pattern
				.compile("var bookid ='([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})'")
				.matcher(contentHtml);
		if (bookIdMatcher.find())
			bookId = bookIdMatcher.group(1);

		Matcher totalPagesMatcher = Pattern.compile("var pageIds = new Array\\(([0-9]+)\\)").matcher(contentHtml);
		if (totalPagesMatcher.find())
			pagesCount = Integer.parseInt(totalPagesMatcher.group(1));
	}

	public ArrayList<String> findIds(int from) throws IOException, InterruptedException {
		int count = 10;

		String pageIdsJson = HttpClient.newHttpClient()
				.send(HttpRequest.newBuilder(URI.create("https://www.rekhta.org/EbookData/GetEbookPageIds/?slug=" + slug
						+ "&lang=1&from=" + from + "&count=" + count)).GET().build(),
						HttpResponse.BodyHandlers.ofString())
				.body();

		IdHolder idHolder = new Gson().fromJson(pageIdsJson, new TypeToken<IdHolder>() {
		}.getType());
		
		return idHolder.ids;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getSlug() {
		return slug;
	}
	
	public int getPagesCount() {
		return pagesCount;
	}
	
	public String getBookId() {
		return bookId;
	}
}
