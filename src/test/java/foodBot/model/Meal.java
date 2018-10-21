package foodBot.model;

public class Meal {
	private String m_name;
	private int m_price;
	
	public Meal(String name, int price) {
		setName(name);
		setPrice(price);
	}

	public String getName() {
		return m_name;
	}

	private void setName(String m_name) {
		this.m_name = m_name;
	}

	public int getPrice() {
		return m_price;
	}

	private void setPrice(int m_price) {
		this.m_price = m_price;
	}
	
	@Override
	public String toString() {
		return getName() + " (" + getPrice() / 100 + ")";
	}
}
