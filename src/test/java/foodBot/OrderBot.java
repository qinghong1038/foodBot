package foodBot;
// other classes are being used and imported
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import foodBot.model.Meal;


public class OrderBot extends TelegramLongPollingBot {
	// 1. initialize constants
	public static final String choice1 = "/1";
	public static final String choice2 = "/2";
	public static final String choice3 = "/3";

	// 2. created on class instantiation
	public Map<String, AtomicInteger> m_choices = new HashMap<>();
	private Meal m_meal1 = new Meal("Spaghetti", 950);
	private Meal m_meal2 = new Meal("Burrito", 1150);;

	// 3. here a new instance / object of class is being created (within the Constructor)
	public OrderBot() {
		reset();
	}

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String userMessage = update.getMessage().getText();

			String responseMessageText;
			if (!m_choices.keySet().contains(userMessage)) {
				String mealDescr1 = choice1 + " " + m_meal1 + "\n";
				String mealDescr2 = choice2 + " " + m_meal2 + "\n";
				String decline = choice3 + " " + "Nein, danke!";

				responseMessageText = mealDescr1 + mealDescr2 + decline;
			} else {
				m_choices.get(userMessage).incrementAndGet();
				
				responseMessageText = "Thanks for choosing: " + userMessage;
			}

			SendMessage myMessage = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId()).setText(responseMessageText);
			
			try {
				execute(myMessage); // Call method to send the message
				// System.out.println(userMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}

			for (String choice : m_choices.keySet()) {
				System.out.println(choice + " " + m_choices.get(choice).get());
			}
			System.out.println();
		}
	}

	// return the human readable name of the bot
	public String getBotUsername() {
		return "RajaThiedmannsFoodBot";
	}

	// return secret for authentication
	public String getBotToken() {
		return "657674321:AAGQDtt0w6Equnwaw55R-I--dkXDHInHfeM";
	}

	public void reset() {
		// initialize (inner) state: choice <-> responses given (initial 0)
		m_choices.clear();
		m_choices.put(choice1, new AtomicInteger(0));
		m_choices.put(choice2, new AtomicInteger(0));
		m_choices.put(choice3, new AtomicInteger(0));
	}

	public void changeMeals(Meal meal1, Meal meal2) {
		m_meal1 = meal1;
		m_meal2 = meal2;
		reset();
	}
}
