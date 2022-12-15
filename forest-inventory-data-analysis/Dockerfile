FROM python:3.10.4

WORKDIR /usr/src/app

COPY requirements.txt main.py .env ./
ADD lib lib
RUN pip install --no-cache-dir -r requirements.txt

CMD [ "python", "./main.py" ]