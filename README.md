## Telegram Bot that recognizes voice messages

### To make it work, you have to:
* Create your own Telegram Bot with @BotFather bot (set the received token to FOREST_INVENTORY_BOT_TOKEN env variable)
* Create your own Yandex Cloud. You will need:
  * Service account (set its API Token to FOREST_INVENTORY_YC_TOKEN env variable)
  * Object storage bucket (set its id to FOREST_INVENTORY_YC_FOLDER_ID)

### Run the Bot
* Just run main method from file BotLauncher.kt of the module forest-inventory-telegram-bot

### Use it
* Open your bot in the Telegram app
* Type command "/start" as usual Telegram Bot
* Send a voice message - the Bot will reply with recognized text
* Enjoy!