package util;

public class HistoryItem {
	//{"id":"622","lastVisitTime":1488430705718.574,"title":"Magic Actions | Change Your Options","typedCount":0,"url":"https://www.chromeactions.com/magic-options.html","visitCount":1}
	public String id;
	public double lastVisitTime;
	public String title;
	public int typedCount;
	public String url;
	public int visitCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getLastVisitTime() {
		return lastVisitTime;
	}
	public void setLastVisitTime(double lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTypedCount() {
		return typedCount;
	}
	public void setTypedCount(int typedCount) {
		this.typedCount = typedCount;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	
}
