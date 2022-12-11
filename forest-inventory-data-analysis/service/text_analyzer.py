import logging
import pandas as pd


class TextAnalyzer:
    @staticmethod
    def analyze(text) -> pd.DataFrame:
        logging.info("Will analyze: %s", text)
        data = [
            {
                "item": "Вагон синий",
                "count": "1"
            },
            {
                "item": "колеса квадратные",
                "count": "10",
                "unit": "шт"
            },
            {
                "item": "красный коробка",
                "count": "5",
                "unit": "шт"
            }
        ]
        return pd.DataFrame(data)
