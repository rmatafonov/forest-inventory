from repository.inventory_repository import InventoryRepository
from service.text_analyzer import TextAnalyzer


class Processor:
    @staticmethod
    def process(recognized_text: str):
        df = TextAnalyzer.analyze(recognized_text)
        InventoryRepository.insert(df.to_dict())
