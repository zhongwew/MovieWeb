package Entitypackage;

import java.util.Vector;

public class CartInfo {
	private Vector<String> items;
	public CartInfo() {
		items = new Vector<String>();
	}
	public Vector<String> getCartInfo(){
		return items;
	}
	public void additems(String item) {
		items.add(item);
	}
}
