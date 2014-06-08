package com.thai.client;

import java.util.List;

public interface Clientdelegate {

	
	public void update(String info);
	public void updateTable(List<String>list);
	public void kick();
}
