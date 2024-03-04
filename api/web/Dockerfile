FROM python:3.11

WORKDIR /web

COPY ./web/ .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 8080

CMD ["gunicorn", "src.wsgi:app", "-w 1", "-b 0.0.0.0:8080", "-t 30"]