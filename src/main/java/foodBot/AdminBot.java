/**
 * 
 */
package foodBot;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import foodBot.model.Meal;

/**
 * @author raja
 */
public class AdminBot extends TelegramLongPollingBot {
	private OrderBot orderBot;
	private Meal m_newMeal1;
	private Meal m_newMeal2;
	private Set<Long> m_allTimeChatIds = new HashSet<>();
	
	public AdminBot(OrderBot bot) {
		this.orderBot = bot;
		
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Timer time = new Timer(); // Instantiate Timer Object
        time.schedule(new ScheduledChangeMealTask(this, orderBot), 
        		30*1000, 60*1000);
	}
	
	@Override
	public void onUpdateReceived(Update update) {
	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	        String userMessageText = update.getMessage().getText();
	        String botMessageText = userMessageText;
			
	        if ( "/status".equals(userMessageText)) {
	        	botMessageText = orderBot.toString();
	        } else if("/reset".equals(userMessageText)) {
	        	orderBot.reset();
	        	botMessageText = "reset done";
	        } else if(userMessageText.startsWith("/changeMeals")) {
	        	try {
	        		String[] split = StringUtils.split(userMessageText, ',');
	        		String mealName1 = split[1];
	        		String mealPrice1 = split[2];
	        		String mealName2 = split[3];
	        		String mealPrice2 = split[4];
	        		
	        		int price1 = Integer.valueOf(mealPrice1);
	        		int price2 = Integer.valueOf(mealPrice2);
	        		
	        		setNewMeal1(new Meal(mealName1, price1));
	        		setNewMeal2(new Meal(mealName2, price2));
	        		
	        		botMessageText += "New Meal 1 and 2 configured for tomorrow!";
	        	} catch (Exception e) {
	        		botMessageText = "Please send change as: /changeMeals,meal1Name,meal1Price,meal2Name,meal2Price";
	        	}
	        }

	        Long chatId = update.getMessage().getChatId();
			SendMessage botResponseMessage = new SendMessage() // Create a SendMessage object with mandatory fields
	        		.setChatId(chatId)
	        		.setText(botMessageText);
			m_allTimeChatIds.add(chatId);
	        
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

	public void notifyNewMealsSend(String latestStatus) {
		setNewMeal1(null);
		setNewMeal2(null);
		
		for (Long chatId : m_allTimeChatIds) {
			SendMessage myMessage = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(chatId).setText(latestStatus);
			try {
				execute(myMessage); // Call method to send the message
				// System.out.println(userMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Meal getNewMeal1() {
		return m_newMeal1;
	}

	private void setNewMeal1(Meal m_newMeal1) {
		this.m_newMeal1 = m_newMeal1;
	}

	public Meal getNewMeal2() {
		return m_newMeal2;
	}

	private void setNewMeal2(Meal m_newMeal2) {
		this.m_newMeal2 = m_newMeal2;
	}
}