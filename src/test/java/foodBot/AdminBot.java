/**
 * 
 */
package foodBot;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author raja
 */
public class AdminBot extends TelegramLongPollingBot {
	private OrderBot orderBot;

	public AdminBot(OrderBot bot) {
		this.orderBot = bot;
	}
	
	@Override
	public void onUpdateReceived(Update update) {
	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	        String userMessageText = update.getMessage().getText();
	        String botMessageText = userMessageText;
	        if ( "/status".equals(userMessageText)) {
	        	botMessageText = orderBot.choices.toString();
	        } else if("/reset".equals(userMessageText)) {
	        	orderBot.reset();
	        	botMessageText = "reset done";
	        } else if(userMessageText.startsWith("/changeMeals")) {
	        	try {
	        		String[] split = StringUtils.split(userMessageText, ',');
	        		String meal1 = split[1];
	        		String meal2 = split[2];
	        		botMessageText = orderBot.choices.toString();
	        		orderBot.changeMeals(meal1, meal2);
	        		botMessageText += "\nMeal 1 and 2 changed";
	        	} catch (Exception e) {
	        		botMessageText = "Please send change as: /changeMeals,mealName1,mealName2";
	        	}
	        }

	        SendMessage botResponseMessage = new SendMessage() // Create a SendMessage object with mandatory fields
	        		.setChatId(update.getMessage().getChatId())
	        		.setText(botMessageText);
	        
	        try {
	            execute(botResponseMessage); // Call method to send the message
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