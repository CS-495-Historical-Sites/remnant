FROM python:3.11

WORKDIR /app

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 8080

CMD ["gunicorn", "hs_backend.wsgi:app", "-w 1", "-b 0.0.0.0:8080", "-t 30"]

