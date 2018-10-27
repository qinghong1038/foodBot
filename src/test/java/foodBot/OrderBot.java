package foodBot;
import java.util.ArrayList;
// other classes are being used and imported
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import foodBot.model.Meal;


public class OrderBot extends TelegramLongPollingBot {
	// 1. initialize constants
	private static final String CALLBACK1 = "callback1";
	private static final String CALLBACK2 = "callback2";
	private static final String CALLBACK3 = "callback3";

	public static final String choice1 = "/1";
	public static final String choice2 = "/2";
	public static final String choice3 = "/3";

	// 2. created on class instantiation
	public Map<String, Meal> m_choices = new HashMap<>();
	private Meal m_meal1;
	private Meal m_meal2;
	private Meal m_noMeal;

	// 3. here a new instance / object of class is being created (within the Constructor)
	public OrderBot() {
		reset();
	}

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String responseMessageText = "Hi! Tomorrows meals in our cantine:";
			String mealDescr1 = "\n1. " + m_meal1;
			String mealDescr2 = "\n2. " + m_meal2;

			responseMessageText += mealDescr1 + mealDescr2;

			SendMessage myMessage = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId()).setText(responseMessageText);
			
			InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
			List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
			List<InlineKeyboardButton> rowInline = new ArrayList<>();
			rowInline.add(new InlineKeyboardButton().setText("Meal 1")
					.setCallbackData(CALLBACK1));
			rowInline.add(new InlineKeyboardButton().setText("Meal 2")
					.setCallbackData(CALLBACK2));
			rowInline.add(new InlineKeyboardButton().setText("No, thanks")
					.setCallbackData(CALLBACK3));
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

		} else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            String answer = "Thanks for choosing: ";
            if (call_data.equals(CALLBACK1)) {
            	Meal m = m_choices.get(choice1);
            	answer += m.getName() + ". Please bring " + m.getPrice() *0.01d + " CHF tomorrow.";
				m.getAmountOfOrders().incrementAndGet();
            } else if (call_data.equals(CALLBACK2)) {
            	Meal m = m_choices.get(choice2);
            	answer += m.getName() + ". Please bring " + m.getPrice() *0.01d + " CHF tomorrow.";
				m.getAmountOfOrders().incrementAndGet();
            } else if (call_data.equals(CALLBACK3)) {
            	answer = "Ok, maybe next time!";
				m_choices.get(choice3).getAmountOfOrders().incrementAndGet();
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
		m_meal1 = new Meal("Spaghetti", 950);
		m_choices.put(choice1, m_meal1);
		m_meal2 = new Meal("Burrito", 1150);
		m_choices.put(choice2, m_meal2);
		m_noMeal = new Meal("NoThanks", 0);
		m_choices.put(choice3, m_noMeal);
	}

	public void changeMeals(Meal meal1, Meal meal2) {
		reset();
		m_meal1 = meal1;
		m_choices.put(choice1, m_meal1);
		m_meal2 = meal2;
		m_choices.put(choice2, m_meal2);
	}
}
