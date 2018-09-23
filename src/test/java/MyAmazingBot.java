import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

// https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started
public class MyAmazingBot extends TelegramLongPollingBot {
	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String completeResponseText = update.getMessage().getText();
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId()).setText(completeResponseText);
			try {
				execute(message); // Call method to send the message
				System.out.println(completeResponseText);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getBotUsername() {
		return "RajaThiedmannsMyAmazingbot";
	}

	@Override
	public String getBotToken() {
		return "657674321:AAGQDtt0w6Equnwaw55R-I--dkXDHInHfeM";
	}
}