import logging


class InventoryRepository:
    @staticmethod
    def insert(analyzed):
        logging.info("Will insert the data %s", analyzed)
