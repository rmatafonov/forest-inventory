from flask import request
from flask_api import status
from flask_restful import Resource, reqparse, abort

from lib.repository.inventory_repository import InventoryRepository
from lib.service.text_analyzer import TextAnalyzer

parser = reqparse.RequestParser()
parser.add_argument("text")


class DataAnalysisApi(Resource):
    __repository = InventoryRepository()

    def post(self):
        return TextAnalyzer.analyze(request.get_json())
