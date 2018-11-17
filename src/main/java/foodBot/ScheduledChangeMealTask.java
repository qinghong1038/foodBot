package foodBot;

import java.util.TimerTask;

import foodBot.model.Meal;

public class ScheduledChangeMealTask extends TimerTask {
	private AdminBot m_adminBot;
	private OrderBot m_orderBot;

	public ScheduledChangeMealTask(AdminBot adminBot, OrderBot orderBot) {
		this.m_adminBot = adminBot;
		this.m_orderBot = orderBot;
	}

	@Override
	public void run() {
		Meal n1 = m_adminBot.getNewMeal1();
		Meal n2 = m_adminBot.getNewMeal2();
		if (n1 != null && n2 != null) {
			String latestStatus = m_orderBot.toString();
			m_orderBot.changeMeals(n1, n2);
			m_adminBot.notifyNewMealsSend(latestStatus);
		}
	}
}
