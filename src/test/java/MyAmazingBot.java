import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyAmazingBot extends TelegramLongPollingBot {
	private Map<String, AtomicInteger> choices = new HashMap<>();
	public static final String choice1 = "/1";
	public static final String choice2 = "/2";
	public static final String choice3 = "/3";

	public MyAmazingBot() {
		choices.put(choice1, new AtomicInteger(0));
		choices.put(choice2, new AtomicInteger(0));
		choices.put(choice3, new AtomicInteger(0));
	}

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String userMessage = update.getMessage().getText();

			String responseMessageText;
			if (!choices.keySet().contains(userMessage)) {
				String meal1 = choice1 + " " + "Spaghetti" + "\n";
				String meal2 = choice2 + " " + "Burrito" + "\n";
				String decline = choice3 + " " + "Nein, danke!";

				responseMessageText = meal1 + meal2 + decline;
			} else {
				choices.get(userMessage).incrementAndGet();
				
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

			for (String choice : choices.keySet()) {
				System.out.println(choice + " " + choices.get(choice).get());
			}
			System.out.println();
		}
	}

	public String getBotUsername() {
		return "RajaThiedmannsMyAmazingbot";
	}

	public String getBotToken() {
		return "657674321:AAGQDtt0w6Equnwaw55R-I--dkXDHInHfeM";
	}
}