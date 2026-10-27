"""Simulates an IoT sensor posting readings to the API.
Usage: python simulator.py <API_KEY> [base_url] [interval_seconds]
Uses only the standard library. Roughly 1 in 10 readings is a spike to trigger alerts.
"""
import json, random, sys, time, urllib.request, urllib.error

api_key = sys.argv[1]
base = sys.argv[2] if len(sys.argv) > 2 else "http://localhost:8080"
interval = float(sys.argv[3]) if len(sys.argv) > 3 else 2

while True:
    value = round(random.gauss(30, 3), 2)
    if random.random() < 0.1:
        value += random.uniform(15, 30)   # spike
    req = urllib.request.Request(
        f"{base}/api/readings/ingest",
        data=json.dumps({"value": value}).encode(),
        headers={"Content-Type": "application/json", "X-API-KEY": api_key},
        method="POST")
    try:
        with urllib.request.urlopen(req) as r:
            print(value, r.read().decode())
    except urllib.error.HTTPError as e:
        print("HTTP error", e.code, e.read().decode())
    time.sleep(interval)
