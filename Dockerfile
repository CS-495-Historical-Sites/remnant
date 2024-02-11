FROM python:3.11

WORKDIR /hs_backend

COPY requirements.txt requirements.txt

COPY sources/wikidata.json wikidata.json

RUN pip install -r requirements.txt

EXPOSE 8080

COPY . .

CMD ["gunicorn", "hs_backend.wsgi:app", "-w 1", "-b 0.0.0.0:8080", "-t 30"]
