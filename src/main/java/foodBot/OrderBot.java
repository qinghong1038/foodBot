package foodBot;
import java.util.ArrayList;
// other classes are being used and imported
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import foodBot.model.Meal;


public class OrderBot extends TelegramLongPollingBot {
	// 1. initialize constants
	public static final String choice1 = "/1";
	public static final String choice2 = "/2";
	public static final String choice3 = "/3";
	public static final String cancelOrder = "/cancelOrder";

	// 2. created on class instantiation
	private Map<String, Meal> m_choices = new HashMap<>();
	private Set<Long> m_allTimeChatIds = new HashSet<>();
	private Map<Long, Meal> m_chatIdsWithOrders = new HashMap<>();
	
	// 3. here a new instance / object of class is being created (within the Constructor)
	public OrderBot() {
		reset();
	}

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		Message message = update.getMessage();
		String alreadyOrdered = "You already ordered!";
		
		if (update.hasMessage() && message.hasText()) {
			Long chatId = message.getChatId();
			if (m_chatIdsWithOrders.keySet().contains(chatId)) {
				SendMessage myMessage = new SendMessage() // Create a SendMessage object with mandatory fields
						.setChatId(chatId).setText(alreadyOrdered);
				InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
				List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
				List<InlineKeyboardButton> rowInline = new ArrayList<>();
				rowInline.add(new InlineKeyboardButton().setText("Cancel my order!")
						.setCallbackData(cancelOrder));
				
				// Set the keyboard to the markup
				rowsInline.add(rowInline);
				// Add it to the message
				markupInline.setKeyboard(rowsInline);
				myMessage.setReplyMarkup(markupInline);
				
				try {
					execute(myMessage); // Call method to send the message
					// System.out.println(userMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else {
				String responseMessageText = "Hi! Tomorrows meals in our cantine:";
				String mealDescr1 = "\n1. " + m_choices.get(choice1);
				String mealDescr2 = "\n2. " + m_choices.get(choice2);
				
				responseMessageText += mealDescr1 + mealDescr2;
				m_allTimeChatIds.add(chatId);
				
				SendMessage myMessage = new SendMessage() // Create a SendMessage object with mandatory fields
						.setChatId(chatId).setText(responseMessageText);
				
				InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
				List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
				List<InlineKeyboardButton> rowInline = new ArrayList<>();
				rowInline.add(new InlineKeyboardButton().setText("Meal 1")
						.setCallbackData(choice1));
				rowInline.add(new InlineKeyboardButton().setText("Meal 2")
						.setCallbackData(choice2));
				rowInline.add(new InlineKeyboardButton().setText("No, thanks")
						.setCallbackData(choice3));
				// Set the keyboard to the markup
				rowsInline.add(rowInline);
				// Add it to the message
				markupInline.setKeyboard(rowsInline);
				myMessage.setReplyMarkup(markupInline);
				
				try {
					execute(myMessage); // Call method to send the message
					// System.out.println(userMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		} else if (update.hasCallbackQuery()) {
            // Set variables
            CallbackQuery callbackQuery = update.getCallbackQuery();
			String call_data = callbackQuery.getData();
            Message call_message = callbackQuery.getMessage();
			long message_id = call_message.getMessageId();
            long chat_id = call_message.getChatId();

            String answer = "Unexpected callback - please contact your administrator!";
            
            if (m_choices.keySet().contains(call_data)) {
            	if (m_chatIdsWithOrders.keySet().contains(chat_id)) {
            		answer = alreadyOrdered;
            	} else {
            		Meal m = m_choices.get(call_data);

            		// actual order!
            		m.getAmountOfOrders().incrementAndGet();
            		m_chatIdsWithOrders.put(chat_id, m);
            		
            		if (choice3.equals(call_data)) {
            			answer = "Ok, maybe next time!";
            		} else {
            			answer = "Thanks for choosing: " + m.getName() + ". Please bring " + m.getPrice() *0.01d + " CHF tomorrow.";
            		}
            	}
            } else if (cancelOrder.equals(call_data)) {
            	// actual cancel of order
            	Meal remove = m_chatIdsWithOrders.remove(chat_id);
            	if (remove != null) {
	            	remove.getAmountOfOrders().decrementAndGet();
	            	answer = "Your order has been cancelled!";
            	} else {
            		answer = "You can't cancel an outdated order!";
            	}
            	
            }
            
            EditMessageText new_message = new EditMessageText()
            		.setChatId(chat_id)
            		.setMessageId(new Long(message_id).intValue())
            		.setText(answer);
            try {
            	execute(new_message); 
            } catch (TelegramApiException e) {
            	e.printStackTrace();
            }
        }
		
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String choice : m_choices.keySet()) {
			Meal meal = m_choices.get(choice);
			sb.append("\n" + meal.getName() + ": " + meal.getAmountOfOrders().get());
		}
		sb.append("\nNumber of participants: " + m_allTimeChatIds.size());
		
		return sb.toString();
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
		m_chatIdsWithOrders.clear();
		m_choices.put(choice1, new Meal("Spaghetti", 950));
		m_choices.put(choice2, new Meal("Burrito", 1150));
		m_choices.put(choice3, new Meal("NoThanks", 0));
	}

	public void changeMeals(Meal meal1, Meal meal2) {
		reset();
		m_choices.put(choice1, meal1);
		m_choices.put(choice2, meal2);
		
		String changeMealText = "Meals have changed!";
		
		for (Long chatId : m_allTimeChatIds) {
			SendMessage myMessage = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(chatId).setText(changeMealText);
			try {
				execute(myMessage); // Call method to send the message
				// System.out.println(userMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		
	}
}
