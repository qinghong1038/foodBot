# foodBot

# Criteria

## A Planning

### The scenario
 - A client and/or adviser has been identified.
 - A problem requiring a solution or an unanswered question has been described.
 - The word count for the scenario is approximately 250.
 
Cook and cafeteria owner who defines the food schedule for the upcoming weeks. Students and staff often don't know what they're about to have for lunch. This results in fewer meals being purchased because they do not know whether they should bring money or home-made lunch instead.

In addition to that it'd be useful being informed in case of sold out meals or changes in menu.
 
### Initial consultation with client and/or adviser
 - The evidence of consultation has been referred to in the scenario.
 - Any documentation associated with evidence of consultation has been linked to the cover page.


### The proposed product
 - The proposed product has been identified.
 - The proposed product resolves the inadequacies identified or the unanswered question.
 - The justification of the proposed product is approximately 250 words.

An application that helps informing students and staff on a daily basis (~6:30 and 7:30 in the morning) about the upcoming meals that day. The application can be configured in advance for the upcoming meals of the week / month.

Optional: an interaction between the recepients of the information and their preferred choice for the upcoming meal could be made.

### Specific performance (success) criteria
 - Specific performance criteria have been drawn up that make it possible to evaluate the success of the product in criterion E.

## B Design

### Use Cases

```
[Koch]-(Essen zubereiten),(Essen zubereiten)<(Essen),(isst)<(Essen),[Schueler]-(isst),[Schueler]-(waehlt Essen am Vortag),[Koch]-(plant Mengen),(plant Mengen)<(informiert ueber Essen),(waehlt Essen am Vortag)-(informiert ueber Essen)
```

![](http://yuml.me/691bf6a7.png)

### Data Model

```
[FoodBot|choices:Map;|onUpdateReceived();getBotUsername();getBotToken()],[Main||static void main()]-instantiates>[FoodBot],[TelegramLongPollingBot]^[FoodBot]
```


![](http://yuml.me/223933cc.png)
