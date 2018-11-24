# foodBot

# Criteria

## A Planning

### The scenario
 
Cook and cafeteria owner who defines the food schedule for the upcoming weeks. Students and staff often don't know what they're about to have for lunch. This results in fewer meals being purchased because they do not know whether they should bring money or home-made lunch instead.

In addition to that it'd be useful being informed in case of sold out meals or changes in menu.
 
### Initial consultation with client and/or adviser
```
 - The evidence of consultation has been referred to in the scenario.
 - Any documentation associated with evidence of consultation has been linked to the cover page.
```

### The proposed product

An application that helps informing students and staff on a daily basis (~6:30 and 7:30 in the morning) about the upcoming meals that day. The application can be configured in advance for the upcoming meals of the week / month.

Optional: an interaction between the recipients of the information and their preferred choice for the upcoming meal could be made.

```
### Specific performance (success) criteria
 - Specific performance criteria have been drawn up that make it possible to evaluate the success of the product in criterion E.
```

## B Design

### [Record of Tasks (ROT)](workLog.txt)

```
### Test plan 
for sucesss criteria
```

```
### Brief summary of methods 
```

```
### Design of the solution 
detailed flow charts
graphical visualisation of the output
```

## C Development

### Use Cases

```
[Admin]-(current order status),
[Admin]-(change meals),
[Admin]-(reset bot),
(change meals)>(current order status),
[Customer]-(chooses food),
[Customer]-(change order),
[Customer]-(cancels order),
(change order)>(cancels order)
```

![](http://yuml.me/e9dc5c26.png)

### UML Model

```
[AdminBot|orderBot:OrderBot|onUpdateReceived();getBotUsername();getBotToken()],
[FoodBot||static void main()],
[OrderBot|choices:Map;chatIdsWithOrders:Map;allTimeChatIds:Set|reset();changeMeals()],
[Meal|name:String;price:int;amountOfOrder:AtomicInteger|],
[TelegramLongPollingBot||onUpdateReceived();getBotUsername();getBotToken()],

[FoodBot]-instantiates>[OrderBot],
[FoodBot]-instantiates>[AdminBot],

[TelegramLongPollingBot]^[OrderBot],
[TelegramLongPollingBot]^[AdminBot],

[AdminBot]->[OrderBot],

[OrderBot]->[Meal],
[AdminBot]->[Meal]
```

![](http://yuml.me/f1db6ecb.png)

```
### Screenshots  
```

```
### Identification of sources used with citation
 - e.g. Telegram library and examples
```


### Examples  

#### 1
#### 2
#### 3
#### 4
#### 5
#### 6
#### 7 


## D Functionality

## running

```
mvn clean verify exec:java
```
