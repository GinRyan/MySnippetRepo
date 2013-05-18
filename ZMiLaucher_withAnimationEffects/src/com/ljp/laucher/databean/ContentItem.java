package com.ljp.laucher.databean;

public class ContentItem {
	
	public int id;
	public String from;
	public int icon;
	public int from_icon;
	public String text;
	public boolean choice;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getFrom_icon() {
		return from_icon;
	}

	public void setFrom_icon(int from_icon) {
		this.from_icon = from_icon;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int item0_icon) {
		this.icon = item0_icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}

}