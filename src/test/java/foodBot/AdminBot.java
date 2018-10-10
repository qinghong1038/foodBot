/**
 * 
 */
package foodBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author raja
 *
 */
public class AdminBot extends TelegramLongPollingBot {
	@Override
	public void onUpdateReceived(Update update) {
	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	                .setChatId(update.getMessage().getChatId())
	                .setText(update.getMessage().getText());
	        try {
	            execute(message); // Call method to send the message
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public String getBotUsername() {
		return "secondfoodbot";
	}

	public String getBotToken() {
		return "683028665:AAFacGJ5e8lnh1d5EfgSBsPKonGBjElFLZU";
	}
}