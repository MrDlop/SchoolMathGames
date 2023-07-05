async def send_file(websocket, filename):
    await websocket.send("file")


import asyncio
from websockets.sync.client import connect


def hello():
    with connect("ws://localhost:8001") as websocket:
        websocket.send("file")
        with open("index1.jpg", "rb") as f:
            inf = f.readline()
            while inf:
                websocket.send(inf)
                inf = f.readline()
            websocket.send("None")


hello()
